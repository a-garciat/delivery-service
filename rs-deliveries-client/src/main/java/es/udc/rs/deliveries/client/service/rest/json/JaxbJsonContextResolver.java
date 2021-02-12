package es.udc.rs.deliveries.client.service.rest.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBElement;

@Provider
public class JaxbJsonContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public JaxbJsonContextResolver() {
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JaxbAnnotationModule());
        this.mapper.addMixIn(JAXBElement.class, JAXBElementMixin.class);
    }

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
        return mapper;
    }

}
