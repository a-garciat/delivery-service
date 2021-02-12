package es.udc.rs.deliveries.jaxrs.exceptions;

import es.udc.rs.deliveries.jaxrs.dto.ExceptionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.HasShipmentExceptionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.InputValidationExceptionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.InstanceNotFoundExceptionDtoJaxb;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.HasShipmentException;
import es.udc.ws.util.exceptions.InputValidationException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class HasShipmentsExceptionMapper implements ExceptionMapper<HasShipmentException> {
    @Override
    public Response toResponse(HasShipmentException e) {
        ExceptionDtoJaxb exceptionDtoJaxb = new ExceptionDtoJaxb("HasShipments");
        exceptionDtoJaxb.addParam("message", e.getMessage());
        exceptionDtoJaxb.addParam("customerId", String.valueOf(e.getCustomerId()));
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(exceptionDtoJaxb).build();

    }
}
