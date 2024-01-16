package com.ferrite.dom.query;

import com.ferrite.dom.DOMNode;
import com.ferrite.dom.exceptions.query.QueryInvalidSyntaxException;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryEngine {
  private DOMNode root;
  private DOMNode result;
  private String[] tokens;
  private String query;
  private Pattern queryPattern = Pattern.compile("\\s'([^'])|\\s(\\w)");

  public void query(DOMNode root, String query) throws QueryInvalidSyntaxException {
    this.root = root;
    this.query = query;
    tokenize(this.query);
    if (tokens.length < 1) {
      throwInvalidSyntaxException(0, "FROM [type]");
    }

    switch (tokens[0].toUpperCase()) {
      case "FROM" -> {
        fromToken();
      }
    }
  }

  private void fromToken() throws QueryInvalidSyntaxException {
    if (tokens.length < 2) {
      throwInvalidSyntaxException(1, "type");
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
}
