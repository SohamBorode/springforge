package dev.springforge.core.autoconfigure;

import dev.springforge.core.CorrelationIdFilter;
import dev.springforge.core.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;

/**
 * Automatically registers SpringForge web components in servlet-based
 * Spring Boot applications.
 *
 * Applications can provide their own beans to override these defaults.
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SpringForgeAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler springForgeGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public CorrelationIdFilter springForgeCorrelationIdFilter() {
        return new CorrelationIdFilter();
    }
}
