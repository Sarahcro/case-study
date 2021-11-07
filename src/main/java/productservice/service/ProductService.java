package productservice.service;

import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import productservice.client.RedSkyClient;
import productservice.dto.PriceDto;
import productservice.dto.ProductResponseDto;
import productservice.dto.UpdateProductDto;
import productservice.exception.BaseException;
import productservice.exception.ExceptionFactory;
import productservice.mapper.ProductMapper;
import productservice.repository.PriceRepository;

@Service
@Slf4j
public class ProductService {

  private final RedSkyClient redSkyClient;
  private final PriceRepository priceRepository;
  private final ProductMapper productMapper;

  public ProductService(RedSkyClient redSkyClient, PriceRepository priceRepository,
      ProductMapper productMapper) {
    this.redSkyClient = redSkyClient;
    this.priceRepository = priceRepository;
    this.productMapper = productMapper;
  }

  /**
   * Gets product and price details and returns a complete response
   *
   * @param productId the product id
   * @return {@link ProductResponseDto} containing product details
   * @throws BaseException when error occurs
   */
  public ProductResponseDto getProductDetails(String productId) throws BaseException {
    var productDetails = redSkyClient.getProductDetails(productId);
    var prices = priceRepository.findAllByProductId(productId);
    log.info("Found [{}] prices for product [{}]", prices.size(), productId);
    return productMapper.dataToResponseDto(productDetails, prices);
  }

  /**
   * Validates that the request contains unique currenyCodes and saves updates to DB
   *
   * @param updateRequest contains updatable fields
   * @param productId     the productId
   * @throws BaseException if request was invalid
   */
  public void updateProductDetails(UpdateProductDto updateRequest, String productId)
      throws BaseException {
    var newPrices = updateRequest.getCurrentPrices();
    var uniqueCount = newPrices.stream().map(PriceDto::getCurrencyCode).distinct().count();
    if (uniqueCount < newPrices.size()) {
      throw ExceptionFactory.createInvalidInputException(
          "Cannot provide multiple prices with the same currencyCode");
    }
    var priceList = newPrices
        .stream().map(p -> productMapper.priceDtoToEntity(p, productId))
        .collect(Collectors.toList());
    priceRepository.saveAll(priceList);
  }
}
