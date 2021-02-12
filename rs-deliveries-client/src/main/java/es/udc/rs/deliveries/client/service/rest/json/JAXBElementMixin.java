package es.udc.rs.deliveries.client.service.rest.json;

import com.fasterxml.jackson.annotation.JsonValue;

public interface JAXBElementMixin<T> {
    @JsonValue
    Object getValue();
}
