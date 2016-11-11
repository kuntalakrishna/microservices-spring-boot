package com.dev.ops.micro.service.user.interceptor;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
public class LoggingInterceptor {

	private static final Logger LOGGER = LogManager.getLogger(LoggingInterceptor.class);

	@Before("execution(* com.dev.ops.micro.service.user..*.*(..)) && !execution(* com.dev.ops.micro.service.user.orika..*.*(..))")
	public void logMethodStart(JoinPoint joinPoint) {
		String methodSignature = getMethodSignature(joinPoint).toString();
		LOGGER.info("START of " + methodSignature);
	}

	@AfterReturning("execution(* com.dev.ops.micro.service.user..*.*(..)) && !execution(* com.dev.ops.micro.service.user.orika..*.*(..))")
	public void logMethodEnd(JoinPoint joinPoint) {
		String methodSignature = getMethodSignature(joinPoint).toString();
		LOGGER.info("END of " + methodSignature);
	}

	@AfterThrowing("execution(* com.dev.ops.micro.service.user..*.*(..)) && !execution(* com.dev.ops.micro.service.user.orika..*.*(..))")
	public void logMethodException(JoinPoint joinPoint) {
		String methodSignature = getMethodSignature(joinPoint).toString();
		LOGGER.info("ERROR of " + methodSignature);
	}

	@Around("execution(* com.dev.ops.micro.service.user..controllers..*.*(..))")
	public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
		StopWatch stopWatch = new StopWatch();

		stopWatch.start();
		Object retVal = joinPoint.proceed();
		stopWatch.stop();

		StringBuilder logMessage = new StringBuilder(getMethodSignature(joinPoint));
		logMessage.append(" execution time: ");
		logMessage.append(stopWatch.getTotalTimeMillis());
		logMessage.append(" ms");
		LOGGER.debug("PERF_LOG=" + logMessage.toString());
		return retVal;
	}

	private String getMethodSignature(JoinPoint joinPoint) {
		StringBuilder logMessage = new StringBuilder();
		logMessage.append(joinPoint.getTarget().getClass().getName());
		logMessage.append(".");
		logMessage.append(joinPoint.getSignature().getName());
		logMessage.append("(");

		Object[] args = joinPoint.getArgs();
		for(Object arg : args) {
			if(null != arg) {
				logMessage.append(arg.getClass().getSimpleName()).append(", ");
			}
		}

		if(args.length > 0) {
			logMessage.delete(logMessage.length() - 2, logMessage.length());
		}

		logMessage.append(")");
		return logMessage.toString();
	}
}