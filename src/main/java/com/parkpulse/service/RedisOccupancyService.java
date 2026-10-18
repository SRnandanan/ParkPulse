package com.parkpulse.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class RedisOccupancyService {

    private static final DefaultRedisScript<Long> TRANSITION_OCCUPANCY_SCRIPT =
        loadTransitionOccupancyScript();

    private final StringRedisTemplate redisTemplate;

    public RedisOccupancyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static DefaultRedisScript<Long> loadTransitionOccupancyScript() {
        DefaultRedisScript<Long> script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("scripts/transition_occupancy.lua"));
        script.setResultType(Long.class);
        return script;
    }

    /**
     * Apply an occupancy transition only when the bay is in the expected state.
     *
     * @param facilityId the facility identifier
     * @param bayId      the parking bay identifier
     * @param occupied   true to transition from vacant to occupied, false to transition to vacant
     * @return true if the transition was applied
     */
    public boolean transitionOccupancy(String facilityId, String bayId, boolean occupied) {
        String key = "parkpulse:bay:occupancy:" + facilityId + ":" + bayId;
        String expectedCount = occupied ? "0" : "1";
        String newCount = occupied ? "1" : "0";
        Long transitioned = redisTemplate.execute(
                TRANSITION_OCCUPANCY_SCRIPT,
                Collections.singletonList(key),
                expectedCount,
                newCount);
        return Long.valueOf(1).equals(transitioned);
    }
}