package com.parkpulse.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedisOccupancyService {

    private final StringRedisTemplate redisTemplate;

    public RedisOccupancyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Increment the occupancy counter for a parking bay.
     * Uses Redis HINCRBY for atomic increment.
     *
     * @param facilityId the facility identifier
     * @param bayId      the parking bay identifier
     * @return the new occupancy count
     */
    @Transactional
    public long incrementOccupancy(String facilityId, String bayId) {
        String key = "parkpulse:bay:occupancy:" + facilityId + ":" + bayId;
        return redisTemplate.opsForHash().increment(key, "count", 1);
    }

    /**
     * Decrement the occupancy counter for a parking bay.
     * Uses Redis HINCRBY for atomic decrement.
     *
     * @param facilityId the facility identifier
     * @param bayId      the parking bay identifier
     * @return the new occupancy count
     */
    @Transactional
    public long decrementOccupancy(String facilityId, String bayId) {
        String key = "parkpulse:bay:occupancy:" + facilityId + ":" + bayId;
        long newCount = redisTemplate.opsForHash().increment(key, "count", -1);
        // Ensure count doesn't go below 0
        if (newCount < 0) {
            redisTemplate.opsForHash().increment(key, "count", 1); // revert
            return 0;
        }
        return newCount;
    }

    /**
     * Get the current occupancy count for a parking bay.
     *
     * @param facilityId the facility identifier
     * @param bayId      the parking bay identifier
     * @return the current occupancy count
     */
    public long getOccupancy(String facilityId, String bayId) {
        String key = "parkpulse:bay:occupancy:" + facilityId + ":" + bayId;
        Object count = redisTemplate.opsForHash().get(key, "count");
        return count != null ? Long.parseLong(count.toString()) : 0L;
    }

    /**
     * Set the occupancy count directly (useful for initialization or sync).
     *
     * @param facilityId the facility identifier
     * @param bayId      the parking bay identifier
     * @param count      the occupancy count to set
     */
    @Transactional
    public void setOccupancy(String facilityId, String bayId, long count) {
        String key = "parkpulse:bay:occupancy:" + facilityId + ":" + bayId;
        redisTemplate.opsForHash().put(key, "count", count);
    }

    /**
     * Check if a parking bay is currently occupied.
     *
     * @param facilityId the facility identifier
     * @param bayId      the parking bay identifier
     * @return true if occupied (count > 0), false if vacant
     */
    public boolean isOccupied(String facilityId, String bayId) {
        return getOccupancy(facilityId, bayId) > 0;
    }
}