package com.shangpin.logger;

/**
 * Created by lizhongren on 2016/4/25.
 */
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class Log4jAop {
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
        //execution(* org.apache.log4j.Logger.*(..))||execution(* org.apache.log4j.Category.error(..))||execution(* org.slf4j.*.*(..))||execution(* org.apache.commons.logging.*.*(..))
        // execution(* com.shangpin.logtemplate.schedule.TestInterface.*(..))||
    @Pointcut("execution(* com.shangpin.logtemplate.schedule.TestImpl.*(..))")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before(JoinPoint pjp){
        // 调用方法的参数
        Object[] args = pjp.getArgs();
        System.out.println("before");
        // 调用的方法名
        String method = pjp.getSignature().getName();
        // 获取目标对象(形如：com.action.admin.LoginAction@1a2467a)
        Object target = pjp.getTarget();
        //获取目标对象的类名(形如：com.action.admin.LoginAction)
        String targetName = pjp.getTarget().getClass().getName();
        if(null!=args){
            if("error".equals(method)){
                if(1==args.length) {
                    logError.error(args);
                }else if(2==args.length){
                    logError.error((String)args[0],args[1]);
                }
            }
        }

    }


}
