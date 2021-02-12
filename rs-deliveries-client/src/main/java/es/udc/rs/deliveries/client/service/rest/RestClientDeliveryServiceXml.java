package es.udc.rs.deliveries.client.service.rest;

import javax.ws.rs.core.MediaType;

public class RestClientDeliveryServiceXml extends RestClientDeliveryService {
	
	@Override
	protected MediaType getMediaType() {
		return MediaType.APPLICATION_XML_TYPE;
	}

}
