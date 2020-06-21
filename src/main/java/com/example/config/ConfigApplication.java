package com.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootApplication
@EnableConfigurationProperties(CustomProperties.class)
public class ConfigApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ConfigApplication.class)
//				.initializers(ctx -> ctx.getEnvironment().getPropertySources().addLast(new CustomPropertySource()))
                .run(args);
    }

    @Bean
    ApplicationRunner applicationRunner(
            Environment environment,
            @Value("${greeting.message: Default Greeting ${greeting.end}}") String defaultValue,
            @Value("${HOME}") String userHome,
            @Value("${greeting.arg:}") String fromProgramArgs,
            @Value("${spring.datasource.url}") String datasourceUrlEnvVar,
            CustomProperties customPropertyMessage,
            CustomProperties2 customProperties,
            @Value("${message-from-config-server}") String greetingFromConfigServer,
            @Value("${server.vault.greeting:No Vault}") String greetingFromVaultServer) {
        return args -> {
            log.info("message from Environment: {}", environment.getProperty("message-from-properties"));
            log.info("message from defaultValue: {}", defaultValue);
            log.info("User Home: {}", userHome);
            log.info("form program args: {}", fromProgramArgs);
            log.info("form environment variable SPRING_DATASOURCE_URL: {}", datasourceUrlEnvVar);
            log.info("from custom property: {}", customPropertyMessage.getMessage());
            log.info("from custom property 2: {}", customProperties.getMessage());
            log.info("from github config server: {}", greetingFromConfigServer);
            log.info("from Vault config server: {}", greetingFromVaultServer);
        };
    }

    static class CustomPropertySource extends PropertySource<String> {
        CustomPropertySource() {
            super("custom");
        }

        @Override
        public Object getProperty(String s) {
            return name.equalsIgnoreCase("custom-source-message")
                    ? "Greeting from " + CustomPropertySource.class.getSimpleName()
                    : "";
        }
    }
}

@Configuration
class CustomConfiguration {

    @Bean
    @ConfigurationProperties("custom2")
    CustomProperties2 customProperties2() {
        return new CustomProperties2();
    }
}

@Data
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("custom")
class CustomProperties {
    private final String message;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class CustomProperties2 {
    private String message;
}
