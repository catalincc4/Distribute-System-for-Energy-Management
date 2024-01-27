package com.sd.MonitoringAndCommunicationService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensorDataDTO {
    private Long timestamp;
    private Double measuredValue;
    private String device_id;
}
