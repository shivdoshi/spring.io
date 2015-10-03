package com.shivang.secretsharing.Advices;

import java.lang.reflect.Method;

import org.springframework.aop.AfterReturningAdvice;

import com.shivang.secretsharing.pojo.Secret;

public class HijackAfter implements AfterReturningAdvice {

	public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
		String methodName = method.getName();
		if(methodName.equals("storeSecret")){
			System.out.println(args[0] +" creates a secrete with ID "+ returnValue);
		}	
	}
}