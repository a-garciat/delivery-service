package es.udc.rs.deliveries.jaxrs.util;

import es.udc.rs.deliveries.jaxrs.dto.AtomActionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.AtomLinkDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.ShipmentDtoJaxb;
import es.udc.rs.deliveries.jaxrs.resources.CustomerResource;
import es.udc.rs.deliveries.jaxrs.resources.ShipmentResource;
import es.udc.rs.deliveries.model.shipment.Shipment;
import es.udc.rs.deliveries.model.shipment.ShipmentStatus;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static es.udc.rs.deliveries.model.shipment.ShipmentStatus.PENDING;

public class ShipmentToShipmentDtoJaxbConversor {
    public static Shipment toShipment(ShipmentDtoJaxb shipment) {
        return new Shipment(
                shipment.getShipmentId(),
                shipment.getCustomerId(),
                shipment.getPackageReference(),
                shipment.getAddress(),
                shipment.getStatus(),
                shipment.getCreationDate(),
                null
        );

    }

    public static ShipmentDtoJaxb toShipmentDto(Shipment shipment, URI baseUri, String type) {
        AtomLinkDtoJaxb selfLink = ServiceUtil.getLinkFromUri(baseUri, ShipmentResource.class,
                shipment.getShipmentId(), "self", "Self link", type);
        AtomLinkDtoJaxb associatedCustomerLink = ServiceUtil.getLinkFromUri(baseUri, CustomerResource.class,
                shipment.getCustomerId(), "related", "Associated customer link", type);
        AtomActionDtoJaxb cancelActionLink = null;
        if (shipment.getStatus()==PENDING) {
            cancelActionLink = ServiceUtil.getActionFromUri("#RsDeliveriesService.CancelShipment", baseUri,
                    ShipmentResource.class, shipment.getShipmentId(), "Cancel", "changeStatus/cancel");
        }
        List<AtomLinkDtoJaxb> links = new ArrayList<AtomLinkDtoJaxb>();
        links.add(selfLink);
        links.add(associatedCustomerLink);
        return new ShipmentDtoJaxb(
                shipment.getShipmentId(),
                shipment.getCustomerId(),
                shipment.getPackageReference(),
                shipment.getAddress(),
                shipment.getStatus(),
                shipment.getCreationDate(),
                shipment.getDeliveryDate(),
                shipment.getStatus().equals(ShipmentStatus.DELIVERED) ? 0 : (Duration.between(LocalDateTime.now(), shipment.getMaxDeliveryDate()).toHoursPart()),
                links,
                cancelActionLink
        );
    }

    public static List<ShipmentDtoJaxb> toShipmentDto(List<Shipment> shipments, URI baseUri, String type) {
        List<ShipmentDtoJaxb> shipmentDtos = new ArrayList<>(shipments.size());
        for (Shipment shipment : shipments) {
            shipmentDtos.add(toShipmentDto(shipment, baseUri, type));
        }
        return shipmentDtos;
    }
}