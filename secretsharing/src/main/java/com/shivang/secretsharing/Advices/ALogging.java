package com.shivang.secretsharing.Advices;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.shivang.secretsharing.SecretServiceClass;
import com.shivang.secretsharing.Exceptions.UnauthorizedException;
import com.shivang.secretsharing.pojo.User;

@Aspect
public class ALogging {

	private Map<String, User> usersDB;

	@Before("execution(* com.shivang.secretsharing.SecretServiceClass.readSecret(..))")
	public void readSecret(JoinPoint joinPoint) {

		Object[] arg1 = joinPoint.getArgs();
		SecretServiceClass ssc = (SecretServiceClass) joinPoint.getTarget();
		usersDB = ssc.getUsersDB();

		UUID key = (UUID) arg1[1];
		System.out.println(arg1[0] + " reads the secret of ID " + key);
		if (!isAuth(arg1[0], key)) {
			throw new UnauthorizedException(arg1[0].toString());
		}
	}

	@Before("execution(* com.shivang.secretsharing.SecretServiceClass.shareSecret(..))")
	public void shareSecret(JoinPoint joinPoint) {

		Object[] arg1 = joinPoint.getArgs();
		SecretServiceClass ssc = (SecretServiceClass) joinPoint.getTarget();
		usersDB = ssc.getUsersDB();

		UUID key = (UUID) arg1[1];
		System.out.println(arg1[0] + " shares the secret of ID " + key + " with " + arg1[2]);
		if (!isAuth(arg1[0], key)) {
			throw new UnauthorizedException(arg1[0].toString());
		}

	}

	@AfterReturning(pointcut = "execution(* com.shivang.secretsharing.SecretServiceClass.storeSecret(..))", returning = "result")
	public void storeSecret(JoinPoint joinPoint, Object result) {
		Object[] arg1 = joinPoint.getArgs();
		System.out.println(arg1[0] + " creates a secrete with ID " + result);
	}

	@Around("execution(* com.shivang.secretsharing.SecretServiceClass.unshareSecret(..))")
	public void logAround(ProceedingJoinPoint joinPoint) throws Throwable {

		Object[] arg1 = joinPoint.getArgs();
		SecretServiceClass ssc = (SecretServiceClass) joinPoint.getTarget();
		usersDB = ssc.getUsersDB();
		System.out.println(arg1[0] + " unshares the secret of ID " + arg1[1] + " with " + arg1[2]);
		UUID key = (UUID) arg1[1];
		if (isOwner(arg1[0], key)) {
			joinPoint.proceed(); // continue to unshare
		} else if (!isAuth(arg1[0], key)) {
			throw new UnauthorizedException(arg1[0].toString());
		} else {
			// silently handle
		}
	}

	/*
	 * Checks if User can access Secret
	 */
	boolean isOwner(Object userID, UUID secretID) {
		if (!usersDB.get(userID).getOwnSecrets().containsKey(secretID)) {
			return false;
		}
		return true;
	}

	/*
	 * Checks if User can access Secret
	 */
	boolean isAuth(Object userID, UUID secretID) {
		if (!usersDB.get(userID).getOwnSecrets().containsKey(secretID)
				&& !usersDB.get(userID).getReceivedSecrets().containsKey(secretID)) {
			return false;
		}
		return true;
	}

}