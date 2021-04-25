local key1 = KEYS[1]
local receiveArgJson = cjson.decode(ARGV[1])

local expire = receiveArgJson.expire
local value = receiveArgJson.value

local jsonResultTemp = {}
local jsonOperationTemp = {}

jsonResultTemp["key"] = tostring(key1)
jsonResultTemp["operation"] = jsonOperationTemp
jsonOperationTemp["set"] = redis.call("set", key1, value)
jsonOperationTemp["expire"] = redis.call("expire", key1, expire)
jsonOperationTemp["get"] = redis.call("get", key1)
jsonOperationTemp["ttl"] = redis.call("ttl", key1)

local result = {}
--springboot redisTemplate接收的是List,如果返回的数组内容是json对象,需要将json对象转成字符串,客户端才能接收
result[1] = cjson.encode(jsonResultTemp)
--将源参数内容一起返回
result[2] = ARGV[1]

return result