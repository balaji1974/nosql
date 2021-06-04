package com.bala.nosql.redis.springbootsample.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
	// This uses the default configuration, if not needed then use the custom configuration which is commented below
	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
	    return new JedisConnectionFactory();
	}
	
	// Not needed if we use default connection properties
	/*
	 * @Bean JedisConnectionFactory jedisConnectionFactory() {
	 * JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
	 * jedisConFactory.setHostName("localhost"); jedisConFactory.setPort(6379);
	 * return jedisConFactory; }
	 */

	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
	    RedisTemplate<String, Object> template = new RedisTemplate<>();
	    template.setConnectionFactory(jedisConnectionFactory());
	    return template;
	}
	
	
}
