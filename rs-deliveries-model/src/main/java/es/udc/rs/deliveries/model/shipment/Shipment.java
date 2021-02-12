package es.udc.rs.deliveries.model.shipment;

import java.time.LocalDateTime;
import java.util.Objects;

public class Shipment {

    private Long shipmentId;
    private Long customerId;
    private Long packageReference;
    private String address;
    private ShipmentStatus status;
    private LocalDateTime creationDate;
    private LocalDateTime maxDeliveryDate;
    private LocalDateTime deliveryDate;
    
	public Shipment(Long shipmentId, Long customerId, Long packageReference, String address, ShipmentStatus status,
			LocalDateTime creationDate, LocalDateTime maxDeliveryDate) {
		super();
		this.shipmentId = shipmentId;
		this.customerId = customerId;
		this.packageReference = packageReference;
		this.address = address;
		this.status = status;
		this.creationDate = creationDate;
		this.maxDeliveryDate = maxDeliveryDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Shipment shipment = (Shipment) o;
		return Objects.equals(shipmentId, shipment.shipmentId) &&
				Objects.equals(customerId, shipment.customerId) &&
				Objects.equals(packageReference, shipment.packageReference) &&
				Objects.equals(address, shipment.address) &&
				status == shipment.status &&
				Objects.equals(creationDate, shipment.creationDate) &&
				Objects.equals(maxDeliveryDate, shipment.maxDeliveryDate) &&
				Objects.equals(deliveryDate, shipment.deliveryDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(shipmentId, customerId, packageReference, address, status, creationDate, maxDeliveryDate, deliveryDate);
	}

	public Shipment(Shipment s){
		this(s.getShipmentId(), s.getCustomerId(), s.getPackageReference(), s.getAddress(),
				s.getStatus(), s.getCreationDate(), s.getMaxDeliveryDate());
		this.deliveryDate = s.deliveryDate;
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

	public LocalDateTime getMaxDeliveryDate() {
		return maxDeliveryDate;
	}

	public void setMaxDeliveryDate(LocalDateTime maxDeliveryDate) {
		this.maxDeliveryDate = maxDeliveryDate;
	}

	public LocalDateTime getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDateTime deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
}