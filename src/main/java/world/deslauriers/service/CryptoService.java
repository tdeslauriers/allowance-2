package world.deslauriers.service;

public interface CryptoService {

    String encryptAes256Gcm(byte[] plainText, String password) throws Exception;

    String decryptAes256Gcm(String cipherText, String password) throws Exception;
}
