package com.github.willb611.activemq;

import com.github.willb611.activemq.jms.JmsMessageReader;
import com.github.willb611.activemq.jms.JmsMessageSender;
import org.apache.activemq.command.ActiveMQQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.jms.JMSException;
import javax.jms.Queue;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String TEST_QUEUE_NAME = "FOO.BAR";
    private static final String APP_CONTEXT_FILENAME = "app-context.xml";

    private final ApplicationContext applicationContext;
    private final int MESSAGE_COUNT = 2500;

    public static void main(String[] args) {
      // init spring context
      ApplicationContext context = new ClassPathXmlApplicationContext(APP_CONTEXT_FILENAME);
      new Main(context).readAndOrWriteMessages();
      // close spring application context
      ((ClassPathXmlApplicationContext) context).close();
    }

    public Main(ApplicationContext applicationContext) {
      this.applicationContext = applicationContext;
    }

    public void readAndOrWriteMessages() {
      //sendMessages();
      readMessages();
    }

    private void sendMessages() {
      JmsMessageSender jmsMessageSender = (JmsMessageSender)applicationContext.getBean("jmsMessageSender");

      // sendTextToDestination to a code specified destination
      Queue queue = new ActiveMQQueue(TEST_QUEUE_NAME);
      for (int i = 0; i < MESSAGE_COUNT; i++) {
        jmsMessageSender.sendTextToDestination(i + ": hello Another Message", queue);
        logger.info("Sent message " + i);
      }
    }

  public void readMessages() {
    try {
      // get bean from context
      JmsMessageReader jmsMessageReader = (JmsMessageReader) applicationContext.getBean("jmsMessageReader");

      ActiveMQQueue queue = new ActiveMQQueue(TEST_QUEUE_NAME);
      for (int i = 0; i < MESSAGE_COUNT; i++) {
        String message = jmsMessageReader.read(queue);
        logger.info("[readMessages] Read: " + message);
      }
    } catch (JMSException e) {
      logger.error("[readMessages] Error: " + e.getErrorCode(), e);
    }
  }
}
