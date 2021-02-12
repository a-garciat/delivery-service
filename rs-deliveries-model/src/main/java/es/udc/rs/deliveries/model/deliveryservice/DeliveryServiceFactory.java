package es.udc.rs.deliveries.model.deliveryservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class DeliveryServiceFactory {

	private static final String CLASS_NAME_PARAMETER = "DeliveryServiceFactory.className";
	private static DeliveryService service = null;

	private DeliveryServiceFactory() {
	}

	@SuppressWarnings("rawtypes")
	private static DeliveryService getInstance() {
		try {
			String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
			Class serviceClass = Class.forName(serviceClassName);
			return (DeliveryService) serviceClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	public static synchronized  DeliveryService getService() {

		if (service == null) {
			service = getInstance();
		}
		return service;

	}
}
