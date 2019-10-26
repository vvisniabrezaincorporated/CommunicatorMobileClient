package pl.wnb.communicator.model;

import java.io.Serializable;
import java.util.Arrays;

public class KeyDetails implements Serializable {
    private String email;
    private byte[] publicKey;

    public KeyDetails(String email, byte[] publicKey) {
        this.email = email;
        this.publicKey = publicKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "KeyDetails{" +
                "email='" + email + '\'' +
                ", publicKey=" + Arrays.toString(publicKey) +
                '}';
    }
}
