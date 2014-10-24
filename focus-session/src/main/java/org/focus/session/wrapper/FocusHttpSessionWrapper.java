package org.focus.session.wrapper;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.focus.session.AbstractFocusSession;
import org.focus.session.exception.FocusSesssionInitException;
import org.focus.session.store.HttpSessionStore;
import org.focus.session.util.ObjectUtils;
import org.focus.session.util.SPIHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FocusHttpSessionWrapper extends AbstractFocusSession{
	private static final long serialVersionUID = 1;
	private static final Logger LOGGER = LoggerFactory.getLogger(FocusHttpSessionWrapper.class); 
	
	private HttpSession session;
	private String sid;
	private  Map<String, Object> arrts;
	private  HttpSessionStore sessionStore = SPIHolder.getService(HttpSessionStore.class);
	private boolean useLocalSession = false;
	

	public FocusHttpSessionWrapper(HttpSession session,String sessionContext) {
		if(ObjectUtils.notNull(session)){
			this.session = session;
			this.sid = this.session.getId();
			if(ObjectUtils.notNull(this.sid)){
				this.sid = new StringBuilder(sessionContext).append("_").append(this.sid).toString();
			}
			initAttr(sid);
		}else{
			LOGGER.warn("session is null");
		}
	}
	
	public FocusHttpSessionWrapper(String sid, HttpSession session) {
        if(ObjectUtils.notNull(session)){
            this.session = session;
            this.sid = sid;
            initAttr(sid);
        }
        
        
    }

	/**
	 * @param sid
	 */
	private void initAttr(String sid) {
		try {
			this.arrts = this.sessionStore.get(sid);
			LOGGER.info("current user session id 【{}】 ，There are 【{}】 objects in the session",sid,this.arrts.size());
		} catch (FocusSesssionInitException e) {
			LOGGER.error("error init session,sid is {} \r\n {}",sid,e);
		}
	}
	
	
	

	@Override
	public HttpSession getSession() {
		return this.session;
	}

	@Override
	public String getId() {
		return ObjectUtils.notNull(this.sid)?this.sid:this.session.getId();
	}

	@Override
	public Object getAttribute(String key) {
		Object result = null;
		if(useLocalSession()){
			result = this.getSession().getAttribute(key);
			LOGGER.info("从原生态session中获取：{} 当前用户sid【{}】",key,sid);
		}else {
			result = this.arrts.get(key);
			LOGGER.info("从集中生态session中获取：{} 当前用户sid【{}】",key,sid);
		}
		return result;
	}

	/**
	 * @return
	 */
	private boolean useLocalSession() {
		return !ObjectUtils.notNull(arrts)&&ObjectUtils.notNull(session)&&useLocalSession;
	}

	@Override
	public Enumeration<?> getAttributeNames() {
		if(useLocalSession()){
			return this.session.getAttributeNames();
		}
		return Collections.enumeration(arrts.keySet());
	}

	@Override
	public void invalidate() {
		if(useLocalSession()) this.session.invalidate();
		else{
			this.arrts.clear();
			this.sessionStore.delete(sid);
		}
	}

	@Override
	public void setAttribute(String name, Object value) {
		if(useLocalSession())this.session.setAttribute(name, value);
		else{
			this.arrts.put(name, value);
			this.sessionStore.replace(sid, arrts);
		}
	}

	@Override
	public void removeAttribute(String name) {
		if(useLocalSession())this.session.removeAttribute(name);
		else{
			this.arrts.remove(name);
			this.sessionStore.replace(sid, arrts);
		}
	}

}
