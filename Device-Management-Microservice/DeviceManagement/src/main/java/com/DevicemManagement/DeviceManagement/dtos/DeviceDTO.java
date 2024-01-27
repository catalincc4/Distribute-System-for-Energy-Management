package com.DevicemManagement.DeviceManagement.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeviceDTO {
    private UUID id;
    private  String description;
    private String address;
    private Double maxHourlyEnergyConsumption;
    private String userId;
}
