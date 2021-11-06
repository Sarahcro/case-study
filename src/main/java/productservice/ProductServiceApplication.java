package productservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ProductServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProductServiceApplication.class, args);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public ReloadableResourceBundleMessageSource messageSource(){
    var messageBundle = new ReloadableResourceBundleMessageSource();
    messageBundle.setBasename("classpath:messages/messages");
    messageBundle.setDefaultEncoding("UTF-8");
    return messageBundle;
  }

}
