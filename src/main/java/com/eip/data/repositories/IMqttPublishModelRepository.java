package com.eip.data.repositories;

import com.eip.data.entity.CanMqttMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMqttPublishModelRepository extends JpaRepository<CanMqttMessage, Long> {
}
