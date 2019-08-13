package cn.tongdun.web.security.example;

import cn.fraudmetrix.cache.redis.RedisClient;
import cn.fraudmetrix.cache.redis.jedis.JedisFactory;
import cn.fraudmetrix.cache.redis.jedis.JedisResourcePool;
import cn.tongdun.web.security.example.redis.JedisConnectionFactoryProxy;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.SessionRepository;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;

import javax.cache.configuration.Factory;
import java.net.URI;
import java.util.Properties;

/**
 * Created by binsong.li on 2019/03/18.
 */
@Configuration
public class RedisConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisConfig.class);

    @Autowired
    private Environment environment;

    @Value("${redis.uri}")
    private String redisUri;

    @Bean
    public JedisResourcePool createPool() throws Exception {
        URI uri = new URI(redisUri);
        Factory<JedisResourcePool> factory = JedisFactory.createJedisFactory(uri, new Properties());

        JedisResourcePool pool = factory.create();
        return pool;
    }

    @Bean
    public RedisClient redisClient() throws Exception {
        JedisResourcePool pool = createPool();
        String profile = getProfile();
        RedisClient client = new RedisClient("datacompute-" + profile, pool::getResource, redis.clients.jedis.Jedis::close);
        return client;
    }

    private String getProfile() {
        String profile = "";
        if (ArrayUtils.contains(environment.getActiveProfiles(), "dev")) {
            profile = "dev";
        } else if (ArrayUtils.contains(environment.getActiveProfiles(), "test")) {
            profile = "test";
        } else if (ArrayUtils.contains(environment.getActiveProfiles(), "prod")) {
            profile = "prod";
        } else {
            profile = "test";
        }

        return profile;
    }

    @Bean
    public JedisConnectionFactoryProxy jedisConnectionFactory() throws Exception {
        final JedisConnectionFactoryProxy rccf = new JedisConnectionFactoryProxy(createPool());
        return rccf;
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate() throws Exception {
        LOGGER.info("create redisTemplate");
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        redisTemplate.setKeySerializer(redisKeySerializer());
        redisTemplate.setHashKeySerializer(redisKeySerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public RedisSerializer<?> redisKeySerializer() {
        return new StringRedisSerializer();
    }

    @Bean
    public SessionRepository sessionRepository() throws Exception {
        RedisOperationsSessionRepository sessionRepository = new RedisOperationsSessionRepository(redisTemplate());
        return sessionRepository;
    }

}
