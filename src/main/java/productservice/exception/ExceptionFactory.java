package productservice.exception;

public class ExceptionFactory {

  public static ResourceNotFoundException createResourceNotFoundException(String message,
      Throwable cause) {
    return new ResourceNotFoundException(message, cause);
  }

  public static ExternalServiceException createExternalServiceException(String message,
      Throwable cause) {
    return new ExternalServiceException(message, cause);
  }

}
