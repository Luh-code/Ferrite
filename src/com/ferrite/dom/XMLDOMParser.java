package com.ferrite.dom;

import com.ferrite.dom.exceptions.*;
import com.ferrite.serialization.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;

public class XMLDOMParser {
  public Node parseNodes(XMLToken[] xmlTokens) throws DOMXMLParsingIllegalTokenTypeException, DOMXMLParsingIllegalTagException, DOMNodeEdgeDuplicationException, DOMNodeRuleNonExistentException, DOMNodeRuleTypeViolationException, DOMNodeRulePluralityViolationException, DOMXMLParsingMissingClosingTokenException, DOMXMLParsingMissingPairingException, DOMXMLParsingMissingOpeningTokenException, DOMXMLParsingNullTokenException, DOMXMLParsingDuplicateVariantException, DOMXMLParsingIllegalNoneTypeVariantSettingException, DOMXMLParsingMismatchedVariantTypeException {
    Node root = null;
    Map<XMLToken, Node> pairing = new HashMap<>();
    Map<Node, XMLToken> revPairing = new HashMap<>();
    BiConsumer<XMLToken, Node> pairingAdder = (XMLToken token, Node node) -> {
      pairing.put(token, node);
      revPairing.put(node, token);
    };
    // The top node of this stack is the 'current' node
    Stack<Node> nodeStack = new Stack<>();
    nodeStack.push(null); // push null to make sure, that the current node is not set
    for (XMLToken token : xmlTokens) {
      if (token == null) {
        throw new DOMXMLParsingNullTokenException();
      }
      // Check token type
      switch (token) {
        case XMLOpeningToken xmlOpeningToken -> {
          Node temp = new Node(getNodeType(xmlOpeningToken.getData())); // Create node from token

          pairingAdder.accept(xmlOpeningToken, temp); // add to pairings

          if (nodeStack.peek() == null) { // set current if null
            nodeStack.pop();
            nodeStack.push(temp);
          } else if (revPairing.containsKey(nodeStack.peek())) { // otherwise get pairing
            XMLToken currentToken = revPairing.get(nodeStack.peek());
            // Check if the depth of the currentToken is as deep as the new token
            if (xmlOpeningToken.getDepth() > currentToken.getDepth()) {
              // If so, set current to new and create edge between current and the new node
              nodeStack.peek().addEdge(temp);
              nodeStack.push(temp);
            } else {
              throw new DOMXMLParsingMissingClosingTokenException(nodeStack.peek());
            }
          } else {
            throw new DOMXMLParsingMissingPairingException(nodeStack.peek());
          }
          if (root == null) { // set root if null
            root = temp;
          }
        }
        case XMLClosingToken xmlClosingToken -> {
          // Throw if currently no node is current
          if (nodeStack.peek() == null) {
            throw new DOMXMLParsingMissingOpeningTokenException(xmlClosingToken);
          }
          // Throw if the current node is missing its pairing
          if (!revPairing.containsKey(nodeStack.peek())) {
            throw new DOMXMLParsingMissingPairingException(nodeStack.peek());
          }
          // Throw if the closing token does not match with the opening token of the current node
          if (revPairing.get(nodeStack.peek()) != xmlClosingToken.getPairToken()) {
            throw new DOMXMLParsingMissingOpeningTokenException(xmlClosingToken);
          }
          // Remove the current node from being current
          nodeStack.pop();
          // Add null if stack is otherwise empty, to indicate, that there is no current node
          if (nodeStack.isEmpty()) {
            nodeStack.push(null);
          }
        }
        case XMLAttributeToken xmlAttributeToken -> {
          Node temp = new Node(getNodeType(xmlAttributeToken.getData())); // Create node from tag
          pairingAdder.accept(xmlAttributeToken, temp);
          // Throw if current is null
          if (nodeStack.peek() == null) {
            throw new DOMXMLParsingMissingOpeningTokenException(xmlAttributeToken);
          }
          nodeStack.peek().addEdge(temp);
        }
        case XMLTextToken xmlTextToken -> {
          // Throw if current is null
          if (nodeStack.peek() == null) {
            throw new DOMXMLParsingMissingOpeningTokenException(xmlTextToken);
          }
          if (nodeStack.peek().getVariant() != null) {
            throw new DOMXMLParsingDuplicateVariantException(nodeStack.peek());
          }
          try {
            switch (nodeStack.peek().getType().getRule().getVariantType()) {
              case INTEGER -> {
                nodeStack.peek().setVariant(new NodeVariant(Integer.parseInt(xmlTextToken.getText())));
              }
              case FLOAT -> {
                nodeStack.peek().setVariant(new NodeVariant(Float.parseFloat(xmlTextToken.getText())));
              }
              case STRING -> {
                nodeStack.peek().setVariant(new NodeVariant(xmlTextToken.getText()));
              }
              case BOOLEAN -> {
                nodeStack.peek().setVariant(new NodeVariant(Boolean.parseBoolean(xmlTextToken.getText())));
              }
              case NONE -> {
                throw new DOMXMLParsingIllegalNoneTypeVariantSettingException(xmlTextToken.getText());
              }
            }
          } catch (NumberFormatException e) {
            throw new DOMXMLParsingMismatchedVariantTypeException(e);
          }
        }
        case null, default -> throw new DOMXMLParsingIllegalTokenTypeException(token);
      }
    }
    return root;
  }

  private NodeType getNodeType(String tag) throws DOMXMLParsingIllegalTagException {
    NodeType match = NodeType.fromString(tag.toUpperCase());
    if (match == null) {
      throw new DOMXMLParsingIllegalTagException(tag);
    }
    return match;
  }
}
