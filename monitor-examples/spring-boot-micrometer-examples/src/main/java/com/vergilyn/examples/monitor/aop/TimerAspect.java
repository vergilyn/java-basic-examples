package com.vergilyn.examples.monitor.aop;

import com.vergilyn.examples.monitor.monitor.MetricsMonitor;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Aspect
public class TimerAspect {

	@Around(value = "execution(* com.vergilyn.examples.monitor.service.*.*(..))")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Signature signature = point.getSignature();

		MethodSignature methodSignature = (MethodSignature) signature;
		Method method = methodSignature.getMethod();

		Timer metricsTimer = MetricsMonitor.getMethodCost(method);
		AtomicReference<Throwable> throwableRef = new AtomicReference<>();

		Object result = metricsTimer.recordCallable(() -> {
			try {
				return point.proceed();
			} catch (Throwable e) {
				throwableRef.set(e);
			}

			return null;
		});

		Throwable throwable = throwableRef.get();
		if (throwable != null){
			throw throwable;
		}

		return result;
	}
}
