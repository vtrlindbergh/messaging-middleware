package br.imd.queuemanager.properties;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "channel")
public class ChannelProperties {

  private MessageChannel generic;
  private MessageChannel specific;

  public MessageChannel getGeneric() {
    return generic;
  }

  public void setGeneric(MessageChannel generic) {
    this.generic = generic;
  }

  public MessageChannel getSpecific() {
    return specific;
  }

  public void setSpecific(MessageChannel specific) {
    this.specific = specific;
  }

  public List<String> getAllContext() {
    return Stream.of(getGeneric().context(), getSpecific().context())
        .flatMap(Collection::stream)
        .toList();
  }

  public record MessageChannel(String name, List<String> context) {}
}



