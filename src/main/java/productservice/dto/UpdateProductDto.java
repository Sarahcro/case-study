package productservice.dto;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateProductDto {

  /**
   * List of price/currency pairs
   */
  @Valid
  @NotEmpty
  private List<PriceDto> currentPrices;

}
