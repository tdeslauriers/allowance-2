package world.deslauriers.service;

import jakarta.inject.Singleton;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

@Singleton
public class CryptoServiceImpl implements CryptoService{

    private static final int AES_256_GCM_SALT_LENGTH = 16; // in bytes
    private static final int AES_256_GCM_IV_LENGTH = 12; // in bytes, 12 = GCM recommended? Doesn't make sense.
    private static final int AES_256_GCM_TAG_LENGTH = 128; // in bits, // tag length must be 128, 120, 112, 104, or 96
    private static final String AES_256_GCM_ALGORITHM = "AES/GCM/NoPadding";

    @Override
    public String encryptAes256Gcm(byte[] plainText, String password) throws Exception {

        // setup
        var salt = createRandomNonce(AES_256_GCM_SALT_LENGTH);
        var iv = createRandomNonce(AES_256_GCM_IV_LENGTH);
        var key = deriveAESKeyFromPassword(password.toCharArray(), salt);

        var cipher = Cipher.getInstance(AES_256_GCM_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(AES_256_GCM_TAG_LENGTH, iv));

        byte[] cipherText = cipher.doFinal(plainText);

        // pre-pend iv and salt
        byte[] cipherPlusIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();

        // base64 encode
        return Base64.getEncoder().encodeToString(cipherPlusIvSalt);
    }

    @Override
    public String decryptAes256Gcm(String cipherText, String password) throws Exception {

        // decode base64
        byte[] decoded = Base64.getDecoder().decode(cipherText.getBytes(UTF_8));

        // split iv, salt, and ciphertext byte arrays
        var buffer = ByteBuffer.wrap(decoded);

        var iv = new byte[AES_256_GCM_IV_LENGTH];
        buffer.get(iv);

        var salt = new byte[AES_256_GCM_SALT_LENGTH];
        buffer.get(salt);

        var encrypted = new byte[buffer.remaining()];
        buffer.get(encrypted);

        // derive key from password
        var key = deriveAESKeyFromPassword(password.toCharArray(), salt);
        var cipher = Cipher.getInstance(AES_256_GCM_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, new GCMParameterSpec(AES_256_GCM_TAG_LENGTH, iv));

        byte[] plainText = cipher.doFinal(encrypted);

        return new String(plainText, UTF_8);
    }

    // gen salts and iv's
    private byte[] createRandomNonce(int numOfBytes){
        var nonce = new byte[numOfBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    // derive AES 256-bit key from password
    private SecretKey deriveAESKeyFromPassword(char[] password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        var keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        var keySpec = new PBEKeySpec(password, salt, 65536, 256);

        return new SecretKeySpec(keyFactory.generateSecret(keySpec).getEncoded(), "AES");
    }

}
