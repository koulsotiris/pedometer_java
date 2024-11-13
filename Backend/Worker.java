import java.lang.Math;
import java.util.ArrayList;

public class Worker{
    Worker(){

    }
    public Pair<String, ArrayList<Float>> workerjob(Pair<String, ArrayList<ArrayList<String>>> packet) {
        ArrayList<ArrayList<String>> paketo = packet.getValue();
    
        // Extract elevation values
        float ele1 = Float.parseFloat(paketo.get(0).get(2));
        float ele2 = Float.parseFloat(paketo.get(1).get(2));
        float ele3 = Float.parseFloat(paketo.get(2).get(2));
        float sele1 = Math.max(0, ele2 - ele1); // Only consider positive elevation differences
        float sele2 = Math.max(0, ele3 - ele2); // Only consider positive elevation differences
        float totalElevation = sele1 + sele2;
    
        // Extract time values
        String time1 = paketo.get(0).get(3);
        String time2 = paketo.get(2).get(3);
        String time3 = paketo.get(1).get(3);
        String numbersOnly1 = time1.replaceAll("\\D", "");
        String numbersOnly2 = time2.replaceAll("\\D", "");
        String numbersOnly3 = time3.replaceAll("\\D", "");
    
        float year1 = Float.parseFloat(numbersOnly1.substring(0, 4));
        float month1 = Float.parseFloat(numbersOnly1.substring(4, 6));
        float day1 = Float.parseFloat(numbersOnly1.substring(6, 8));
        float hour1 = Float.parseFloat(numbersOnly1.substring(8, 10));
        float minute1 = Float.parseFloat(numbersOnly1.substring(10, 12));
        float second1 = Float.parseFloat(numbersOnly1.substring(12, 14));
    
        float year2 = Float.parseFloat(numbersOnly2.substring(0, 4));
        float month2 = Float.parseFloat(numbersOnly2.substring(4, 6));
        float day2 = Float.parseFloat(numbersOnly2.substring(6, 8));
        float hour2 = Float.parseFloat(numbersOnly2.substring(8, 10));
        float minute2 = Float.parseFloat(numbersOnly2.substring(10, 12));
        float second2 = Float.parseFloat(numbersOnly2.substring(12, 14));
    
        float year3 = Float.parseFloat(numbersOnly3.substring(0, 4));
        float month3 = Float.parseFloat(numbersOnly3.substring(4, 6));
        float day3 = Float.parseFloat(numbersOnly3.substring(6, 8));
        float hour3 = Float.parseFloat(numbersOnly3.substring(8, 10));
        float minute3 = Float.parseFloat(numbersOnly3.substring(10, 12));
        float second3 = Float.parseFloat(numbersOnly3.substring(12, 14));
    
        // Calculate time differences
        float syear = (year2 - year1) * 31536000 + (month2 - month1) * 2628288 + (day2 - day1) * 86400 +
                      (hour2 - hour1) * 3600 + (minute2 - minute1) * 60 + (second2 - second1);
    
        float syear2 = (year3 - year2) * 31536000 + (month3 - month2) * 2628288 + (day3 - day2) * 86400 +
                       (hour3 - hour2) * 3600 + (minute3 - minute2) * 60 + (second3 - second2);
    
        float totalSeconds = syear + syear2;
    
        // Extract latitude and longitude values
        float lat1 = Float.parseFloat(paketo.get(0).get(0));
        float lon1 = Float.parseFloat(paketo.get(0).get(1));
    
        float lat2 = Float.parseFloat(paketo.get(1).get(0));
        float lon2 = Float.parseFloat(paketo.get(1).get(1));
    
        float lat3 = Float.parseFloat(paketo.get(2).get(0));
        float lon3 = Float.parseFloat(paketo.get(2).get(1));
    
        // Calculate distances
        float d1 = calculateDistance(lat3, lon3, lat2, lon2);
        float d2 = calculateDistance(lat2, lon2, lat1, lon1);
        float totalDistance = d1 + d2;
    
        // Calculate speed
        float speed = totalDistance / totalSeconds;
    
        // Create the result ArrayList
        ArrayList<Float> result = new ArrayList<>();
        result.add(totalDistance);
        result.add(speed);
        result.add(totalElevation);
        result.add(totalSeconds);
    
        // Create and return the Pair
        return new Pair<>(packet.getKey(), result);
    }
    
    // Helper method to calculate distance using Haversine formula
    private float calculateDistance(float lat1, float lon1, float lat2, float lon2) {
        double earthRadius = 6371; // Radius of the Earth in kilometers
    
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
    
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
    
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    
        // Distance in kilometers
        double distance = earthRadius * c;
    
        // Convert distance to meters
        return (float) (distance * 1000);
    }
    
}

