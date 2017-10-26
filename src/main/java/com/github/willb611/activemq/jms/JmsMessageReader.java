package com.github.willb611.activemq.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

@Service
public class JmsMessageReader {
  private static final int READ_TIMEOUT_MILLIS = 5000;
  private static final Logger logger = LoggerFactory.getLogger(JmsMessageReader.class);

  private JmsTemplate jmsTemplate;

  @Autowired
  public JmsMessageReader(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
    this.jmsTemplate.setReceiveTimeout(READ_TIMEOUT_MILLIS);
    logger.info("Set read timeout to: " + READ_TIMEOUT_MILLIS + " millis");
  }

  public String read(final Destination dest) throws JMSException {
    Message message = this.jmsTemplate.receive(dest);
    if (message == null) {
      throw new JMSException("No message found at destination " + dest);
    }
    TextMessage textMessage = (TextMessage) message;
    return textMessage.getText();
  }
}
