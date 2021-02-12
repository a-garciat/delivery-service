package es.udc.rs.deliveries.model.deliveryservice;

import java.lang.reflect.InaccessibleObjectException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.SocketHandler;
import es.udc.rs.deliveries.model.customer.Customer;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.HasShipmentException;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidShipmentCancelationException;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidStatusChangeException;
import es.udc.rs.deliveries.model.shipment.Shipment;
import es.udc.rs.deliveries.model.shipment.ShipmentStatus;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.validation.PropertyValidator;

public class MockDeliveryService implements DeliveryService {

	private static Map<Long,Customer> customersMap = MockDeliveryServiceDB.getCustomersMap();
	private static Map<Long,Shipment> shipmentsMap = MockDeliveryServiceDB.getShipmentsMap();
	private static Map<Long,List<Shipment>> shipmentsByUserMap = MockDeliveryServiceDB.getShipmentsByUserMap();

	private static long lastCustomerId = 0;
	private static long lastShippingId = 0;
	private static final int CIF_LENGTH = 9;
	

	private static synchronized long getNextCustomerId() {
		return ++lastCustomerId;
	}
	
	private static synchronized long getNextShippingId() {
		return ++lastShippingId;
	}

	private void guard(Boolean condition, String messageIfFailure) throws InputValidationException {
		if (!condition) {
			throw new InputValidationException(messageIfFailure);
		}
	}

	private static void validateCif(String cif) throws InputValidationException {
		if(cif == null)
			throw new InputValidationException("Cif mustn't be null");
		if (cif.length() != CIF_LENGTH){
			throw new InputValidationException("Cif must have a length of" + CIF_LENGTH);
		}
		//first char must be  letter
		if ( !Character.isLetter( cif.charAt(0) ) ){
			throw new InputValidationException("Cif must start with a letter");
		}
		//all chars but first and last must be digits
		for(int i=1; i<CIF_LENGTH-1; i++){
			if ( !Character.isDigit( cif.charAt(i) ) ){
				throw new InputValidationException("All Cif chars but first and last must be digits");
			}
		}
	}

	private static void validateCustomer(Customer c) throws InputValidationException {
		if (c == null) {
			throw new InputValidationException("Customer can't be null.");
		}
		PropertyValidator.validateMandatoryString( "name", c.getName());
		PropertyValidator.validateMandatoryString( "address", c.getAddress());
		validateCif(c.getCif());
	}


	private static void checkAssociatedShipmentForDeletion(long id) throws HasShipmentException {
		if (!(shipmentsByUserMap.get(id).isEmpty())) {
			throw new HasShipmentException(id);
		}
	}

	@Override
	public Customer addCustomer(Customer c) throws InputValidationException{
		validateCustomer(c);
		c.setCustomerId(getNextCustomerId());
		c.setCreationDate(LocalDateTime.now());
		customersMap.put(c.getCustomerId(), new Customer(c));
		shipmentsByUserMap.put(c.getCustomerId(), new ArrayList<Shipment>());
		return c;
	}

	@Override
	public void removeCustomer(long id) throws InstanceNotFoundException, HasShipmentException {
		if (customersMap.get(id) == null) {
			throw new InstanceNotFoundException(id, Customer.class.getName());
		}
		checkAssociatedShipmentForDeletion(id);
		customersMap.remove(id);
		shipmentsByUserMap.remove(id);
	}

	@Override
	public void updateCustomer(Customer c) throws InstanceNotFoundException, InputValidationException {
		validateCustomer(c);
		Customer c_permanent = customersMap.get(c.getCustomerId());
		if (c_permanent == null) {
			throw new InstanceNotFoundException(c.getCustomerId(),
					Customer.class.getName());
		}
		c_permanent.setName(c.getName());
		c_permanent.setAddress(c.getAddress());
		c_permanent.setCif(c.getCif());
	}

	@Override
	public Customer findCustomerById(long id) throws InstanceNotFoundException {
		Customer c = customersMap.get(id);
		if (c == null) {
			throw new InstanceNotFoundException(id, Customer.class.getName());
		}
		return new Customer(c);
	}

	@Override
	public List<Customer> findCustomersByName(String keyword) {
		List<Customer> foundCustomers = new ArrayList<Customer>();
		String searchKeyword = keyword != null ? keyword.toLowerCase() : "";
		for ( Customer c : customersMap.values() ){
			if(c.getName().toLowerCase().contains(searchKeyword)){
				foundCustomers.add(new Customer(c));
			}
		}
		return foundCustomers;
	}

	@Override
	public Shipment findShipmentById(long id) throws InstanceNotFoundException {
		Shipment s = shipmentsMap.get(id);
		if (s == null) {
			throw new InstanceNotFoundException(id, Shipment.class.getName());
		}
		return new Shipment(s);
	}

