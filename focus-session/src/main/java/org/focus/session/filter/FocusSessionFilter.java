/**
 * 
 */
package org.focus.session.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.focus.session.util.ObjectUtils;
import org.focus.session.wrapper.FocusHttpRequestWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuyi
 * 
 */
public class FocusSessionFilter implements Filter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FocusSessionFilter.class);

	private String sessionId = "SessionID";
	private String sessionContext = "default";

	@Override
	public void init(FilterConfig config) throws ServletException {
		LOGGER.info("focus session plug filter init...");
		final String initSessionId = config.getInitParameter("sessionId");
		if (ObjectUtils.notNull(initSessionId))
			this.sessionId = initSessionId;
		final String initSessionContext = config
				.getInitParameter("sessionContext");
		if (ObjectUtils.notNull(initSessionContext))
			this.sessionContext = initSessionContext;
		LOGGER.info("focus session plug init success");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String sessionIdValue = getSessionIdValue(req);
		if(!ObjectUtils.notNull(sessionIdValue)){
			request.setAttribute("sessionContext", sessionContext);
			chain.doFilter(new FocusHttpRequestWrapper(req), response);
			return;
		}
		 if(!sessionIdValue.toLowerCase().contains(sessionContext.toLowerCase())){
			 sessionIdValue = sessionContext+"_"+sessionIdValue;
         }
		chain.doFilter(new FocusHttpRequestWrapper(req, sessionIdValue), response);
	}

	@Override
	public void destroy() {

	}

	private String getSessionIdValue(HttpServletRequest request) {
		final Cookie[] cookies = request.getCookies();
		String value = null;
		if (ObjectUtils.notNull(cookies)) {
			for (Cookie sCookie : cookies) {
				if (sCookie.getName().equals(sessionId)) {
					value = sCookie.getValue();
					LOGGER.info("session id value {}", value);
					return value;
				}
			}
		}
		return value;
	}
	

}
