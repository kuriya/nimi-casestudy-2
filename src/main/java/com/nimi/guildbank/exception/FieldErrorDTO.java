package com.nimi.guildbank.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The type Field error dto.
 */
@AllArgsConstructor
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldErrorDTO {
    private final String objectName;
    private final String field;
    private final String message;
}
