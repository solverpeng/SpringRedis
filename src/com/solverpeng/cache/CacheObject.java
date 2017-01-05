package com.solverpeng.cache;

import java.io.Serializable;

public class CacheObject<T> implements Serializable{
	
	private static final long serialVersionUID = 811157572797534004L;
	
	private String key;
	private T object;
	
	public T getObject() {
		return object;
	}

	public void setObject(T object) {
		this.object = object;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}