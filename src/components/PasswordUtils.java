package components;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



/* Utility class for password hashing and verification with backward compatibility.
 *    Uses SHA-256 hashing with email as salt.
 *    
 *    https://www.baeldung.com/sha-256-hashing-java
 *    https://stackoverflow.com/questions/69791042/how-to-validate-user-password-after-hashing-using-sha-256-salted-in-java
 * 
 */


public class PasswordUtils {
    // Hash a password using SHA-256 with a simple salt (email). Returns hex string.
    public static String hashPassword(String password, String salt) {
        if (password == null) return null;
        if (salt == null) salt = "";
        String input = password + salt;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashed);
        } catch (NoSuchAlgorithmException e) {
            // Fallback: return plain password 
            return password;
        }
    }

    // Verify password with backward compatibility: accepts either stored plain text or hashed value
    public static boolean verifyPassword(String candidatePassword, String storedPassword, String salt) {
        if (candidatePassword == null || storedPassword == null) return false;
        // First check plain equality 
        if (storedPassword.equals(candidatePassword)) return true;
        // Then check hashed equality
        String hashedCandidate = hashPassword(candidatePassword, salt);
        return storedPassword.equals(hashedCandidate);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
