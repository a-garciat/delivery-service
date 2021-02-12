package es.udc.rs.deliveries.jaxrs.exceptions;

import es.udc.rs.deliveries.jaxrs.dto.ExceptionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.InputValidationExceptionDtoJaxb;
import es.udc.ws.util.exceptions.InputValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InputValidationExceptionMapper implements ExceptionMapper<InputValidationException> {
    @Override
    public Response toResponse(InputValidationException ex) {
        ExceptionDtoJaxb exceptionDtoJaxb = new ExceptionDtoJaxb("InputValidation");
        exceptionDtoJaxb.addParam("message", ex.getMessage());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(exceptionDtoJaxb)
                .build();
    }
}
