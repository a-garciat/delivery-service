package es.udc.rs.deliveries.jaxrs.util;

import es.udc.rs.deliveries.jaxrs.dto.AtomActionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.AtomLinkDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.CustomerDtoJaxb;
import es.udc.rs.deliveries.jaxrs.resources.CustomerResource;
import es.udc.rs.deliveries.jaxrs.resources.ShipmentResource;
import es.udc.rs.deliveries.model.customer.Customer;
import es.udc.rs.deliveries.model.deliveryservice.DeliveryServiceFactory;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CustomerToCustomerDtoJaxbConversor {
    public static Customer toCustomer(CustomerDtoJaxb customer) {
        return new Customer(
                customer.getCustomerId(),
                customer.getName(),
                customer.getCif(),
                customer.getAddress(),
                LocalDateTime.now());
    }

    public static CustomerDtoJaxb toCustomerDto(Customer customer, URI baseUri, String type) {
        AtomLinkDtoJaxb selfLink = ServiceUtil.getLinkFromUri(baseUri, CustomerResource.class,
                customer.getCustomerId(), "self", "Self link", type);
        AtomLinkDtoJaxb associatedShipmentsLink = ServiceUtil.getFirstTenShipments(baseUri, ShipmentResource.class,
                "shipments", "First ten associated shipments", type, customer.getCustomerId());
        AtomActionDtoJaxb deleteActionLink = null;
        try {
            if (DeliveryServiceFactory.getService().findShipmentsByCustomerIdAndStatus(customer.getCustomerId(), null, 1, 1).isEmpty()) {
                deleteActionLink = ServiceUtil.getActionFromUri("#RsDeliveriesService.DeleteCustomer", baseUri,
                        CustomerResource.class, customer.getCustomerId(), "Delete", "deleteCustomer");
            }
        } catch (Exception e){
            e.printStackTrace();
            deleteActionLink = null;
        }
        List<AtomLinkDtoJaxb> links = new ArrayList<AtomLinkDtoJaxb>();
        links.add(selfLink);
        links.add(associatedShipmentsLink);
        return new CustomerDtoJaxb(
            customer.getCustomerId(),
                customer.getName(),
                customer.getCif(),
                customer.getAddress(),
                links,
                deleteActionLink
        );
    }

    public static List<CustomerDtoJaxb> toCustomerDto(List<Customer> customers, URI baseUri, String type) {
        List<CustomerDtoJaxb> customerDtos = new ArrayList<>(customers.size());
        for (Customer customer : customers) {
            customerDtos.add(toCustomerDto(customer, baseUri, type));
        }
        return customerDtos;
    }
}
