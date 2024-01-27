package com.sd.MonitoringAndCommunicationService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeviceMessage {
    private String deviceId;
    private String operation;
}
