package org.keniding.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        Map<String, String> errorResponse = new HashMap<>();

        if (exception instanceof NoSuchElementException) {
            errorResponse.put("error", "Not Found");
            errorResponse.put("message", exception.getMessage());
            return Response.status(Response.Status.NOT_FOUND).entity(errorResponse).build();
        }

        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).build();
    }
}
