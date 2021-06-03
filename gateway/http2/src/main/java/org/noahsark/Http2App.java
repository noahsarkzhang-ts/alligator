package org.noahsark;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http2.Http2Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Http2App
 */
@SpringBootApplication
public class Http2App
{
    public static void main(String[] args) {
        SpringApplication.run(Http2App.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return factory -> {
            Connector connector = new Connector();
            connector.addUpgradeProtocol(new Http2Protocol());
            connector.setPort(8080);
            factory.addAdditionalTomcatConnectors(connector);
        };
    }

}
