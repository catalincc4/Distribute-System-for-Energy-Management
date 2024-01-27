package com.DevicemManagement.DeviceManagement.controller;

import com.DevicemManagement.DeviceManagement.dtos.DeviceDTO;
import com.DevicemManagement.DeviceManagement.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @PostMapping(value = "/addDevice")
    public ResponseEntity<?> addDevice(@RequestBody DeviceDTO deviceDTO) {
        return this.deviceService.addDevice(deviceDTO);
    }

    @PutMapping(value = "/updateDevice")
    public ResponseEntity<?> updateDevice(@RequestBody DeviceDTO deviceDTO) {
        return this.deviceService.updateDevice(deviceDTO);
    }

    @DeleteMapping(value = "/deleteDevice/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") String id) {
        return this.deviceService.deleteDevice(UUID.fromString(id));
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllDevices(){
        return this.deviceService.getAllDevices();
    }

    @GetMapping(value = "/allByUser/{id}")
    public ResponseEntity<?> getAllDevicesByUser(@PathVariable String id){
        return this.deviceService.getAllDevicesByUser(UUID.fromString(id));
    }
}
