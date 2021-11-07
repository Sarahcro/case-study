package productservice.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PriceDto {

  @NotNull
  private Double value;

  @NotEmpty
  private String currencyCode;

}
