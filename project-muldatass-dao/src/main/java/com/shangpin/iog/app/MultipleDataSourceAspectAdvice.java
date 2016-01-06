package com.shangpin.iog.app;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.shangpin.iog.dao.base.HKIBaseDao;
import com.shangpin.iog.dao.base.IBaseDao;

@Component
@Aspect
public class MultipleDataSourceAspectAdvice {

    @Around("execution(* com.shangpin.iog.product.dao.*.*(..))")
    public Object doAround(ProceedingJoinPoint jp) throws Throwable {
        if (jp.getTarget() instanceof IBaseDao) {
            MultipleDataSource.setDataSourceKey("iogDataSource");
        } else if (jp.getTarget() instanceof HKIBaseDao) {
            MultipleDataSource.setDataSourceKey("ioghkDataSource");
        }
        return jp.proceed();
    }
}