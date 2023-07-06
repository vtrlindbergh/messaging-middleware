from kafka import KafkaConsumer
import time

topic = 'test-topic'
bootstrap_servers = ['localhost:9092']
consumer_group_id = 'sample'

consumer = KafkaConsumer(topic, group_id=consumer_group_id, bootstrap_servers=bootstrap_servers)

start_time = time.time()

num_messages = 0
for message in consumer:
    num_messages += 1
    if num_messages % 1000 == 0:
        elapsed_time = time.time() - start_time
        throughput = num_messages / elapsed_time
        print(f"{num_messages} messages processed in {elapsed_time:.2f} seconds, throughput: {throughput:.2f} msg/s")

consumer.close()
