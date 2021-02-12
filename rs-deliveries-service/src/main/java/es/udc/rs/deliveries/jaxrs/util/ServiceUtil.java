package es.udc.rs.deliveries.jaxrs.util;

import es.udc.rs.deliveries.jaxrs.dto.AtomActionDtoJaxb;
import es.udc.rs.deliveries.jaxrs.dto.AtomLinkDtoJaxb;
import es.udc.rs.deliveries.model.shipment.ShipmentStatus;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class ServiceUtil {

    private static List<MediaType> responseMediaTypes = Arrays
            .asList(new MediaType[] { MediaType.APPLICATION_XML_TYPE,
                    MediaType.APPLICATION_JSON_TYPE });

    public static String getTypeAsStringFromHeaders(HttpHeaders headers) {
        List<MediaType> mediaTypes = headers.getAcceptableMediaTypes();
        for (MediaType m : mediaTypes) {
            MediaType compatibleType = getCompatibleAcceptableMediaType(m);
            if (compatibleType != null) {
                return compatibleType.toString();
            }
        }
        return null;
    }

    private static MediaType getCompatibleAcceptableMediaType(MediaType type) {
        for (MediaType m : responseMediaTypes) {
            if (m.isCompatible(type)) {
                return m;
            }
        }
        return null;
    }

    public static AtomLinkDtoJaxb getLinkFromUri(URI baseUri, Class<?> resourceClass,
                                                 Object instanceId, String rel, String title, String type) {
        Link.Builder linkBuilder = Link
                .fromPath(
                        baseUri.toString()
                                + UriBuilder.fromResource(resourceClass)
                                .build().toString() + "/"
                                + instanceId).rel(rel).title(title);
        if (type != null) {
            linkBuilder.type(type);
        }
        Link link = linkBuilder.build();
        return new AtomLinkDtoJaxb(link.getUri(), link.getRel(), link.getType(), link.getTitle());
    }

    public static AtomActionDtoJaxb getActionFromUri(String metadata, URI baseUri, Class<?> resourceClass,
                                                 Object instanceId, String title, String actionPathParam) {
        //return new AtomActionDtoJaxb(metadata, "http://localhost:7070/rs-deliveries-service/customers/" + instanceId + "/" + actionPathParam, title);
        Link.Builder linkBuilder = Link
                .fromPath(
                        baseUri.toString()
                                + UriBuilder.fromResource(resourceClass)
                                .build().toString() + "/"
                                + instanceId).title(title);
        Link link = linkBuilder.build();
        return new AtomActionDtoJaxb(metadata, link.getUri().toString()+'/'+actionPathParam, link.getTitle());
    }

    /*public static Link getIntervalLink2(UriInfo uriInfo, Map<String, Object> queryParams, String rel, String title, String type, Class<?> resourceClass) {
        UriBuilder uriBuilder = UriBuilder.fromUri(uriInfo.getBaseUri())
                .path(UriBuilder.fromResource(resourceClass).toString());
        for ()
        uriBuilder.queryParam()
        Link.Builder linkBuilder = Link.fromUriBuilder(uriBuilder)
                .rel(rel)
                .title(title);
        if (type!=null) {
            linkBuilder.type(type);
        }
        return linkBuilder.build();
    }*/

    public static AtomLinkDtoJaxb getFirstTenShipments(URI baseUri, Class<?> resourceClass, String rel, String title, String type, long customerId) {
        Link link = getShipmentsIntervalLink(baseUri, resourceClass, 1, 10, customerId, null, rel, title, type);
        return new AtomLinkDtoJaxb(link.getUri(), link.getRel(), link.getType(), link.getTitle());
    }

    public static Link getShipmentsIntervalLink(URI baseUri, Class<?> resourceClass, int startIndex, int count, long customerId, ShipmentStatus status , String rel, String title, String type) {
        UriBuilder uriBuilder = UriBuilder.fromUri("").replacePath(baseUri.toString()
                + UriBuilder.fromResource(resourceClass)
                .build().toString() + "/")
                .queryParam("first", startIndex)
                .queryParam("max", count)
                .queryParam("customerId", customerId);
        if (status != null) {
                uriBuilder.queryParam("status", status);
        }
        Link.Builder linkBuilder = Link.fromUriBuilder(uriBuilder)
                .rel(rel)
                .title(title);
        if (type!=null) {
            linkBuilder.type(type);
        }
        return linkBuilder.build();
    }

    public static Link getCustomerIntervalLink(URI baseUri, Class<?> resourceClass, String name, String rel, String title, String type) {
        UriBuilder uriBuilder = UriBuilder.fromUri("").replacePath(baseUri.toString()
                + UriBuilder.fromResource(resourceClass)
                .build().toString() + "/")
                .queryParam("name", name);
        Link.Builder linkBuilder = Link.fromUriBuilder(uriBuilder)
                .rel(rel)
                .title(title);
        if (type!=null) {
            linkBuilder.type(type);
        }
        return linkBuilder.build();
    }

}
