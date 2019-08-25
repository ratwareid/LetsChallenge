package com.ratwareid.letschallenge;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;

public class IDFactory {

    public static String generateUniqueKey(String prefix) throws NoSuchAlgorithmException {

        String ts = getTimeStamp();
        String combine = prefix.concat(ts);

        return hashMD5(combine);
    }

    public static String hashMD5(String input) throws NoSuchAlgorithmException {
        try {

            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getTimeStamp(){
        Date date= new Date();

        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        return ts.toString();
    }
}
