package es.udc.rs.deliveries.client.service.rest;

import es.udc.rs.deliveries.client.service.rest.dto.*;
import es.udc.rs.deliveries.client.service.rest.exception.HasShipmentException;
import es.udc.rs.deliveries.client.service.rest.exception.InvalidShipmentCancelationException;
import es.udc.rs.deliveries.client.service.rest.exception.InvalidStatusChangeException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JaxbExceptionConversor {

    public static InputValidationException toInputValidationException(
            InputValidationExceptionDtoJaxb exDto) {
        return new InputValidationException(exDto.getMessage());
    }

    public static InstanceNotFoundException toInstanceNotFoundException(
            InstanceNotFoundExceptionDtoJaxb exDto) {
        return new InstanceNotFoundException(exDto.getInstanceId(),
                exDto.getInstanceType());
    }

    public static InstanceNotFoundException toInstanceNotFoundException(
            ExceptionDtoJaxb exDto) {
        Object instanceId = null;
        String instanceType = null;

        for (ExceptionParamDtoJaxb exParamDto : exDto.getParam()) {
            switch (exParamDto.getKey()) {
                case "instanceId": {
                    instanceId = Long.valueOf(exParamDto.getValue());
                    break;
                }
                case "instanceType": {
                    instanceType = exParamDto.getValue();
                    break;
                }
            }
        }
        return new InstanceNotFoundException(instanceId, instanceType);
    }

    public static InputValidationException toInputValidationException(
            ExceptionDtoJaxb exDto) {
        String message = null;

        for (ExceptionParamDtoJaxb exParamDto : exDto.getParam()) {
            switch (exParamDto.getKey()) {
                case "message": {
                    message = exParamDto.getValue();
                    break;
                }
            }
        }
        return new InputValidationException(message);
    }

    public static HasShipmentException toHasShipmentException(
            ExceptionDtoJaxb exDto) {
        String message = null;
        long customerId = -1;

        for (ExceptionParamDtoJaxb exParamDto : exDto.getParam()) {
            switch (exParamDto.getKey()) {
                case "message": {
                    message = exParamDto.getValue();
                    break;
                }
                case "customerId": {
                    customerId = Long.valueOf(exParamDto.getValue());
                    break;
                }
            }
        }
        return new HasShipmentException(customerId);
    }

    public static InvalidShipmentCancelationException toInvalidShipmentCancelationException(
            ExceptionDtoJaxb exDto) {
        ShipmentStatus oldStatus = null;

        for (ExceptionParamDtoJaxb exParamDto : exDto.getParam()) {
            switch (exParamDto.getKey()) {
                case "oldStatus": {
                    oldStatus = ShipmentStatus.fromValue(exParamDto.getValue());
                    break;
                }
            }
        }
        return new InvalidShipmentCancelationException(oldStatus);
    }

    public static InvalidStatusChangeException toInvalidStatusChangeException(
            ExceptionDtoJaxb exDto) {
        ShipmentStatus oldStatus = null;
        ShipmentStatus newStatus = null;

        for (ExceptionParamDtoJaxb exParamDto : exDto.getParam()) {
            switch (exParamDto.getKey()) {
                case "oldStatus": {
                    oldStatus = ShipmentStatus.fromValue(exParamDto.getValue());
                    break;
                }
                case "newStatus": {
                    newStatus = ShipmentStatus.fromValue(exParamDto.getValue());
                    break;
                }
            }
        }
        return new InvalidStatusChangeException(oldStatus, newStatus);
    }



}