package productservice.entity;

import lombok.Data;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
@Data
public class Price {

  @PrimaryKeyColumn(name = "productId", type = PrimaryKeyType.PARTITIONED)
  private String productId;

  @PrimaryKeyColumn(name = "currencyCode")
  private String currencyCode;

  @Column
  private Double value;
}
