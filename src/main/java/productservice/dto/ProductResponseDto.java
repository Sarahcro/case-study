package productservice.dto;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductResponseDto extends UpdateProductDto {

  private String id;
  private String name;

}
