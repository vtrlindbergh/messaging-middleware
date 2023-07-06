# !pip install pika


import pika
import json

class GerenciadorFilaRabbitMQ:
    def __init__(self, host, usuario, senha):
        self.connection = pika.BlockingConnection(pika.ConnectionParameters(
            host=host, credentials=pika.PlainCredentials(usuario, senha)))
        self.channel = self.connection.channel()

    def criar_fila (self, nome_fila):
        self.channel.queue_declare(queue=nome_fila)

    def criar_receptor (self, receptor, tipo):
         self.channel.exchange_declare (exchange=receptor, exchange_type=tipo)

    def vincular_receptor_fila (self, receptor, fila, chave):
         self.channel.queue_bind(exchange=receptor,queue=fila,routing_key=chave)

    def publicar_mensagemRMQ(self, receptor, nome_fila, payload):
        mensagem = json.dumps(payload)
        self.channel.basic_publish(exchange=receptor, routing_key=nome_fila, body=mensagem)

    def consumir_fila(self, nome_fila, retorno):
        self.channel.basic_consume(queue=nome_fila, on_message_callback=retorno, auto_ack=True)
        self.channel.start_consuming()

    def fechar_conexao(self):
        self.connection.close()

#Definindo as rotinas de criação e consumo das filas - RabbitMq
def retorno(ch, metodo, propriedade, mensagem):
    payload = json.loads(mensagem.decode("utf-8"))
    contexto = payload["contexto"]
    destinatario = payload["destinatario"]
    mensagem = payload["mensagem"]
    print(f"Recebido: Contexto: {contexto}, Destinatário: {destinatario}, Mensagem: {mensagem}")

# Configurações de conexão
host = 'localhost'
port = 1883
usuario = 'guest'
senha = 'guest'

# Criando uma instância do gerenciador de filas
gfRabbit = GerenciadorFilaRabbitMQ(host, usuario, senha)

# Criando uma fila
gfRabbit.criar_fila('DISTRIB')

# Criando um payload
payload = {
    "contexto": "DISTRIB",
    "destinatario": "appQWERT",
    "mensagem": "Enviando mensagem pelo RabbitMQ!"
}

# Publicando uma mensagem na fila
gfRabbit.publicar_messagem('exchange', payload)

# Consumindo a fila
gfRabbit.consumir_fila('queue', retorno)

# Fechando a conexão
gfRabbit.fechar_conexao()
