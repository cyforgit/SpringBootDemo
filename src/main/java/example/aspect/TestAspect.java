package example.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;

@Aspect
@Component
public class TestAspect {

    @Pointcut("execution(* example.controller.controller.*(..))")
    void pointCut() {
    }

    @Before("pointCut()") //
    public void beforeMethod(JoinPoint jp) {
        System.out.println("TestAspect beforeMethod :" + jp.getSignature().getName());
    }

    @After("pointCut()")
    public void afterMethod(JoinPoint joinPoint) {
        System.out.println("TestAspect afterMethod");

    }

    @AfterThrowing("pointCut()")
    public void afterThrowing() {
        System.out.println("TestAspect afterThrowing");
    }

    @AfterReturning("pointCut()")
    public void afterReturn() {
        System.out.println("TestAspect afterReturn");
    }

    @Around("pointCut()")
    public Object aroundMethod(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.out.println("TestAspect aroundMethod");
        return proceedingJoinPoint.proceed();
    }
}
