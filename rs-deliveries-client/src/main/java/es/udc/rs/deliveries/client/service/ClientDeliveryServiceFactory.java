package es.udc.rs.deliveries.client.service;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientDeliveryServiceFactory {

	private final static String CLASS_NAME_PARAMETER = "ClientDeliveryServiceFactory.className";
	private static ClientDeliveryService service = null;

	private ClientDeliveryServiceFactory() {
	}

	@SuppressWarnings("rawtypes")
	private static ClientDeliveryService getInstance() {
		try {
			String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class serviceClass = Class.forName(serviceClassName);
			return (ClientDeliveryService) serviceClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public synchronized static ClientDeliveryService getService() {

		if (service == null) {
			service = getInstance();
		}
		return service;

	}
}
