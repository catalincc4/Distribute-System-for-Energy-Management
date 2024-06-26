package com.sd.MonitoringAndCommunicationService.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue-data.name}")
    private String dataQueue;

    @Value("${rabbitmq.queue-device.name}")
    private String deviceQueue;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routingKeyData}")
    private String routingKeyData;

    @Value("${rabbitmq.routingKeyDevice}")
    private String routingKeyDevice;

    @Bean
    public Queue dataQueue() {
        return new Queue(dataQueue);
    }

    @Bean
    public Queue deviceQueue() {
        return new Queue(deviceQueue);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding bindingData() {
        return BindingBuilder.bind(dataQueue())
                .to(exchange())
                .with(routingKeyData);
    }

    @Bean
    public Binding bindingDevice() {
        return BindingBuilder.bind(deviceQueue())
                .to(exchange())
                .with(routingKeyDevice);
    }

    @Bean
    public MessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }


}
