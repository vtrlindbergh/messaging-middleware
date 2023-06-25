package br.imd.queuemanager.kafka;

import static java.util.function.Predicate.not;

import br.imd.queuemanager.properties.ChannelProperties;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConfig.class);

  private static final int NUM_PARTITIONS = 1;
  private static final short REPLICATION_FACTOR = 1;

  private final ChannelProperties properties;

  public KafkaConfig(ChannelProperties properties) {this.properties = properties;}

  @Bean
  public KafkaAdmin kafkaAdmin() {
    return new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092"));
  }

  @Bean
  public AdminClient adminClient(KafkaAdmin kafkaAdmin) throws Exception {
    final var adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
    final var currentTopics = adminClient.listTopics().names().get();
    final var topics = properties.getAllContext().stream()
        .filter(not(currentTopics::contains))
        .peek(topicName -> LOGGER.info("Create Topic Kafka: {}", topicName))
        .map(topicName -> TopicBuilder.name(topicName).partitions(NUM_PARTITIONS).replicas(REPLICATION_FACTOR).build())
        .toList();

    adminClient.createTopics(topics).all().get();
    return adminClient;
  }
}
