package com.internousdev.rose.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class LogoutAction extends ActionSupport implements SessionAware {

	private Map<String,Object>session;

	public String execute() {
		String userId = String.valueOf(session.get("userId"));

		String tempSaveUserFlg = String.valueOf(session.get("saveUserFlg"));
		boolean saveUserFlg = "null".equals(tempSaveUserFlg)? false  : Boolean.valueOf(tempSaveUserFlg);
		session.clear();
		if(saveUserFlg) {
			session.put("saveUserFlg", saveUserFlg);
			session.put("userId", userId);
		}
		return SUCCESS;
	}

	public Map<String, Object> getSession() {
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

}
