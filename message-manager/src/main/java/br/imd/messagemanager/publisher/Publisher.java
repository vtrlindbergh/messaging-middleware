package br.imd.messagemanager.publisher;

import br.imd.messagemanager.model.MiddlewareMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher {

  private static final String SPECIFIC = "specific";

  private final RabbitTemplate rabbitTemplate;

  public Publisher(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  public void send(MiddlewareMessage message) {
    final var context = message.context();
    final var body = message.body();

    rabbitTemplate.convertAndSend(SPECIFIC, context, body);
  }
}
