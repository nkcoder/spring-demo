package org.nkcoder.validation.exception;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.nkcoder.validation.controller.EmployeeController;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice(assignableTypes = EmployeeController.class)
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ApiError handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    log.error("method argument not valid: {}", exception.getMessage());
    String errorMessage = "field error";

    BindingResult bindingResult = exception.getBindingResult();
    if (bindingResult.hasErrors()) {
      errorMessage = bindingResult.getFieldErrors().stream()
          .map(FieldError::getDefaultMessage)
          .collect(Collectors.joining(" | "));
    }

    return ApiError.builder()
        .code("INVALID_ARGUMENT")
        .message(errorMessage)
        .build();
  }

}
