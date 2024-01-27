import pika
import csv
import time
from datetime import datetime
import json

class DataMessage:
    def __init__(self, timestamp, device_id, measuredValue):
        self.timestamp = timestamp
        self.device_id = device_id
        self.measuredValue =measuredValue

def send_message():
    credentials = pika.PlainCredentials(
         username="gpmctssc",
        password="P68Tg-X1xXryRg7caU9_2pOwtChXfsto"
    )

# Connection parameters
    connection_params = pika.ConnectionParameters(
        host='porpoise.rmq.cloudamqp.com',  
        port=5672,
         virtual_host="gpmctssc",
         credentials=credentials
    )
# Establish a connection
    connection = pika.BlockingConnection(connection_params)

# Create a channel
    channel = connection.channel()
    channel.exchange_declare("exchange_data", durable=True, exchange_type="topic")

    queue_name = 'data_queue'
    channel.queue_declare(queue=queue_name, durable=True)

    timestamp = int(time.time())
    with open("sensor.csv", "r") as file:
        csv_reader = csv.DictReader(file)
        for row in csv_reader:
            data = DataMessage(timestamp=timestamp, device_id='a1412deb-e5f2-404d-9d7c-11a431df5d9d', measuredValue=row['0']) 
            message = json.dumps(data.__dict__)
            print(message)
            channel.basic_publish(exchange='exchange_data', routing_key='1234', body=message)
            timestamp += 10*60
            time.sleep(0.5)        

    # Close the connection
    connection.close()

if __name__ == '__main__':
    send_message()