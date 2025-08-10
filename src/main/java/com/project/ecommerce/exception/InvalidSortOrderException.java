package com.project.ecommerce.exception;

public class InvalidSortOrderException extends RuntimeException {
  public InvalidSortOrderException(String order) {
    super("Invalid sort order: " + order +
            ". You can only use 'asc', 'desc', or omit this parameter to default to 'asc'.");
  }
}