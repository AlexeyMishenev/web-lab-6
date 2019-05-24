package ru.ifmo.wst.rs;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ResourceExceptionMapper implements ExceptionMapper<ResourceException> {
  @Override
  public Response toResponse(ResourceException e) {
    return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
  }
}
