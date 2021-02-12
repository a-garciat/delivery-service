package es.udc.rs.deliveries.model.deliveryservice;

import es.udc.rs.deliveries.model.customer.Customer;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.HasShipmentException;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidShipmentCancelationException;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidStatusChangeException;
import es.udc.rs.deliveries.model.shipment.Shipment;
import es.udc.rs.deliveries.model.shipment.ShipmentStatus;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface DeliveryService {

    public Customer addCustomer(Customer c) throws InputValidationException;

    public void removeCustomer(long id) throws InstanceNotFoundException, HasShipmentException;

    public void updateCustomer(Customer c) throws InstanceNotFoundException, InputValidationException;

    public Customer findCustomerById(long id) throws InstanceNotFoundException;

    public List<Customer> findCustomersByName(String keyword);

    public Shipment addShipment(Shipment s) throws InputValidationException;

    public void updateShipmentStatus(long id, ShipmentStatus newStatus) throws InputValidationException, InvalidStatusChangeException, InstanceNotFoundException;

    public void cancelShipment(long id) throws InstanceNotFoundException, InvalidShipmentCancelationException;

    public Shipment findShipmentById(long id) throws InstanceNotFoundException;

    public List<Shipment> findShipmentsByCustomerIdAndStatus(long customerId, ShipmentStatus status, int firstShipment, int maxShipments) throws InstanceNotFoundException;
}
