package es.udc.rs.deliveries.jaxrs.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "errorParamType")
public class ExceptionParamDtoJaxb {

    @XmlAttribute(required = true)
    private String key;
    @XmlAttribute(required = true)
    private String value;

    public ExceptionParamDtoJaxb() {
    }

    public ExceptionParamDtoJaxb(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ExceptionParamDtoJaxb{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}