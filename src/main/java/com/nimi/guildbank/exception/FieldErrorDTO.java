/*
 * PEARSON PROPRIETARY AND CONFIDENTIAL INFORMATION SUBJECT TO NDA
 * Copyright (c) 2017 Pearson Education, Inc.
 * All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains the property of
 * Pearson Education, Inc. The intellectual and technical concepts contained
 * herein are proprietary to Pearson Education, Inc. and may be covered by U.S.
 * and Foreign Patents, patent applications, and are protected by trade secret
 * or copyright law. Dissemination of this information, reproduction of this
 * material, and copying or distribution of this software is strictly forbidden
 * unless prior written permission is obtained from Pearson Education, Inc.
*/
package com.nimi.guildbank.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * The type Field error dto.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldErrorDTO {

    private final String objectName;

    private final String field;

    private final String message;

    /**
     * Instantiates a new Field error dto.
     *
     * @param dto     the dto
     * @param field   the field
     * @param message the message
     */
    public FieldErrorDTO(String dto, String field, String message) {
        this.objectName = dto;
        this.field = field;
        this.message = message;
    }

    /**
     * Gets object name.
     *
     * @return the object name
     */
    public String getObjectName() {
        return objectName;
    }

    /**
     * Gets field.
     *
     * @return the field
     */
    public String getField() {
        return field;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

}
