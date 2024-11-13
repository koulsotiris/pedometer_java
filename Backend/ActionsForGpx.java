import java.io.*;
import java.net.*;
import java.lang.Integer;
import java.util.ArrayList;
import javax.swing.plaf.synth.SynthCheckBoxMenuItemUI;
 
public class ActionsForGpx extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    boolean stop = false;
    ArrayList<Float> results = null;
    Object lock = new Object();
    String gpx = null;
    Socket socket;
    boolean flag = !false;
    float mesi_anavasi,mesi_apostasi,mesos_xronos = 0;

    //To noumero tou thread tha mpei os kleidi
    static int num = 0;
    public ActionsForGpx(Socket connection) {
        try {
            socket = connection;
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
            num++; 
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    public void run() {
        try{
            //lamvanoume gpx
            gpx = (String) in.readObject();
            //tha mpainei se wait mexri na teleiosei i epksergasia kai to reduce
            synchronized(lock){
                while(flag){
                    try{
                        lock.wait();
                    } catch(InterruptedException e){
                        System.out.println(e);
                    }
                }
            }
            synchronized(socket){
                socket.notify();
                String tst = new String("tst");
                out.writeObject(tst);
            }
            for( float x : results){
                System.out.println(x);
            }
            out.writeObject(results);
            out.flush();
            out.writeObject(mesi_apostasi);
            out.flush();
            out.writeObject(mesi_anavasi);
            out.flush();
            out.writeObject(mesos_xronos);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);// need it in readObject 
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }
    
    public String get_gpx(){
        return gpx;
    }
    public String get_num(){
        return Integer.toString(num);
    }

    public void set_result_averages(float an , float ap, float xr){
        mesos_xronos = xr;
        mesi_anavasi=an;
        mesi_apostasi = ap;
    }
    public void set_result(ArrayList<Float> deg){
        results = deg;
        synchronized(lock){
            lock.notify();
        }
        flag = !flag;
    }
}