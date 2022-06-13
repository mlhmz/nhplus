package utils;

import at.favre.lib.crypto.bcrypt.BCrypt;

/**
 * A util to check hash a password and validate it.
 * The reason to use this util is to make sure passwords are safe in the database, in case of a database breach.
 */
public class PasswordHashUtil {
    /**
     * hashes password for the database
     *
     * @param password the password to be hashed
     * @return String with the Password
     */
    public static String hashPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * Checks if Password-Hash is valid to the users one
     *
     * @param password entered password
     * @param hash the hash that is in the database
     * @return boolean if password is valid
     */
    public static boolean isValidPassword(String password, String hash) {
        return BCrypt.verifyer().verify(password.toCharArray(), hash).verified;
    }
}
