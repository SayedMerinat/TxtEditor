package txt_editor;

public class VigenereCipher {
    public static String encrypt(String plaintext, String key) {
        StringBuilder ciphertext = new StringBuilder();
        int keyLength = key.length();
        for (int i = 0, j = 0; i < plaintext.length(); i++) {
            char plainChar = plaintext.charAt(i);
            if (isBasicLatinAlphabet(plainChar)) {
                char keyChar = key.charAt(j % keyLength);
                int shift = (Character.toUpperCase(plainChar) - 'A' + Character.toUpperCase(keyChar) - 'A') % 26;
                char encryptedChar = (char) (Character.isUpperCase(plainChar) ? ('A' + shift) : ('a' + shift));
                ciphertext.append(encryptedChar);
                j++;
            } else {
                ciphertext.append(plainChar);
            }
        }
        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext, String key) {
        StringBuilder plaintext = new StringBuilder();
        int keyLength = key.length();
        for (int i = 0, j = 0; i < ciphertext.length(); i++) {
            char encryptedChar = ciphertext.charAt(i);
            if (isBasicLatinAlphabet(encryptedChar)) {
                char keyChar = key.charAt(j % keyLength);
                int shift = (Character.toUpperCase(encryptedChar) - 'A' - (Character.toUpperCase(keyChar) - 'A') + 26) % 26;
                char decryptedChar = (char) (Character.isUpperCase(encryptedChar) ? ('A' + shift) : ('a' + shift));
                plaintext.append(decryptedChar);
                j++;
            } else {
                plaintext.append(encryptedChar);
            }
        }
        return plaintext.toString();
    }

    public static boolean isBasicLatinAlphabet(char c) {
        return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z');
    }

    public static void main(String[] args) {
        String plaintext = "Ceci est un test, éàè öäü {}[]()";
        String key = "KEY";
        
        // Encryption
        String encryptedText = encrypt(plaintext, key);
        System.out.println("Encrypted: " + encryptedText);

        // Decryption
        String decryptedText = decrypt(encryptedText, key);
        System.out.println("Decrypted: " + decryptedText);
    }
}
