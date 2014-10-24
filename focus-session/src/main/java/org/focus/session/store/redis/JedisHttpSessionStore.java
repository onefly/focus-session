package org.focus.session.store.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.focus.session.conversion.Conversion;
import org.focus.session.exception.FocusException;
import org.focus.session.exception.FocusSesssionInitException;
import org.focus.session.store.HttpSessionStore;
import org.focus.session.util.Config;
import org.focus.session.util.ObjectUtils;
import org.focus.session.util.SPIHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisHttpSessionStore implements HttpSessionStore {
	
	private final Conversion conversion = SPIHolder.getService(Conversion.class);
	private final static JedisPool pool;
	private final static Logger LOGGER = LoggerFactory.getLogger(JedisHttpSessionStore.class);

	private int timeout = Config.getInt("sessionTimeOut") * 60;

	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(100);
		config.setMaxWaitMillis(1000l);
		config.setMaxTotal(500);
		pool = new JedisPool(config, Config.getString("redisIp"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.focus.session.store.HttpSessionStore#get(java.lang.String)
	 */
	@Override
	public Map<String, Object> get(String sessionId)
			throws FocusSesssionInitException {
		Jedis jedis = null;
		Map<String, Object> value = null;
		boolean borrowOrOprSuccess = true;
		try {
			jedis = pool.getResource();
			final byte[] key = conversion.conversion(sessionId);
			value = conversion.unconversion(jedis.get(key));
			if (!ObjectUtils.notNull(value)) {
				value = new ConcurrentHashMap<>();
				jedis.set(key, conversion.conversion(value));
			}
			jedis.expire(key, timeout);
		} catch (JedisConnectionException e) {
			borrowOrOprSuccess = false;
			LOGGER.error("jedis error {}",e);
			if (jedis != null)
				pool.returnBrokenResource(jedis);
		} finally {
			if (borrowOrOprSuccess)
				pool.returnResource(jedis);
		}
		return value;
	}

	public void add(String sessionId, Map<String, Object> session)
			throws FocusException {
		Jedis jedis = null;
		boolean borrowOrOprSuccess = true;
		try {
			jedis = pool.getResource();
			jedis.set(conversion.conversion(sessionId),
					conversion.conversion(session));
		} catch (JedisConnectionException e) {
			borrowOrOprSuccess = false;
			LOGGER.error("jedis error {}",e);
			if (jedis != null)
				pool.returnBrokenResource(jedis);
		} finally {
			if (borrowOrOprSuccess)
				pool.returnResource(jedis);
		}

	}

	@Override
	public Map<String, Object> replace(String sessionId,
			Map<String, Object> newSession) throws FocusException {
		Jedis jedis = null;
		Map<String, Object> value = null;
		boolean borrowOrOprSuccess = true;
		try {
			jedis = pool.getResource();
			final byte[] key = conversion.conversion(sessionId);
			jedis.del(key);
			jedis.set(key, conversion.conversion(newSession));
			jedis.expire(key, timeout);
		} catch (JedisConnectionException e) {
			borrowOrOprSuccess = false;
			
			LOGGER.error("jedis error {}",e);
			if (jedis != null)
				pool.returnBrokenResource(jedis);
		} finally {
			if (borrowOrOprSuccess)
				pool.returnResource(jedis);
		}
		return value;
	}

	@Override
	public boolean delete(String sessionId) throws FocusException {
		Jedis jedis = null;
		long value = 0;
		boolean borrowOrOprSuccess = true;
		try {
			jedis = pool.getResource();
			value = jedis.del(conversion.conversion(sessionId));
		} catch (JedisConnectionException e) {
			borrowOrOprSuccess = false;
			LOGGER.error("jedis error {}",e);
			if (jedis != null)
				pool.returnBrokenResource(jedis);
		} finally {
			if (borrowOrOprSuccess)
				pool.returnResource(jedis);
		}
		return value > 0;
	}

	@Override
	public boolean shutdown() {
		try {
			pool.getResource().flushAll();
			pool.destroy();
		} catch (JedisConnectionException e) {
			LOGGER.error("jedis error {}",e);
		}
		return true;
	}

}
