package es.udc.rs.deliveries.model.deliveryservice;

import es.udc.rs.deliveries.model.customer.Customer;
import es.udc.rs.deliveries.model.shipment.Shipment;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MockDeliveryServiceDB {

    private static Map<Long, Customer> customersMap = new LinkedHashMap<Long,Customer>();
    private static Map<Long, Shipment> shipmentsMap = new LinkedHashMap<Long,Shipment>();
    private static Map<Long, List<Shipment>> shipmentsByUserMap = new LinkedHashMap<Long,List<Shipment>>();

    private MockDeliveryServiceDB() {

    }

    public static Map<Long, Customer> getCustomersMap(){
        return customersMap;
    }

    public static Map<Long, Shipment> getShipmentsMap(){
        return shipmentsMap;
    }

    public static Map<Long, List<Shipment>> getShipmentsByUserMap(){
        return shipmentsByUserMap;
    }

}
