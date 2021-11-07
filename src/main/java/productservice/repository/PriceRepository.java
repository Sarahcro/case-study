package productservice.repository;

import java.util.List;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;
import productservice.entity.Price;

@Repository
public interface PriceRepository extends CassandraRepository<Price, String> {

  List<Price> findAllByProductId(String productId);

}
