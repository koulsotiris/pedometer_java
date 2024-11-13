import java.io.*;
import java.net.*;
import javax.management.RuntimeErrorException;
 import java.util.ArrayList;
public class Client extends Thread {
    ArrayList<Float> result = null;
    String path = null;
    float mesi_apostasi,mesi_anavasi,mesos_xronos = 0;
    Client(String txt) {
        path = txt;
    }
 
    public void run() {
        ObjectOutputStream out= null ;
        ObjectInputStream in = null ;
        Socket requestSocket= null ;
        try {
            String host = "localhost";
            /* Create socket for contacting the server on port 4321*/
            requestSocket = new Socket(host, 4321);

            /* Create the streams to send and receive data from server */
            out = new ObjectOutputStream(requestSocket.getOutputStream());
            in = new ObjectInputStream(requestSocket.getInputStream());

            String gpx = new Chunks().ReadGpx(path);
            //System.out.println("To gpx einai meta to ReadGpx : "+gpx);
            //stelnoume gpx
            out.writeObject(gpx);
            out.flush();
            synchronized(requestSocket){
                try {
                    while (in.readObject() == null) {
                        requestSocket.wait();
                    }                    
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            //to apotelesma
            result = (ArrayList<Float>) in.readObject();
            mesi_apostasi = (float) in.readObject();
            mesi_anavasi = (float) in.readObject();
            mesos_xronos = (float) in.readObject();
            //i ektiposi tou apotelesmatos
            System.out.println("H sinoliki apostasi einai : "+result.get(0));
            System.out.println("H sinoliki anavasi einai : "+result.get(2));
            System.out.println("H sinoliki taxitita einai : "+result.get(1));
            System.out.println("O sinolikos xronos einai : "+result.get(3));
            System.out.println("Mesi apostasi : "+mesi_apostasi+"mesi anavasi : "+mesi_anavasi+"mesos xronos : "+mesos_xronos);
        } catch (UnknownHostException unknownHost) {
            System.err.println("You are trying to connect to an unknown host!");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch(ClassNotFoundException e){
            System.err.println(e);
        } finally {
            try {
                in.close(); out.close();
                requestSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    public static void main(String [] args) {
        new Client("route1.gpx").start();
        //new Client().start();
    }
}