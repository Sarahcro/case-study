package productservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {

  /**
   * Error code
   */
  private String code;

  /**
   * Localized error message mapping to a code
   */
  private String message;

  /**
   * Further details about the error
   */
  private String details;
}
