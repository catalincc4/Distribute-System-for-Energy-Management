package com.DevicemManagement.DeviceManagement.dtos.builders;

import com.DevicemManagement.DeviceManagement.dtos.DeviceDTO;
import com.DevicemManagement.DeviceManagement.entity.Device;

public class DeviceBuilder {
    public static DeviceDTO convertEntityToDTO(Device device){
        return DeviceDTO
                .builder()
                .id(device.getId())
                .address(device.getAddress())
                .maxHourlyEnergyConsumption(device.getMaxHourlyEnergyConsumption())
                .description(device.getDescription())
                .userId(
                        device.get_user() == null ? "" : device.get_user().getId().toString()
                )
                .build();
    }
}
