package com.ferrite.dom;

import com.ferrite.FerriteException;
import com.ferrite.controller.Controller;
import com.ferrite.dom.treewalker.TreeWalker;
import com.ferrite.serialization.XMLToken;
import com.ferrite.serialization.XMLTokenizer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Set;

public class Document {
  private DOMNode root;

  public void deserializeDocument(String filename) throws FerriteException {
    this.root = deserialize(filename);
  }

  private DOMNode deserialize(String filename) throws FerriteException {
    String xmlText;
    try {
      xmlText = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    XMLTokenizer tokenizer = new XMLTokenizer();
    tokenizer.tokenize(xmlText);
    XMLToken[] tokens = tokenizer.getTokens();

    XMLParser xmlParser = new XMLParser();
    xmlParser.parseNodes(tokens);
    importAll(xmlParser.getNodes());

    return xmlParser.getRoot();
  }

  public DOMNode getRoot() {
    return root;
  }

  private void importAll(Set<DOMNode> nodes) throws FerriteException {
    for (DOMNode node : nodes) { // for every node
      // Check if external
      Optional<DOMNode> external = node.getEdge(NodeType.EXTERNAL);
      if (external.isEmpty()) {
        continue;
      }
      NodeVariant externalVar = external.get().getVariant();
      if (externalVar == null || externalVar.getType() != NodeVariantType.BOOLEAN || !externalVar.getBoolean()) {
        continue;
      }

      // If yes, check path
      Optional<DOMNode> path = node.getEdge(NodeType.PATH);
      if (path.isEmpty()) {
        continue;
      }
      NodeVariant pathVar = path.get().getVariant();
      if (pathVar == null || pathVar.getType() != NodeVariantType.STRING) {
        continue;
      }

      // If path exists, deserialize document at path and add node, recursively
      DOMNode subroot = deserialize(pathVar.getString());
      node.replaceEdges(subroot);
    }


  }

  public static void main(String[] args) {
    Document d = new Document();
    try {
      d.deserializeDocument("src/test.xml");
    } catch (FerriteException e) {
      throw new RuntimeException(e);
    }

    Controller controller = new Controller(32, 32);

    TreeWalker tw = new TreeWalker(controller);
    tw.dispatch(d.getRoot());
  }
}
