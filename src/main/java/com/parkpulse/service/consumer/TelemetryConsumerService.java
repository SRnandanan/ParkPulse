package com.parkpulse.service.consumer;

import com.parkpulse.model.GateSensorEvent;
import com.parkpulse.model.ParkingBayEvent;
import com.parkpulse.model.PaymentKioskEvent;
import com.parkpulse.repository.TelemetryEventRepository;
import com.parkpulse.service.RedisOccupancyService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class TelemetryConsumerService {

    private final TelemetryEventRepository telemetryEventRepository;
    private final RedisOccupancyService redisOccupancyService;

    public TelemetryConsumerService(
            TelemetryEventRepository telemetryEventRepository,
            RedisOccupancyService redisOccupancyService) {
        this.telemetryEventRepository = telemetryEventRepository;
        this.redisOccupancyService = redisOccupancyService;
    }

    // Listen to gate sensor events topic
    @KafkaListener(
            topics = com.parkpulse.config.KafkaConfig.TOPIC_GATE_SENSOR,
            groupId = "parkpulse-telemetry-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleGateSensorEvent(@Payload GateSensorEvent event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }
        if (event.getEventTimestamp() == null) {
            event.setEventTimestamp(Instant.now());
        }
        if (event.getSourceService() == null) {
            event.setSourceService("gate-sensor-service");
        }

        telemetryEventRepository.save(event.getId(), event.getEventType(), event.getSourceService(),
            event.getFacilityId(), event.getEventTimestamp(), event);

        String bayId = event.getBayId();
        if (bayId != null && !bayId.isBlank() && event.getLicensePlate() != null) {
            if ("ENTRY".equalsIgnoreCase(event.getEventType())) {
                redisOccupancyService.transitionOccupancy(event.getFacilityId(), bayId, true);
            } else if ("EXIT".equalsIgnoreCase(event.getEventType())) {
                redisOccupancyService.transitionOccupancy(event.getFacilityId(), bayId, false);
            }
        }
    }

    // Listen to parking bay events topic
    @KafkaListener(
            topics = com.parkpulse.config.KafkaConfig.TOPIC_PARKING_BAY,
            groupId = "parkpulse-telemetry-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handleParkingBayEvent(@Payload ParkingBayEvent event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }
        if (event.getEventTimestamp() == null) {
            event.setEventTimestamp(Instant.now());
        }
        if (event.getSourceService() == null) {
            event.setSourceService("parking-bay-service");
        }

        telemetryEventRepository.save(event.getId(), event.getEventType(), event.getSourceService(),
            event.getFacilityId(), event.getEventTimestamp(), event);

        if ("OCCUPIED".equalsIgnoreCase(event.getEventType()) && event.getLicensePlate() != null) {
            redisOccupancyService.transitionOccupancy(event.getFacilityId(), event.getBayId(), true);
        } else if ("VACANT".equalsIgnoreCase(event.getEventType())) {
            redisOccupancyService.transitionOccupancy(event.getFacilityId(), event.getBayId(), false);
        }
    }

    // Listen to payment kiosk events topic
    @KafkaListener(
            topics = com.parkpulse.config.KafkaConfig.TOPIC_PAYMENT_KIOSK,
            groupId = "parkpulse-telemetry-group",
            containerFactory = "kafkaListenerContainerFactory")
    public void handlePaymentKioskEvent(@Payload PaymentKioskEvent event) {
        if (event.getId() == null) {
            event.setId(UUID.randomUUID());
        }
        if (event.getEventTimestamp() == null) {
            event.setEventTimestamp(Instant.now());
        }
        if (event.getSourceService() == null) {
            event.setSourceService("payment-kiosk-service");
        }

        telemetryEventRepository.save(event.getId(), event.getEventType(), event.getSourceService(),
            event.getFacilityId(), event.getEventTimestamp(), event);

        if (event.getLicensePlate() != null && event.getTransactionId() != null) {
            // payment and validation events are persisted and can be used for downstream session updates
        }
    }
}