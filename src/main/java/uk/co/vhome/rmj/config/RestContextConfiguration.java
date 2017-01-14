package uk.co.vhome.rmj.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
@ComponentScan(
		basePackages = "uk.co.vhome",
		useDefaultFilters = false,
		includeFilters = @ComponentScan.Filter(RestController.class)
)
public class RestContextConfiguration extends WebMvcConfigurerAdapter
{
}
