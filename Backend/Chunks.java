import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import java.io.StringReader;
public class Chunks {
    Chunks(){

    }
// Diavazei to Gpx kai to metatrepei se string
    public String ReadGpx(String route){
      String line;
      BufferedReader reader = null;
      String gpxstr = "";
      try{
      reader = new BufferedReader(new FileReader(route)); // dimiourgia reader
      line = reader.readLine();
      outerloop:
      while ((line != null)) {
        gpxstr = gpxstr + line;
        line = reader.readLine();
      }
        reader.close();}
      catch (IOException e) { // exception
              System.out.println	("Error reading line ...");
              System.out.println(e.toString());
          }
        return gpxstr;
      }
    // metatrepei to parapanw string se ena pair me to gpx san key kai to array me tis times san value
    public Pair<String, ArrayList<ArrayList<ArrayList<String>>>> Chunking(String gpx, String numb){
        String lati = null ;
        ArrayList<ArrayList<String>> chunk = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<ArrayList<String>>> chunks = new ArrayList<ArrayList<ArrayList<String>>>();
        String lont = null ;
        String elem = null ;
        String timer = null;
        String line = gpx;
        int counter = 0 ;
        int counter2 = 0 ;
        int indexer=gpx.indexOf("<wpt") ;
        int indexer3= 0 ;
        
        while (indexer3<gpx.indexOf("</gpx>")-6){

                lati=line.substring(line.indexOf("lat=", indexer)+5, line.indexOf("lon", indexer)-2);
                
              
                lont=line.substring(line.indexOf("lon=", indexer)+5, line.indexOf(">", indexer)-1);
                
    
                elem= line.substring(line.indexOf("<ele>", indexer)+5, line.indexOf("</ele>", indexer));               
                timer= line.substring(line.indexOf("<time>", indexer)+6, line.indexOf("</time>", indexer));
                ArrayList<String> waypoint = new ArrayList<String>();
                waypoint.add(lati);
                waypoint.add(lont);
                waypoint.add(elem);
                waypoint.add(timer);
                counter++;
                counter2++;
                chunk.add(waypoint);
                if(counter2 == 3) {
                  ArrayList<ArrayList<String>> chunk2 = (ArrayList<ArrayList<String>>)chunk.clone();
                    chunks.add(chunk2);
                    chunk.clear();
                    counter2 = 1;
                    chunk.add(waypoint);
                  }  
                int indexer2=gpx.indexOf("<wpt", indexer+1) ;
                indexer= indexer2;
                int indexer4=gpx.indexOf("</wpt>", indexer3+1) ;
                indexer3= indexer4;
              
            }
            Pair<String, ArrayList<ArrayList<ArrayList<String>>>> pair = new Pair<>(numb, chunks);
            return pair;
             
        }
        

        
        
        //pragmatopoiei mia reduced arraylist gia kathe result pou tou dinete 
        public ArrayList< Pair<String, ArrayList<Float>>> Reduce(Pair<String, ArrayList<Float>> results  , ArrayList< Pair<String, ArrayList<Float>>> Mapped ){
          boolean flag= false;
          for(Pair<String, ArrayList<Float>> kei : Mapped){
            if (kei.getKey().equals(results.getKey())){
              flag = true;
              (kei.getValue()).set(0 , ((kei.getValue()).get(0)) + ((results.getValue()).get(0)));
              (kei.getValue()).set(1 , (((kei.getValue()).get(1)) + ((results.getValue()).get(1))));
              (kei.getValue()).set(2 , ((kei.getValue()).get(2)) + ((results.getValue()).get(2)));
              (kei.getValue()).set(3 , ((kei.getValue()).get(3)) + (results.getValue()).get(3));
              break;
            }
          }
          if (flag == false){
             
            Pair<String, ArrayList<Float>> pair2 = new Pair<>(results.getKey(), results.getValue());
            Mapped.add(pair2);           
          }
          return Mapped;
        }

        public static String getGPXCreator(String gpxText) throws Exception {
          // Parse the GPX text as an XML document
          Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder()
                         .parse(new InputSource(new StringReader(gpxText)));
          
          // Get the root element of the GPX document
          Element root = doc.getDocumentElement();
          
          // Get the value of the "creator" attribute of the root element
          String creator = root.getAttribute("creator");
          
          return creator;
        }
         
}
