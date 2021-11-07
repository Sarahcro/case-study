package productservice.dto.redsky;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class ItemDto {

  @JsonAlias(value = "product_description")
  private ProductDescriptionDto productDescription;
}
