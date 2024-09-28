package io.github.RobsonFe.ManagerBookAPI.utils;

import java.security.SecureRandom;
import java.util.Base64;

// Para gerar chave de segurança 
public class SecrectKeyGenerator {

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256 bits
        random.nextBytes(bytes);
        String encodedKey = Base64.getEncoder().encodeToString(bytes);
        System.out.println("A sua Chave: " + encodedKey);
    }
}
