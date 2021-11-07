package productservice.service

import org.mapstruct.factory.Mappers
import productservice.client.RedSkyClient
import productservice.dto.redsky.ItemDto
import productservice.dto.redsky.ProductDescriptionDto
import productservice.dto.redsky.ProductDto
import productservice.dto.redsky.RedSkyDataDto
import productservice.dto.redsky.RedSkyProductResponseDto
import productservice.entity.Price
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

        def redskyDetails = new RedSkyProductResponseDto(data: new RedSkyDataDto(product: new ProductDto(tcin: id, item: new ItemDto(productDescription: new ProductDescriptionDto(title: "Test Product")))))
        def price1 = new Price(value: 100.00, currencyCode: 'USD')
        def price2 = new Price(value: 102.00, currencyCode: 'CAD')
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
}
