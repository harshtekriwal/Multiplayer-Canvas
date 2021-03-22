package User;

import javax.xml.crypto.Data;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class UserDetails {
    private String username;
    private String password;
    private DataInputStream input;
    private DataOutputStream output;
    public UserDetails(String username, String password, DataInputStream input, DataOutputStream output){
        this.username=username;
        this.password=password;
        this.input=input;
        this.output=output;
    }
    public DataInputStream getDataInputStream(){
        return input;
    }
    public DataOutputStream getDataOutputStream(){
        return output;
    }
    public String getUsername(){
        return username;
    }
}
