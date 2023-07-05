package br.imd.messagemanager.property;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "channel")
public class ChannelProperty {
  private Channel generic;
  private Channel specific;

  public Channel getGeneric() {
    return generic;
  }

  public void setGeneric(Channel generic) {
    this.generic = generic;
  }

  public Channel getSpecific() {
    return specific;
  }

  public void setSpecific(Channel specific) {
    this.specific = specific;
  }

  public record Channel(String name, List<String> context) {}
}
