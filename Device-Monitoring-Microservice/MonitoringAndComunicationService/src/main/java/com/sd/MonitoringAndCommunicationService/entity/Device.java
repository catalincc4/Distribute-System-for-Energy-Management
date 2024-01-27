package com.sd.MonitoringAndCommunicationService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "device")
public class Device {
    @Id
    private UUID id;

    @OneToMany(mappedBy = "device", cascade = CascadeType.ALL)
    private List<SensorData> sensorDataList;
}
