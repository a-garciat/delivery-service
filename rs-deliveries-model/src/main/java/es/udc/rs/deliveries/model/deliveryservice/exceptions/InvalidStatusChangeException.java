package es.udc.rs.deliveries.model.deliveryservice.exceptions;

import es.udc.rs.deliveries.model.shipment.Shipment;
import es.udc.rs.deliveries.model.shipment.ShipmentStatus;

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
