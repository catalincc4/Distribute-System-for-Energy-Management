package com.sd.MonitoringAndCommunicationService.service;

import com.sd.MonitoringAndCommunicationService.dto.SensorDataDTO;
import com.sd.MonitoringAndCommunicationService.entity.DataMessage;
import com.sd.MonitoringAndCommunicationService.entity.DeviceMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@EnableScheduling
public class ConsumerData {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerData.class);

    private final DeviceService deviceService;

    private final SensorDataService sensorDataService;

    private static  Map<String, List<DataMessage>> values;


    private final SimpMessagingTemplate messagingTemplate;

    public ConsumerData(DeviceService deviceService, SensorDataService sensorDataService, SimpMessagingTemplate messagingTemplate) {
        this.deviceService = deviceService;
        this.sensorDataService = sensorDataService;
        this.messagingTemplate = messagingTemplate;
        values = new HashMap<>();
    }

    @RabbitListener(queues = {"${rabbitmq.queue-data.name}"})
    public void consumeData(DataMessage message) {
        LOGGER.info("New data received\n" + message.toString());
        if(values.containsKey(message.getDevice_id())){
            values.get(message.getDevice_id()).add(message);
        }else{
            List<DataMessage> messages = new ArrayList<>();
            messages.add(message);
            values.put(message.getDevice_id(), messages);
        }
        this.messagingTemplate.convertAndSend("/topic/ws/data/" + message.getDevice_id().toLowerCase(), message);
    }

    @Scheduled(fixedRate = 3000)
    public void computeAverageConsumption(){
        for (String key: values.keySet()) {
            List<DataMessage> messages = values.get(key);


            Date date = new Date(messages.get(0).getTimestamp()*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("mm"); // Use "mm" for minutes

            String minutes = sdf.format(date);
            System.out.println("Minutes: " + minutes);
            long modifiedTimestamp = messages.get(0).getTimestamp() - (60L * Integer.parseInt(minutes));

            Double averageConsumption = messages.get(messages.size() - 1).getMeasuredValue() - messages.get(0).getMeasuredValue();

            this.messagingTemplate.convertAndSend("/topic/ws/notification/" + key.toLowerCase(), averageConsumption);

            SensorDataDTO data =
                    SensorDataDTO.builder()
                            .device_id(key)
                            .measuredValue(averageConsumption)
                            .timestamp(modifiedTimestamp)
                            .build();
            sensorDataService.addData(data);
        }
        values = new HashMap<>();
    }


    @RabbitListener(queues = {"${rabbitmq.queue-device.name}"})
    public void consume(DeviceMessage message) {
        if (message.getOperation().equals("ADD")) {
            LOGGER.info("device added");
            deviceService.addDevice(message.getDeviceId());
        }

        if (message.getOperation().equals("DELETE")) {
            LOGGER.info("device deleted");
            deviceService.deleteDevice(message.getDeviceId());
        }
    }


}
