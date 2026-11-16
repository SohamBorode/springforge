package dev.springforge.core.autoconfigure;

import dev.springforge.core.CorrelationIdFilter;
import dev.springforge.core.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SpringForgeAutoConfigurationTest {

    private final WebApplicationContextRunner webContextRunner =
            new WebApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(
                            SpringForgeAutoConfiguration.class
                    ));

    @Test
    void registersDefaultBeansInServletWebApplication() {
        webContextRunner.run(context -> {
            assertEquals(1, context.getBeansOfType(GlobalExceptionHandler.class).size());
            assertEquals(1, context.getBeansOfType(CorrelationIdFilter.class).size());
        });
    }

    @Test
    void backsOffWhenApplicationProvidesItsOwnHandler() {
        webContextRunner
                .withBean(GlobalExceptionHandler.class, GlobalExceptionHandler::new)
                .run(context -> {
                    assertEquals(1, context.getBeansOfType(GlobalExceptionHandler.class).size());
                    assertEquals(1, context.getBeansOfType(CorrelationIdFilter.class).size());
                });
    }

    @Test
    void doesNotRegisterWebBeansInNonWebApplication() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        SpringForgeAutoConfiguration.class
                ))
                .run(context -> {
                    assertFalse(context.containsBean("springForgeGlobalExceptionHandler"));
                    assertFalse(context.containsBean("springForgeCorrelationIdFilter"));
                });
    }
}
