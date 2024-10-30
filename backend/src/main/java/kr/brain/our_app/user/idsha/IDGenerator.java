package kr.brain.our_app.user.idsha;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IDGenerator {
    public String generateIdFromEmail(String email)throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(email.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hashBytes);
    }
    private String bytesToHex(byte[] hashBytes){
        StringBuilder retString = new StringBuilder();
        for(byte b : hashBytes){
            String hex = Integer.toHexString(b);
            if(hex.length() == 1)retString.append('0');
            retString.append(hex);
        }
        return retString.toString();
    }
}
