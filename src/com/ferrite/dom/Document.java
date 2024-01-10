package com.ferrite.dom;

import com.ferrite.dom.exceptions.*;
import com.ferrite.serialization.XMLToken;
import com.ferrite.serialization.XMLTokenizer;
import com.ferrite.serialization.exceptions.SerializationMismatchedAttributeValueCountException;
import com.ferrite.serialization.exceptions.SerializationTokenMissingClosingTagException;
import com.ferrite.serialization.exceptions.SerializationTokenMissingOpeningTagException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Document {
  private DOMNode root;

  public void deserialize(String filename) {
    String xmlText;
    try {
      xmlText = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    XMLTokenizer tokenizer = new XMLTokenizer();
    try {
      tokenizer.tokenize(xmlText);
    } catch (SerializationTokenMissingClosingTagException | SerializationTokenMissingOpeningTagException |
             SerializationMismatchedAttributeValueCountException e) {
      throw new RuntimeException(e);
    }
    XMLToken[] tokens = tokenizer.getTokens();

    XMLParser xmlParser = new XMLParser();
    try {
      xmlParser.parseNodes(tokens);
    } catch (DOMXMLParsingIllegalTokenTypeException | DOMXMLParsingIllegalTagException |
             DOMNodeEdgeDuplicationException | DOMNodeRuleNonExistentException | DOMNodeRuleTypeViolationException |
             DOMNodeRulePluralityViolationException | DOMXMLParsingMissingClosingTokenException |
             DOMXMLParsingMissingPairingException | DOMXMLParsingMissingOpeningTokenException |
             DOMXMLParsingNullTokenException | DOMXMLParsingDuplicateVariantException |
             DOMXMLParsingIllegalNoneTypeVariantSettingException | DOMXMLParsingMismatchedVariantTypeException e) {
      throw new RuntimeException(e);
    }

  }

  public static void main(String[] args) {
    Document d = new Document();
    d.deserialize("src/test.xml");
  }
}
