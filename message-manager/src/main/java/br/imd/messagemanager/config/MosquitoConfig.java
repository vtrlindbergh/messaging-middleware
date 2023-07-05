package br.imd.messagemanager.config;

import br.imd.messagemanager.listener.MosquitoListener;
import br.imd.messagemanager.property.ChannelProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MosquitoConfig {
  private static final Logger LOGGER = LogManager.getLogger(MosquitoConfig.class);
  private static final String CLIENT_ID = "mqtt-listener";

  private final ChannelProperty channelProperty;
  private final MosquitoListener mosquitoListener;

  public MosquitoConfig(ChannelProperty channelProperty, MosquitoListener mosquitoListener) {
    this.channelProperty = channelProperty;
    this.mosquitoListener = mosquitoListener;
  }

  @Bean
  public MqttClient mqttClient(@Value("${spring.mqtt.broker-url}") String broker) throws MqttException {
    final var mqttClient = new MqttClient(broker, CLIENT_ID, new MemoryPersistence());
    final var connOpts = new MqttConnectOptions();
    connOpts.setCleanSession(true);

    mqttClient.setCallback(mosquitoListener);

    mqttClient.connect(connOpts);
    mqttClient.subscribe(channelProperty.getGeneric().name());

    LOGGER.info("Listening to MQTT topic: {}", channelProperty.getGeneric().name());
    return mqttClient;
  }
}
