package com.sd.MonitoringAndCommunicationService.controller;

import com.sd.MonitoringAndCommunicationService.service.SensorDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin
@RestController
@RequestMapping("/api/data")
public class SensorDataController {

    private final SensorDataService sensorDataService;

    public SensorDataController(SensorDataService sensorDataService) {
        this.sensorDataService = sensorDataService;
    }

    @GetMapping("/{deviceId}/{timestamp}")
    public ResponseEntity<?> getDataForDevice(@PathVariable("deviceId") String deviceId, @PathVariable("timestamp") Long timestamp){
        return this.sensorDataService.getDataForDevice(deviceId, timestamp/1000);
    }

}
