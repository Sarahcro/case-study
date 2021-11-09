package productservice.integration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import productservice.dto.PriceDto
import productservice.dto.UpdateProductDto
import productservice.entity.Price
import productservice.repository.PriceRepository
import wiremock.org.apache.http.HttpHeaders

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse
import static com.github.tomakehurst.wiremock.client.WireMock.get
import static org.hamcrest.Matchers.hasSize
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ProductITSpec extends BaseITSpec {

    @Autowired
    PriceRepository priceRepository

    def 'can get product details'() {
        given: 'a valid request'
        def id = "54456110"
        def request = MockMvcRequestBuilders.get("/products/$id")
        //add a few prices to the repo to return
        def price1 = new Price(productId: id, currencyCode: 'USD', value: 100.00)
        def price2 = new Price(productId: id, currencyCode: 'CAD', value: 105.00)
        priceRepository.saveAll([price1, price2])

        wireMockServer.stubFor(get("/redsky/redsky_aggregations/v1/redsky/case_study_v1?tcin=$id&key=testApiKey")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("redsky/sampleResponse.json")))

        when: 'request is excecuted'
        def response = mockMvc.perform(request)

        then: 'successful response with body'
        response.andExpect(status().isOk())
                .andExpect(jsonPath('$.id').value("54456110"))
                .andExpect(jsonPath('$.name').value("How to Train Your Dragon: The Hidden World (Blu-ray + DVD + Digital)"))
                .andExpect(jsonPath('$.currentPrices', hasSize(2)))
                .andExpect(jsonPath('$.currentPrices[0].value').value(105.0))
                .andExpect(jsonPath('$.currentPrices[0].currencyCode').value('CAD'))
                .andExpect(jsonPath('$.currentPrices[1].value').value(100.0))
                .andExpect(jsonPath('$.currentPrices[1].currencyCode').value('USD'))
    }


    def 'get product details fails when product is not found in RedSky'() {
        given: 'an invalid request'
        def id = "111111"
        def request = MockMvcRequestBuilders.get("/products/$id")

        wireMockServer.stubFor(get("/redsky/redsky_aggregations/v1/redsky/case_study_v1?tcin=$id&key=testApiKey")
                .willReturn(aResponse().withStatus(404)))

        when: 'request is excecuted'
        def response = mockMvc.perform(request)

        then: 'not found response with error body'
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath('$.code').value("error.resource.notFound"))
                .andExpect(jsonPath('$.message').value("The requested resource was not found"))
                .andExpect(jsonPath('$.details').value("Product 111111 was not found"))
    }

    def 'can get product details fails when RedSky fails'() {
        given: 'a request'
        def id = "111111"
        def request = MockMvcRequestBuilders.get("/products/$id")

        wireMockServer.stubFor(get("/redsky/redsky_aggregations/v1/redsky/case_study_v1?tcin=$id&key=testApiKey")
                .willReturn(aResponse().withStatus(500)))

        when: 'request is excecuted'
        def response = mockMvc.perform(request)

        then: '500 response with error body'
        response.andExpect(status().isInternalServerError())
                .andExpect(jsonPath('$.code').value("error.external.service"))
                .andExpect(jsonPath('$.message').value("There was a problem calling an external service"))
                .andExpect(jsonPath('$.details').value("Unexpected error occurred retrieving product details"))
    }

    def 'can update product details'() {
        given: 'a valid request with body'
        def id = "54456"
        def requestBody = new UpdateProductDto(currentPrices: [new PriceDto(value: 300.0, currencyCode: 'CAD'), new PriceDto(value: 300.0, currencyCode: 'USD')])
        def request = MockMvcRequestBuilders.put("/products/$id").content(objectMapper.writeValueAsString(requestBody)).header("Content-Type", "application/json")

        //add some prices to the repo
        def price1 = new Price(productId: id, currencyCode: 'INR', value: 7388.25)
        def price2 = new Price(productId: id, currencyCode: 'USD', value: 100.00)

        priceRepository.saveAll([price1, price2])

        when: 'request is excecuted'
        def response = mockMvc.perform(request)

        then: 'successful response with no body'
        response.andExpect(status().isAccepted())

        and: 'repo has updated prices'
        def prices = priceRepository.findAllByProductId(id)
        prices.size() == 3
        prices[0].value == requestBody.currentPrices[0].value
        prices[0].currencyCode == requestBody.currentPrices[0].currencyCode
        prices[1].value == price1.value //wasn't updated
        prices[1].currencyCode == price1.currencyCode
        prices[2].value == requestBody.currentPrices[1].value
        prices[2].currencyCode == requestBody.currentPrices[1].currencyCode

    }


    def 'update product details fails when request contains two prices of same currencyCode'() {
        given: 'a request with invalid body'
        def id = "54453336"
        def requestBody = new UpdateProductDto(currentPrices: [new PriceDto(value: 300.0, currencyCode: 'CAD'), new PriceDto(value: 300.0, currencyCode: 'CAD')])
        def request = MockMvcRequestBuilders.put("/products/$id").content(objectMapper.writeValueAsString(requestBody)).header("Content-Type", "application/json")

        when: 'request is excecuted'
        def response = mockMvc.perform(request)

        then: '400 response with error message'
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.code').value("error.input.invalid"))
                .andExpect(jsonPath('$.message').value("The client input was invalid"))
                .andExpect(jsonPath('$.details').value("Cannot provide multiple prices with the same currencyCode"))

    }

    def 'update product details fails when request missing price value'() {
        given: 'a request with invalid body'
        def id = "54453336"
        def requestBody = new UpdateProductDto(currentPrices: [new PriceDto(currencyCode: 'CAD'), new PriceDto(value: 300.0, currencyCode: 'CAD')])
        def request = MockMvcRequestBuilders.put("/products/$id").content(objectMapper.writeValueAsString(requestBody)).header("Content-Type", "application/json")

        when: 'request is excecuted'
        def response = mockMvc.perform(request)

        then: '400 response with error message'
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.code').value("error.input.invalid"))
                .andExpect(jsonPath('$.message').value("The client input was invalid"))
                .andExpect(jsonPath('$.details').value("currentPrices[0].value must not be null"))
    }

    def 'update product details fails when request missing currency value'() {
        given: 'a request with invalid body'
        def id = "54453336"
        def requestBody = new UpdateProductDto(currentPrices: [new PriceDto(value: 1.99), new PriceDto(value: 300.0, currencyCode: 'CAD')])
        def request = MockMvcRequestBuilders.put("/products/$id").content(objectMapper.writeValueAsString(requestBody)).header("Content-Type", "application/json")

        when: 'request is excecuted'
        def response = mockMvc.perform(request)

        then: '400 response with error message'
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.code').value("error.input.invalid"))
                .andExpect(jsonPath('$.message').value("The client input was invalid"))
                .andExpect(jsonPath('$.details').value("currentPrices[0].currencyCode must not be empty"))
    }

    def 'update product details fails when request missing prices'() {
        given: 'a request with invalid body'
        def id = "54453336"
        def requestBody = new UpdateProductDto(currentPrices: [])
        def request = MockMvcRequestBuilders.put("/products/$id").content(objectMapper.writeValueAsString(requestBody)).header("Content-Type", "application/json")

        when: 'request is excecuted'
        def response = mockMvc.perform(request)

        then: '400 response with error message'
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath('$.code').value("error.input.invalid"))
                .andExpect(jsonPath('$.message').value("The client input was invalid"))
                .andExpect(jsonPath('$.details').value("currentPrices must not be empty"))
    }
}
