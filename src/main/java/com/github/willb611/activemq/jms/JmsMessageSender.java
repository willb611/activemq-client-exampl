package com.github.willb611.activemq.jms;

import javax.jms.Destination;
import javax.jms.Message;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class JmsMessageSender {
  public static final String DEFAULT_DESTINATION = "DEFAULT.DEST";

  private JmsTemplate jmsTemplate;

  @Autowired
  public JmsMessageSender(JmsTemplate jmsTemplate) {
    this.jmsTemplate = jmsTemplate;
  }


  public void sendToDefaultDestination(final String text) {
    this.jmsTemplate.send(session -> {
      Message message = session.createTextMessage(text);
      //set ReplyTo header of Message, like a return address
      message.setJMSReplyTo(new ActiveMQQueue(DEFAULT_DESTINATION));
      return message;
    });
  }

  public void sendToDefaultDestinationWithNoReplyHeader(final String text) {
    this.jmsTemplate.convertAndSend(text);
  }

  public void sendTextToDestination(final String text, final Destination dest) {
    this.jmsTemplate.send(dest, session -> session.createTextMessage(text));
  }
}