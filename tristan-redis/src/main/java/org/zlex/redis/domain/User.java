package org.zlex.redis.domain;

import java.io.Serializable;

public class User implements Serializable {

	private String uid;

	private String address;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}
