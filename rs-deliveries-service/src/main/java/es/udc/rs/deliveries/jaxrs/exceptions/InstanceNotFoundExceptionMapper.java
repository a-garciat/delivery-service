package es.udc.rs.deliveries.jaxrs.exceptions;

import es.udc.rs.deliveries.jaxrs.dto.ExceptionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.InstanceNotFoundExceptionDtoJaxb;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InstanceNotFoundExceptionMapper implements
        ExceptionMapper<InstanceNotFoundException> {

    @Override
    public Response toResponse(InstanceNotFoundException ex) {
        ExceptionDtoJaxb exceptionDtoJaxb = new ExceptionDtoJaxb("InstanceNotFound");
        exceptionDtoJaxb.addParam("instanceId", ex.getInstanceId().toString());
        exceptionDtoJaxb.addParam("instanceType", ex.getInstanceType().substring(ex.getInstanceType().lastIndexOf('.') + 1));
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(exceptionDtoJaxb).build();

    }
}