package club.banyuan.studyweb.proxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggerProxy {

    private Logger logger= LoggerFactory.getLogger(LoggerProxy.class);

    @Pointcut("@annotation(club.banyuan.studyweb.annotation.LoggerAnnotation)")
    public void LoggerProxy() {

    }

    @Before(value = "LoggerProxy()")
    public void before(JoinPoint joinPoint){
        logger.info("进入 {}",joinPoint.getSignature().getName());
        Object [] object=joinPoint.getArgs();
        for (Object obj:object) {
            logger.info("入参 {}",obj);
        }
    }

    @AfterReturning(value = "LoggerProxy()" ,returning = "result")
    public void after(JoinPoint joinPoint,Object result){
        logger.info("返回值 {}",result);
    }

}
