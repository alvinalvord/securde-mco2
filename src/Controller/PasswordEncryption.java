package Controller;

import java.security.*;

public class PasswordEncryption {
	
	public static final char[] HEX = "0123456789abcdef".toCharArray ();
	
	public static String hash (String password) {
		
		try {
			MessageDigest md = MessageDigest.getInstance ("SHA-512");
			byte[] bytes = md.digest (password.getBytes ());
			
			StringBuilder sb = new StringBuilder ();
			
			for (int i = 0; i < bytes.length; i++) {
				sb.append (HEX[(bytes[i] >> 4) & 0x0f])
					.append (HEX[bytes[i] & 0x0f]);
			}
			
			return sb.toString ();
		} catch (NoSuchAlgorithmException e) {
			Logger.log ("Password hash error", "unable to hash password");
			System.out.println ("Exiting program now");
			System.exit (0);
		}
		
		return null;
	} 
	
}
