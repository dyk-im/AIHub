package com.dyks.aihubbe.global.auth.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisService {
	private static final long AUTH_CODE_EXPIRE_SECONDS = 300;

	private final StringRedisTemplate redisTemplate;//Redis에 접근하기 위한 Spring의 Redis 템플릿 클래스

	public String getData(String key) {//지정된 키(key)에 해당하는 데이터를 Redis에서 가져오는 메서드
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return valueOperations.get(key);
	}

	public void setData(String key, String value) {//지정된 키(key)에 값을 저장하는 메서드
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(key, value);
	}

	public void setDataExpire(String key, String value) {//지정된 키(key)에 값을 저장하고, 지정된 시간(duration) 후에 데이터가 만료되도록 설정하는 메서드
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		Duration expireDuration = Duration.ofSeconds(AUTH_CODE_EXPIRE_SECONDS);
		valueOperations.set(key, value, expireDuration);
	}

	public void deleteData(String key) {//지정된 키(key)에 해당하는 데이터를 Redis에서 삭제하는 메서드
		redisTemplate.delete(key);
	}
	/**
	 * Refresh Token 저장
	 */
	public void setRefreshToken(String email, String refreshToken, Long refreshTokenExpirationPeriod) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set(email, refreshToken, Duration.ofMillis(refreshTokenExpirationPeriod)); // 만료 시간 설정
	}

	/**
	 * Refresh Token 가져오기
	 */
	public String getRefreshToken(String email) {
		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
		return valueOperations.get(email);
	}

	/**
	 * Refresh Token 삭제
	 */
	public void deleteRefreshToken(String email) {
		redisTemplate.delete(email);
	}
}