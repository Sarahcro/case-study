package productservice.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

  private static final String CODE_NOT_FOUND = "error.resource.notFound";

  ResourceNotFoundException(String message, Throwable cause) {
    super(message, cause, CODE_NOT_FOUND, HttpStatus.NOT_FOUND);
  }
}
