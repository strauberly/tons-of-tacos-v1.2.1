package com.adamstraub.tonsoftacos.services.security.EncryptionService;

import jakarta.annotation.PostConstruct;
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
    @Value("${EX4}")
    private int ex4;
    @Value("${EX5}")
    private int ex5;
    @Value("${EX6}")
    private int ex6;
    @Value("${EX7}")
    private int ex7;
    @Value("${EX7}")
    private int ex8;
    @Value("${EX9}")
    private int ex9;
    @Value("${EX10}")
    private int ex10;
    @Value("${EX11}")
    private int ex11;
    @Value("${EX12}")
    private int ex12;
    @Value("${EX13}")
    private int ex13;
    @Value("${EX14}")
    private int ex14;
    @Value("${EX15}")
    private int ex15;
    @Value("${EX16}")
    private int ex16;
    @Value("${EX17}")
    private int ex17;
    @Value("${EX18}")
    private int ex18;
    @Value("${EX19}")
    private int ex19;
    @Value("${EX20}")
    private int ex20;
    @Value("${EX21}")
    private int ex21;
    @Value("${EX22}")
    private int ex22;
    @Value("${EX23}")
    private int ex23;
    @Value("${EX24}")
    private int ex24;
    @Value("${EX25}")
    private int ex25;


    private int[] excluded;

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

    @PostConstruct
    public void init() {
        excluded = new int[] {ex1,ex2, ex3, ex4, ex5, ex6, ex7, ex8, ex9, ex10, ex11, ex12, ex13, ex14, ex15, ex16, ex17,
                ex18, ex19, ex20, ex21, ex22, ex23, ex24, ex25 };
    }

    private char randomChar() {
        int min = charMin, max = charMax;
        char choice;
        boolean isExcluded;

//        do {
//            choice =(char) ((int) (Math.random() * (max - min)) + min);
//            isExcluded = false;
//            for (int ex : excluded) {
//                if (choice == ex) {
//                    isExcluded = true;
//                    break;
//                }
//            }
//        }while (isExcluded);
//        return choice;
        //        String safeChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!#$%&'*+-.^_`|~";
        String safeChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int randomIndex = (int) (Math.random() * safeChars.length());
        return safeChars.charAt(randomIndex);
    }
}
