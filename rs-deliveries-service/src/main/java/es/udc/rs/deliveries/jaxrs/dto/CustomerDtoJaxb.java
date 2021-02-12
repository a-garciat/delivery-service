package es.udc.rs.deliveries.jaxrs.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDateTime;
import java.util.List;

@XmlRootElement(name = "customer")
@XmlType(name = "customerType", propOrder = {"customerId", "name", "Cif", "address", "links", "deleteAction"})
public class CustomerDtoJaxb {

    @XmlAttribute(name = "customerId", required = true)
    private Long customerId;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private String Cif;
    @XmlElement(required = true)
    private String address;
    @XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
    private List<AtomLinkDtoJaxb> links;
    @XmlElement(name = "action", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata")
    private AtomActionDtoJaxb deleteAction;

    public CustomerDtoJaxb () {
    }

    public CustomerDtoJaxb(Long customerId, String name, String cif, String address, List<AtomLinkDtoJaxb> links, AtomActionDtoJaxb deleteAction) {
        this.customerId = customerId;
        this.name = name;
        Cif = cif;
        this.address = address;
        this.links = links;
        this.deleteAction = deleteAction;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCif() {
        return Cif;
    }

    public void setCif(String cif) {
        Cif = cif;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) { this.address = address; }

    public List<AtomLinkDtoJaxb> getLinks() { return this.links; }

    public void setLink(List<AtomLinkDtoJaxb> links) { this.links = links; }

    public AtomActionDtoJaxb getDeleteAction() {
        return deleteAction;
    }

    public void setDeleteAction(AtomActionDtoJaxb deleteAction) {
        this.deleteAction = deleteAction;
    }

    @Override
    public String toString() {
        return "CustomerDtoJaxb{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", Cif='" + Cif + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
