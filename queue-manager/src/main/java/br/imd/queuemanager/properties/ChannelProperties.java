package br.imd.queuemanager.properties;

import java.util.List;
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

  public record MessageChannel(String name, List<String> context) {}
}



