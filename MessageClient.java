
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.jms.MessageConsumer;
import java.util.Scanner;


public class MessageClient {
    private static final String ACTIVEMQ_URL="tcp://47.94.242.99:61616";
    private static final String PRODUCER_QUEUE_NAME = "client";
    private static final String CONSUMER_QUEUE_NAME="blinkg";
    public static void main(String[] args) throws JMSException {
        ActiveMQConnectionFactory factory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        Connection connection=factory.createConnection();
        connection.start();
        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destinationP=session.createQueue(PRODUCER_QUEUE_NAME);
        Destination destinationC=session.createQueue(CONSUMER_QUEUE_NAME);
        javax.jms.MessageProducer producer=session.createProducer(destinationP);
        MessageConsumer consumer=session.createConsumer(destinationC);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                TextMessage message1=(TextMessage) message;
                try {
                    System.out.println("收到消息"+message1.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        Scanner sc=new Scanner(System.in);
        for (int i = 0; i < 100; i++) {
            TextMessage message=session.createTextMessage(sc.next());
            producer.send(message);
        }

        connection.close();
    }
}
