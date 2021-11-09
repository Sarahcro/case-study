package productservice.mapper

import org.mapstruct.factory.Mappers
import productservice.dto.PriceDto
import productservice.dto.redsky.RedSkyItemDto
import productservice.dto.redsky.RedSkyProductDto
import productservice.dto.redsky.RedSkyDataDto
import productservice.dto.redsky.RedSkyProductDescriptionDto
import productservice.dto.redsky.RedSkyProductResponseDto
import productservice.entity.Price
import spock.lang.Specification

class ProductMapperSpec extends Specification {

    def productMapper = Mappers.getMapper(ProductMapper)

    def 'can map product details and prices to a product response'(){
        given: 'RedSkyProductDetails and Price List'
        def redskyDetails = new RedSkyProductResponseDto(data: new RedSkyDataDto(product: new RedSkyProductDto(tcin: "555555", item: new RedSkyItemDto(productDescription: new RedSkyProductDescriptionDto(title: "Test Product")))))
        def price1 = new Price(value: 100.95, currencyCode: 'USD')
        def price2 = new Price(value: 102.98, currencyCode: 'CAD')
        def prices = [price1, price2]

        when: 'mapper is called'
        def response = productMapper.dataToResponseDto(redskyDetails, prices)

        then: 'response is mapped correctly'
        response.id == redskyDetails.data.product.tcin
        response.name == redskyDetails.data.product.item.productDescription.title
        response.currentPrices.size() == 2
        response.currentPrices[0].value == price1.value
        response.currentPrices[0].currencyCode == price1.currencyCode
        response.currentPrices[1].value == price2.value
        response.currentPrices[1].currencyCode == price2.currencyCode

    }

    def 'can map a PriceDto to an entity'(){
        given: 'a priceDto and an id'
        def priceDto = new PriceDto(value: 100.00, currencyCode: "USD")
        def id = "9999"

        when: "mapper is called"
        def response = productMapper.priceDtoToEntity(priceDto, id)

        then: 'response is mapped correctly'
        response.productId == id
        response.value == priceDto.value
        response.currencyCode == priceDto.currencyCode
    }
}
