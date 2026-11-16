package dev.springforge.core.autoconfigure;

import dev.springforge.core.CorrelationIdFilter;
import dev.springforge.core.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class SpringForgeAutoConfigurationTest {

    private final WebApplicationContextRunner webContextRunner =
            new WebApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(
                            SpringForgeAutoConfiguration.class
                    ));

    @Test
    void registersDefaultBeansInServletWebApplication() {
        webContextRunner.run(context -> {
            assertThat(context).hasSingleBean(GlobalExceptionHandler.class);
            assertThat(context).hasSingleBean(CorrelationIdFilter.class);
        });
    }

    @Test
    void backsOffWhenApplicationProvidesItsOwnHandler() {
        webContextRunner
                .withBean(GlobalExceptionHandler.class, GlobalExceptionHandler::new)
                .run(context -> {
                    assertThat(context).hasSingleBean(GlobalExceptionHandler.class);
                    assertThat(context).hasSingleBean(CorrelationIdFilter.class);
                });
    }

    @Test
    void doesNotRegisterWebBeansInNonWebApplication() {
        new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of(
                        SpringForgeAutoConfiguration.class
                ))
                .run(context -> {
                    assertThat(context).doesNotHaveBean(GlobalExceptionHandler.class);
                    assertThat(context).doesNotHaveBean(CorrelationIdFilter.class);
                });
    }
}
