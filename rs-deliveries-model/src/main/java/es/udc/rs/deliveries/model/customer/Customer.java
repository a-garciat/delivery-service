package es.udc.rs.deliveries.model.customer;

import java.time.LocalDateTime;
import java.util.Objects;

public class Customer {

    private Long customerId;
    private String name;
    private String Cif;
    private String address;
    private LocalDateTime creationDate;
    
	public Customer(Long customerId, String name, String cif, String address, LocalDateTime creationDate) {
		super();
		this.customerId = customerId;
		this.name = name;
		Cif = cif;
		this.address = address;
		this.creationDate = creationDate;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Customer customer = (Customer) o;
		return Objects.equals(customerId, customer.customerId) &&
				Objects.equals(name, customer.name) &&
				Objects.equals(Cif, customer.Cif) &&
				Objects.equals(address, customer.address) &&
				Objects.equals(creationDate, customer.creationDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId, name, Cif, address, creationDate);
	}

	public Customer(Customer c) {
		this(c.getCustomerId(), c.getName(), c.getCif(), c.getAddress(), c.getCreationDate());
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

	public void setAddress(String address) {
		this.address = address;
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}

}