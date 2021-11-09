package productservice.service

import org.mapstruct.factory.Mappers
import org.springframework.http.HttpStatus
import productservice.client.RedSkyClient
import productservice.dto.PriceDto
import productservice.dto.UpdateProductDto
import productservice.dto.redsky.RedSkyProductDto
import productservice.dto.redsky.RedSkyDataDto
import productservice.dto.redsky.RedSkyItemDto
import productservice.dto.redsky.RedSkyProductDescriptionDto
import productservice.dto.redsky.RedSkyProductResponseDto
import productservice.entity.Price
import productservice.exception.InvalidInputException
import productservice.mapper.ProductMapper
import productservice.repository.PriceRepository
import spock.lang.Specification

class ProductServiceSpec extends Specification {

    def redSkyClientMock = Mock(RedSkyClient)
    def priceRepoMock = Mock(PriceRepository)
    def productMapper = Mappers.getMapper(ProductMapper)

    def productService = new ProductService(redSkyClientMock, priceRepoMock, productMapper)

    def 'can get products by id'(){
        given: 'a product id'
        def id = "12345"

        def redskyDetails = new RedSkyProductResponseDto(data: new RedSkyDataDto(product: new RedSkyProductDto(tcin: id, item: new RedSkyItemDto(productDescription: new RedSkyProductDescriptionDto(title: "Test Product")))))
        def price1 = new Price(value: 100.95, currencyCode: 'USD')
        def price2 = new Price(value: 102.98, currencyCode: 'CAD')
        when: "getProductDetails is called"
        def response = productService.getProductDetails(id)

        then: 'expected response is returned'
        response.id == id
        response.name == redskyDetails.data.product.item.productDescription.title
        response.currentPrices.size() == 2
        response.currentPrices[0].value == price1.value
        response.currentPrices[0].currencyCode == price1.currencyCode
        response.currentPrices[1].value == price2.value
        response.currentPrices[1].currencyCode == price2.currencyCode

        and: 'expected interactions occur'
        1 * redSkyClientMock.getProductDetails(id) >> redskyDetails
        1 * priceRepoMock.findAllByProductId(id) >> [price1, price2]
    }

    def 'can get products by id succeeds even if no prices exist for product'(){
        given: 'a product id'
        def id = "12345"

        def redskyDetails = new RedSkyProductResponseDto(data: new RedSkyDataDto(product: new RedSkyProductDto(tcin: id, item: new RedSkyItemDto(productDescription: new RedSkyProductDescriptionDto(title: "Test Product")))))

        when: "getProductDetails is called"
        def response = productService.getProductDetails(id)

        then: 'expected response is returned'
        response.id == id
        response.name == redskyDetails.data.product.item.productDescription.title
        response.currentPrices.size() == 0

        and: 'expected interactions occur'
        1 * redSkyClientMock.getProductDetails(id) >> redskyDetails
        1 * priceRepoMock.findAllByProductId(id) >> []
    }

    def 'can update products by id'(){
        given: 'a product id and request'
        def id = "12345"
        def updateRequest = new UpdateProductDto(currentPrices: [new PriceDto(value: 45.34, currencyCode: "USD")])
        def price1 = new Price(productId: id, value: 45.34, currencyCode: 'USD')

        when: "updateProductDetails is called"
        productService.updateProductDetails(updateRequest, id)

        then: 'no exceptions or response is returned'
        noExceptionThrown()

        and: 'expected interactions occur, correct value was saved to repository'
        1 * priceRepoMock.saveAll(_) >> {
            args ->
                List<Price> pricesSaved = args[0]
                pricesSaved.size() == 1
                pricesSaved[0].productId == id
                pricesSaved[0].currencyCode == updateRequest.currentPrices[0].currencyCode
                pricesSaved[0].value == updateRequest.currentPrices[0].value
                [price1]
        }
    }

    def 'update product fails if several prices specified for the same country code'(){
        given: 'a product id and request'
        def id = "12345"
        def updateRequest = new UpdateProductDto(currentPrices: [new PriceDto(value: 45.34, currencyCode: "USD"), new PriceDto(value: 600.34, currencyCode: "USD")])

        when: "updateProductDetails is called"
        productService.updateProductDetails(updateRequest, id)

        then: 'InvalidInputException is thrown'
        def ex = thrown(InvalidInputException)
        ex.code == "error.input.invalid"
        ex.message == "Cannot provide multiple prices with the same currencyCode"
        ex.httpStatus == HttpStatus.BAD_REQUEST

        and: 'expected interactions occur, nothing saved to repository'
        0 * priceRepoMock.saveAll(_)
    }
}
