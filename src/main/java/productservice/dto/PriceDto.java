package productservice.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class PriceDto {

  /**
   * The price value
   */
  @NotNull
  private Double value;

  /**
   * The price currency
   */
  @NotEmpty
  private String currencyCode;

}
