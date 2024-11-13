import javax.sound.sampled.SourceDataLine;
import java.lang.Thread;
import java.util.ArrayList;
public class App{
    /*YPARXOUN SXOLIA GIA NA VOITHITHEI O DIORTHOTIS
      H VASIKH MAS MAIN EINAI H App.java
      KAI YPARXOUN H Workers.java KAI H Client.java
    */
    Pair<String, ArrayList<ArrayList<ArrayList<String>>>> chunks = new Pair(null,null) ;
    private static int sinolikos_xronos , sinoliki_anavasi, sinoliki_apostasi, numOfGpxs = 0;
    //ARRAYLIST GIA TA STATISTIKA ANA XRHSTH POY DEN XRHSIMOPOIH8HKE EN TELEI VLEPE LINE:91
    //private static ArrayList<Pair<String, ArrayList<Float>>> ClientStats = new ArrayList<Pair<String, ArrayList<Float>>>();
    public static void main(String args[]){
        ServerThreads workerServer = new ServerThreads(1); //Thread gia to WorkerServer
        workerServer.start();
        ServerThreads gpxServer = new ServerThreads(0); //Thread gia ta gpxServer
        gpxServer.start();
        Chunks funct = new Chunks();
        int totalchunks=0;
        while(true){
            numOfGpxs++;
            //System.out.println("Numofgpxs : "+numOfGpxs);
            ArrayList< Pair<String, ArrayList<Float>>> Mapp = new ArrayList<>();
            ArrayList<Pair<String , Integer>> NumberofChunks = new ArrayList<>();
            for(int i = 0 ; i < gpxServer.threadsGpx.size(); i ++){
                //xreiazetai oste na exei prolavei na to lavei to thread ActionsForGpx
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
                String gpx = gpxServer.threadsGpx.get(i).get_gpx();
                String num = gpxServer.threadsGpx.get(i).get_num();
                //chunk ta wpt
                Pair<String, ArrayList<ArrayList<ArrayList<String>>>> chunks = funct.Chunking(gpx,num);
                Pair<String, Integer> nochunks = new Pair<>(chunks.getKey(),((chunks.getValue())).size());
                NumberofChunks.add(nochunks);
                totalchunks = totalchunks + nochunks.getValue();
                /////ROUND ROBIN /////
                int j=0;
                for(ArrayList<ArrayList<String>> k : chunks.getValue()){
                    if (j>=workerServer.threadsWorkers.size()){
                        j=0;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println(e);
                    }
                    workerServer.threadsWorkers.get(j).set_chunk(new Pair<>(chunks.getKey(), k)); 
                    j=j+1;
                }
                //workerServer.threadsWorkers.get(j).set_chunk(Pair<gpx,chunks.getValue().get(j)>);
            }
            try {
                    Thread.sleep(1000);
            } catch (InterruptedException e) {
                    System.out.println(e);
            }   
            //pairnw to results apo tous workers
            ArrayList< Pair<String, ArrayList<Float>>> results = new ArrayList<>();
            ArrayList< Pair<String, ArrayList<Float>>> resoneworker= new ArrayList<>();
            int j=0;
            while(results.size()<totalchunks && j < workerServer.threadsWorkers.size() ){
                resoneworker= workerServer.threadsWorkers.get(j).get_result();
                for(Pair<String, ArrayList<Float>> iter : resoneworker){
                    results.add(iter);
                }
                j=j+1;
            }
            //REDUCE
            for(Pair<String, ArrayList<Float>> res : results){
                Mapp = funct.Reduce( res , Mapp ) ;
            }
            if(gpxServer.threadsGpx.size()!=0){
            for(Pair<String, ArrayList<Float>> kei : Mapp ){
                (kei.getValue()).set(1 , (((kei.getValue()).get(1)))/results.size());
                sinoliki_apostasi += Mapp.get(0).getValue().get(0);
                sinoliki_anavasi += Mapp.get(0).getValue().get(2);
                sinolikos_xronos += Mapp.get(0).getValue().get(3);
                gpxServer.threadsGpx.get(Integer.parseInt(kei.getKey())-1).set_result_averages( (float)sinoliki_anavasi/numOfGpxs, (float)sinoliki_apostasi/numOfGpxs, (float) sinolikos_xronos/numOfGpxs);
                gpxServer.threadsGpx.get(Integer.parseInt(kei.getKey())-1).set_result(kei.getValue()); 
                gpxServer.threadsGpx.remove(Integer.parseInt(kei.getKey())-1);
            }}
            
            
            
            
            //Edw exoume prospa8hsei na kanoume ypologismo statistikwn xrhsth
            /*if (ClientStats.isEmpty()){
                Pair<String , ArrayList <Float>> pair3 = new Pair<>(funct.getGPXCreator(Mapp.getKey()), Mapp.getValue());
                ClientStats.add( pair3 );//Pair<String , ArrayList <Float>> );
             }
             else{
                boolean f = false;
                for (Pair<String, ArrayList<Float>> x : ClientStats){
                    String comp = funct.getGPXCreator(Mapp.getKey());
                    if ( x == comp){
                        f=true;
                        (x.getValue()).set(0 , ( (x.getValue()).get(0) + (x.getValue()).get(0)/ClientStats.size() ) );
                        (x.getValue()).set(1 , (  (x.getValue()).get(1) + (x.getValue()).get(1)/ClientStats.size() ) );
                        (x.getValue()).set(2 , ( (x.getValue()).get(2) + (x.getValue()).get(2)/ClientStats.size() ) );
                    }
                    
                }        if (f==false){
                    ClientStats.add(reduced_timh);
                }
            }*/
        }
    }
}