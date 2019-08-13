package cn.tongdun.web.security.example.redis;

import cn.fraudmetrix.cache.redis.jedis.JedisResourcePool;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

/**
 * Created by binsong.li on 2019/03/18.
 */
public class JedisConnectionFactoryProxy extends JedisConnectionFactory {

    private JedisResourcePool pool;

    public JedisConnectionFactoryProxy(JedisResourcePool pool) {
        super();
        this.pool = pool;
    }

    @Override
    protected Pool<Jedis> createRedisPool() {
        return (Pool<Jedis>) pool;
    }

    @Override
    public boolean getUsePool() {
        return true;
    }

    @Override
    public boolean isRedisSentinelAware() {
        return false;
    }
}
