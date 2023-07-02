package br.imd.queuemanager.mosquitto;

import br.imd.queuemanager.properties.ChannelProperties;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

@Configuration
public class MosquittoConfig {
  private static final String CLIENT_ID = "mqtt-client";

  private final ChannelProperties channelProperties;

  public MosquittoConfig(ChannelProperties channelProperties) {this.channelProperties = channelProperties;}

  @Bean
  public MqttPahoClientFactory mqttClientFactory(@Value("${spring.mqtt.broker-url}") String brokerUrl) {
    final var factory = new DefaultMqttPahoClientFactory();
    final var options = new MqttConnectOptions();
    options.setServerURIs(new String[] {brokerUrl});
    factory.setConnectionOptions(options);

    return factory;
  }

  @Bean
  public MessageChannel mqttInputChannel() {
    return new DirectChannel();
  }

  @Bean
  public MessageChannel mqttOutboundChannel() {
    return new DirectChannel();
  }

  @Bean
  public MqttPahoMessageDrivenChannelAdapter mqttInbound(MqttPahoClientFactory clientFactory) {
    final var topics = channelProperties.getAllContext();
    final var adapter =
        new MqttPahoMessageDrivenChannelAdapter(CLIENT_ID, clientFactory, topics.toArray(new String[0]));
    adapter.setCompletionTimeout(5000);
    adapter.setConverter(new DefaultPahoMessageConverter());
    adapter.setQos(2);
    adapter.setOutputChannel(mqttInputChannel());
    return adapter;
  }
}
