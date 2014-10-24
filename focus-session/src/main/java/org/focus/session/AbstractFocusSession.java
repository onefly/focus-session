package org.focus.session;

import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public abstract class AbstractFocusSession implements HttpSession,Serializable{
	private static final long serialVersionUID = 1L;
	public abstract HttpSession getSession();
	

	@Override
	public long getCreationTime() {
		return this.getSession().getCreationTime();
	}

	@Override
	public abstract String getId();
	@Override
	public abstract Object getAttribute(String name) ;
	@Override
	public abstract Enumeration<?> getAttributeNames() ;
	@Override
	public abstract void invalidate();
	@Override
	public abstract void setAttribute(String name, Object value) ;
	public  Object getValue(String name){
		return this.getSession().getValue(name);
	}

	@Override
	public long getLastAccessedTime() {
		return this.getSession().getLastAccessedTime();
	}

	@Override
	public ServletContext getServletContext() {
		return this.getSession().getServletContext();
	}

	@Override
	public void setMaxInactiveInterval(int interval) {
		getSession().setMaxInactiveInterval(interval);
	}

	@Override
	public int getMaxInactiveInterval() {
		return this.getSession().getMaxInactiveInterval();
	}

	@Override
	public HttpSessionContext getSessionContext() {
		return this.getSession().getSessionContext();
	}



	@Override
	public String[] getValueNames() {
		return getSession().getValueNames();
	}



	@Override
	public void putValue(String name, Object value) {
		this.getSession().putValue(name, value);
	}

	@Override
	public abstract void removeAttribute(String name);

	@Override
	public void removeValue(String name) {
		this.getSession().removeValue(name);
	}

	

	@Override
	public boolean isNew() {
		return this.getSession().isNew();
	}

}
