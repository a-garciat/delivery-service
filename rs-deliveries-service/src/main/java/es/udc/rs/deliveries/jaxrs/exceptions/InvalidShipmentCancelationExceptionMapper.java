package es.udc.rs.deliveries.jaxrs.exceptions;

import es.udc.rs.deliveries.jaxrs.dto.ExceptionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.InvalidShipmentCancelationExceptionDtoJaxb;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidShipmentCancelationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidShipmentCancelationExceptionMapper implements ExceptionMapper<InvalidShipmentCancelationException> {

    @Override
    public Response toResponse(InvalidShipmentCancelationException e) {
        ExceptionDtoJaxb exceptionDtoJaxb = new ExceptionDtoJaxb("InvalidShipmentCancelation");
        exceptionDtoJaxb.addParam("oldStatus", e.getOldStatus().name());
        return Response.status(Response.Status.BAD_REQUEST).entity(exceptionDtoJaxb).build();
    }
}
