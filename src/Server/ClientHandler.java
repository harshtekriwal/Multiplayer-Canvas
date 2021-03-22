package Server;

import Server.Database.DatabaseService;
import User.UserDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private boolean isSocketRunning;
    private  int portNo;
    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String username="";
    private ConcurrentHashMap<Socket, UserDetails> onlineUsers;
    DatabaseService databaseService= new DatabaseService();
    public ClientHandler(int portNo,Socket socket,ConcurrentHashMap<Socket,UserDetails> users) throws IOException {
        this.portNo=portNo;
        this.socket=socket;
        this.onlineUsers=users;
        this.isSocketRunning=true;
        this.inputStream=new DataInputStream(socket.getInputStream());
        this.outputStream=new DataOutputStream(socket.getOutputStream());
    }
    @Override
    public void run() {
        while(isSocketRunning) {
            String messageRecieved = "";
            try {
                messageRecieved=inputStream.readUTF();
                if(messageRecieved.charAt(0)=='L'){
                    String userName;
                    String password;
                    String[]splitLoginString = messageRecieved.split("-");
                    userName=splitLoginString[0].substring(1);
                    password=splitLoginString[1];
                    if(databaseService.isValidUser(userName,password)){
                        UserDetails user=new UserDetails(userName,password,inputStream,outputStream);
                        outputStream.writeUTF("LS");
                        onlineUsers.put(socket,user);
                        String listOnlineUsers="";
                        username=userName;
                        String usersList="";
                        for(UserDetails details : onlineUsers.values()){
                            usersList=usersList+details.getUsername();
                            usersList=usersList+"\n";
                        }
                        outputStream.writeUTF(usersList);
                    }
                    else{
                        outputStream.writeUTF("LF");
                    }
                }
                else
                    if(messageRecieved.charAt(0)=='R'){
                        String[] splitRegisterString = messageRecieved.split("-");
                        String userName=splitRegisterString[0].substring(1);
                        String password=splitRegisterString[1];
                        if(databaseService.doesUserExists(userName)){
                            outputStream.writeUTF("RF");
                            outputStream.flush();
                        }
                        else{
                            databaseService.registerUser(userName,password);
                            outputStream.writeUTF("RS");
                            outputStream.flush();
                        }
                    }
               else
                   if(messageRecieved.charAt(0)=='C'){
                       for(Map.Entry<Socket,UserDetails> entry : onlineUsers.entrySet()) {
                           if(entry.getKey() != socket) {
                               UserDetails userDetails = entry.getValue();
                               userDetails.getDataOutputStream().writeUTF(username + " : " + messageRecieved.substring(1));
                           }
                       }
                       System.out.println("Message : " + messageRecieved.substring(1));
                   }
                else{
                       System.out.println("this is a point message");
                       for(Map.Entry<Socket, UserDetails> entry : onlineUsers.entrySet()) {
                           System.out.println("Online users : " + entry.getValue().getUsername());
                           if(entry.getKey() != socket) {
                               UserDetails userDetails= entry.getValue();
                               userDetails.getDataOutputStream().writeUTF(messageRecieved);
                           }
                       }
                   }

            } catch (IOException e) {
                e.printStackTrace();
                isSocketRunning = false;
                System.out.println("Thread has stopped!");
                onlineUsers.remove(socket);

            }
        }
    }
}
