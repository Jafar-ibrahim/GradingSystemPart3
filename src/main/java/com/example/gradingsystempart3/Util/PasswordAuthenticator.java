package com.example.gradingsystempart3.Util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordAuthenticator {
    private static final int WORKLOAD_FACTOR = 12;

    public static String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(WORKLOAD_FACTOR));
    }
    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}