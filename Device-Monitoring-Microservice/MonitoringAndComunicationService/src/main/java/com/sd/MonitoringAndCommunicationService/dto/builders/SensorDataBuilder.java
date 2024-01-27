package com.sd.MonitoringAndCommunicationService.dto.builders;

import com.sd.MonitoringAndCommunicationService.dto.SensorDataDTO;
import com.sd.MonitoringAndCommunicationService.entity.SensorData;

public class SensorDataBuilder {

    public static SensorDataDTO convertEntityToDTO(SensorData sensorData){
        return SensorDataDTO.builder()
                .device_id(sensorData.getDevice().getId().toString())
                .measuredValue(sensorData.getMeasuredValue())
                .timestamp(sensorData.getTimestamp())
                .build();
    }
}
