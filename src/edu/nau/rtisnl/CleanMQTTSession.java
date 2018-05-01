package edu.nau.rtisnl;

import java.util.Date;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;

public class CleanMQTTSession {

	public static void main(String[] args) throws MqttSecurityException, MqttException, InterruptedException {
		if(args.length != 3){
			System.out.println("Must include three arguments: garden common name, this server's hostname, and the remote ip address");
			System.out.println("For example, to remove a subscription on exp249a-12 from romer2, execute this program using: ");
			System.out.println("java CleanMQTTSession exp249a-12 romer2 exp249a-12.egr.nau.edu");
			System.exit(0);
		}
	String common_name = args[0];
	String host_name = args[1];
	String remote_ip_address = args[2];
	
	String mqtt_port = "1883";
	
	
	String subTopic = common_name + "/data/#";
	String qos = "2";
	String broker = "tcp://" + remote_ip_address + ":" + mqtt_port;
	String subID = common_name + "/" + host_name + "/rdf_subscriber";
	
		System.out.println("\r\n===== MQTT Params " + new Date(System.currentTimeMillis()) + " =====\r\n"
				+  "[Remote IP]: " + remote_ip_address + "\r\n"
				+  "[Common Name]: " + common_name + "\r\n"
				+  "[MQTT Port]: " + mqtt_port + " seconds\r\n"
				+  "[Subscriber Topic]: " + subTopic + "\r\n"
				+  "[Quality of Service (QoS)]: " + qos + "\r\n"
				+  "[MQTT Broker]: " + broker + "\r\n"
				+  "[Subscriber ID]: " + subID + "\r\n"
				);



	


	MqttDefaultFilePersistence subPersistence = new MqttDefaultFilePersistence(".");

	//Init the MQTT subscriber
	MqttClient subClient = new MqttClient(broker, subID, subPersistence);

	//Set the MQTT connection options
	MqttConnectOptions connOpts = new MqttConnectOptions();
	connOpts.setCleanSession(true); //set to false to maintain session in client and broker
	
	System.out.println("Connecting subscriber to broker " + broker + " as client " + subID + "...");
	
	subClient.connect(connOpts);
	
	System.out.println("\tOK - subscriber connected to " + broker);
	
	//Make sure we didn't hit the timeout
	if(!subClient.isConnected()){
		System.out.println("\tERROR - " + subID + " not subscribed to " + subTopic);
		//Force disconnect to clean up
		subClient.disconnect();			
		return;
	}	
	System.out.println("\tOK - " + subID + " subscribed to " + subTopic);
	Thread.sleep(1000);
	subClient.disconnect();
	System.out.println("\tDisconnected. Exiting...");

	}

}
