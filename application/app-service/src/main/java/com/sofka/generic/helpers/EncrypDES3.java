package com.sofka.generic.helpers;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class EncrypDES3 {

  private final SecretKey deskey;
  private final Cipher c;
  private byte[] cipherByte;

  public EncrypDES3() throws NoSuchAlgorithmException, NoSuchPaddingException {
    KeyGenerator keygen = KeyGenerator.getInstance("DESede");
    deskey = keygen.generateKey();
    c = Cipher.getInstance("DESede");
  }


  public byte[] Encrytor(String str) throws InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException {
    c.init(Cipher.ENCRYPT_MODE, deskey);
    byte[] src = str.getBytes();
    cipherByte = c.doFinal(src);
    return cipherByte;
  }

  public byte[] Decryptor(byte[] buff) throws InvalidKeyException,
      IllegalBlockSizeException, BadPaddingException {
    c.init(Cipher.DECRYPT_MODE, deskey);
    cipherByte = c.doFinal(buff);
    return cipherByte;
  }
}