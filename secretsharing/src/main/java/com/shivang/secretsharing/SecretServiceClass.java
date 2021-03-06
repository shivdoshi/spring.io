package com.shivang.secretsharing;

import java.util.Map;
import java.util.UUID;

import com.shivang.secretsharing.Interfaces.SecretService;
import com.shivang.secretsharing.pojo.Secret;
import com.shivang.secretsharing.pojo.User;

public class SecretServiceClass implements SecretService {
	private Map<String, User> usersDB;
	
	public SecretServiceClass(){

	}

	public Map<String, User> getUsersDB() {
		return usersDB;
	}

	public void setUsersDB(Map<String, User> usersDB) {
		this.usersDB = usersDB;
	}
	
	public UUID storeSecret(String userId, Secret secret) {
		UUID id = UUID.randomUUID();
		usersDB.get(userId).addOwnSecret(id, secret);
		return id;
	}

	public Secret readSecret(String userId, UUID secretId) {
		Secret s = usersDB.get(userId).readSharedSecret(secretId);
		if(s == null)
			s = usersDB.get(userId).readOwnSecret(secretId);
		return s;
	}

	public void shareSecret(String userId, UUID secretId, String targetUserId) {
		Secret s = usersDB.get(userId).readOwnSecret(secretId);
		if(s == null)
			s = usersDB.get(userId).readSharedSecret(secretId);
		usersDB.get(targetUserId).addSharedSecret(secretId,s);
	}

	public void unshareSecret(String userId, UUID secretId, String targetUserId) {
		usersDB.get(targetUserId).unshareSecret(secretId);
	}

}
