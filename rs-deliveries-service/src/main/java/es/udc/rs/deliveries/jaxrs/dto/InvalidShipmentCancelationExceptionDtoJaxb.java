package es.udc.rs.deliveries.jaxrs.dto;

import es.udc.rs.deliveries.model.shipment.ShipmentStatus;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "invalidShipmentCancelationException")
@XmlType(name="invalidShipmentCancelationException", propOrder = {"oldStatus"})
public class InvalidShipmentCancelationExceptionDtoJaxb {

    @XmlAttribute(required = true)
    private String errorType;
    @XmlElement(required = true)
    private ShipmentStatus oldStatus;

    public InvalidShipmentCancelationExceptionDtoJaxb(){}

    public InvalidShipmentCancelationExceptionDtoJaxb(ShipmentStatus oldStatus) {
        this.errorType = "InvalidShipmentCancelation";
        this.oldStatus = oldStatus;
    }

    public ShipmentStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(ShipmentStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }
}
