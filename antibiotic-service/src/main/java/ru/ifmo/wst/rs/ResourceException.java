package ru.ifmo.wst.rs;

import javax.ws.rs.core.Response;
import lombok.Getter;

public class ResourceException extends Exception {
  @Getter
  private final String reason;

  private final Response.Status status;

  public ResourceException(String reason) {
    this(reason, Response.Status.BAD_REQUEST);
  }

  public ResourceException(String reason, Response.Status status) {
    this(null, reason, status);
  }

  public ResourceException(String message, String reason, Response.Status status) {
    super();
    this.reason = reason;
    this.status = status;
  }
}