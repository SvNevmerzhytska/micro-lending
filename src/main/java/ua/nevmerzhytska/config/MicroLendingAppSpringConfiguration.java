package ua.nevmerzhytska.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "ua.nevmerzhytska"
})
public class MicroLendingAppSpringConfiguration {
}
