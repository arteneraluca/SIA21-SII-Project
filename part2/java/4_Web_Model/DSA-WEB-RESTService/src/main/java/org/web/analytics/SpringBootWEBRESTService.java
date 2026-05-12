package org.web.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.web.analytics.config.AnalyticsProperties;

@SpringBootApplication(scanBasePackages = {"org.web.analytics", "org.j4di"})
@EnableConfigurationProperties(AnalyticsProperties.class)
public class SpringBootWEBRESTService {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootWEBRESTService.class, args);
    }
}
