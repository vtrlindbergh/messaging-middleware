package br.imd.messagemanager.listener;

import br.imd.messagemanager.model.MiddlewareMessage;
import br.imd.messagemanager.publisher.Publisher;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {

  private final Publisher publisher;

  public RabbitMQListener(Publisher publisher) {
    this.publisher = publisher;
  }

  @RabbitListener(queues = "#{channelProperty.generic.name()}")
  public void handler(@Payload MiddlewareMessage message) {
    publisher.send(message);
  }
}
