package productservice.dto;

import lombok.Data;

@Data
public class ProductResponseDto {

  private Long id;
  private String name;
  private PriceDto current_price;
}
