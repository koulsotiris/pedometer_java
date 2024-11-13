import java.io.*;
import java.net.*;
import java.util.ArrayList;
 
public class ServerThreads extends Thread{
    int num;
    ServerSocket s;
    Socket providerSocket;
    public ArrayList <ActionsForWorkers> threadsWorkers = new ArrayList <ActionsForWorkers>();
    public ArrayList <ActionsForGpx> threadsGpx = new ArrayList <ActionsForGpx>();

    
    public ServerThreads(int x){
        num = x;
    }
    
    public void run(){
        if (num == 0){
            try {
 
                // Create Server Socket 
                s = new ServerSocket(4321);//the same port as before, 10 connections
                
                while (true) {
                    // Accept the connection 
                    providerSocket = s.accept();
                    // Handle the request 
                    ActionsForGpx t = new ActionsForGpx(providerSocket);
                    t.start();

                    threadsGpx.add(t);
                }
     
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    providerSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }else if(num == 1){
           try {
 
                /* Create Server Socket */
                s = new ServerSocket(4322);//the same port as before, 10 connections
                while(true) { 
                    /* Accept the connection */
                    providerSocket = s.accept();
                    /* Handle the request */
                    ActionsForWorkers t = new ActionsForWorkers(providerSocket);
                    t.start();
                    
                    threadsWorkers.add(t);
                
                }
     
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    providerSocket.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            
        }
    }
}