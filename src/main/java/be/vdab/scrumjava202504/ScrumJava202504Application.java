package be.vdab.scrumjava202504;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class ScrumJava202504Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ScrumJava202504Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ScrumJava202504Application.class);
    }
}
