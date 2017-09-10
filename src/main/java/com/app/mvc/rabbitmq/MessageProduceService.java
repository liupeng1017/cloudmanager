package com.app.mvc.rabbitmq;

import com.app.mvc.config.GlobalConfig;
import com.app.mvc.config.GlobalConfigKey;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by jimin on 16/3/11.
 */
@Service("messageProduceService")
public class MessageProduceService {

    //    @Autowired
    private AmqpTemplate template;

    public void pushToMessageQueue(String routingKey, String message) {
        template.convertAndSend(routingKey, message);
    }

    public void pushToMessageQueue(String message) {
        pushToMessageQueue(GlobalConfig.getStringValue(GlobalConfigKey.RABBITMQ_DEFAULT_QUEUE_NAME, "testQ"), message);
    }
}
