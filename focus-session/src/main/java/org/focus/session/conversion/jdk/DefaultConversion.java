package org.focus.session.conversion.jdk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.focus.session.conversion.Conversion;

public class DefaultConversion implements Conversion {

	@Override
	public byte[] conversion(Object src) {
		  try (ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	                ObjectOutputStream oos = new ObjectOutputStream(baos);) {  
	            oos.writeObject(src);  
	            return baos.toByteArray();  
	        } catch (IOException e) {  
	            throw new RuntimeException(e);  
	        }  
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T unconversion(byte[] src) {
		   try (ByteArrayInputStream bais = new ByteArrayInputStream(src);  
	                ObjectInputStream ois = new ObjectInputStream(bais);  
	        ) {  
	            return (T) ois.readObject();  
	        } catch (Exception e) {  
	            throw new RuntimeException(e);  
	        } 
	}

}
