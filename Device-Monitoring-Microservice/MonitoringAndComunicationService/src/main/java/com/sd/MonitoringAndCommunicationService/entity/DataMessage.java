package com.sd.MonitoringAndCommunicationService.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DataMessage {

    private Long timestamp;
    private String device_id;
    private Double measuredValue;

}
