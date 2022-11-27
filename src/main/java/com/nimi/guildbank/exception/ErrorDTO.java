package com.nimi.guildbank.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for transfering error message with a list of field errors.
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorDTO {
	private final String message;
	private final String description;
	private List<FieldErrorDTO> fieldErrors;

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
}
