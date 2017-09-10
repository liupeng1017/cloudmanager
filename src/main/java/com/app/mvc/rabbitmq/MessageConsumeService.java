package com.app.mvc.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

/**
 * Created by jimin on 16/3/11.
 */
@Slf4j
public class MessageConsumeService implements MessageListener {

    @Override
    public void onMessage(Message message) {
        log.info(new String(message.getBody()));
    }
}
