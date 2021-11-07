package productservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends BaseException {

  private static final String CODE_INPUT_INVALID = "error.input.invalid";

  InvalidInputException(String message) {
    super(message, CODE_INPUT_INVALID, HttpStatus.BAD_REQUEST);
  }
}
