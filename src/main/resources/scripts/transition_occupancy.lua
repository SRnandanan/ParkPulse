local current = tonumber(redis.call('HGET', KEYS[1], 'count') or '0')
if current ~= tonumber(ARGV[1]) then
    return 0
end
redis.call('HSET', KEYS[1], 'count', ARGV[2])
return 1
