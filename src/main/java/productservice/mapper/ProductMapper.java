package productservice.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import productservice.dto.PriceDto;
import productservice.dto.ProductResponseDto;
import productservice.dto.redsky.RedSkyProductResponseDto;
import productservice.entity.Price;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mapping(target = "id", source = "details.data.product.tcin")
  @Mapping(target = "name", source = "details.data.product.item.productDescription.title")
  @Mapping(target = "currentPrices", source = "prices")
  ProductResponseDto dataToResponseDto(RedSkyProductResponseDto details, List<Price> prices);

  @Mapping(target = "productId", source = "productId")
  Price priceDtoToEntity(PriceDto priceDto, String productId);

}
