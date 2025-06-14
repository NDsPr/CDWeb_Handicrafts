package com.handicrafts.util;

import org.mindrot.jbcrypt.BCrypt;

public class EncryptPasswordUtil {
    // Sử dụng thư viện BCrypt
    public static String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String inputPassword, String hashedPassword) {
        return BCrypt.checkpw(inputPassword, hashedPassword);
    }
}