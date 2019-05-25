package ru.ifmo.wst.client;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
class Result<T> {
  private boolean error;
  private String errorMessage;
  private T result;

  static <T> Result<T> error(String message) {
    return new Result<>(true, message, null);
  }

  static <T> Result<T> ok(T result) {
    return new Result<>(false, null, result);
  }
}