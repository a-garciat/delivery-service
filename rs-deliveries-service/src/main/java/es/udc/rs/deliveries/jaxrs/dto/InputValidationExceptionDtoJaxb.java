package es.udc.rs.deliveries.jaxrs.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="inputValidationException")
@XmlType(name="inputValidationExceptionType")
public class InputValidationExceptionDtoJaxb {

    @XmlAttribute(required = true)
    private String errorType;
    @XmlElement(required = true)
    private String message;

    public InputValidationExceptionDtoJaxb() {
    }

    public InputValidationExceptionDtoJaxb(String message) {
        this.errorType = "InputValidation";
        this.message = message;
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

    @Override
    public String toString() {
        return "InputValidationExceptionDtoJaxb{" +
                "errorType='" + errorType + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}
