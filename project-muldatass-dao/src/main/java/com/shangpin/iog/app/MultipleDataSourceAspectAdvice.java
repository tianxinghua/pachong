package com.shangpin.iog.app;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.shangpin.iog.dao.base.HKMapper;
import com.shangpin.iog.dao.base.Mapper;

@Component
@Aspect
public class MultipleDataSourceAspectAdvice {

    @Around("execution(* com.shangpin.iog.*.*(..))")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        if (jp.getTarget() instanceof Mapper) {
            MultipleDataSource.setDataSourceKey("iogDataSource");
        } else if (jp.getTarget() instanceof HKMapper) {
            MultipleDataSource.setDataSourceKey("ioghkDataSource");
        }
        return jp.proceed();
    }
}