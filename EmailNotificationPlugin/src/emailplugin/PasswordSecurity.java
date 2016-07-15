package emailplugin;

import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.*;

/**
 * This encryption class was found on code2learn.com and was slightly
 * modified to suit the needs of the email notification plugin.
 *
 * @author		http://www.code2learn.com/2011/06/encryption-and-decryption-of-data-using.html
 */
public class PasswordSecurity {
	private static final String ALGO = "AES";
	private static final byte[] keyValue = 
			new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't',
					'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };
	/**
	 * Uses formula to encrypt password
	 *
	 * @parameter	data		Password as String to encrypt
	 * @return  				String containing encrypted password
	 * @exception	Exception	Throws Exception in case of error
	 */
	public String encrypt(String data) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(data.getBytes());
		String encryptedValue = new BASE64Encoder().encode(encVal);
		return encryptedValue;
	}
	/**
	 * Uses formula to decrypt password
	 *
	 * @parameter	encryptedData	Password as encrypted string
	 * @return  					String containing decrypted password
	 * @exception	Exception		Throws Exception in case of error
	 */
	public String decrypt(String encryptedData) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}
	/**
	 * Uses javax.crypt library to generate a secret key from byte array
	 * for encryption and decryption
	 *
	 * @return  Security key from java security library
	 */
	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}
}
