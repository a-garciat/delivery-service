package es.udc.rs.deliveries.jaxrs.dto;


import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.LinkedList;
import java.util.List;

@XmlRootElement(name = "error")
@XmlType(name = "errorType")
public class ExceptionDtoJaxb {

    @XmlAttribute(required = true)
    private String errorType;
    @XmlElement(name = "param", required = true)
    private List<ExceptionParamDtoJaxb> params;

    public ExceptionDtoJaxb() {
    }

    public ExceptionDtoJaxb(String errorType) {
        this.errorType = errorType;
    }

    public ExceptionDtoJaxb(String errorType, List<ExceptionParamDtoJaxb> params) {
        this(errorType);
        this.params = params;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public List<ExceptionParamDtoJaxb> getParams() {
        return params;
    }

    public void setParams(List<ExceptionParamDtoJaxb> params) {
        this.params = params;
    }

    public void addParam(String key, String value) {
        if (params == null) {
            params = new LinkedList();
        }
        params.add(new ExceptionParamDtoJaxb(key, value));
    }

    @Override
    public String toString() {
        return "ExceptionDtoJaxb{" +
                "errorType='" + errorType + '\'' +
                ", params=" + params +
                '}';
    }

}