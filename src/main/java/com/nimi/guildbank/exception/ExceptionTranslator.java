package com.nimi.guildbank.exception;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

/**
 * Controller advice to translate the server side exceptions to client-friendly
 * json structures.
 */
@ControllerAdvice
public class ExceptionTranslator {
	public static final String ERR_VALIDATION = "Validation errors found";
	public static final String ERR_METHOD_NOT_SUPPORTED = "error.methodNotSupported";
	public static final String ERR_INTERNAL_SERVER_ERROR = "error.internalServerError";

	/**
	 * Process validation error error dto.
	 *
	 * @param ex the ex
	 * @return the error dto
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processValidationError(MethodArgumentNotValidException ex) {
		BindingResult result = ex.getBindingResult();
		List<FieldError> fieldErrors = result.getFieldErrors();
		return processFieldErrors(fieldErrors);
	}


	private ErrorDTO processFieldErrors(List<FieldError> fieldErrors) {
		ErrorDTO dto = new ErrorDTO(ERR_VALIDATION);
		fieldErrors.stream().forEach(fieldError -> dto.add(null, fieldError.getField(), fieldError.getDefaultMessage()));
		return dto;
	}

	/**
	 * Process method not supported exception error dto.
	 *
	 * @param exception the exception
	 * @return the error dto
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseBody
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	public ErrorDTO processMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
		return new ErrorDTO(ERR_METHOD_NOT_SUPPORTED, exception.getMessage());
	}

	/**
	 * Process constraint violation exception error dto.
	 *
	 * @param ex the ex
	 * @return the error dto
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processConstraintViolationException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
		StringBuilder clientErrorMessage = new StringBuilder();
		constraintViolations.forEach(v -> clientErrorMessage.append(v.getMessage()));
		return new ErrorDTO(clientErrorMessage.toString());
	}

	/**
	 * Process null value nested path exception error dto.
	 *
	 * @param ex the ex
	 * @return the error dto
	 */
	@ExceptionHandler(NullValueInNestedPathException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processNullValueInNestedpathException(NullValueInNestedPathException ex) {
		StringBuilder clientErrorMessage = new StringBuilder();
		clientErrorMessage.append(ex.getPropertyName());
		clientErrorMessage.append(":");
		clientErrorMessage.append(ex.getCause());
		return new ErrorDTO(clientErrorMessage.toString());
	}

	/**
	 * Process missing servlet parameter exception error dto.
	 *
	 * @param ex the ex
	 * @return the error dto
	 */
	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
		StringBuilder clientErrorMessage = new StringBuilder();
		clientErrorMessage.append("Missing Required Parameter");
		clientErrorMessage.append(ex.getParameterName());
		clientErrorMessage.append("'");
		return new ErrorDTO(clientErrorMessage.toString());
	}

	/**
	 * Process data integrity violation exception error dto.
	 *
	 * @param ex the ex
	 * @return the error dto
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processDataIntegrityViolationException(DataIntegrityViolationException ex) {
		return new ErrorDTO(ex.getRootCause().getMessage());
	}

	/**
	 * Process json parser exception error dto.
	 *
	 * @param ex the ex
	 * @return the error dto
	 */
	@ExceptionHandler(JsonParseException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO processJSONParserException(JsonParseException ex) {
		return new ErrorDTO(ex.getMessage());
	}

	/**
	 * Handle http message not readable error dto.
	 *
	 * @param ex the ex
	 * @return the error dto
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		if (ex.getCause() instanceof InvalidFormatException) {
			String fieldName = ((InvalidFormatException) ex.getCause()).getPath().get(0).getFieldName();
			Object value = ((InvalidFormatException) ex.getCause()).getValue();
			StringBuilder clientErrorMessage = new StringBuilder();
			clientErrorMessage.append(fieldName);
			clientErrorMessage.append(": Invalid value \'");
			clientErrorMessage.append(value);
			clientErrorMessage.append("\'");
			return new ErrorDTO(clientErrorMessage.toString());
		}
		return new ErrorDTO(ex.getMostSpecificCause().getMessage());
	}

	/**
	 * Process validation exception error dto.
	 *
	 * @param ex the ex
	 * @return the error dto
	 */
	@ExceptionHandler(ValidationException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	@ResponseBody
	protected ErrorDTO procecssValidationException(ValidationException ex) {
		return new ErrorDTO(ex.getMessage());
	}

	/**
	 * Process empty result data exception error dto.
	 *
	 * @param ex the ex
	 */
	@ExceptionHandler(EmptyResultDataAccessException.class)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ResponseBody
	public void processEmptyResultDataAccessException(EmptyResultDataAccessException ex) {
	}

	/**
	 * Handle method argument type missmatch exception error dto.
	 *
	 * @param ex the ex
	 * @return the error dto
	 */
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleMethodArgumentTypeMissmatchException(MethodArgumentTypeMismatchException ex) {
		StringBuilder clientErrorMessage = new StringBuilder();
		clientErrorMessage.append(ex.getName());
		clientErrorMessage.append(": Invalid value '");
		clientErrorMessage.append(ex.getValue());
		clientErrorMessage.append("' was given.");
		return new ErrorDTO(clientErrorMessage.toString());
	}



	/**
	 * Process runtime exception response entity.
	 *
	 * @param ex the ex
	 * @return the response entity
	 * @throws Exception the exception
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorDTO> processRuntimeException(Exception ex) {
		BodyBuilder builder;
		ErrorDTO errorVM;
		ResponseStatus responseStatus = AnnotationUtils.findAnnotation(ex.getClass(), ResponseStatus.class);
		if (responseStatus != null) {
			builder = ResponseEntity.status(responseStatus.value());
			errorVM = new ErrorDTO("error." + responseStatus.value().value(), responseStatus.reason());
		} else {
			builder = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR);
			errorVM = new ErrorDTO(ERR_INTERNAL_SERVER_ERROR, "Internal server error");
		}
		return builder.body(errorVM);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	protected ErrorDTO procecssIlegalArgumentException(IllegalArgumentException ex) {
		return new ErrorDTO(ex.getMessage());
	}
}
