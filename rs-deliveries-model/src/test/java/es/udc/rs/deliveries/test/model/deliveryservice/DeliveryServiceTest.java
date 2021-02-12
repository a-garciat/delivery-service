package es.udc.rs.deliveries.test.model.deliveryservice;

import es.udc.rs.deliveries.model.customer.Customer;
import es.udc.rs.deliveries.model.deliveryservice.MockDeliveryServiceDB;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.HasShipmentException;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidShipmentCancelationException;
import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidStatusChangeException;
import es.udc.rs.deliveries.model.shipment.Shipment;
import es.udc.rs.deliveries.model.shipment.ShipmentStatus;
import es.udc.rs.deliveries.model.deliveryservice.DeliveryService;
import es.udc.rs.deliveries.model.deliveryservice.DeliveryServiceFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DeliveryServiceTest {

    private static final long NON_EXISTENT_CUSTOMER_ID = -1;
    private static final long NON_EXISTENT_SHIPMENT_ID = -1;

    private static final String VALID_CIF = "A1234567X";
    private static final String VALID_CIF2 = "B1245789X";
    private static final String INVALID_CIF = "123456789012345";

    private static DeliveryService deliveryService = null;

    private static Map<Long,Customer> customersMap = null;
    private static Map<Long,Shipment> shipmentsMap = null;
    private static Map<Long,List<Shipment>> shipmentsByUserMap = null;


    @BeforeAll
    public static void init() {
        deliveryService = DeliveryServiceFactory.getService();

        customersMap = MockDeliveryServiceDB.getCustomersMap();
        shipmentsMap = MockDeliveryServiceDB.getShipmentsMap();
        shipmentsByUserMap = MockDeliveryServiceDB.getShipmentsByUserMap();

    }

    private Customer getValidCustomer(){
        return new Customer(null, "Customer Name", VALID_CIF, "Customer Address", LocalDateTime.now() );
    }

    private Customer getInvalidCustomer(){
        return new Customer(null, "Customer Name", INVALID_CIF, "Customer Address", LocalDateTime.now() );
    }

    private Shipment getValidShipment(long customerId){
        return new Shipment(null, customerId, (long)1,  "Address 1", null, LocalDateTime.now(), LocalDateTime.now() );
    }

    private Customer createCustomer(Customer c){
        Customer addedCustomer = null;
        try {
            addedCustomer = deliveryService.addCustomer(c);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedCustomer;
    }

    private Shipment addShipment(Shipment s) {
        try {
            return deliveryService.addShipment(s);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
    }


    private void removeCustomer(long customerId) throws InstanceNotFoundException {
        if (customersMap.get(customerId) == null) {
            throw new InstanceNotFoundException(customerId, Customer.class.getName());
        }
        List<Shipment> sList = shipmentsByUserMap.get(customerId);
        for (Shipment s : sList){
            shipmentsMap.remove(s.getShipmentId());
        }
        shipmentsByUserMap.remove(customerId);
        customersMap.remove(customerId);

    }

    private Shipment getValidShipment(Customer c) {
        return new Shipment(null, c.getCustomerId(), 1234L,
                "Test Address", null, null, LocalDateTime.MAX);
    }

    @Test
    void testAddShipment() throws  InputValidationException, Exception {
        Customer c = getValidCustomer();
        Shipment s;
        try {
            c = createCustomer(c);
            s = getValidShipment(c);
            s = addShipment(s);
            assert(s.getStatus() == ShipmentStatus.PENDING);
            assert(s.getCreationDate() != null);
            assert(s.getShipmentId() != null);
            Shipment shipmentFound = deliveryService.findShipmentById(s.getShipmentId());
            assertEquals(s, shipmentFound);
            List<Shipment> shipments = deliveryService.findShipmentsByCustomerIdAndStatus(c.getCustomerId(),
                    ShipmentStatus.PENDING,
                    0,
                    1);

            assert(shipments.contains(s));
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            removeCustomer(c.getCustomerId());
        }
    }

    @Test
    void testAddNullShipment() {
        assertThrows(InputValidationException.class, () -> deliveryService.addShipment(null));
    }

    @Test
    void testAddShipmentWithUnkownCustomer() {
        Customer c = getValidCustomer();
        Shipment s = getValidShipment(c);
        assertThrows(InputValidationException.class, () -> deliveryService.addShipment(s));
    }

    @Test
    void testAddShipmentWithNullCustomerID() {
        Shipment s = new Shipment(null, null, 1234L, "Test", null,
                null, null);
        assertThrows(InputValidationException.class, () -> deliveryService.addShipment(s));
    }

    @Test
    void testAddShipmentWithNullReference() throws InstanceNotFoundException {
        Customer c = createCustomer(getValidCustomer());
        Shipment s = new Shipment(null, c.getCustomerId(), null, "Test", null,
                null, null);
        try {
            assertThrows(InputValidationException.class, () -> deliveryService.addShipment(s));
        } finally {
            removeCustomer(c.getCustomerId());
        }
    }

    @Test
    void testAddShipmentWithNullAddress() throws InstanceNotFoundException {
        Customer c = createCustomer(getValidCustomer());
        Shipment s = new Shipment(null, c.getCustomerId(), 12L, null, null,
                null, null);
        try {
            assertThrows(InputValidationException.class, () -> deliveryService.addShipment(s));
        } finally {
            removeCustomer(c.getCustomerId());
        }
    }

    @Test
    void testCancelShipment() throws InstanceNotFoundException {
        Customer c = createCustomer(getValidCustomer());
        Shipment s = addShipment(getValidShipment(c));
        try {
            deliveryService.cancelShipment(s.getShipmentId());
            s = deliveryService.findShipmentById(s.getShipmentId());
            assert(s.getStatus() == ShipmentStatus.CANCELLED);
        } catch (Exception e){
            fail(e.getMessage());
        } finally {
            removeCustomer(c.getCustomerId());
        }
    }

    @Test
    void testUpdateShipmentStatus() throws InstanceNotFoundException {
        Customer c = createCustomer(getValidCustomer());
        Shipment s = addShipment(getValidShipment(c));
        Shipment s2 = addShipment(getValidShipment(c));
        try {
            deliveryService.updateShipmentStatus(s.getShipmentId(), ShipmentStatus.SENT);
            deliveryService.updateShipmentStatus(s.getShipmentId(), ShipmentStatus.DELIVERED);
            Shipment s3 = deliveryService.findShipmentById(s.getShipmentId());
            assert (s3.getDeliveryDate() != null);
            deliveryService.updateShipmentStatus(s2.getShipmentId(), ShipmentStatus.SENT);
            deliveryService.updateShipmentStatus(s2.getShipmentId(), ShipmentStatus.REJECTED);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            removeCustomer(c.getCustomerId());
        }

    }

    @Test
    void testUpdateShipmentStatusIncorrectly() throws InstanceNotFoundException {
        Customer c = createCustomer(getValidCustomer());
        Shipment s = addShipment(getValidShipment(c));
        assertThrows(InvalidStatusChangeException.class, () -> deliveryService.updateShipmentStatus(s.getShipmentId(),
                ShipmentStatus.DELIVERED));
        assertThrows(InvalidStatusChangeException.class, () -> deliveryService.updateShipmentStatus(s.getShipmentId(),
                ShipmentStatus.REJECTED));
        try {
            deliveryService.updateShipmentStatus(s.getShipmentId(), ShipmentStatus.SENT);
            assertThrows(InvalidStatusChangeException.class, () -> deliveryService.updateShipmentStatus(s.getShipmentId(),
                    ShipmentStatus.CANCELLED));
            assertThrows(InvalidStatusChangeException.class, () -> deliveryService.updateShipmentStatus(s.getShipmentId(),
                    ShipmentStatus.PENDING));
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            removeCustomer(c.getCustomerId());
        }
    }

    @Test
    void testCancelSentShipment() throws InstanceNotFoundException {
        Customer c = createCustomer(getValidCustomer());
        Shipment s = addShipment(getValidShipment(c));
        try {
            deliveryService.updateShipmentStatus(s.getShipmentId(), ShipmentStatus.SENT);
            assertThrows(InvalidShipmentCancelationException.class, () -> deliveryService.cancelShipment(s.getShipmentId()));
        } catch (InputValidationException | InstanceNotFoundException | InvalidStatusChangeException e) {
            fail(e.getMessage());
        } finally {
            removeCustomer(c.getCustomerId());
        }
    }

    @Test
    void testValidAddFindRemoveCustomer() throws InputValidationException, InstanceNotFoundException, HasShipmentException {
        Customer customer = getValidCustomer();
        deliveryService.addCustomer(customer);
        Customer customerFound = deliveryService.findCustomerById(customer.getCustomerId());
        assertEquals(customer, customerFound);
        deliveryService.removeCustomer(customerFound.getCustomerId());
        assertThrows(InstanceNotFoundException.class, () -> { deliveryService.findCustomerById(customerFound.getCustomerId()); });
    }

    @Test
    void testInvalidAddCustomerCifLength() {
        Customer customer = new Customer(null, "Customer Name", "A253B", "Customer Address", LocalDateTime.now() );
        assertThrows(InputValidationException.class, () -> { deliveryService.addCustomer(customer);});
    }

    @Test
    void testInvalidAddCustomerCifLetter() {
        Customer customer = new Customer(null, "Customer Name", "01245789X", "Customer Address", LocalDateTime.now() );
        assertThrows(InputValidationException.class, () -> { deliveryService.addCustomer(customer);});
    }

    @Test
    void testInvalidAddCustomerCifDigits() {
        Customer customer = new Customer(null, "Customer Name", "A1FDR789X", "Customer Address", LocalDateTime.now() );
        assertThrows(InputValidationException.class, () -> { deliveryService.addCustomer(customer);});
    }

    @Test
    void testAddCustomerNullName() {
        Customer customer = new Customer(null, "", "A1245789X", "Customer Address", LocalDateTime.now() );
        assertThrows(InputValidationException.class, () -> { deliveryService.addCustomer(customer);});
    }

    @Test
    void testAddCustomerNullAddress() {
        Customer customer = new Customer(null, "Customer Name", "A1245789X", "", LocalDateTime.now() );
        assertThrows(InputValidationException.class, () -> { deliveryService.addCustomer(customer);});
    }

    @Test
    void testFindNonExistentCustomerById() {
        assertThrows(InstanceNotFoundException.class, () -> { deliveryService.findCustomerById(NON_EXISTENT_CUSTOMER_ID); });
    }

    @Test
    void testRemoveNonExistentCustomer() {
        assertThrows(InstanceNotFoundException.class, () -> { deliveryService.removeCustomer(NON_EXISTENT_CUSTOMER_ID); });
    }

    @Test
    void testRemoveCustomerWithAssociatedShipment() throws InputValidationException, InstanceNotFoundException {
        Customer customer = getValidCustomer();
        Customer customerCreated = deliveryService.addCustomer(customer);
        Shipment shipment = new Shipment(null, customerCreated.getCustomerId(), (long) 1, "Address", null, null, LocalDateTime.now());
        Shipment shipmentCreated = deliveryService.addShipment(shipment);
        try {
            assertThrows(HasShipmentException.class, () -> {
                deliveryService.removeCustomer(customerCreated.getCustomerId());
            });
        } finally {
            removeCustomer(customerCreated.getCustomerId());

        }
    }

    @Test
    void testUpdateCustomer() throws InputValidationException, InstanceNotFoundException {
        //Init DB
        Customer customer = createCustomer(getValidCustomer());
        try{
            Customer customerToUpdate = new Customer(customer.getCustomerId(), "New Name", VALID_CIF2, "New Address", LocalDateTime.MAX);

            deliveryService.updateCustomer(customerToUpdate);

            Customer updatedCustomer = deliveryService.findCustomerById(customer.getCustomerId());

            customerToUpdate.setCreationDate(customer.getCreationDate());
            assertEquals(customerToUpdate, updatedCustomer);

        } finally {
            //Clear DB
            removeCustomer(customer.getCustomerId());
        }
    }

    @Test
    void testUpdateInvalidCustomer() throws InstanceNotFoundException {
        Long customerId = createCustomer(getValidCustomer()).getCustomerId();
        try{
            Customer customer = deliveryService.findCustomerById(customerId);
            customer.setCif(INVALID_CIF);
            assertThrows(InputValidationException.class, () -> deliveryService.updateCustomer(customer));
        } finally {
            removeCustomer(customerId);
        }

    }

    @Test
    void testUpdateNonExistentCustomer() {
        Customer c = getValidCustomer();
        c.setCustomerId(NON_EXISTENT_CUSTOMER_ID);

        assertThrows(InstanceNotFoundException.class, () -> deliveryService.updateCustomer(c));
    }

    @Test
    void testFindCustomersByName() throws InstanceNotFoundException {

        Customer c1 = getValidCustomer();
        c1.setName("Customer one");
        Customer c2 = getValidCustomer();
        c2.setName("Customer two");
        Customer c3 = getValidCustomer();
        c3.setName("Customer three");
        Customer c4 = getValidCustomer();
        c4.setName("Not customer three");
        Customer addC1 = null;
        Customer addC2 = null;
        Customer addC3 = null;
        Customer addC4 = null;

        try {
            addC1 = createCustomer(c1);
            addC2 = createCustomer(c2);
            addC3 = createCustomer(c3);
            addC4 = createCustomer(c4);
            List<Customer> lc = deliveryService.findCustomersByName("Er ThreE");
            List<Customer> lc2 = new ArrayList<>();
            lc2.add(c3);
            lc2.add(c4);
            assertEquals(lc, lc2);
        } finally {
            if (addC1 != null)
                removeCustomer(addC1.getCustomerId());
            if (addC2 != null)
                removeCustomer(addC2.getCustomerId());
            if (addC3 != null)
                removeCustomer(addC3.getCustomerId());
            if (addC4 != null)
                removeCustomer(addC4.getCustomerId());
        }

    }

    @Test
    void testFindCustomersByNullName() throws InstanceNotFoundException {

        Customer c1 = getValidCustomer();
        c1.setName("Customer one");
        Customer c2 = getValidCustomer();
        c2.setName("Customer two");
        Customer c3 = getValidCustomer();
        c3.setName("Customer three");
        Customer c4 = getValidCustomer();
        c4.setName("Not customer three");
        Customer addC1 = null;
        Customer addC2 = null;
        Customer addC3 = null;
        Customer addC4 = null;

        try {
            addC1 = createCustomer(c1);
            addC2 = createCustomer(c2);
            addC3 = createCustomer(c3);
            addC4 = createCustomer(c4);
            List<Customer> lc = deliveryService.findCustomersByName(null);
            List<Customer> lc2 = new ArrayList<>();
            lc2.add(c1);
            lc2.add(c2);
            lc2.add(c3);
            lc2.add(c4);
            assertEquals(lc, lc2);
        } finally {
            if (addC1 != null)
                removeCustomer(addC1.getCustomerId());
            if (addC2 != null)
                removeCustomer(addC2.getCustomerId());
            if (addC3 != null)
                removeCustomer(addC3.getCustomerId());
            if (addC4 != null)
                removeCustomer(addC4.getCustomerId());
        }

    }

    @Test
    void testFindShipmentById() throws InstanceNotFoundException, InputValidationException {

        Customer customer = createCustomer(getValidCustomer());
        Shipment shipment = null;

        try {
            shipment = deliveryService.


                    addShipment(getValidShipment(customer.getCustomerId()));

            Shipment foundShipment = deliveryService.findShipmentById(shipment.getShipmentId());

            assertEquals(shipment, foundShipment);
        } finally {

            removeCustomer(customer.getCustomerId());

        }
    }

    @Test
    void testFindNonExistentShipmentById() {

        assertThrows(InstanceNotFoundException.class, () ->{
            deliveryService.findShipmentById(NON_EXISTENT_SHIPMENT_ID);
        });
    }

    @Test
    void testFindShipmentsByCustomerId() throws InstanceNotFoundException, InputValidationException {

        Customer c1 = getValidCustomer();
        c1.setName("Customer one");
        Customer c2 = getValidCustomer();
        c2.setName("Customer two");
        Customer addC1 = null;
        Customer addC2 = null;
        try{

            addC1 = createCustomer(c1);
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            addC2 = createCustomer(c2);
            Shipment addS1 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));
            Shipment addS2 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));

            List<Shipment> shipments = deliveryService.findShipmentsByCustomerIdAndStatus(addC2.getCustomerId(), null, 1, 5);
            List<Shipment> shipmentCopy = new ArrayList<>();
            shipmentCopy.add(addS1);
            shipmentCopy.add(addS2);

            assertEquals(shipments, shipmentCopy);

        } finally {

            if (addC1 != null){
                removeCustomer(addC1.getCustomerId());
            }
            if (addC2 != null) {
                removeCustomer(addC2.getCustomerId());
            }

        }

    }

    @Test
    void testFindShipmentsByCustomerIdWithMax() throws InstanceNotFoundException, InputValidationException {

        Customer c1 = getValidCustomer();
        c1.setName("Customer one");
        Customer c2 = getValidCustomer();
        c2.setName("Customer two");
        Customer addC1 = null;
        Customer addC2 = null;
        try{

            addC1 = createCustomer(c1);
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            addC2 = createCustomer(c2);
            Shipment addS1 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));
            Shipment addS2 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));

            List<Shipment> shipments = deliveryService.findShipmentsByCustomerIdAndStatus(addC2.getCustomerId(), null, 1, 1);
            List<Shipment> shipmentCopy = new ArrayList<>();
            shipmentCopy.add(addS1);

            assertEquals(shipments, shipmentCopy);

        } finally {

            if (addC1 != null){
                removeCustomer(addC1.getCustomerId());
            }
            if (addC2 != null) {
                removeCustomer(addC2.getCustomerId());
            }

        }

    }


    @Test
    void testFindShipmentsByCustomerIdWithIndex() throws InstanceNotFoundException, InputValidationException {

        Customer c1 = getValidCustomer();
        c1.setName("Customer one");
        Customer c2 = getValidCustomer();
        c2.setName("Customer two");
        Customer addC1 = null;
        Customer addC2 = null;
        try{

            addC1 = createCustomer(c1);
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            addC2 = createCustomer(c2);
            Shipment addS1 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));
            Shipment addS2 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));

            List<Shipment> shipments = deliveryService.findShipmentsByCustomerIdAndStatus(addC2.getCustomerId(), null, 2, 5);
            List<Shipment> shipmentCopy = new ArrayList<>();
            shipmentCopy.add(addS2);

            assertEquals(shipments, shipmentCopy);

        } finally {

            if (addC1 != null){
                removeCustomer(addC1.getCustomerId());
            }
            if (addC2 != null) {
                removeCustomer(addC2.getCustomerId());
            }

        }

    }

    @Test
    void testFindShipmentsByCustomerIdAndStatus() throws InstanceNotFoundException, InputValidationException {

        Customer c1 = getValidCustomer();
        c1.setName("Customer one");
        Customer c2 = getValidCustomer();
        c2.setName("Customer two");
        Customer addC1 = null;
        Customer addC2 = null;
        try{

            addC1 = createCustomer(c1);
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            addC2 = createCustomer(c2);
            Shipment addS1 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));
            Shipment addS2 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));
            try {
                deliveryService.updateShipmentStatus(addS2.getShipmentId(), ShipmentStatus.SENT);
            } catch (Exception e) {
                //do nothing
            }
            List<Shipment> shipments = deliveryService.findShipmentsByCustomerIdAndStatus(addC2.getCustomerId(), ShipmentStatus.SENT, 1, 5);
            List<Shipment> shipmentCopy = new ArrayList<>();
            addS2.setStatus(ShipmentStatus.SENT);
            shipmentCopy.add(addS2);

            assertEquals(shipments, shipmentCopy);

        } finally {

            if (addC1 != null){
                removeCustomer(addC1.getCustomerId());
            }
            if (addC2 != null) {
                removeCustomer(addC2.getCustomerId());
            }

        }

    }

    @Test
    void testFindShipmentsByNonExistentCustomerId() throws InputValidationException, InstanceNotFoundException {

        Customer c1 = getValidCustomer();
        c1.setName("Customer one");
        Customer c2 = getValidCustomer();
        c2.setName("Customer two");
        Customer addC1 = null;
        Customer addC2 = null;
        try{

            addC1 = createCustomer(c1);
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            deliveryService.addShipment(getValidShipment(addC1.getCustomerId()));
            addC2 = createCustomer(c2);
            Shipment addS1 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));
            Shipment addS2 = deliveryService.addShipment(getValidShipment(addC2.getCustomerId()));
            try {
                deliveryService.updateShipmentStatus(addS2.getShipmentId(), ShipmentStatus.SENT);
            } catch (Exception e) {
                //do nothing
            }
            assertThrows(InstanceNotFoundException.class, () -> deliveryService.findShipmentsByCustomerIdAndStatus(NON_EXISTENT_CUSTOMER_ID, ShipmentStatus.SENT, 1, 5));

        } finally {

            if (addC1 != null){
                removeCustomer(addC1.getCustomerId());
            }
            if (addC2 != null) {
                removeCustomer(addC2.getCustomerId());
            }

        }

    }

}
