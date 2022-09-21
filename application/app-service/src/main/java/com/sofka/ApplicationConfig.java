package com.sofka;

import java.util.List;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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

  public static final String EXCHANGE = "wallet-core";

  @Bean
  public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
    var admin = new RabbitAdmin(rabbitTemplate);
    admin.declareExchange(new TopicExchange(EXCHANGE));

    return admin;
  }

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
