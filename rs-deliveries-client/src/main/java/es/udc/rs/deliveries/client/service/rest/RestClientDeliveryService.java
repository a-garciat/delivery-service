package es.udc.rs.deliveries.client.service.rest;

import es.udc.rs.deliveries.client.service.rest.exception.HasShipmentException;
import es.udc.rs.deliveries.client.service.rest.exception.InvalidShipmentCancelationException;
import es.udc.rs.deliveries.client.service.rest.exception.InvalidStatusChangeException;
import es.udc.rs.deliveries.client.service.rest.jaxbconversor.CustomerDtoToCustomerDtoJaxbConversor;
import es.udc.rs.deliveries.client.service.rest.json.JaxbJsonContextResolver;
import org.glassfish.jersey.jackson.JacksonFeature;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import es.udc.rs.deliveries.client.service.ClientDeliveryService;
import es.udc.rs.deliveries.client.service.rest.dto.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.ArrayList;
import java.util.List;

public abstract class RestClientDeliveryService implements ClientDeliveryService {

	private static javax.ws.rs.client.Client client = null;

	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientDeliveryService.endpointAddress";
	private WebTarget endPointWebTarget = null;

	/*
	 * Client instances are expensive resources. It is recommended a configured
	 * instance is reused for the creation of Web resources. The creation of Web
	 * resources, the building of requests and receiving of responses are
	 * guaranteed to be thread safe. Thus a Client instance and WebTarget
	 * instances may be shared between multiple threads.
	 */
	private static Client getClient() {
		if (client == null) {
			client = ClientBuilder.newClient();
			client.register(JacksonFeature.class);
			client.register(JaxbJsonContextResolver.class);
		}
		return client;
	}

