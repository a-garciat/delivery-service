package es.udc.rs.deliveries.model.deliveryservice.exceptions;

import es.udc.rs.deliveries.model.shipment.ShipmentStatus;

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
