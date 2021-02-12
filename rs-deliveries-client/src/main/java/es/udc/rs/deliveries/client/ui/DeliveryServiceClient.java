package es.udc.rs.deliveries.client.ui;

import es.udc.rs.deliveries.client.service.ClientDeliveryService;
import es.udc.rs.deliveries.client.service.ClientDeliveryServiceFactory;
import es.udc.rs.deliveries.client.service.rest.dto.CustomerDtoJaxb;
import es.udc.rs.deliveries.client.service.rest.dto.ShipmentDtoJaxb;
import es.udc.rs.deliveries.client.service.rest.dto.ShipmentStatus;

import java.util.List;

public class DeliveryServiceClient {

	public static void main(String[] args) {

		if (args.length == 0) {
			printUsageAndExit();
		}
		ClientDeliveryService clientDeliveryService = ClientDeliveryServiceFactory.getService();
		if ("-addCustomer".equalsIgnoreCase(args[0])) {
			validateArgs(args, 4, new int[] {});

			// [-addCustomer] DeliveryServiceClient -addCustomer <name> <cif> <address>

			try {
				CustomerDtoJaxb c = new CustomerDtoJaxb();
				c.setName(args[1]);
				c.setCif(args[2]);
				c.setAddress(args[3]);
				CustomerDtoJaxb customer = ClientDeliveryServiceFactory.getService().addCustomer(c); // Invoke method from the clientDeliveryService
				System.out.println("Customer " + customer.getCustomerId() + " " + "created sucessfully");
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-removeCustomer".equalsIgnoreCase(args[0])) {
			validateArgs(args, 2, new int[] { 1 });

			// [-removeCustomer] DeliveryServiceClient -removeCustomer <customerId>

			try {
				long customerId = Long.valueOf(args[1]);
				ClientDeliveryServiceFactory.getService().deleteCustomer(customerId);
				System.out.println("Customer " + customerId + " " + "deleted successfully");
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}

		} else if ("-searchShipments".equalsIgnoreCase(args[0])){
			validateArgs(args, 5, new int[] { 1, 3, 4 });

			// [-searchShipments] DeliveryServiceClient -searchShipments <customerId> <status> <first> <max>

			try {
				long customerId = Long.valueOf(args[1]);
				ShipmentStatus status = args[2].equals("ANY") ? null : ShipmentStatus.valueOf(args[2]);
				int first = Integer.valueOf(args[3]);
				int max = Integer.valueOf(args[4]);
				List<ShipmentDtoJaxb> shipments = ClientDeliveryServiceFactory.getService().findShipmentsByCustomerIdAndStatus(customerId, status, first, max);
				System.out.println("Found " + shipments.size() + " shipments");
				for (ShipmentDtoJaxb shipment: shipments) {
					System.out.println("--------------------------");
					System.out.println("Shipment: " + shipment.getShipmentId()
										+ "\nCustomer: " + shipment.getCustomerId()
										+ "\nAddress: " + shipment.getAddress()
										+  "\nCreation date: " + shipment.getCreationDate()
										+  "\nPackage reference: " + shipment.getPackageReference()
										+  "\nStatus: " + shipment.getStatus()
										+  "\nDelivery date: " + shipment.getDeliveryDate()
										+  "\nRemaining Hours: " + shipment.getRemainingHours());
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-updateStatus".equalsIgnoreCase(args[0])){
			validateArgs(args, 3, new int[] { 1 });

			// [-updateStatus] DeliveryServiceClient -updateStatus <shipmentId> <status>

			try {
				long shipmentId = Long.valueOf(args[1]);
				ShipmentStatus status = ShipmentStatus.valueOf(args[2]);
				ClientDeliveryServiceFactory.getService().updateShipmentStatus(shipmentId, status);
				System.out.println("Changed status of shipment " + shipmentId + " to " + status);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		} else if ("-searchAllShipments".equalsIgnoreCase(args[0])){
			validateArgs(args, 4, new int[] { 3 });


			// [-searchAllShipments] DeliveryServiceClient -searchAllShipments <customerId> <status> <intervalSize>

			try {
				long customerId = Long.valueOf(args[1]);
				ShipmentStatus status =  args[2].equals("ANY") ? null : ShipmentStatus.valueOf(args[2]);
				int intervalSize = Integer.valueOf(args[3]);
				List< List<ShipmentDtoJaxb> > shipmentIntervals =
						ClientDeliveryServiceFactory.getService().
								findShipmentsByCustomerIdAndStatusFull(customerId, status, intervalSize);

				if (shipmentIntervals.size() == 0)
					return;
				int countShipments = ((shipmentIntervals.size() - 1) * intervalSize) +
						shipmentIntervals.get(shipmentIntervals.size()-1).size();
				System.out.println("Found " + shipmentIntervals.size() +
						" intervals, for a total of " + countShipments + " shipments");
				int countIntervals = 0;
				for (List<ShipmentDtoJaxb> shipmentInterval: shipmentIntervals) {
					System.out.println("##########################");
					System.out.println("Interval #" +  countIntervals++);
					for (ShipmentDtoJaxb shipment : shipmentInterval) {
						System.out.println("--------------------------");
						System.out.println("Shipment: " + shipment.getShipmentId()
								+ "\nCustomer: " + shipment.getCustomerId()
								+ "\nAddress: " + shipment.getAddress()
								+ "\nCreation date: " + shipment.getCreationDate()
								+ "\nPackage reference: " + shipment.getPackageReference()
								+ "\nStatus: " + shipment.getStatus()
								+ "\nDelivery date: " + shipment.getDeliveryDate()
								+ "\nRemaining Hours: " + shipment.getRemainingHours());
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
		}
	}

	public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
		if (expectedArgs != args.length) {
			printUsageAndExit();
		}
		for (int i = 0; i < numericArguments.length; i++) {
			int position = numericArguments[i];
			try {
				Double.parseDouble(args[position]);
			} catch (NumberFormatException n) {
				printUsageAndExit();
			}
		}
	}

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

	public static void printUsage() {
		System.err.println(
				"Usage:\n" + "    [-addCustomer]    DeliveryServiceClient -addCustomer <name> <cif> <address>\n" +
		                     "    [-removeCustomer]   DeliveryServiceClient -removeCustomer <customerId>\n" +
						     "    [-searchShipments]   DeliveryServiceClient -searchShipments <customerId> <status> <first> <max>\n" +
				             "    [-updateStatus] DeliveryServiceClient -updateStatus <shipmentId> <status>\n" +
							 "    [-searchAllShipments] DeliveryServiceClient -searchAllShipments <customerId> <status> <intervalSize>");
	}

}
