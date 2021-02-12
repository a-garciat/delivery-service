@XmlSchema(namespace = "http://es.udc.es/deliveries/xml", elementFormDefault = javax.xml.bind.annotation.XmlNsForm.QUALIFIED)
@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(type= LocalDateTime.class, value= LocalDateTimeXmlAdapter.class)
})
@XmlAccessorType(XmlAccessType.FIELD)
package es.udc.rs.deliveries.jaxrs.dto;
import es.udc.rs.deliveries.jaxrs.jaxb.LocalDateTimeXmlAdapter;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchema;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.LocalDateTime;
