package com.DevicemManagement.DeviceManagement.dtos.builders;

import com.DevicemManagement.DeviceManagement.dtos.UserDTO;
import com.DevicemManagement.DeviceManagement.entity.User;

public class UserBuilder {
    public static UserDTO convertEntityToDTO(User user){
        return UserDTO
                .builder()
                .id(user.getId())
                .devices(user.getDevices().stream().map(DeviceBuilder::convertEntityToDTO).toList())
                .build();
    }
}
