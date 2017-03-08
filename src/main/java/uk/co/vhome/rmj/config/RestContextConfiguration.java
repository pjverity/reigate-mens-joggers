package uk.co.vhome.rmj.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// Enabling this allows Spring to inject a CsrfToken in to the CsrfController
@EnableWebSecurity
@Configuration
@ComponentScan(
		basePackages = "uk.co.vhome",
		useDefaultFilters = false,
		includeFilters = @ComponentScan.Filter(RestController.class)
)
public class RestContextConfiguration extends WebMvcConfigurerAdapter
{
}
