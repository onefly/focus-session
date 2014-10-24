package org.focus.session.util;

import java.util.Objects;

public class ObjectUtils {
	
	public final static  boolean notNull(Object string){
		return !Objects.equals(string, "")&&!Objects.equals(string, null);
	}

}
