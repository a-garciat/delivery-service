package es.udc.rs.deliveries.jaxrs.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import java.net.URI;

@XmlType(name = "atomActionType", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata")
public class AtomActionDtoJaxb {
    @XmlAttribute(name = "metadata",  required = true)
    private String metadata;
    @XmlAttribute(name = "target", required = true)
    private String target;
    @XmlAttribute(name = "title", required = true)
    private String title;

    public AtomActionDtoJaxb() {
    }

    public AtomActionDtoJaxb(String metadata, String target, String title) {
        this.metadata = metadata;
        this.target = target;
        this.title = title;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
