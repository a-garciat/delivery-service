package es.udc.rs.deliveries.client.service;

import es.udc.rs.deliveries.client.service.rest.dto.CustomerDtoJaxb;
import es.udc.rs.deliveries.client.service.rest.dto.ShipmentDtoJaxb;
import es.udc.rs.deliveries.client.service.rest.dto.ShipmentStatus;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface ClientDeliveryService {

    public CustomerDtoJaxb addCustomer(CustomerDtoJaxb c) throws InputValidationException;

    public void deleteCustomer(long id) throws InstanceNotFoundException;

    void updateShipmentStatus(long id, ShipmentStatus newStatus) throws InputValidationException, InstanceNotFoundException;

    public List<ShipmentDtoJaxb> findShipmentsByCustomerIdAndStatus(long customerId, ShipmentStatus status, int firstShipment, int maxShipments);

    List<List<ShipmentDtoJaxb>> findShipmentsByCustomerIdAndStatusFull(long customerId, ShipmentStatus status, int intervalSize);
}
