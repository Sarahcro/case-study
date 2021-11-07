package productservice.dto.redsky;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class ProductDescriptionDto {

  private String title;

  @JsonAlias(value = "downstream_description")
  private String downstreamDescription;

}
