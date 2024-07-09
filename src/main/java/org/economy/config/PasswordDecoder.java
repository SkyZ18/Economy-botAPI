package org.economy.config;

import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class PasswordDecoder {

    public String decodePassword(String password) {
        return new String(Base64.decodeBase64(password), StandardCharsets.UTF_8);
    }
}