	@Override
	public List<Shipment> findShipmentsByCustomerIdAndStatus(long customerId, ShipmentStatus status,
														 int firstShipment, int maxShipments) throws InstanceNotFoundException {

		firstShipment = firstShipment < 0 ? 0: firstShipment;
		maxShipments = maxShipments < 1 ? 1 : maxShipments;

		Customer c = customersMap.get(customerId);
		if (c == null){
			throw new InstanceNotFoundException(customerId, Customer.class.getName());
		}

		List<Shipment> shipmentsCopy = new ArrayList<Shipment>();
		List<Shipment> shipments = shipmentsByUserMap.get(customerId);
		if (shipments == null) {
			return shipmentsCopy;
		}


		int pos = 0;
		int skippedShipments = 0;
		int returnedShipments = 0;
		int listSize = shipments.size();

		//skip Shipments before the first to return
		while( (pos < listSize) && (skippedShipments < firstShipment-1) ){
			if ( (status == null) || status.equals(shipments.get(pos).getStatus()) ){
				skippedShipments++;
			}
			pos++;
		}

		//get at most maxShipments to return
		while( (pos < listSize) && (returnedShipments < maxShipments) ){
			Shipment shipment = shipments.get(pos);
			if ( appendShipmentIfStatus(shipmentsCopy, shipment, status) ){
				returnedShipments++;
			}
			pos++;
		}

		return shipmentsCopy;
	}

	private static boolean appendShipmentIfStatus(List<Shipment> shipments, Shipment shipment, ShipmentStatus status){
		if( (status == null) || status.equals(shipment.getStatus()) ){
			shipments.add( new Shipment(shipment) );
			return true;
		} else {
			return false;
		}
	}

	private void validateShipment(Shipment s) throws InputValidationException{
		guard(s != null, "A null shipment cannot be added");
		guard(s.getCustomerId() != null, "A null customer ID cannot be provided");
		guard(customersMap.containsKey(s.getCustomerId()), "The customer does not exist");
		guard(s.getAddress() != null, "A shipment with a null address cannot be added");
		guard(s.getPackageReference() != null, "The package referenc cannot be null");
	}

	@Override
	public Shipment addShipment(Shipment s) throws InputValidationException {
		validateShipment(s);
		s.setShipmentId(getNextShippingId());
		s.setCreationDate(LocalDateTime.now());
		s.setStatus(ShipmentStatus.PENDING);
		s.setMaxDeliveryDate(LocalDateTime.now().plusHours(48L));
		Shipment newShipment = new Shipment(s);
		shipmentsMap.put(s.getShipmentId(), newShipment);
		shipmentsByUserMap.get(s.getCustomerId()).add(newShipment);
		return s; //This avoids returning a direct reference from our datamodel
	}

	@Override
	public void updateShipmentStatus(long id, ShipmentStatus newStatus) throws InputValidationException, InvalidStatusChangeException, InstanceNotFoundException {
		guard(newStatus != null, "The new status cannot be null");
		Shipment shipment = shipmentsMap.get(id);
		if (shipment == null) {
			throw new InstanceNotFoundException(id, Shipment.class.getName());
		}
		switch (newStatus) {
			case SENT:
				if (shipment.getStatus() != ShipmentStatus.PENDING) {
					throw new InvalidStatusChangeException(shipment.getStatus(), newStatus);
				}
				break;
			case REJECTED:
				if (shipment.getStatus() != ShipmentStatus.SENT) {
					throw new InvalidStatusChangeException(shipment.getStatus(), newStatus);
				}
				break;
			case DELIVERED:
				if (shipment.getStatus() != ShipmentStatus.SENT) {
					throw new InvalidStatusChangeException(shipment.getStatus(), newStatus);
				}
				shipment.setDeliveryDate(LocalDateTime.now());
				break;
			case CANCELLED:
				if (shipment.getStatus() != ShipmentStatus.PENDING) {
					throw new InvalidStatusChangeException(shipment.getStatus(), newStatus);
				}
			case PENDING:
				throw new InvalidStatusChangeException(shipment.getStatus(), newStatus);
		}
		shipment.setStatus(newStatus);
	}

	@Override
	public void cancelShipment(long id) throws InstanceNotFoundException, InvalidShipmentCancelationException {
		Shipment shipment = shipmentsMap.get(id);
		if (shipment == null) {
			throw new InstanceNotFoundException(id, Shipment.class.getName());
		}
		if (shipment.getStatus() == ShipmentStatus.PENDING) {
			shipment.setStatus(ShipmentStatus.CANCELLED);
		} else {
			throw new InvalidShipmentCancelationException(shipment.getStatus());
		}
	}
}
