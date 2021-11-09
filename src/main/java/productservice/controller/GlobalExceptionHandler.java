package productservice.controller;

import java.util.Locale;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import productservice.dto.ErrorResponseDto;
import productservice.exception.BaseException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final String DEFAULT_ERROR_CODE = "error.runtime.default";
  private static final String ERROR_INVALID_INPUT = "error.input.invalid";

  private final MessageSource messageSource;

  public GlobalExceptionHandler(@Qualifier("messageSource") MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @ExceptionHandler(BaseException.class)
  public ResponseEntity<ErrorResponseDto> processBaseException(BaseException be,
      HttpServletResponse response) {
    Locale curentLocale = LocaleContextHolder.getLocale();
    String code = be.getCode() != null ? be.getCode() : DEFAULT_ERROR_CODE;
    String msg = messageSource.getMessage(code, null, curentLocale);
    response.setStatus(be.getHttpStatus().value());
    var responseDto = ErrorResponseDto.builder().code(code).message(msg).details(be.getMessage())
        .build();
    return new ResponseEntity<>(responseDto, be.getHttpStatus());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> processMethodArgumentNotValidException(
      MethodArgumentNotValidException ex) {
    Locale curentLocale = LocaleContextHolder.getLocale();
    String code = ERROR_INVALID_INPUT;
    String msg = messageSource.getMessage(code, null, curentLocale);
    var details = ex.getBindingResult().getAllErrors().stream()
        .map(error -> (error instanceof FieldError) ?
            ((FieldError) error).getField() + " " + error.getDefaultMessage()
            : error.getDefaultMessage())
        .collect(Collectors.joining(", "));
    var responseDto = ErrorResponseDto.builder().code(code).message(msg).details(details).build();
    return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
  }
}
