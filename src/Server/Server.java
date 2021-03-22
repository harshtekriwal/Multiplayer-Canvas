package Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    public static void main(String []args){

    }
    public static void startServer() throws IOException{
        HashMap<String, Socket> users=new HashMap<>();
        ServerSocket server = new ServerSocket(5000);
        while(true){
            Socket s = null;
            try{
                s=server.accept();
                DataInputStream input = new DataInputStream(s.getInputStream());
                DataOutputStream output= new DataOutputStream(s.getOutputStream());
                String choice = input.readUTF();
                if(choice.equals("Register")){
                    String username=input.readUTF();
                    String password=input.readUTF();
                    output.writeUTF("Congrats");
                }
                else{
                    String username=input.readUTF();
                    String password=input.readUTF();
                    output.writeUTF("Congrats");
                }
            }
            catch (Exception E){
                s.close();
            }
        }

    }
}
