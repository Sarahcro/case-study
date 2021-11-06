package productservice.client;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException.NotFound;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import productservice.dto.redsky.RedSkyProductResponseDto;
import productservice.exception.BaseException;
import productservice.exception.ExceptionFactory;

@Component
@Slf4j
//add circuitbreaker
public class RedSkyClient {

  private final RestTemplate restTemplate;
  private final String productDetailsUrl;
  private final String apiKey;

  public RedSkyClient(RestTemplate restTemplate,
      @Value("${services.redSky.productDetails}") String productDetailsUrl,
      @Value("${services.redSky.key}") String apiKey) {
    this.restTemplate = restTemplate;
    this.productDetailsUrl = productDetailsUrl;
    this.apiKey = apiKey;
  }

  /**
   * Calls the RedSky service to fetch product details
   *
   * @param id the id of the requested product
   * @return {@link RedSkyProductResponseDto} product details
   * @throws BaseException when redsky returns an error
   */
  public RedSkyProductResponseDto getProductDetails(String id) throws BaseException {
    log.info("Calling RedSky for product details for id [{}]", id);
    URI uri = UriComponentsBuilder.fromHttpUrl(productDetailsUrl).queryParam("tcin", id)
        .queryParam("key", apiKey).build().toUri();
    var request = RequestEntity.get(uri).accept(MediaType.APPLICATION_JSON).build();
    return executeRequest(request);
  }

  private RedSkyProductResponseDto executeRequest(final RequestEntity<Void> request)
      throws BaseException {
    try {
      return restTemplate.exchange(request, RedSkyProductResponseDto.class).getBody();
    } catch (NotFound nf) {
      log.error("RedSky returned Not Found. request: [{}], response: [{}]", request.toString(),
          nf.getResponseBodyAsString(), nf);
      throw ExceptionFactory.createResourceNotFoundException("The requested product was not found",
          nf);
    } catch (RestClientException ex) {
      log.error("Unexpected error occurred when calling RedSky, request: [{}], response: [{}]",
          request.toString(), ex.getMessage(), ex);
      throw ExceptionFactory.createExternalServiceException(
          "Unexpected error occurred retrieving product details", ex);
    }
  }


}
