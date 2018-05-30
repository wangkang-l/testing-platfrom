package com.bgw.testing.server.aspect;

import com.bgw.testing.common.enums.ErrorCode;
import com.bgw.testing.server.config.ServerException;
import com.bgw.testing.server.util.BaseJsonUtils;
import com.bgw.testing.server.util.BaseMDCUtils;
import com.bgw.testing.server.util.BaseStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class ControllerAspect {

    @Pointcut("execution(public * com.bgw.testing.server.controller.*Controller.*(..))")
    public void webLog() {

    }

    @Before("webLog()")
    public void before(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        log.info("URL:{}, HTTP_METHOD:{}, CLASS_METHOD:{}, PARAMS:{}",
                request.getRequestURL().toString(),
                request.getMethod(),
                joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(),
                Arrays.toString(joinPoint.getArgs()));
    }


    /**
     * 后置异常通知
     * @param joinPoint
     * @param e
     * @throws ServerException
     */
    @AfterThrowing(value = "webLog()", throwing = "e")
    public void afterThrowing(JoinPoint joinPoint, Throwable e) throws ServerException {
        log.error(getTrace(e));
        if (e.getClass().equals(ServerException.class)) {

        } else {
            throw new ServerException(ErrorCode.BAD_REQUEST.errorKey);
        }
    }

    /**
     * 环绕通知,环绕增强，相当于MethodInterceptor
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("webLog()")
    public Object arround(ProceedingJoinPoint pjp) throws Throwable {
        //初始化生成日志ID
        BaseMDCUtils.put(BaseStringUtils.uuid());
        Object o =  pjp.proceed();
        log.info("RESULT : {}", BaseJsonUtils.writeValue(o));
        BaseMDCUtils.clear();
        return o;
    }


//    /**
//     * 后置最终通知,final增强，不管是抛出异常或者正常退出都会执行
//     * @param jp
//     */
//    @After("webLog()")
//    public void after(JoinPoint jp){
//
//    }

//    @AfterReturning(returning = "ret", pointcut = "webLog()")
//    public void afterReturning(Object ret) throws Throwable {
//        BaseMDCUtils.clear();
//    }



    /**
     * 将异常信息输出到log文件
     * @param t
     * @return
     */
    public static String getTrace(Throwable t) {
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }

}
