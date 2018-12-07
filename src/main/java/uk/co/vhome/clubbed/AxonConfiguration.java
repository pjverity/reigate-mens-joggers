package uk.co.vhome.clubbed;

import org.axonframework.eventhandling.saga.repository.SagaStore;
import org.axonframework.eventhandling.saga.repository.inmemory.InMemorySagaStore;
import org.axonframework.eventsourcing.eventstore.EventStorageEngine;
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AxonConfiguration
{
	@Bean
	public RestTemplate restTemplate()
	{
		return new RestTemplate();
	}

	@Bean
	EventStorageEngine eventStorageEngine()
	{
		return new InMemoryEventStorageEngine();
	}

	@Bean
	SagaStore sagaStore()
	{
		return new InMemorySagaStore();
	}
}
