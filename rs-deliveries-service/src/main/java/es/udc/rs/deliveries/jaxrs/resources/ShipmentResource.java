package es.udc.rs.deliveries.jaxrs.resources;

import es.udc.rs.deliveries.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.ShipmentDtoJaxb;
import es.udc.rs.deliveries.jaxrs.util.CustomerToCustomerDtoJaxbConversor;
import es.udc.rs.deliveries.jaxrs.util.ServiceUtil;
import es.udc.rs.deliveries.jaxrs.util.ShipmentToShipmentDtoJaxbConversor;
import es.udc.rs.deliveries.model.customer.Customer;
import es.udc.rs.deliveries.model.deliveryservice.DeliveryService;
import es.udc.rs.deliveries.model.deliveryservice.DeliveryServiceFactory;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidShipmentCancelationException;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidStatusChangeException;
import es.udc.rs.deliveries.model.shipment.Shipment;
import es.udc.rs.deliveries.model.shipment.ShipmentStatus;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.annotation.Target;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.rs.deliveries.model.shipment.ShipmentStatus.DELIVERED;

@OpenAPIDefinition(
        info = @Info(
                title = "Deliveries Service",
                description = "System for managing deliveries",
                version = "2.0",
                contact = @Contact(
                        name = "Grupo IA 04",
                        url = "https://git.fic.udc.es/docencia-ia-2021/ia04"
                ),
                license = @License(
                        name = "BSD",
                        url = "https://opensource.org/licenses/BSD-3-Clause"
                )
        ),
        servers = @Server(

                url = "http://localhost:7070/rs-deliveries-service"
        ),
        tags = {
                @Tag(
                        name = "customers"
                ),
                @Tag(name = "shipments")
        }
)
@Path("shipments")
public class ShipmentResource {

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Operation(
            tags = {"shipments"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "CREATED SHIPMENT"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BAD REQUEST"
                    )
            }
    )
    public Response addShipment(final ShipmentDtoJaxb shipmentDto, @Context final UriInfo uriInfo, @Context HttpHeaders headers) throws
            InputValidationException {
        Shipment shipment = ShipmentToShipmentDtoJaxbConversor.toShipment(shipmentDto);
        shipment = DeliveryServiceFactory.getService().addShipment(shipment);
        final ShipmentDtoJaxb resultShipmentDto = ShipmentToShipmentDtoJaxbConversor.toShipmentDto(shipment, uriInfo.getBaseUri(),
                ServiceUtil.getTypeAsStringFromHeaders(headers));
        final String requestUri = uriInfo.getRequestUri().toString();
        return Response.created(URI.create(requestUri + (requestUri.endsWith("/") ? "" : "/") + shipment.getCustomerId()))
                .entity(resultShipmentDto).build();
    }

    /*@GET
    @Path("/0")
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public ShipmentDtoJaxb createFake(@PathParam("shipmentId") final String customerId) {
        return new ShipmentDtoJaxb(1L, 1L, 1L, "Calle Vistaalegre", DELIVERED, LocalDateTime.now(), LocalDateTime.now(), 2);
    }*/

    @POST
    @Path("/{shipmentId}/changeStatus")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Operation(
            tags = {"shipments"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "SHIPMENT STATUS UPDATED"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BAD REQUEST OR INVALID STATUS CHANGE"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND"
                    )
            }
    )
    public void updateShipmentStatus(@QueryParam("newStatus") final String newStatus,
                                     @PathParam("shipmentId") final String shipmentId)
            throws InstanceNotFoundException, InputValidationException, InvalidStatusChangeException {
        Long longShipmentId;
        ShipmentStatus statusNewStatus;
        try {
            statusNewStatus = ShipmentStatus.valueOf(newStatus != null ? newStatus : "");
            longShipmentId = Long.valueOf(shipmentId);
        } catch (final NumberFormatException ex) {
            throw new InputValidationException("Invalid Request: " + "unable to parse shipment id '" + shipmentId + "'");
        } catch (final IllegalArgumentException ex) {
            throw new InputValidationException("Invalid Request: unable to parse new status '" + newStatus + "'");
        }
        DeliveryServiceFactory.getService().updateShipmentStatus(longShipmentId, statusNewStatus);
    }

    @POST
    @Path("/{shipmentId}/changeStatus/cancel")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Operation(
            tags = {"shipments"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "SHIPMENT CANCELED"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BAD REQUEST OR INVALID SHIPMENT CANCELATION"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND"
                    )
            }
    )
    public void cancelStatus(@PathParam("shipmentId") final String shipmentId)
            throws InstanceNotFoundException, InputValidationException, InvalidShipmentCancelationException {
        Long longShipmentId;
        try {
            longShipmentId = Long.valueOf(shipmentId);
        } catch (final NumberFormatException ex) {
            throw new InputValidationException("Invalid Request: " + "unable to parse shipment id '" + shipmentId + "'");
        }
        DeliveryServiceFactory.getService().cancelShipment(longShipmentId);
    }

    @GET
    @Path("/{shipmentId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Operation(
            tags = {"shipments"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "FOUND SHIPMENT"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BAD REQUEST"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "NOT FOUND"
                    )
            }
    )
    public ShipmentDtoJaxb findShipmentById(@PathParam("shipmentId") final String shipmentId, @Context final UriInfo uriInfo, @Context HttpHeaders headers)
            throws InputValidationException, InstanceNotFoundException {
        Long id;
        try {
            id = Long.valueOf(shipmentId);
        } catch (final NumberFormatException exception) {
            throw new InputValidationException("Invalid Request: "
                    + "unable to parse shipment id '" + shipmentId + "'");
        }

        return ShipmentToShipmentDtoJaxbConversor.toShipmentDto(DeliveryServiceFactory.getService().findShipmentById(id), uriInfo.getBaseUri(),
                ServiceUtil.getTypeAsStringFromHeaders(headers));
    }


    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Operation(
            tags = {"shipments"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "FOUND SHIPMENTS"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "BAD REQUEST"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CUSTOMERID SPECIFIED DOES NOT EXIST"
                    )
            }
    )
    public Response findShipmentsByCustomerIdAndStatus(@QueryParam("customerId") final String customerIdString,
                                                       @QueryParam("status") final String status,
                                                       @QueryParam("first") final String first,
                                                       @QueryParam("max") final String max,
                                                       @Context final UriInfo uriInfo,
                                                       @Context HttpHeaders headers)
            throws InputValidationException, InstanceNotFoundException {

        String type = ServiceUtil.getTypeAsStringFromHeaders(headers);
        int firstShipment, maxShipment;
        ShipmentStatus shipmentStatus;
        Long customerId;

        try {
            customerId = Long.valueOf(customerIdString);
        } catch (final NumberFormatException exception) {
            throw new InputValidationException("Invalid Request: "
                    + "unable to parse customerId field '" + customerIdString + "'");
        }

        try {
            firstShipment = Integer.valueOf(first);
        } catch (final NumberFormatException exception) {
            throw new InputValidationException("Invalid Request: "
                    + "unable to parse 'first' field '" + first + "'");
        }

        try {
            maxShipment = Integer.valueOf(max);
            shipmentStatus = status != null ? ShipmentStatus.valueOf(status) : null;
            /*ShipmentStatus.valueOf(status != null ? status : );*/
        } catch (final NumberFormatException exception) {
            throw new InputValidationException("Invalid Request: "
                    + "unable to parse 'max' field '" + max + "'");
        } catch (final IllegalArgumentException ex) {
            throw new InputValidationException("Invalid Request: unable to parse new status '" + status + "'");
        }

        List<Shipment> shipments = DeliveryServiceFactory.getService().findShipmentsByCustomerIdAndStatus(customerId, shipmentStatus, firstShipment, maxShipment);
        List<ShipmentDtoJaxb> shipmentDtos = ShipmentToShipmentDtoJaxbConversor.toShipmentDto(shipments, uriInfo.getBaseUri(), type);

        Link nextLink = getNextLink(uriInfo.getBaseUri(), shipmentStatus, firstShipment, maxShipment, shipments.size(), type, ShipmentResource.class, customerId);
        Link previousLink = getPreviousLink(uriInfo.getBaseUri(), shipmentStatus, firstShipment, maxShipment, type, ShipmentResource.class, customerId);
        Link selfLink = getSelfLink(uriInfo.getBaseUri(), shipmentStatus, firstShipment, maxShipment, type, ShipmentResource.class, customerId);
        Response.ResponseBuilder response = Response.ok().entity(new GenericEntity<List<ShipmentDtoJaxb>>(shipmentDtos){});
        response.links(selfLink);
        if (nextLink != null) {
            response.links(nextLink);
        }
        if (previousLink != null) {
            response.links(previousLink);
        }
        return response.build();
    }

    private static Link getNextLink(URI baseUri, ShipmentStatus status,
                                    int startIndex, int count, int numberOfProducts, String type, Class<?> resourceClass, long customerId) {
        if (numberOfProducts < count) {
            return null;
        }
        return ServiceUtil.getShipmentsIntervalLink(baseUri, resourceClass, startIndex
                + count, count, customerId, status, "next", "Next " + count + " shipments", type);
    }

    private Link getPreviousLink(URI baseUri, ShipmentStatus status,
                                 int startIndex, int count, String type, Class<?> resourceClass, long customerId) {
        if (startIndex <= 1) {
            return null;
        }
        startIndex = startIndex - count;
        if (startIndex < 1) {
            startIndex = 1;
        }
        return ServiceUtil.getShipmentsIntervalLink(baseUri, resourceClass, startIndex,
                count, customerId, status, "previous", "Previous " + count + " shipments", type);
    }

    private Link getSelfLink(URI baseUri, ShipmentStatus status,
                             int startIndex, int count, String type, Class<?> resourceClass, long customerId) {
        return ServiceUtil.getShipmentsIntervalLink(baseUri, resourceClass, startIndex,
                count, customerId, status, "self", "Current interval of shipments", type);
    }
}
