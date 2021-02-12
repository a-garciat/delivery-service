package es.udc.rs.deliveries.jaxrs.dto;


import es.udc.rs.deliveries.model.deliveryservice.exceptions.HasShipmentException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="hasShipmentException")
@XmlType(name="hasShipmentExceptionType")
public class HasShipmentExceptionDtoJaxb {
    @XmlAttribute(required = true)
    private String errorType;
    @XmlElement(required = true)
    private String message;
    @XmlElement(required = true)
    private long customerId;

    public HasShipmentExceptionDtoJaxb() {

    }

    @Override
    public String toString() {
        return "HasShipmentExceptionDtoJaxb{" +
                "errorType='" + errorType + '\'' +
                ", message='" + message + '\'' +
                ", customerId=" + customerId +
                '}';
    }

    public HasShipmentExceptionDtoJaxb(long customerId, String message) {
        this.customerId = customerId;
        this.message = message;
        this.errorType = "HasShipmentException";
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }
}
