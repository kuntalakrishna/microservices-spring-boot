/*
 *
 * Copyright (C) 2016 Krishna Kuntala <kuntala.krishna@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
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

/**
 * The class Logging Interceptor contains the spring AOP related methods
 * which concentrates on logging when the control enters & exits any methods.
 * It also contains the methods which will measure the time a particular service has taken to process and respond.
 */
@Aspect
@Component
public class LoggingInterceptor {

	/** The LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(LoggingInterceptor.class);

	/**
	 * Log method which will be invoked (according to pointcuts specified below) before each method "is invoked"
	 * in the classes from package com.dev.ops.micro.service.user and its sub packages. It will ignore
	 * the methods in the classes from package com.dev.ops.micro.service.user.orika and its subpackages.
	 *
	 * @param joinPoint the contains the information related to method signature and the join point
	 */
	@Before("execution(* com.dev.ops.micro.service.user..*.*(..)) && !execution(* com.dev.ops.micro.service.user.orika..*.*(..))")
	public void logMethodStart(JoinPoint joinPoint) {
		String methodSignature = getMethodSignature(joinPoint).toString();
		LOGGER.info("START of " + methodSignature);
	}

	/**
	 * Log method which will be invoked (according to pointcuts specified below) after each method "execution is completed"
	 * in the classes from package com.dev.ops.micro.service.user and its sub packages. It will ignore
	 * the methods in the classes from package com.dev.ops.micro.service.user.orika and its subpackages.
	 *
	 * @param joinPoint the contains the information related to method signature and the join point
	 */
	@AfterReturning("execution(* com.dev.ops.micro.service.user..*.*(..)) && !execution(* com.dev.ops.micro.service.user.orika..*.*(..))")
	public void logMethodEnd(JoinPoint joinPoint) {
		String methodSignature = getMethodSignature(joinPoint).toString();
		LOGGER.info("END of " + methodSignature);
	}

	/**
	 * Log method which will be invoked (according to pointcuts specified below) if there is any "exception thrown" from the methods
	 * in the classes from package com.dev.ops.micro.service.user and its sub packages. It will ignore
	 * the methods in the classes from package com.dev.ops.micro.service.user.orika and its subpackages.
	 *
	 * @param joinPoint the contains the information related to method signature and the join point
	 */
	@AfterThrowing("execution(* com.dev.ops.micro.service.user..*.*(..)) && !execution(* com.dev.ops.micro.service.user.orika..*.*(..))")
	public void logMethodException(JoinPoint joinPoint) {
		String methodSignature = getMethodSignature(joinPoint).toString();
		LOGGER.info("ERROR of " + methodSignature);
	}

	/**
	 * This method will be invoked (according to pointcut specified below) for earch REST API controller.
	 * Takes care of performance logging for each REST API invoked. The logs would help in the performance analysis
	 * of the REST APIs and help in plotting the response graphs according to the traffic.
	 *
	 * @param joinPoint the contains the information related to method signature and the join point
	 * @return the object
	 * @throws Throwable the throwable
	 */
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

	/**
	 * Gets the method signature.
	 *
	 * @param joinPoint the contains the information related to method signature and the join point
	 * @return the method signature in the string format
	 */
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