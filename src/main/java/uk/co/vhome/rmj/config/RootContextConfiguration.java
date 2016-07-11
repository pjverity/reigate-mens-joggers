package uk.co.vhome.rmj.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@ComponentScan(
		basePackages = "uk.co.vhome",
		excludeFilters = @ComponentScan.Filter(Controller.class)
)
public class RootContextConfiguration
{
}
