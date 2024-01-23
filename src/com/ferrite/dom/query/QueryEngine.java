package com.ferrite.dom.query;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.NodeType;
import com.ferrite.dom.NodeVariant;
import com.ferrite.dom.exceptions.DOMNodeVariantIllegalVariantTypeException;
import com.ferrite.dom.exceptions.DOMNodeVariantTypeMismatchException;
import com.ferrite.dom.exceptions.query.QueryEmptyResultException;
import com.ferrite.dom.exceptions.query.QueryInvalidSyntaxException;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The QueryEngine interprets a query in the form of a String, and returns a single object in the end.
 * If the if getResult() returns null, that means, that no query was run jet.
 * If you query multiple times on the same QueryEngine the result before it will be replaced.
 * Check comments in QueryEngine for syntax tips
 */
public class QueryEngine {
  private DOMNode currentNode;
  private String[] tokens;
  private String query;
  private Pattern queryPattern = Pattern.compile("'([^']+)'|([^\\s']+)");

  private ArrayList<DOMNode> selected;
  private int currentToken = 0;
  private String lastTag = "";

  /*
  SYNTAX:
    FROM '[node type]' - removes all selected nodes, that do not match the criteria
    GET '[node type]'='[node alias]' - gets the first node, that matches the criteria, selects all edges
  Can be stacked/not limited to one, eg.: "FROM machines GET 'ALIAS'=='test machine0' GET 'ALIAS'=='state0'"
   */

  public void queryTop(DOMNode current, String query) throws QueryInvalidSyntaxException, QueryEmptyResultException {
    query(current.getRoot(), query);
  }

  public void query(DOMNode root, String query) throws QueryInvalidSyntaxException, QueryEmptyResultException {
    this.currentNode = root;
    this.query = query;
    tokenize(this.query);
    this.currentToken = 0;
    /*if (tokens.length < 1) {
      throwInvalidSyntaxException(0, "Bounding command");
    }*/

    /*ArrayList<DOMNode> edges = new ArrayList<>(this.currentNode.getEdges());
    this.selected = edges;*/
    this.selected = (ArrayList<DOMNode>) this.currentNode.getEdges().clone();

    while (currentToken < this.tokens.length-1) {
      switch (tokens[currentToken].toUpperCase()) {
        case "FROM" -> {
          lastTag = "FROM";
          fromToken();
        }
        case "GET" -> {
          lastTag = "GET";
          getToken();
        }
        default -> {
          throwInvalidSyntaxException(currentToken, "FROM or GET");
        }
      }
      ++currentToken;
    }
  }

  private void ensureTokenCount(int furtherReqTokens, String message) throws QueryInvalidSyntaxException {
    if (this.currentToken + furtherReqTokens >= this.tokens.length) {
      throwInvalidSyntaxException(this.currentToken+1, message);
    }
  }

  private void fromToken() throws QueryInvalidSyntaxException, QueryEmptyResultException {
    ensureTokenCount(1, "[identifier]");
    ++currentToken;
    this.selected.removeIf(node -> node.getType() != NodeType.fromString(this.tokens[currentToken]));
    if (this.selected.isEmpty()) {
      throw new QueryEmptyResultException(this.query);
    }
  }

  private void getToken() throws QueryInvalidSyntaxException, QueryEmptyResultException {
    ensureTokenCount(3, "[type][==/!=/</>][value]");
    String type = this.tokens[++currentToken];
    String operator = this.tokens[++currentToken];
    String value = this.tokens[++currentToken];
    for (DOMNode node : this.selected) {
      Optional<DOMNode> subNode = node.getEdge(NodeType.fromString(type));
      if (subNode.isEmpty() || !compareVariant(subNode.get().getVariant(), operator, value)) {
        continue;
      }
      this.currentNode = node;
      /*ArrayList<DOMNode> edges = new ArrayList<>(this.currentNode.getEdges());
      this.selected = edges;*/
      this.selected = (ArrayList<DOMNode>) this.currentNode.getEdges().clone();
      return;
    }
    throw new QueryEmptyResultException(this.query);
  }

  private boolean compareVariant(NodeVariant var, String operation, String value) throws QueryInvalidSyntaxException {
    switch (operation) {
      case "==" -> {
        return var.isEqual(value);
      }
      case "!=" -> {
        return var.notEquals(value);
      }
      case "<" -> {
        return var.lesser(value);
      }
      case ">" -> {
        return var.greater(value);
      }
      default -> {
        throwInvalidSyntaxException(currentToken-1, "== or != or < or >");
        return false;
      }
    }
  }

  private void throwInvalidSyntaxException(int tokenIndex, String expected) throws QueryInvalidSyntaxException {
    int positon = tokenIndex;
    for (int i = 0; i < tokenIndex; i++) {
      if (this.tokens.length < i) {
        continue;
      }
      positon += this.tokens[i].length();
    }
    throw new QueryInvalidSyntaxException(this.query, positon, expected);
  }

  private void tokenize(String query) {
    Matcher matcher = queryPattern.matcher(query);
    ArrayList<String> temp = new ArrayList<>();

    while (matcher.find()) {
      temp.add(matcher.group(1) == null ? matcher.group(2) : matcher.group(1));
    }

    this.tokens = temp.toArray(String[]::new);
  }

  public DOMNode getResult() {
    if (lastTag.equals("GET")) {
      return this.currentNode;
    }
    return this.selected.getFirst();
  }

  public ArrayList<DOMNode> getSelected() {
    return selected;
  }
}
