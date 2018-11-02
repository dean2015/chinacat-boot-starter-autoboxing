package com.chinacat.boot.starter.autoboxing.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author chinacat
 */
@ConfigurationProperties(prefix = AutoboxingProperties.PREFIX)
@Data
public class AutoboxingProperties {

    public static final String PREFIX = "chinacat.autoboxing.ignore";

    private List<String> defaultPatterns = Arrays.asList(
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**"
    );

    private List<String> patterns = new ArrayList<>(5);

}
