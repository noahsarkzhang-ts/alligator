package org.noahsark.registration.configuration;

import org.noahsark.registration.repository.MemoryRepository;
import org.noahsark.registration.repository.Repository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by hadoop on 2021/3/13.
 */
@Configuration
@Component
public class RegistrationConfigration {

    @Bean
    public Repository repository() {
        return new MemoryRepository();
    }
}
