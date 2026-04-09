package com.adamstraub.tonsoftacos.services.security.EncryptionService;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class EncryptionService implements IEncryptionService{
    @Value("${BEGIN_KEY}")
    private int beginKey;
    @Value("${END_KEY}")
    private int endKey;
    @Value("${CHAR_MIN}")
    private int charMin;
    @Value("${CHAR_MAX}")
    private int charMax;
    @Value("${SUB_MIN}")
    private int subMin;
    @Value("${SUB_MAX}")
    private int subMax;
    @Value("${EX1}")
    private int ex1;
    @Value("${EX2}")
    private int ex2;
    @Value("${EX3}")
    private int ex3;



    @Override
    public String encrypt(String string) {
        byte[] codeBytes = string.getBytes(StandardCharsets.UTF_8);
        List<Integer> rolledCodeBytes = new ArrayList<>();
        int codeByteValue;
        for (byte codeByte : codeBytes) {
            codeByteValue = codeByte;
            codeByteValue += beginKey;
            rolledCodeBytes.add(codeByteValue);
        }

//      new collection with altered char values
        List<Character> chars = new ArrayList<>();
        for (int integer : rolledCodeBytes) {
            chars.add((char) integer);
        }
//      for each element insert three new random chars
        for (int i = 0; i < chars.size(); i++) {
            chars.add(i, randomChar());
            i++;
            chars.add(i, randomChar());
            i++;
            chars.add(i, randomChar());
            i++;
        }
        chars.add(randomChar());
        chars.add(randomChar());
        chars.add(randomChar());

        StringBuilder encryptionBuilder = new StringBuilder(chars.size());
        for (Character ch : chars) {
            encryptionBuilder.append(ch);
        }
        return encryptionBuilder.toString();
    }

    @Override
    public String decrypt(String encodedString) {
        byte[] decodedBytes = getBytes(encodedString);
        int decodeByteValue;
        List<Character> decodedChars = new ArrayList<>();
        StringBuilder decrypt = new StringBuilder(0);
        for (byte codeByte : decodedBytes) {
            decodeByteValue = codeByte;
            decodeByteValue -= beginKey;
            decodedChars.add((char) decodeByteValue);
        }
        for (Character ch : decodedChars) {
            decrypt.append(ch);
        }
        return decrypt.toString();
    }

    private byte @NotNull [] getBytes(String encodedString) {
        String decodedStart = String.valueOf(encodedString.charAt(beginKey));
        String decodedEnd = String.valueOf(encodedString.charAt(encodedString.length() - endKey));
        String wholeDecoded = "";
        StringBuilder decoded = new StringBuilder();
        for (int i = beginKey; i < encodedString.length(); i = i + endKey) {
            decoded.append(encodedString.charAt(i));
        }
        decoded = new StringBuilder(decoded.substring(subMin, decoded.toString().length() - subMax));
        wholeDecoded = wholeDecoded.concat(decodedStart + decoded + decodedEnd);
        return wholeDecoded.getBytes(StandardCharsets.UTF_8);
    }


    private char randomChar() {
        int min = charMin, max = charMax;
        int random = (int) (Math.random() * ((max - min)) + min);
        int[] excluded = {ex1, ex2, ex3};
        char choice = 0;
        for (int ex : excluded) {
            choice = random == ex ? randomChar() : (char) random;
        }
        return choice;
    }
}
