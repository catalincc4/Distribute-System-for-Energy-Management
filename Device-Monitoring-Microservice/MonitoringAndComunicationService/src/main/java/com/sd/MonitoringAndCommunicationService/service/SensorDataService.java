package com.sd.MonitoringAndCommunicationService.service;

import com.sd.MonitoringAndCommunicationService.dto.SensorDataDTO;
import com.sd.MonitoringAndCommunicationService.dto.builders.SensorDataBuilder;
import com.sd.MonitoringAndCommunicationService.entity.Device;
import com.sd.MonitoringAndCommunicationService.entity.SensorData;
import com.sd.MonitoringAndCommunicationService.repository.DeviceRepository;
import com.sd.MonitoringAndCommunicationService.repository.SensorDataRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class SensorDataService {

    private final SensorDataRepository sensorDataRepository;

    private final DeviceRepository deviceRepository;

    public SensorDataService(SensorDataRepository sensorDataRepository, DeviceRepository deviceRepository) {
        this.sensorDataRepository = sensorDataRepository;
        this.deviceRepository = deviceRepository;
    }

    public void addData(SensorDataDTO data){
        Optional<Device> device = deviceRepository.findById(UUID.fromString(data.getDevice_id()));

        if(device.isPresent()){
            SensorData sensorData =
                    SensorData.builder()
                            .timestamp(data.getTimestamp())
                            .measuredValue(data.getMeasuredValue())
                            .device(device.get())
                            .build();
            sensorDataRepository.save(sensorData);
        }
    }

    @Transactional
    public ResponseEntity<?> getDataForDevice(String deviceId, Long timestamp){
        Optional<Device> device = deviceRepository.findById(UUID.fromString(deviceId));

        if(device.isPresent()){
            List<SensorData> sensorData = sensorDataRepository.findSensorDataByDeviceAndTimestampBetween(device.get(),timestamp, timestamp+(24L* 3600L));
            return new ResponseEntity<>(sensorData.stream().map(SensorDataBuilder::convertEntityToDTO).toList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }
}
