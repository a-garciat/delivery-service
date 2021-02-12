package es.udc.rs.deliveries.jaxrs.resources;

import es.udc.rs.deliveries.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.deliveries.jaxrs.util.CustomerToCustomerDtoJaxbConversor;
import es.udc.rs.deliveries.jaxrs.util.ServiceUtil;
import es.udc.rs.deliveries.model.customer.Customer;
import es.udc.rs.deliveries.model.deliveryservice.DeliveryService;
import es.udc.rs.deliveries.model.deliveryservice.DeliveryServiceFactory;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.HasShipmentException;
import es.udc.rs.deliveries.model.shipment.ShipmentStatus;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Path("customers")
public class CustomerResource {

    @GET
    @Path("/{customerId}")
    @Operation(
            tags = {"customers"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(schema = @Schema(implementation = CustomerDtoJaxb.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found"
                    )
            }
    )
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public CustomerDtoJaxb findById(@PathParam("customerId") final String customerId, @Context UriInfo uriInfo, @Context HttpHeaders headers) throws InputValidationException, InstanceNotFoundException {
        Long id;
        try {
            id = Long.valueOf(customerId);

        } catch (final NumberFormatException exception) {
            throw new InputValidationException("Invalid Request: "
                    + "unable to parse customer id '" + customerId + "'");
        }
        return CustomerToCustomerDtoJaxbConversor.toCustomerDto(DeliveryServiceFactory.getService().findCustomerById(id), uriInfo.getBaseUri(),
                ServiceUtil.getTypeAsStringFromHeaders(headers));
    }

    @GET
    @Operation(
            tags = {"customers"},
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(schema = @Schema(implementation = CustomerDtoJaxb.class))
            )
    )
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    public Response findCustomersByName(@QueryParam("name") final String name, @Context UriInfo uriInfo, @Context HttpHeaders headers) {
        String type = ServiceUtil.getTypeAsStringFromHeaders(headers);

        final String parsedName = name == null ? "" : name;

        final List<Customer> customers = DeliveryServiceFactory.getService().findCustomersByName(parsedName);

        Link selfLink = getSelfLink(uriInfo.getBaseUri(), type, CustomerResource.class, parsedName);

        List<CustomerDtoJaxb> customerDtos = CustomerToCustomerDtoJaxbConversor.toCustomerDto(customers, uriInfo.getBaseUri(), type);

        Response.ResponseBuilder response = Response.ok().entity( new GenericEntity<List<CustomerDtoJaxb>>(customerDtos){});
        response.links(selfLink);
        return response.build();
    }

    private Link getSelfLink(URI baseUri, String type, Class<?> resourceClass, String name) {
        return ServiceUtil.getCustomerIntervalLink(baseUri, resourceClass, name, "self", "Current list of customers", type);
    }

    @DELETE
    @Path("/{customerId}")
    @Operation(
            tags = {"customers"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Customer deleted",
                            content = @Content(schema = @Schema(implementation = CustomerDtoJaxb.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request or customer has associated shipments"
                    )
            }

    )
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void deleteCustomer(@PathParam("customerId") final String id) throws InputValidationException, InstanceNotFoundException, HasShipmentException {
        Long customerId;
        try {
            customerId = Long.valueOf(id);
        } catch (final NumberFormatException ex) {
            throw new InputValidationException("Invalid Request: " + "unable to parse customer id '" + id + "'");
        }
        DeliveryServiceFactory.getService().removeCustomer(customerId);
    }

    @POST
    @Path("{customerId}/deleteCustomer")
    @Operation(
            tags = {"customers"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Customer deleted",
                            content = @Content(schema = @Schema(implementation = CustomerDtoJaxb.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request or customer has associated shipments"
                    )
            }

    )
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void deleteCustomerOverloadedPost(@PathParam("customerId") final String id) throws InputValidationException, InstanceNotFoundException, HasShipmentException {
        deleteCustomer(id);
    }

    @PUT
    @Path("/{customerId}")
    @Operation(
            tags = {"customers"},
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Customer updated",
                            content = @Content(schema = @Schema(implementation = CustomerDtoJaxb.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Customer not found"
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    )
            }

    )
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void updateCustomer(final CustomerDtoJaxb customerDto, @PathParam("customerId") final String id) throws
            InputValidationException, InstanceNotFoundException {
        Long customerId;
        try {
            customerId = Long.valueOf(id);
        } catch (final NumberFormatException ex) {
            throw new InputValidationException("Invalid Request: " + "unable to parse customer id '" + id + "'");
        }
        if (!customerId.equals(customerDto.getCustomerId())) {
            throw new InputValidationException("Invalid Request: invalid customer Id '" + customerDto.getCustomerId() +
                    "'" + " for customer '" + customerId + "'");
        }
        final Customer customer = CustomerToCustomerDtoJaxbConversor.toCustomer(customerDto);
        DeliveryServiceFactory.getService().updateCustomer(customer);
    }

    @POST
    @Operation(
            tags = {"customers"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Customer created",
                            content = @Content(schema = @Schema(implementation = CustomerDtoJaxb.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Bad request"
                    )
            }

    )
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response addCustomer(final CustomerDtoJaxb customerDto, @Context final UriInfo uriInfo, @Context HttpHeaders headers) throws
            InputValidationException {
        Customer customer = CustomerToCustomerDtoJaxbConversor.toCustomer(customerDto);
        customer = DeliveryServiceFactory.getService().addCustomer(customer);
        final CustomerDtoJaxb resultCustomerDto = CustomerToCustomerDtoJaxbConversor.toCustomerDto(customer, uriInfo.getBaseUri(),
                ServiceUtil.getTypeAsStringFromHeaders(headers));
        final String requestUri = uriInfo.getRequestUri().toString();
        return Response.created(URI.create(requestUri + (requestUri.endsWith("/") ? "" : "/") + customer.getCustomerId()))
                .entity(resultCustomerDto).build();
    }


}