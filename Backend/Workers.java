import java.beans.DesignMode;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import javax.management.RuntimeErrorException;

public class Workers{

    public static void main(String [] args) {
        
            new WorkerThread().start();
    }

}