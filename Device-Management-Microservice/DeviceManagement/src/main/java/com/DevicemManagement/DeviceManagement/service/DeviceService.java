package com.DevicemManagement.DeviceManagement.service;

import com.DevicemManagement.DeviceManagement.dtos.DeviceDTO;
import com.DevicemManagement.DeviceManagement.dtos.builders.DeviceBuilder;
import com.DevicemManagement.DeviceManagement.entity.Device;
import com.DevicemManagement.DeviceManagement.entity.Message;
import com.DevicemManagement.DeviceManagement.entity.User;
import com.DevicemManagement.DeviceManagement.repository.DeviceRepository;
import com.DevicemManagement.DeviceManagement.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    private final UserRepository userRepository;

    private final RabbitMQDataSender sender;

    public DeviceService(DeviceRepository deviceRepository, UserRepository userRepository, RabbitMQDataSender sender) {
        this.deviceRepository = deviceRepository;
        this.userRepository = userRepository;
        this.sender = sender;
    }

    public ResponseEntity<?> addDevice(DeviceDTO deviceDTO) {
        User user = null;
        try {
            UUID id = UUID.fromString(deviceDTO.getUserId());
            user = this.userRepository.getReferenceById(id);
        }catch (IllegalArgumentException ignored){
        }
        Device device =
                Device
                .builder()
                .address(deviceDTO.getAddress())
                .description(deviceDTO.getDescription())
                .maxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption())
                ._user(user)
                .build();
        this.deviceRepository.saveAndFlush(device);
        sender.sendMessage(
                Message.builder()
                        .deviceId(device.getId().toString())
                        .operation("ADD")
                        .build()
        );
        return new ResponseEntity<>(DeviceBuilder.convertEntityToDTO(device), HttpStatus.OK);
    }

    public ResponseEntity<?> updateDevice(DeviceDTO deviceDTO){
        Optional<Device> dbDevice = this.deviceRepository.findById(deviceDTO.getId());
        User user = this.userRepository.getReferenceById(UUID.fromString(deviceDTO.getUserId()));
        if(dbDevice.isPresent()){
            Device device = dbDevice.get();
            device.setDescription(deviceDTO.getDescription());
            device.setAddress(deviceDTO.getAddress());
            device.setMaxHourlyEnergyConsumption(deviceDTO.getMaxHourlyEnergyConsumption());
            device.set_user(user);
            this.deviceRepository.save(device);
            return new ResponseEntity<>(DeviceBuilder.convertEntityToDTO(device), HttpStatus.OK);
        }
        return new ResponseEntity<>("Device not found", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> deleteDevice(UUID id){
        this.deviceRepository.deleteById(id);
        sender.sendMessage(
                Message.builder()
                        .deviceId(id.toString())
                        .operation("DELETE")
                        .build()
        );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getAllDevices(){
        return new ResponseEntity<>(this.deviceRepository.findAll().stream().map(DeviceBuilder::convertEntityToDTO).toList(), HttpStatus.OK);
    }
    @Transactional
    public ResponseEntity<?> getAllDevicesByUser(UUID id){
        Optional<User> user = this.userRepository.findById(id);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get().getDevices().stream().map(DeviceBuilder::convertEntityToDTO).toList(), HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found", HttpStatus.BAD_REQUEST);
    }

}
