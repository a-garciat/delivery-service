package es.udc.rs.deliveries.client.service.rest.jaxbconversor;

import es.udc.rs.deliveries.client.service.rest.dto.CustomerDtoJaxb;
import es.udc.rs.deliveries.client.service.rest.dto.ObjectFactory;

import javax.xml.bind.JAXBElement;

public class CustomerDtoToCustomerDtoJaxbConversor {

    public static JAXBElement<CustomerDtoJaxb> toJaxbCustomer(CustomerDtoJaxb customerDto) {
        JAXBElement<CustomerDtoJaxb> jaxbElement = new ObjectFactory().createCustomer(customerDto);
        return jaxbElement;
    }

}
