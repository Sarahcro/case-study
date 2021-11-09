package productservice.dto.redsky;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class RedSkyItemDto {

  @JsonAlias(value = "product_description")
  private RedSkyProductDescriptionDto productDescription;
}
