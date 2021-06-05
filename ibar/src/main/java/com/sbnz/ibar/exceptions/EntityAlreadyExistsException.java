package com.sbnz.ibar.exceptions;

import lombok.Getter;
import lombok.Setter;

public class EntityAlreadyExistsException extends Exception {

    @Getter
    @Setter
    private String entityName;

    @Getter
    @Setter
    private Object entityId;

    public EntityAlreadyExistsException(String entityName, Object entityId) {
        super(String.format("Entity %s with ID $s already exists.", entityName, entityId));
    }

}
