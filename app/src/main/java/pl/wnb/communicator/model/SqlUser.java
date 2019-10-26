package pl.wnb.communicator.model;

public class SqlUser {
    private String username;
    private String loggedUsername;
    private String email;
    private String password;
    private String privateKey;
    private String publicKey;

    public SqlUser(String username, String loggedUsername, String email, String password, String privateKey, String publicKey) {
        this.username = username;
        this.loggedUsername = loggedUsername;
        this.email = email;
        this.password = password;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLoggedUsername() {
        return loggedUsername;
    }

    public void setLoggedUsername(String loggedUsername) {
        this.loggedUsername = loggedUsername;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public String toString() {
        return "SqlUser{" +
                "username='" + username + '\'' +
                ", loggedUsername='" + loggedUsername + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                '}';
    }
}
