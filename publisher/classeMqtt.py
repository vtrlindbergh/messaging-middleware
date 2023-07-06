#!pip install paho.mqtt

import paho.mqtt.client as mqtt

class GerenciadorFilaMqtt:
    def __init__(self, host, port, usuario, senha):
        self.client = mqtt.Client()
        self.client.username_pw_set(usuario, senha)
        self.client.on_connect = self.on_connect
        self.client.on_message = self.on_message
        self.client.connect(host, port)

    def on_connect(self, client, userdata, flags, rc):
        print("Conectado com o código de retorno: " + str(rc))
        client.subscribe("DISTRIB")

    def on_message(self, client, userdata, msg):
        payload = json.loads(msg.payload.decode("utf-8"))
        contexto = payload["contexto"]
        destinatario = payload["destinatario"]
        mensagem = payload["mensagem"]
        print(f"Recebido: Contexto: {contexto}, Destinatário: {destinatario}, Mensagem: {mensagem}")

    def publicar_mensagemMQ(self, mensagem):
        msg = json.dumps(mensagem)
        self.client.publish("DISTRIB", msg)

    def iniciar(self):
        self.client.loop_forever()

    def parar(self):
        self.client.disconnect()


# Configurações de conexão
host = 'localhost'
port = 1883
usuario = 'guest'
senha = 'guest'


# Criando uma instância do gerenciador de filas MQTT
gfMqtt = GerenciadorFilaMqtt(host, port, usuario, senha)

# Criando um payload
payload = {
    "contexto": "DISTRIB",
    "destinatario": "appXPTO",
    "mensagem": "Enviando mensagem pelo MQTT!"
}

# Publicando uma mensagem no tópico
gfMqtt.publicar_mensagem(payload)

# Iniciando o gerenciador de filas MQTT
gfMqtt.iniciar()
