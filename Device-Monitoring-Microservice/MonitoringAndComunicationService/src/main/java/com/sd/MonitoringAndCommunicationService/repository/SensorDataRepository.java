package com.sd.MonitoringAndCommunicationService.repository;

import com.sd.MonitoringAndCommunicationService.entity.Device;
import com.sd.MonitoringAndCommunicationService.entity.SensorData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {
    List<SensorData> findSensorDataByDeviceAndTimestampBetween(Device device, Long timestamp1, Long timestamp2);
}
