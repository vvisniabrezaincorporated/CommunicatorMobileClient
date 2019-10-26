package pl.wnb.communicator.model.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class AesUtil {

    private final int keySize;
    private final int iterationCount;
    private final Cipher cipher;

    public AesUtil(int keySize, int iterationCount) {
        this.keySize = keySize;
        this.iterationCount = iterationCount;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw fail(e);
        }
    }

    public String encrypt(String salt, String iv, String passphrase, String plaintext) throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException, UnsupportedEncodingException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException {
            SecretKey key = generateKey(salt, passphrase);
            byte[] encrypted = doFinal(Cipher.ENCRYPT_MODE, key, iv, plaintext.getBytes(StandardCharsets.UTF_8));
            return base64(encrypted);
    }

    public String decrypt(String salt, String iv, String passphrase, String ciphertext) throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException, IllegalBlockSizeException, InvalidKeyException, InvalidAlgorithmParameterException, BadPaddingException, UnsupportedEncodingException {
            SecretKey key = generateKey(salt, passphrase);
            byte[] decrypted = doFinal(Cipher.DECRYPT_MODE, key, iv, base64(ciphertext));
            return new String(decrypted, StandardCharsets.UTF_8);
    }

    private byte[] doFinal(int encryptMode, SecretKey key, String iv, byte[] bytes) throws BadPaddingException, IllegalBlockSizeException, DecoderException, InvalidAlgorithmParameterException, InvalidKeyException {
            cipher.init(encryptMode, key, new IvParameterSpec(hex(iv)));
            return cipher.doFinal(bytes);
    }

    private SecretKey generateKey(String salt, String passphrase) throws NoSuchAlgorithmException, DecoderException, InvalidKeySpecException {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(passphrase.toCharArray(), hex(salt), iterationCount, keySize);
            SecretKey key = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
            return key;
    }

    public static String random(int length) {
        byte[] salt = new byte[length];
        new SecureRandom().nextBytes(salt);
        return hex(salt);
    }

    private static String base64(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    private static byte[] base64(String str) {
        return Base64.decodeBase64(str);
    }

    private static String hex(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    private static byte[] hex(String str) throws DecoderException {
            return Hex.decodeHex(str.toCharArray());
    }

    private IllegalStateException fail(Exception e) {
        return new IllegalStateException(e);
    }
}
