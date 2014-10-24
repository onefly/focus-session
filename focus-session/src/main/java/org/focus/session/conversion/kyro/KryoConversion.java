package org.focus.session.conversion.kyro;

import java.util.HashMap;

import org.focus.session.conversion.Conversion;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoConversion implements Conversion {
	private final static Kryo kryo = new Kryo();
	static {
		kryo.register(HashMap.class);
	}

	@Override
	public byte[] conversion(Object src) {
		try (Output output = new Output(new byte[512])) {
			kryo.writeClassAndObject(output, src);
			return output.toBytes();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unconversion(byte[] src) {
		try (Input input = new Input(src, 0, 512)) {
			return (T) kryo.readClassAndObject(input);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