	private WebTarget getEndpointWebTarget() {
		if (endPointWebTarget == null) {
			endPointWebTarget = getClient()
					.target(ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER));
		}
		return endPointWebTarget;
	}
	
	protected abstract MediaType getMediaType();

	@Override
	public CustomerDtoJaxb addCustomer(CustomerDtoJaxb c) throws InputValidationException {
		WebTarget wt = getEndpointWebTarget().path("customers");
		Response response = wt.request().accept(this.getMediaType())
				.post(Entity.entity(CustomerDtoToCustomerDtoJaxbConversor.toJaxbCustomer(c), this.getMediaType()));
		try {
			validateResponse(Response.Status.CREATED.getStatusCode(), response);
			CustomerDtoJaxb resultCustomer = response.readEntity(CustomerDtoJaxb.class);
			return resultCustomer;
		} catch (InputValidationException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	@Override
	public void deleteCustomer(long id) throws InstanceNotFoundException {
		WebTarget wt = getEndpointWebTarget().path("customers/{id}").resolveTemplate("id", id);
		Response response = wt.request().accept(this.getMediaType()).delete();
		try {
			validateResponse(Response.Status.NO_CONTENT.getStatusCode(), response);
		} catch (InstanceNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	@Override
	public void updateShipmentStatus(long id, ShipmentStatus newStatus) throws InputValidationException, InstanceNotFoundException {
		WebTarget wt = getEndpointWebTarget().path("shipments/{id}/changeStatus").resolveTemplate("id", id).queryParam("newStatus", newStatus);
		Response response = wt.request().accept(this.getMediaType())
				.post(Entity.entity(null, this.getMediaType()));
		try {
			validateResponse(Response.Status.NO_CONTENT.getStatusCode(), response);
		} catch (InputValidationException | InstanceNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	@Override
	public List<ShipmentDtoJaxb> findShipmentsByCustomerIdAndStatus(long customerId, ShipmentStatus status, int firstShipment, int maxShipments) {
		WebTarget wt = getEndpointWebTarget().path("shipments").
				queryParam("customerId", customerId);
		if (status != null) {
			wt = wt.queryParam("status", status);
		}
		wt = wt.queryParam("first", firstShipment).
				queryParam("max", maxShipments);
		Response response = wt.request().accept(this.getMediaType()).get();
		try {
			validateResponse(Response.Status.OK.getStatusCode(), response);
			List<ShipmentDtoJaxb> shipments = response.readEntity(new GenericType<List<ShipmentDtoJaxb>>(){});
			return shipments;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	@Override
	public List< List<ShipmentDtoJaxb> > findShipmentsByCustomerIdAndStatusFull(long customerId, ShipmentStatus status, int maxShipments) {
		WebTarget wt = getEndpointWebTarget().path("shipments").
				queryParam("customerId", customerId);
		if (status != null) {
			wt = wt.queryParam("status", status);
		}
		wt = wt.queryParam("first", 1).
				queryParam("max", maxShipments);
		Response response = wt.request().accept(this.getMediaType()).get();
		try {
			validateResponse(Response.Status.OK.getStatusCode(), response);
			List< List<ShipmentDtoJaxb> > shipmentIntervals = new ArrayList<>();
			List<ShipmentDtoJaxb> shipments = response.readEntity(new GenericType<List<ShipmentDtoJaxb>>(){});

			shipmentIntervals.add(shipments);
			//new code to follow links automatically

			while(response.getLink("next") != null){
				Response linkResponse = getClient().invocation(
						response.getLink("next")
				).get();
				try {
					validateResponse(Response.Status.OK.getStatusCode(),
							linkResponse);
				} catch (Exception ex) {
					throw new RuntimeException(ex);
				}
				shipments = linkResponse.readEntity(new GenericType<List<ShipmentDtoJaxb>>(){});

				shipmentIntervals.add(shipments);

				response.close();
				response = linkResponse;
			}

			return shipmentIntervals;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (response != null) {
				response.close();
			}
		}
	}

	private void validateResponseAux(int expectedStatusCode, Response response)
			throws InstanceNotFoundException, InputValidationException {

		Response.Status statusCode = Response.Status.fromStatusCode(response.getStatus());
		String contentType = response.getMediaType() != null ? response.getMediaType().toString() : null;
		boolean expectedContentType = this.getMediaType().toString().equalsIgnoreCase(contentType);
		if (!expectedContentType && (statusCode.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode())) {
			throw new RuntimeException("HTTP error; status code = " + statusCode);
		}
		switch (statusCode) {
			case NOT_FOUND: {
				InstanceNotFoundExceptionDtoJaxb exDto = response.readEntity(InstanceNotFoundExceptionDtoJaxb.class);
				throw JaxbExceptionConversor.toInstanceNotFoundException(exDto);
			}
			case BAD_REQUEST: {
				InputValidationExceptionDtoJaxb exDto = response.readEntity(InputValidationExceptionDtoJaxb.class);
				throw JaxbExceptionConversor.toInputValidationException(exDto);
			}
			/*case GONE: {
				SaleExpirationExceptionDtoJaxb exDto = response.readEntity(SaleExpirationExceptionDtoJaxb.class);
				throw JaxbExceptionConversor.toSaleExpirationException(exDto);
			}*/
			default:
				if (statusCode.getStatusCode() != expectedStatusCode) {
					throw new RuntimeException("HTTP error; status code = " + statusCode);
				}
				break;
		}
	}

	private void validateResponse(int expectedStatusCode, Response response)
			throws InstanceNotFoundException, InputValidationException, InvalidShipmentCancelationException, InvalidStatusChangeException, HasShipmentException {

		Response.Status statusCode = Response.Status.fromStatusCode(response.getStatus());
		String contentType = response.getMediaType() != null ? response.getMediaType().toString() : null;
		boolean expectedContentType = this.getMediaType().toString().equalsIgnoreCase(contentType);
		if (!expectedContentType && (statusCode.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode())) {
			throw new RuntimeException("HTTP error; status code = " + statusCode);
		}
		switch (statusCode) {
			case NOT_FOUND: {
				ExceptionDtoJaxb exDto = response
						.readEntity(ExceptionDtoJaxb.class);
				if (exDto.getErrorType().equals("InstanceNotFound"))
					throw JaxbExceptionConversor.toInstanceNotFoundException(exDto);
				if (exDto.getErrorType().equals("InvalidStatusChange"))
					throw JaxbExceptionConversor.toInvalidStatusChangeException(exDto);

				throw new RuntimeException("HTTP error; status code = "
							+ statusCode + " errorType = " + exDto.getErrorType());
			}
			case BAD_REQUEST: {
				ExceptionDtoJaxb exDto = response
						.readEntity(ExceptionDtoJaxb.class);
				if (exDto.getErrorType().equals("InputValidation"))
					throw JaxbExceptionConversor.toInputValidationException(exDto);
				if (exDto.getErrorType().equals("HasShipments"))
					throw JaxbExceptionConversor.toHasShipmentException(exDto);
				if (exDto.getErrorType().equals("InvalidShipmentCancelation"))
					throw JaxbExceptionConversor.toInvalidShipmentCancelationException(exDto);

				throw new RuntimeException("HTTP error; status code = "
						+ statusCode + " errorType = " + exDto.getErrorType());
			}
			/*case GONE: {
				SaleExpirationExceptionDtoJaxb exDto = response.readEntity(SaleExpirationExceptionDtoJaxb.class);
				throw JaxbExceptionConversor.toSaleExpirationException(exDto);
			}*/
			default:
				if (statusCode.getStatusCode() != expectedStatusCode) {
					throw new RuntimeException("HTTP error; status code = " + statusCode);
				}
				break;
		}
	}


}
