package rongji.redis.core.components;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import rongji.framework.util.StringUtil;
import rongji.redis.core.annotations.RedisCacheEvict;
import rongji.redis.core.annotations.RedisCacheable;
import rongji.redis.core.impl.RedisClient;

import java.lang.reflect.Method;

@Component
@Aspect
public class RedisCacheAspect {

    private static final Logger logger = LoggerFactory.getLogger(RedisCacheAspect.class);

    @Autowired
    private RedisClient redisClient;

    /**
     * 定义缓存逻辑
     */
    @Around("@annotation(rongji.redis.core.annotations.RedisCacheable)")
    public Object cache(ProceedingJoinPoint pjp) {
        Object result = null;
        Boolean cacheEnable = redisClient.canNotUse(true);
        //判断是否开启缓存
        if (!cacheEnable) {
            try {
                result = pjp.proceed();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return result;
        }

        Method method = getMethod(pjp);
        RedisCacheable cacheable = method.getAnnotation(RedisCacheable.class);

        String fieldKey = parseKey(cacheable.fieldKey(), method, pjp.getArgs());

        //获取方法的返回类型,让缓存可以返回正确的类型
        result = redisClient.opsForHash().hget(cacheable.key(), fieldKey);
        if (result == null) {
            try {
                result = pjp.proceed();
                Assert.notNull(fieldKey);
                result = redisClient.opsForHash().hset(cacheable.key(), fieldKey, result);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }
        return result;
    }

    /**
     * 定义清除缓存逻辑
     */
    @Around(value = "@annotation(rongji.redis.core.annotations.RedisCacheEvict)")
    public Object evict(ProceedingJoinPoint pjp) {
        Object result = null;
        Boolean canNotUse = redisClient.canNotUse(true);
        if (!canNotUse) {
            try {
                Method method = getMethod(pjp);
                RedisCacheEvict cacheEvict = method.getAnnotation(RedisCacheEvict.class);
                String pattern = cacheEvict.key();
                String entId = cacheEvict.entId();
                if (StringUtil.isNotEmpty(entId)) {
                    String _entId = parseKey(entId, method, pjp.getArgs());//取出企业的ID
                    if (StringUtil.isNotEmpty(_entId)) {
                        redisClient.batchDel("adminUserEnt" + _entId);
                    }
                } else {
                    redisClient.batchDel(pattern);
                }
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
            }
        }

        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }

        return result;
    }

    /**
     * 获取被拦截方法对象
     * <p>
     * MethodSignature.getMethod() 获取的是顶层接口或者父类的方法对象
     * 而缓存的注解在实现类的方法上
     * 所以应该使用反射获取当前对象的方法对象
     */
    public Method getMethod(ProceedingJoinPoint pjp) {
        //获取参数的类型
        Object[] args = pjp.getArgs();
        Class[] argTypes = new Class[pjp.getArgs().length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        Method method = null;
        try {
            method = pjp.getTarget().getClass().getMethod(pjp.getSignature().getName(), argTypes);
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
        } catch (SecurityException e) {
            logger.error(e.getMessage(), e);
        }
        return method;
    }

    /**
     * 获取缓存的key
     * key 定义在注解上，支持SPEL表达式
     *
     * @return
     */
    private String parseKey(String key, Method method, Object[] args) {
        //获取被拦截方法参数名列表(使用Spring支持类库)
        LocalVariableTableParameterNameDiscoverer u =
                new LocalVariableTableParameterNameDiscoverer();
        String[] paraNameArr = u.getParameterNames(method);

        //使用SPEL进行key的解析
        ExpressionParser parser = new SpelExpressionParser();
        //SPEL上下文
        StandardEvaluationContext context = new StandardEvaluationContext();
        //把方法参数放入SPEL上下文中
        for (int i = 0; i < paraNameArr.length; i++) {
            context.setVariable(paraNameArr[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, String.class);
    }
}