package com.ferrite.dom.exceptions.query;

public class QueryInvalidSyntaxException extends QueryException {
  public QueryInvalidSyntaxException(String query, int position, String expected) {
    super(String.format("Query Syntax Error:\n'%s'\n%s - expected '%s'", query, " ".repeat(position)+"^", expected));
  }
}
