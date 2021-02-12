package es.udc.rs.deliveries.jaxrs.dto;

import es.udc.rs.deliveries.model.deliveryservice.exceptions.InvalidStatusChangeException;
import es.udc.rs.deliveries.model.shipment.ShipmentStatus;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "invalidStatusChangeException")
@XmlType(name="invalidStatusChangeException", propOrder = {"newStatus", "oldStatus"})
public class InvalidStatusChangeExceptionDtoJaxb {

    @XmlAttribute(required = true)
    private String errorType;
    @XmlElement(required = true)
    private ShipmentStatus newStatus;
    @XmlElement(required = true)
    private ShipmentStatus oldStatus;

    public InvalidStatusChangeExceptionDtoJaxb() {}

    public InvalidStatusChangeExceptionDtoJaxb(ShipmentStatus newStatus, ShipmentStatus oldStatus) {
        this.errorType = "InvalidStatusChange";
        this.newStatus = newStatus;
        this.oldStatus = oldStatus;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public ShipmentStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(ShipmentStatus newStatus) {
        this.newStatus = newStatus;
    }

    public ShipmentStatus getOldStatus() { return oldStatus; }

    public void setOldStatus(ShipmentStatus oldStatus) { this.oldStatus = oldStatus; }

    @Override
    public String toString() {
        return "InvalidStatusChangeExceptionDtoJaxb{" +
                "errorType='" + errorType + '\'' +
                ", newStatus='" + newStatus.toString() + '\'' +
                ", oldStatus='" + oldStatus.toString() + '\'' +
                '}';
    }
}
