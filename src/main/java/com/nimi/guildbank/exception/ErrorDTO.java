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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for transfering error message with a list of field errors.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {

	private final String message;
	private final String description;

	private List<FieldErrorDTO> fieldErrors;

	/**
	 * Instantiates a new Error dto.
	 *
	 * @param message the message
	 */
	public ErrorDTO(String message) {
		this(message, null);
	}

	/**
	 * Instantiates a new Error dto.
	 *
	 * @param message     the message
	 * @param description the description
	 */
	public ErrorDTO(String message, String description) {
		this.message = message;
		this.description = description;
	}

	/**
	 * Instantiates a new Error dto.
	 *
	 * @param message     the message
	 * @param description the description
	 * @param fieldErrors the field errors
	 */
	public ErrorDTO(String message, String description, List<FieldErrorDTO> fieldErrors) {
		this.message = message;
		this.description = description;
		this.fieldErrors = fieldErrors;
	}

	/**
	 * Add.
	 *
	 * @param objectName the object name
	 * @param field      the field
	 * @param message    the message
	 */
	public void add(String objectName, String field, String message) {
		if (fieldErrors == null) {
			fieldErrors = new ArrayList<>();
		}
		fieldErrors.add(new FieldErrorDTO(objectName, field, message));
	}

	/**
	 * Gets message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Gets description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets field errors.
	 *
	 * @return the field errors
	 */
	public List<FieldErrorDTO> getFieldErrors() {
		return fieldErrors;
	}
}
