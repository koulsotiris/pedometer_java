import java.beans.DesignMode;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import javax.management.RuntimeErrorException;

public class WorkerThread extends Thread{
    public WorkerThread(){
        
    }
    
    
    public void run(){
        Worker mastoras = new Worker();
        //epeksergasia tou chunk kai ths sindeshs
        ObjectOutputStream out= null ;
        ObjectInputStream in = null ;
        Socket requestSocket= null ;
        try {
            String host = "192.168.1.40";
            /* Create socket for contacting the WorkerServer on port 4322*/
            requestSocket = new Socket(host, 4322);
            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());
            synchronized(requestSocket){
                try{
                    while(in.readObject()==null){
                        requestSocket.wait();
                    }
                }catch (Exception e) {
                    System.out.println(e);
                }
            }
            Pair<String,ArrayList<ArrayList<String>>> chunk = (Pair<String,ArrayList<ArrayList<String>>>) in.readObject();
            //String strt = (String) in.readObjetc();
            Pair<String, ArrayList<Float>> result = mastoras.workerjob(chunk);
            //String result = "ENDIAMESES TIMES";
            out.writeObject(result);
            out.flush();
            //kleinei i sindesi efoson eksipiretithike to request
            //ksekinaei kainourgio thread gia na ilopoisei allo request 
            new WorkerThread().start();
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close(); out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
        
    
} 