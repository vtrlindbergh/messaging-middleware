package br.imd.queuemanager.rabbitmq;

import br.imd.queuemanager.properties.ChannelProperties;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableRabbit
@Configuration
public class RabbitConfig {
  private static final Logger LOGGER = LoggerFactory.getLogger(RabbitConfig.class);

  private final ChannelProperties properties;

  public RabbitConfig(ChannelProperties properties) {
    this.properties = properties;
  }

  @Bean("genericExchange")
  public Exchange genericExchange() {
    LOGGER.info("Create Exchange RabbitMq: {}", properties.getGeneric().name());
    return ExchangeBuilder.topicExchange(properties.getGeneric().name()).durable(true).build();
  }

  @Bean("specificExchange")
  public Exchange specificExchange() {
    LOGGER.info("Create Exchange RabbitMq: {}", properties.getSpecific().name());
    return ExchangeBuilder.topicExchange(properties.getSpecific().name()).durable(true).build();
  }

  @Bean
  public Declarables declarables(List<List<Queue>> queues, List<List<Binding>> bindings) {
    return new Declarables(Stream.of(queues, bindings)
        .flatMap(Collection::stream)
        .flatMap(Collection::stream)
        .collect(Collectors.toUnmodifiableSet()));
  }

  @Bean("genericQueues")
  public List<Queue> genericQueues() {
    return properties.getGeneric().context().stream()
        .peek(queueName -> LOGGER.info("Create Queue RabbitMq: {}", queueName))
        .map(context -> QueueBuilder.durable(context).build())
        .toList();
  }

  @Bean
  public List<Binding> genericBindings(
      @Qualifier("genericQueues") List<Queue> queues,
      @Qualifier("genericExchange") Exchange exchange) {
    return queues.stream()
        .map(queue -> BindingBuilder.bind(queue).to(exchange).with("").noargs())
        .toList();
  }

  @Bean("specificQueues")
  public List<Queue> specificQueues() {
    return properties.getSpecific().context().stream()
        .peek(queueName -> LOGGER.info("Create Queue RabbitMq: {}", queueName))
        .map(context -> QueueBuilder.durable(context).build())
        .toList();
  }

  @Bean
  public List<Binding> sspecificBindings(
      @Qualifier("specificQueues") List<Queue> queues,
      @Qualifier("specificExchange") Exchange exchange) {
    return queues.stream()
        .map(queue -> BindingBuilder.bind(queue).to(exchange).with(queue.getName()).noargs())
        .toList();
  }
}
