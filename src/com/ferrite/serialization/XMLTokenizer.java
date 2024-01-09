package com.ferrite.serialization;

import com.ferrite.serialization.exceptions.SerializationMismatchedAttributeValueCountException;
import com.ferrite.serialization.exceptions.SerializationTokenMissingClosingTagException;
import com.ferrite.serialization.exceptions.SerializationTokenMissingOpeningTagException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

// A regex-based XML Tokenizer with builtin syntax checking
public class XMLTokenizer {
  private XMLToken[] tokens;
  private Pattern xmlPattern = Pattern.compile("<[^>]+>|[^<]+");
  private Pattern xmlAttibutePattern = Pattern.compile("\\s*([^\\s=]*)\\s*(?:=\\s*\"([^\"]*)\"|=\\s*([^\\s>]*))");

  public void tokenize(String xmlString) throws SerializationTokenMissingClosingTagException, SerializationTokenMissingOpeningTagException, SerializationMismatchedAttributeValueCountException {
    Matcher tagMatcher = xmlPattern.matcher(xmlString);

    ArrayList<XMLToken> temp = new ArrayList<>();

    Stack<XMLOpeningToken> openTokens = new Stack<>();
    //ArrayDeque<XMLToken> closedTokens = new ArrayDeque<>();

    while (tagMatcher.find()) {
      String part = tagMatcher.group();
      //Matcher strippedMatcher = xmlTagStripper.matcher(part);
      String strippedPart = part;
      if (strippedPart.startsWith("<")) {
        strippedPart = strippedPart.substring(1);
        if (strippedPart.startsWith("/")) {
          strippedPart = strippedPart.substring(1);
        }
      }
      if(strippedPart.endsWith(">")) {
        strippedPart = strippedPart.substring(0, strippedPart.length()-1);
      }
      String[] strippedSubParts = strippedPart.split(" ");
      String tag = (strippedSubParts.length>0 ? strippedSubParts[0] : "");

      //System.out.println(part);
      if (!part.startsWith("<")) { // If eval to true, it is plain text
        // Add and match token
        XMLTextToken tempToken = new XMLTextToken(part, openTokens.size());
        if (openTokens.isEmpty()) {
          throw new SerializationTokenMissingOpeningTagException(tempToken);
        }
        tempToken.setPairToken(openTokens.peek());
        temp.add(tempToken);
        continue;
      }

      if (part.charAt(1) == '/') { // if eval to true it is a closing tag
        if (openTokens.isEmpty() || !openTokens.peek().getTag().equals(strippedPart)) {
          throw new SerializationTokenMissingClosingTagException(openTokens.peek().getTag());
        }
        // Set pair tag in openTokens.peek()
        XMLClosingToken tempToken = new XMLClosingToken(tag, openTokens.size()-1);
        tempToken.setPairToken(openTokens.peek());
        openTokens.peek().setPairToken(tempToken);
        // Add closing token to list
        temp.add(tempToken);
        // Remove opening token from openTokens
        openTokens.pop();
        continue;
      }

      // otherwise it must be an opening tag, push to openTokens and add to token list
      XMLOpeningToken tempToken = new XMLOpeningToken(tag, openTokens.size());
      openTokens.push(tempToken);
      temp.add(tempToken);

      // search for attributes ana dd them to the Token list
      int firstSpace = strippedPart.indexOf(" ");
      if(firstSpace == -1) {
        continue;
      }
      XMLAttributeToken[] attributes = extractAttributes(strippedPart.substring(firstSpace), tempToken);
      temp.addAll(Arrays.stream(attributes).toList());
    }

    if (!openTokens.isEmpty()) {
      throw new SerializationTokenMissingClosingTagException(openTokens.peek().getTag());
    }

    this.tokens = temp.toArray(XMLToken[]::new);
  }

  private XMLAttributeToken[] extractAttributes(String header, XMLOpeningToken token) throws SerializationMismatchedAttributeValueCountException {
    Matcher attributeMatcher = xmlAttibutePattern.matcher(header); // Extract Attributes
    ArrayList<String> attributeNames = new ArrayList<>();
    ArrayList<String> attributeValues = new ArrayList<>();
    while (attributeMatcher.find()) {
      // Get attribute names from regex
      String name = attributeMatcher.group(1);
      if (name != null && !name.isEmpty()) {
        attributeNames.add(name);
      }
      // Get attribute values form regex
      String value = attributeMatcher.group(2) != null ? attributeMatcher.group(2) :
              (attributeMatcher.group(3).isEmpty() ? null : attributeMatcher.group(3));
      if (value != null) {
        attributeValues.add(value);
      }
    }

    if (attributeNames.size() != attributeValues.size()) {
      throw new SerializationMismatchedAttributeValueCountException(attributeNames.size(), attributeValues.size());
    }

    // Create XMLAttributeToken[] using attributeNames and attributeValues
    return IntStream.range(0, attributeNames.size())
            .mapToObj(i -> new XMLAttributeToken(attributeNames.get(i), token.getDepth(), attributeValues.get(i)))
            .peek(i -> {
              i.setPairToken(token);
              token.addAttribute(i);
            })
            .toArray(XMLAttributeToken[]::new);
  }

  public static void main(String[] args) {
    XMLTokenizer tokenizer = new XMLTokenizer();
    try {
      tokenizer.tokenize("<outer_tag attr2=\"some value\" attr4=345><inner_tag><tag attr1=5 attr3=\"a b c\"></tag></inner_tag><another_tag>some data</another_tag></outer_tag>");
    } catch (SerializationTokenMissingClosingTagException | SerializationTokenMissingOpeningTagException |
             SerializationMismatchedAttributeValueCountException e) {
      throw new RuntimeException(e);
    }
    //tokenizer.tokenize("<root><element1>value1</element1><element2>value2</element2></root>");
  }
}
