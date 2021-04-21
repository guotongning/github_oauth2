local key1 = KEYS[1]
local receive_arg_json = cjson.decode(ARGV[1])

local result = {}

redis.log(redis.LOG_DEBUG, key1)
redis.log(redis.LOG_DEBUG, ARGV[1], #ARGV[1])

local expire = receive_arg_json.expire
local value = receive_arg_json.value

redis.log(redis.LOG_DEBUG, tostring(expire))
redis.log(redis.LOG_DEBUG, tostring(value))

local jsonResultTemp = {}

jsonResultTemp["key"] = tostring(key1)
jsonResultTemp["set"] = redis.call("set", key1, value)
jsonResultTemp["expire"] = redis.call("expire", key1, expire)
jsonResultTemp["get"] = redis.call("get", key1)
jsonResultTemp["ttl"] = redis.call("ttl", key1)
redis.log(redis.LOG_DEBUG, cjson.encode(jsonResultTemp))

--springboot redisTemplate接收的是List,如果返回的数组内容是json对象,需要将json对象转成字符串,客户端才能接收
result[1] = cjson.encode(jsonResultTemp)
--将源参数内容一起返回
result[2] = ARGV[1]
--打印返回的数组结果，这里返回需要以字符返回
redis.log(redis.LOG_DEBUG, cjson.encode(result))

return result