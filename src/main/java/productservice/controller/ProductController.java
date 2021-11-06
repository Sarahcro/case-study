package productservice.controller;

import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import productservice.dto.ProductResponseDto;
import productservice.dto.redsky.RedSkyProductResponseDto;
import productservice.exception.BaseException;
import productservice.service.ProductService;

@Controller
@Slf4j
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService){
    this.productService = productService;
  }

  /**
   * Endpoint to retrieve a product by id
   *
   * @param id the product id
   * @return
   * @throws BaseException
   */
  @GetMapping(value = "/products/{id}")
  public ResponseEntity<ProductResponseDto> getProductById(
      @NotNull @PathVariable String id) throws BaseException {
    log.info("Request received to get product by id [{}]", id);
    var product = productService.getProductDetails(id);
    log.info("Request complete to get product by id [{}]", id);
    return ResponseEntity.ok(product);
  }

}
