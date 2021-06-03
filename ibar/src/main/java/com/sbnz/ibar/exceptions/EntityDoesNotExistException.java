package com.sbnz.ibar.exceptions;

import lombok.Getter;
import lombok.Setter;

public class EntityDoesNotExistException extends Exception {

    @Getter
    @Setter
    private String entityName;

    @Getter
    @Setter
    private Object entityId;

    public EntityDoesNotExistException(String entityName, Object entityId) {
        super(String.format("Entity %s with ID $s does not exist.", entityName, entityId));
    }

}
