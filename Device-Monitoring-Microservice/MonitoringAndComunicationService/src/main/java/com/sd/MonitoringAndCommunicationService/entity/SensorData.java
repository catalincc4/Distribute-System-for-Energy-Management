package com.sd.MonitoringAndCommunicationService.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "SensorData")
public class SensorData {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "measured_value")
    private Double measuredValue;

    @ManyToOne
    @JoinColumn(name = "device", nullable = false)
    private Device device;

}
