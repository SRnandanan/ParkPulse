package com.parkpulse.generator;

import com.parkpulse.config.KafkaConfig;
import com.parkpulse.model.GateSensorEvent;
import com.parkpulse.model.ParkingBayEvent;
import com.parkpulse.model.PaymentKioskEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.UUID;
import java.util.Random;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class TelemetryLoadGenerator {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public TelemetryLoadGenerator(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Generate a synthetic gate sensor event and publish to Kafka.
     *
     * @param facilityId the facility identifier
     * @param gateId     the gate identifier
     * @return the generated event
     */
    public GateSensorEvent generateAndPublishGateEvent(String facilityId, String gateId) {
        GateSensorEvent event = new GateSensorEvent();
        event.setId(UUID.randomUUID());
        event.setEventTimestamp(Instant.now());
        event.setSourceService("gate-sensor-service");
        event.setFacilityId(facilityId);
        event.setEventType(ThreadLocalRandom.current().nextBoolean() ? "ENTRY" : "EXIT");
        event.setLicensePlate(generateRandomLicensePlate());
        event.setBayId(gateId + "-" + ThreadLocalRandom.current().nextInt(1, 20));

        kafkaTemplate.send(KafkaConfig.TOPIC_GATE_SENSOR, event.getId().toString(), event);
        return event;
    }

    /**
     * Generate a synthetic parking bay event and publish to Kafka.
     *
     * @param facilityId the facility identifier
     * @param bayId      the parking bay identifier
     * @return the generated event
     */
    public ParkingBayEvent generateAndPublishBayEvent(String facilityId, String bayId) {
        ParkingBayEvent event = new ParkingBayEvent();
        event.setId(UUID.randomUUID());
        event.setEventTimestamp(Instant.now());
        event.setSourceService("parking-bay-service");
        event.setFacilityId(facilityId);
        event.setBayId(bayId);
        event.setEventType(ThreadLocalRandom.current().nextBoolean() ? "OCCUPIED" : "VACANT");
        event.setLicensePlate(ThreadLocalRandom.current().nextBoolean() ? generateRandomLicensePlate() : null);

        kafkaTemplate.send(KafkaConfig.TOPIC_PARKING_BAY, event.getId().toString(), event);
        return event;
    }

    /**
     * Generate a synthetic payment kiosk event and publish to Kafka.
     *
     * @param facilityId the facility identifier
     * @param kioskId    the kiosk identifier
     * @return the generated event
     */
    public PaymentKioskEvent generateAndPublishPaymentEvent(String facilityId, String kioskId) {
        PaymentKioskEvent event = new PaymentKioskEvent();
        event.setId(UUID.randomUUID());
        event.setEventTimestamp(Instant.now());
        event.setSourceService("payment-kiosk-service");
        event.setFacilityId(facilityId);
        event.setKioskId(kioskId);
        event.setTransactionId(UUID.randomUUID().toString());
        event.setLicensePlate(generateRandomLicensePlate());
        event.setEventType(ThreadLocalRandom.current().nextBoolean() ? "PAYMENT" : "VALIDATION");

        kafkaTemplate.send(KafkaConfig.TOPIC_PAYMENT_KIOSK, event.getId().toString(), event);
        return event;
    }

    /**
     * Generate a burst of gate sensor events for high-volume testing.
     *
     * @param facilityId the facility identifier
     * @param gateId     the gate identifier
     * @param count      number of events to generate
     */
    public void generateGateEventBurst(String facilityId, String gateId, int count) {
        for (int i = 0; i < count; i++) {
            generateAndPublishGateEvent(facilityId, gateId);
            // Small delay to simulate realistic pacing
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Generate a burst of parking bay events for high-volume testing.
     *
     * @param facilityId the facility identifier
     * @param bayId      the parking bay identifier
     * @param count      number of events to generate
     */
    public void generateBayEventBurst(String facilityId, String bayId, int count) {
        for (int i = 0; i < count; i++) {
            generateAndPublishBayEvent(facilityId, bayId);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Generate a burst of payment kiosk events for high-volume testing.
     *
     * @param facilityId the facility identifier
     * @param kioskId    the kiosk identifier
     * @param count      number of events to generate
     */
    public void generatePaymentEventBurst(String facilityId, String kioskId, int count) {
        for (int i = 0; i < count; i++) {
            generateAndPublishPaymentEvent(facilityId, kioskId);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Generate a random license plate format.
     *
     * @return random license plate string
     */
    private String generateRandomLicensePlate() {
        String[] prefixes = {"ABC", "XYZ", "DEF", "GHI", "JKL", "MNO"};
        String[] suffixes = {"123", "456", "789", "012", "345", "678"};
        Random rand = new Random();
        return prefixes[rand.nextInt(prefixes.length)] +
                suffixes[rand.nextInt(suffixes.length)] +
                String.format("%03d", rand.nextInt(900) + 100);
    }
}