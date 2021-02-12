package es.udc.rs.deliveries.jaxrs.dto;

import es.udc.rs.deliveries.model.shipment.ShipmentStatus;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.time.LocalDateTime;
import java.util.List;

@XmlRootElement(name = "shipment")
@XmlType(name="shipmentType", propOrder = {"shipmentId", "customerId", "packageReference", "address", "status", "creationDate", "deliveryDate", "remainingHours", "links", "cancelAction"})
public class ShipmentDtoJaxb {

    @XmlAttribute(name = "shipmentId", required = true)
    private Long shipmentId;
    @XmlElement(required = true)
    private Long customerId;
    @XmlElement(required = true)
    private Long packageReference;
    @XmlElement(required = true)
    private String address;
    @XmlElement(required = true)
    private ShipmentStatus status;
    @XmlElement(required = true)
    private LocalDateTime creationDate;
    @XmlElement(required = true)
    private LocalDateTime deliveryDate;
    @XmlElement(required = true)
    private int remainingHours;
    @XmlElement(name = "link", namespace = "http://www.w3.org/2005/Atom")
    private List<AtomLinkDtoJaxb> links;
    @XmlElement(name = "action", namespace = "http://schemas.microsoft.com/ado/2007/08/dataservices/metadata")
    private AtomActionDtoJaxb cancelAction;

    public ShipmentDtoJaxb() {
    }

    public ShipmentDtoJaxb(Long shipmentId, Long customerId, Long packageReference, String address, ShipmentStatus status, LocalDateTime creationDate, LocalDateTime deliveryDate, int remainingHours, List<AtomLinkDtoJaxb> links, AtomActionDtoJaxb cancelAction) {
        this.shipmentId = shipmentId;
        this.customerId = customerId;
        this.packageReference = packageReference;
        this.address = address;
        this.status = status;
        this.creationDate = creationDate;
        this.deliveryDate = deliveryDate;
        this.remainingHours = remainingHours;
        this.links = links;
        this.cancelAction = cancelAction;
    }

    public Long getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(Long shipmentId) {
        this.shipmentId = shipmentId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getPackageReference() {
        return packageReference;
    }

    public void setPackageReference(Long packageReference) {
        this.packageReference = packageReference;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getDeliveryDate() { return deliveryDate; }

    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }

    public int getRemainingHours() {
        return remainingHours;
    }

    public void setRemainingHours(int remainingHours) {
        this.remainingHours = remainingHours;
    }

    public List<AtomLinkDtoJaxb> getLinks() {
        return links;
    }

    public void setLinks(List<AtomLinkDtoJaxb> links) {
        this.links = links;
    }

    public AtomActionDtoJaxb getCancelAction() {
        return cancelAction;
    }

    public void setCancelAction(AtomActionDtoJaxb cancelAction) {
        this.cancelAction = cancelAction;
    }

    @Override
    public String toString() {
        return "ShipmentDtoJaxb{" +
                "shipmentId=" + shipmentId +
                ", customerId=" + customerId +
                ", packageReference=" + packageReference +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", creationDate=" + creationDate +
                ", deliveryDate=" + deliveryDate +
                ", remainingHours=" + remainingHours +
                '}';
    }
}
