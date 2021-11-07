package productservice.controller;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import productservice.dto.ProductResponseDto;
import productservice.dto.UpdateProductDto;
import productservice.exception.BaseException;
import productservice.service.ProductService;

@RestController
@Slf4j
public class ProductController {

  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  /**
   * Endpoint to retrieve a product by id
   *
   * @param id the product id
   * @return Response 200 with {@link ProductResponseDto}
   * @throws BaseException if error occurs fetching data
   */
  @GetMapping(value = "/products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductResponseDto> getProductById(
      @NotNull @PathVariable String id) throws BaseException {
    log.info("Request received to get product by id [{}]", id);
    var product = productService.getProductDetails(id);
    log.info("Request complete to get product by id [{}]", id);
    return ResponseEntity.ok(product);
  }

  /**
   * Endpoint to update product information
   *
   * @param id               the product id
   * @param updateProductDto the request body containing updatable fields
   * @return Response 202 Accepted with no body
   * @throws BaseException if request is invalid
   */
  @PutMapping(value = "/products/{id}",
      produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> updateProductById(
      @NotNull @PathVariable String id,
      @Valid @RequestBody final UpdateProductDto updateProductDto) throws BaseException {
    log.info("Request received to update product for id [{}]", id);
    productService.updateProductDetails(updateProductDto, id);
    log.info("Request complete to update product for id [{}]", id);
    return ResponseEntity.accepted().build();
  }

}
