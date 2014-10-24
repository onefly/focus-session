package org.focus.session.conversion.fst;

import java.util.HashMap;

import org.focus.session.conversion.Conversion;
import org.nustaq.serialization.FSTConfiguration;

public class FstConversion implements Conversion {
	
	private static final FSTConfiguration fst = FSTConfiguration.createStructConfiguration();
	static{
		fst.registerClass(HashMap.class);
	}
	@Override
	public byte[] conversion(Object src) {
		return fst.asByteArray(src);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unconversion(byte[] src) {
		return (T) fst.asObject(src);
	}

}
