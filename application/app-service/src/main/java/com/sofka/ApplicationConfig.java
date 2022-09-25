package com.sofka;

import java.util.List;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@ComponentScan(value = "com.sofka.business.usecase", useDefaultFilters = false,
    includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*UseCase")
)
public class ApplicationConfig {

  @Value("${wallet.general.queue.name}")
  private String walletGeneralQueueName;

  @Value("${wallet.register.queue.name}")
  private String walletRegisterQueueName;

  @Value("${wallet.internal.queue.name}")
  private String internalQueueName;

  @Bean(name = "walletGeneralQueue")
  public Queue walletGeneralQueue() {
    return new Queue(walletGeneralQueueName, true);
  }

  @Bean(name = "walletRegisterQueue")
  public Queue walletRegisterQueue() {
    return new Queue(walletRegisterQueueName, true);
  }

/*  @Bean(name = "walletInternalQueue")
  public Queue walletInternalQueue() {
    return new Queue(internalQueueName, true);
  }*/

  @Bean
  public CorsWebFilter corsWebFilter() {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedOrigins(List.of("*"));
    corsConfig.setMaxAge(8000L);
    corsConfig.addAllowedMethod("*");
    corsConfig.addAllowedHeader("*");

    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", corsConfig);

    return new CorsWebFilter(source);
  }
}
