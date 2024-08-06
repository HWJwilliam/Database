package cs.toronto.edu.models;

public class Account {

    // Attributes
    public String username;
    public String password;

    // Initialisation
    public Account(String username, String password){
        this.username = username;
        this.password = password;
    }

    // Getter and setter
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
