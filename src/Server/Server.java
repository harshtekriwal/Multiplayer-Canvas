package Server;

import User.UserDetails;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private boolean isServerRunning;
    private int counter;
    private int portNumber;
    private ConcurrentHashMap<Socket, UserDetails>users;
    public Server(int portNo){
        isServerRunning=false;
        counter=0;
        portNumber=portNo;
        users= new ConcurrentHashMap<>();
    }
    public void start() throws IOException{
        isServerRunning=true;
        ServerSocket serverSocket = new ServerSocket(portNumber);
        while(isServerRunning){
            System.out.println("Server Started!!!");
            Socket socket = serverSocket.accept();
            System.out.println("A new Client is Connected with address : " + socket.getRemoteSocketAddress());
            ClientHandler handler = new ClientHandler(portNumber,socket,users);
            Thread thread = new Thread(handler);
            thread.start();
        }
    }
    public static void main(String []args) throws IOException{
        Server server = new Server(5000);
        server.start();
    }
}
