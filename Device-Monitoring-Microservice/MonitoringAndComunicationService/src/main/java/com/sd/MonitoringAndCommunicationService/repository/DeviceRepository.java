package com.sd.MonitoringAndCommunicationService.repository;

import com.sd.MonitoringAndCommunicationService.entity.Device;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@Transactional
public interface DeviceRepository extends JpaRepository<Device, UUID> {
}
