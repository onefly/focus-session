package org.focus.session.wrapper;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.focus.session.util.ObjectUtils;

public class FocusHttpRequestWrapper extends HttpServletRequestWrapper {
	private String sid;
	public FocusHttpRequestWrapper(HttpServletRequest request) {
		super(request);
	}
	public FocusHttpRequestWrapper(HttpServletRequest request,String sid) {
		super(request);
		this.sid = sid;
	}
	@Override
	public HttpSession getSession() {
		return initSession();
	}
	
	@Override
	public HttpSession getSession(boolean create) {
		final HttpSession session = super.getSession(create);
		if(!ObjectUtils.notNull(session)){
			return null;
		}
		return initSession();
	}
	private HttpSession initSession(){
		if(ObjectUtils.notNull(sid))
			return new FocusHttpSessionWrapper(sid, this.getSession());
			return new FocusHttpSessionWrapper(this.getSession(), (String)getRequest().getAttribute("sessionContext"));
	}
	

}
