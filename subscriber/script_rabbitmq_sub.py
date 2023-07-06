import pika
import time

credentials = pika.PlainCredentials('guest', 'guest')
parameters = pika.ConnectionParameters('localhost', credentials=credentials)
connection = pika.BlockingConnection(parameters)
channel = connection.channel()

QUEUE_NAME = 'test_queue'
channel.queue_declare(queue=QUEUE_NAME)

MESSAGE_COUNT = 100000

def send_messages():
    for i in range(MESSAGE_COUNT):
        channel.basic_publish(exchange='', routing_key=QUEUE_NAME, body='Throughput test message')
    print('Messages sent')

def receive_messages():
    for i in range(MESSAGE_COUNT):
        channel.basic_get(queue=QUEUE_NAME, auto_ack=True)
    print('Messages received')

def measure_time(block):
    start_time = time.time()
    block()
    end_time = time.time()
    return end_time - start_time

receive_time = measure_time(receive_messages)
throughput = MESSAGE_COUNT / receive_time

print('Receive Time: {:.4f} seconds'.format(receive_time))
print('Throughput: {:.4f} messages/second'.format(throughput))
