package com.vergilyn.examples.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MessageDigest Algorithms
 * The algorithm names in this section can be specified when generating an instance of MessageDigest.
 * Algorithm Name	Description
 * MD2 The MD2 message digest algorithm as defined in RFC 1319.
 * MD5 The MD5 message digest algorithm as defined in RFC 1321.
 * SHA-1
 * SHA-256
 * SHA-384
 * SHA-512
 * Hash algorithms defined in the FIPS PUB 180-2.
 * SHA-256 is a 256-bit hash function intended to provide 128 bits of security against collision attacks,
 * while SHA-512 is a 512-bit hash function intended to provide 256 bits of security.
 * A 384-bit hash may be obtained by truncating the SHA-512 output.
 *
 * @author VergiLyn
 * 2016年10月12日
 */
public class FileHashUtil {
    //Algorithms枚举
    public static enum MessageDigestAlgorithms {
        MD2("MD2"), MD5("MD5"), SHA1("SHA-1"), SHA256("SHA-256"), SHA384("SHA-384"), SHA512("SHA-512");
        public String value;

        MessageDigestAlgorithms(String name) {
            this.value = name;
        }
    }

    public static String getFileHash(String filename, String algorithm) throws IOException, NoSuchAlgorithmException {
        byte[] b = createChecksum(filename, algorithm);
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < b.length; i++) {
            result.append(Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    private static byte[] createChecksum(String filename, String algorithm) throws IOException, NoSuchAlgorithmException {
        byte[] buffer = new byte[1024];

        try(InputStream fis = new FileInputStream(filename)) {
            MessageDigest complete = MessageDigest.getInstance(algorithm);
            int numRead;
            do {
                numRead = fis.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            return complete.digest();
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = System.getProperty("user.dir") + "/.gitignore";

        MessageDigestAlgorithms[] algorithms = {MessageDigestAlgorithms.MD5, MessageDigestAlgorithms.SHA1, MessageDigestAlgorithms.SHA256};
        for (MessageDigestAlgorithms algorithm : algorithms) {
            System.out.println(algorithm.value + ": " + getFileHash(filePath, algorithm.value));
        }
    }
}

