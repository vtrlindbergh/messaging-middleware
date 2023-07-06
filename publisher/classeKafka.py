#!pip install kafka

from kafka import KafkaProducer, KafkaConsumer
import json

class GerenciadorFilaKafka:
    def __init__(self, bootstrap_servers):
        self.producer = KafkaProducer(bootstrap_servers=bootstrap_servers, value_serializer=lambda v: json.dumps(v).encode('utf-8'))
        self.consumer = KafkaConsumer(bootstrap_servers=bootstrap_servers, value_deserializer=lambda v: json.loads(v.decode('utf-8')))

    def criar_topico(self, nome_topico):
        # Nenhuma ação necessária para criar um tópico no Kafka
        print (f"Tópico {nome_topico} criado.")

    def publicar_mensagemKFK(self, nome_topico, mensagem):
        self.producer.send(nome_topico, value=mensagem)

    def consumir_topico(self, nome_topico, retorno):
        self.consumer.subscribe([nome_topico])
        for mensagem in self.consumer:
            payload = mensagem.value
            retorno(payload)

    def fechar_conexao(self):
        self.producer.close()
        self.consumer.close()
        

#Definindo as rotinas de criação e consumo das filas - Kafka        
def retorno(payload):
    contexto = payload["contexto"]
    destinatario = payload["destinatario"]
    mensagem = payload["mensagem"]
    print(f"Recebido: Contexto: {contexto}, Destinatário: {destinatario}, Mensagem: {mensagem}")

# Configurações de conexão
bootstrap_servers = 'localhost:9092'

# Criando uma instância do gerenciador de filas Kafka
gfKafka = GerenciadorFilaKafka(bootstrap_servers)

# Criando um tópico
gfKafka.criar_topico('DISTRIB')

# Criando um payload
payload = {
    "contexto": "DISTRIB",
    "destinatario": "appASDFG",
    "mensagem": "Enviando mensgem pelo Kafka!"
}

# Publicando uma mensagem no tópico
gfKafka.publicar_mensagem('DISTRIB', payload)

# Consumindo o tópico
gfKafka.consumir_topico('DISTRIB', retorno)

# Fechando a conexão
gfKafka.fechar_conexao()
       
