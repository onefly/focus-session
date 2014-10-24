package org.focus.session.store;

import java.util.Map;

import org.focus.session.exception.FocusException;
import org.focus.session.exception.FocusSesssionInitException;

public interface HttpSessionStore {

	public Map<String, Object> get(String sessionId) throws FocusSesssionInitException;


	public Map<String, Object> replace(String sessionId,
			Map<String, Object> newSession)throws FocusException;

	public boolean delete(String sessionId) throws FocusException;

	public boolean shutdown();

}
