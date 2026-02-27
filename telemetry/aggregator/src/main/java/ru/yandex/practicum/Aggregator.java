package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Aggregator {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(Aggregator.class, args);
        AggregatorStarter aggregator = ctx.getBean(AggregatorStarter.class);
        aggregator.start();
    }
}
