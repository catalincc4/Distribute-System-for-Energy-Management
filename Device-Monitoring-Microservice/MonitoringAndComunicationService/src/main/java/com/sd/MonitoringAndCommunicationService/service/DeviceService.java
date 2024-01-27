package com.sd.MonitoringAndCommunicationService.service;

import com.sd.MonitoringAndCommunicationService.entity.Device;
import com.sd.MonitoringAndCommunicationService.repository.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public void addDevice(String id){
        var device = Device.builder().id(UUID.fromString(id)).build();
        deviceRepository.save(device);
    }
    public void deleteDevice(String id){
        var device = Device.builder().id(UUID.fromString(id)).build();
        deviceRepository.delete(device);
    }
}
