package productservice.service;

import org.springframework.stereotype.Service;
import productservice.client.RedSkyClient;
import productservice.dto.redsky.RedSkyProductResponseDto;
import productservice.exception.BaseException;

@Service
public class ProductService {

  private final RedSkyClient redSkyClient;

  public ProductService(RedSkyClient redSkyClient){
    this.redSkyClient = redSkyClient;
  }

  public ProductResponseDto getProductDetails(String id) throws BaseException {
    var productDetails = redSkyClient.getProductDetails(id);

  }
}
