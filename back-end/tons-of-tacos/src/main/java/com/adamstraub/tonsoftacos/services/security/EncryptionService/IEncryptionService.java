package com.adamstraub.tonsoftacos.services.security.EncryptionService;

public interface IEncryptionService {
     String encrypt(String string);
    String decrypt(String encodedString);
}
