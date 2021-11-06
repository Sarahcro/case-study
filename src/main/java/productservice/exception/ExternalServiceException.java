package productservice.exception;

import org.springframework.http.HttpStatus;

public class ExternalServiceException extends BaseException {

  private static final String CODE_EXTERNAL_SERVICE = "error.external.service";

  ExternalServiceException(String message, Throwable cause) {
    super(message, cause, CODE_EXTERNAL_SERVICE, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
