package es.udc.rs.deliveries.jaxrs.exceptions;

import es.udc.rs.deliveries.jaxrs.dto.ExceptionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.InvalidStatusChangeExceptionDtoJaxb;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidStatusChangeException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidStatusChangeExceptionMapper implements ExceptionMapper<InvalidStatusChangeException> {

    @Override
    public Response toResponse(InvalidStatusChangeException ex) {
        ExceptionDtoJaxb exceptionDtoJaxb = new ExceptionDtoJaxb("InvalidStatusChange");
        exceptionDtoJaxb.addParam("oldStatus", ex.getOldStatus().toString());
        exceptionDtoJaxb.addParam("newStatus", ex.getNewStatus().toString());
        return Response.status(Response.Status.NOT_FOUND).entity(exceptionDtoJaxb).build();
    }
}
