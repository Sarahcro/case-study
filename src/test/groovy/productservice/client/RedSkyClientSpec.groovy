package productservice.client

import org.springframework.http.HttpStatus
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import productservice.dto.redsky.RedSkyItemDto
import productservice.dto.redsky.RedSkyProductDescriptionDto
import productservice.dto.redsky.RedSkyProductDto
import productservice.dto.redsky.RedSkyDataDto
import productservice.dto.redsky.RedSkyProductResponseDto
import productservice.exception.ExternalServiceException
import productservice.exception.ResourceNotFoundException
import spock.lang.Specification

class RedSkyClientSpec extends Specification {

    def restTemplate = Mock(RestTemplate)
    def productDetailsUrl = "https://testurl.com/productDetails"
    def key = "12345678"

    def redSkyClient = new RedSkyClient(restTemplate, productDetailsUrl, key)

    def 'fetching product details is successful'(){
        given: 'a productId'
        def id = "111111"

        def redskyDetails = new RedSkyProductResponseDto(data: new RedSkyDataDto(product: new RedSkyProductDto(tcin: id, item: new RedSkyItemDto(productDescription: new RedSkyProductDescriptionDto(title: "Test Product")))))

        when: 'getProductDetails is called'
        def response = redSkyClient.getProductDetails(id)

        then: 'valid response is returned'
        response == redskyDetails

        and: 'expected interactions occurred, request was valid'
        1 * restTemplate.exchange(_, RedSkyProductResponseDto.class) >> {
            args ->
                RequestEntity<Void> request = args[0]
                request.url.toString() == "https://testurl.com/productDetails?tcin=111111&key=12345678"
                new ResponseEntity<>(redskyDetails, HttpStatus.OK)
        }
    }

    def 'fetching product details fails when Not Found is returned'(){
        given: 'a productId'
        def id = "111111"

        when: 'getProductDetails is called'
        def response = redSkyClient.getProductDetails(id)

        then: 'ResourceNotFoundException is thrown'
        def ex = thrown(ResourceNotFoundException)
        ex.httpStatus == HttpStatus.NOT_FOUND
        ex.message == 'Product 111111 was not found'

        and: 'expected interactions occurred, exception was thrown'
        1 * restTemplate.exchange(_, RedSkyProductResponseDto.class) >> {
            throw new HttpClientErrorException.NotFound("Not Found", null, null, null)
        }
    }

    def 'fetching product details fails when other error is returned'(){
        given: 'a productId'
        def id = "111111"

        when: 'getProductDetails is called'
        def response = redSkyClient.getProductDetails(id)

        then: 'ExternalServiceException is thrown'
        def ex = thrown(ExternalServiceException)
        ex.httpStatus == HttpStatus.INTERNAL_SERVER_ERROR
        ex.message == "Unexpected error occurred retrieving product details"

        and: 'expected interactions occurred, exception was thrown'
        1 * restTemplate.exchange(_, RedSkyProductResponseDto.class) >> {
            throw new RestClientException("Something went wrong")
        }
    }
}
