package es.udc.rs.deliveries.client.service.rest.exception;

import es.udc.rs.deliveries.client.service.rest.dto.ShipmentStatus;

public class InvalidStatusChangeException extends Exception {
    private ShipmentStatus oldStatus, newStatus;

    public InvalidStatusChangeException(ShipmentStatus oldStatus, ShipmentStatus newStatus) {
        super("A shipment in status " + oldStatus.toString() + " cannot be set to " + newStatus.toString());
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
    }

    public ShipmentStatus getOldStatus() {
        return oldStatus;
    }

    public ShipmentStatus getNewStatus() {
        return newStatus;
    }
}
