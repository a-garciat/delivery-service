package es.udc.rs.deliveries.client.service.rest.exception;

import es.udc.rs.deliveries.client.service.rest.dto.ShipmentStatus;

public class InvalidShipmentCancelationException extends Exception {
    private ShipmentStatus oldStatus;

    public InvalidShipmentCancelationException(ShipmentStatus oldStatus) {
        super("Unable to cancel shipment with status " + oldStatus.toString());
        this.oldStatus = oldStatus;
    }

    public ShipmentStatus getOldStatus() {
        return oldStatus;
    }
}
