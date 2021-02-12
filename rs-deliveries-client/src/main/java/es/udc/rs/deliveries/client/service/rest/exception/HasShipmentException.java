package es.udc.rs.deliveries.client.service.rest.exception;

@SuppressWarnings("serial")
public class HasShipmentException extends Exception {
    private long customerId;

    public HasShipmentException(Long customerId) {
        super("Error: Customer with id " + customerId + " cannot be deleted as it has associated shipments");
        this.customerId = customerId;
    }

    public long getCustomerId() {
        return customerId;
    }

}
