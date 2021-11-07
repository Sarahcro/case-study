package productservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = false)
@Data
public class BaseException extends Exception {

  private final String code;
  private final HttpStatus httpStatus;

  BaseException(final String message, final Throwable cause, final String code, final HttpStatus httpStatus){
    super(message, cause);
    this.code = code;
    this.httpStatus = httpStatus;
  }

  BaseException(final String message, final String code, final HttpStatus httpStatus){
    super(message);
    this.code = code;
    this.httpStatus = httpStatus;
  }

}
