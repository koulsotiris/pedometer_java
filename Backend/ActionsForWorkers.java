import java.io.*;
import java.util.ArrayList;
import javax.swing.text.Segment;
import java.net.*;
public class ActionsForWorkers extends Thread {
    ObjectInputStream in;
    ObjectOutputStream out;
    boolean flag = !false;
    boolean flag2 = !false;
    Socket socket;

    ArrayList< Pair<String, ArrayList<Float>>> res = new ArrayList<>();

    public Object lock = new Object();
    Pair<String, ArrayList<ArrayList<String>>> chunk = null;
    Pair<String, ArrayList<Float>> result = null;
    public ActionsForWorkers(Socket connection) {
        try {
            socket = connection;
            out = new ObjectOutputStream(connection.getOutputStream());
            in = new ObjectInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        try{ 
           synchronized(lock){
                try {
                    while(flag){
                        lock.wait();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
            //notify ton worker
            synchronized(socket){
                socket.notify();
                String tst = new String("tst");
                out.writeObject(tst);
            }
            //stelnoume chunk
            out.writeObject(chunk);
            out.flush();
            //pairnoume result
            result = (Pair<String, ArrayList<Float>>) in.readObject();
            res.add(result);
            //mexri na kalesoume tin get_result gia na parei to apotelesma prin pethanei to thread
            synchronized(lock){
                try {
                    while(flag2){
                        lock.wait();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
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
    public ArrayList< Pair<String, ArrayList<Float>>> get_result(){
         synchronized(lock){
            lock.notify();
        }
        flag2=!flag2;
        return res;
    }

    public void set_chunk(Pair<String,ArrayList<ArrayList<String>>> degvar){
        synchronized(lock){
            lock.notify();
        }
        chunk = degvar;
        flag = !flag;
    }
}