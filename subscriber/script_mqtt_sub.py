import paho.mqtt.client as mqtt
import time

broker_ip = "localhost"
broker_port = 1883
topic = "test-topic"
num_msgs = 10000

start_time = time.time()
num_msgs_received = 0

client = mqtt.Client()
client.connect(broker_ip, broker_port)

def on_connect(client, userdata, flags, rc):
    print("Connected to broker: ", broker_ip)
    client.subscribe(topic)

def on_message(client, userdata, msg):
    global num_msgs_received
    num_msgs_received += 1
    if num_msgs_received % 1000 == 0:
        print(f"{num_msgs_received} messages received")
    if num_msgs_received == num_msgs:
        client.disconnect()

client.on_connect = on_connect
client.on_message = on_message

client.loop_forever()

end_time = time.time()
total_time = end_time - start_time
throughput = num_msgs_received / total_time

print(f"{num_msgs_received} messages received in {total_time:.2f} seconds")
print(f"Throughput: {throughput:.2f} messages per second")
