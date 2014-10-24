package org.focus.session.conversion;


public interface Conversion {

		public byte[] conversion(Object src);
		public <T> T unconversion(byte[] src);
		
}
