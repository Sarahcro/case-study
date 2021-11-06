package productservice.exception;

import org.springframework.http.HttpStatus;


public class BaseException extends Exception {

  private final String code;
  private final HttpStatus httpStatus;

  BaseException(final String message, final Throwable cause, final String code, final HttpStatus httpStatus){
    super(message, cause);
    this.code = code;
    this.httpStatus = httpStatus;
  }

}
