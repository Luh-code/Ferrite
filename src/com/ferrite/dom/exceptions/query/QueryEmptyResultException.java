package com.ferrite.dom.exceptions.query;

public class QueryEmptyResultException extends QueryException {
  public QueryEmptyResultException(String query) {
    super(String.format("The query '%s' ran into an empty result", query));
  }
}
