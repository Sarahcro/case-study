package productservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponseDto extends UpdateProductDto {

    /**
     * The product id
     */
    private String id;

    /**
     * The product name/title
     */
    private String name;
}
