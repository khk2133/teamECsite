package com.internousdev.rose.action;

import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;

public class GoLoginAction extends ActionSupport implements SessionAware{
	private String cartFlg;
	private Map<String, Object> session;
	public String execute() {
		if(cartFlg != null) {
			session.put("cartFlg", cartFlg);
		}
		return SUCCESS;
	}
	public String getCartFlg() {
		return cartFlg;
	}
	public void setCartFlg(String cartFlg) {
		this.cartFlg = cartFlg;
	}
	public Map<String, Object> getSession() {
		return session;
	}
	public void setSession(Map<String, Object> session) {
		this.session = session;
	}
}
