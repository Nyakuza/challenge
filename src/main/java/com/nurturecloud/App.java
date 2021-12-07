package com.nurturecloud;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.google.gson.Gson; 


public class App {



    public final int INITIAL_SIZE = 16;

    public static void main(String[] args) {


        Scanner command = new Scanner(System.in);
        boolean running = true;
        Suburb[] masterSuburbArray = null;
        List<Suburb> closeSuburbList = new ArrayList<Suburb>();
        List<Suburb> fringeSuburbList = new ArrayList<Suburb>();
        JSONParser jsonParser = new JSONParser();
        DecimalFormat decimalFormatter = new DecimalFormat("#.##");
         
        try (FileReader reader = new FileReader("src/main/resources/aus_suburbs.json"))
        {
            //Read JSON file
            Object obj = jsonParser.parse(reader);
            JSONArray suburbList = (JSONArray) obj;
            Gson gson = new Gson();
            masterSuburbArray = gson.fromJson(String.valueOf(suburbList), Suburb[].class); 
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    

        
        while(running){

            
            System.out.print("Please enter a suburb name: ");
            String suburbName = command.nextLine();
            int postCodeInput = -1;

            while ( postCodeInput < 0 || postCodeInput > 9999) {
                System.out.print("Please enter the postcode: ");
                if (command.hasNextInt()) {
                    postCodeInput = command.nextInt();
                } else {
                    command.nextLine();     // closes previous input 
                    System.out.println("Invalid Postcode");
                }
                command.nextLine();         // closes previous input
            } 

            Suburb originSuburb = findByLocalityAndPostCode(Arrays.asList(masterSuburbArray), suburbName.toUpperCase(), postCodeInput);

            if (originSuburb != null) {
                for (Suburb targetSuburb : masterSuburbArray) {
                    if((targetSuburb.getLatitude() != null && targetSuburb.getLongitude() != null) && targetSuburb != originSuburb) {
                        Double distance = CalculateDistance(originSuburb.getLatitude(), targetSuburb.getLatitude(), originSuburb.getLongitude() , targetSuburb.getLongitude());
                            targetSuburb.setDistance(distance);
                       
                        if (distance < 10 & closeSuburbList.size() < 15 ) {                            

                            if (closeSuburbList.size() == 0) {
                                closeSuburbList.add(targetSuburb);
                            } else {   
                                for (int i = 0; i < closeSuburbList.size(); i++) {
                                    if (targetSuburb.getDistance() < closeSuburbList.get(i).getDistance()) {
                                        closeSuburbList.add(i,targetSuburb);
                                        break;
                                    }
                                }
                            }    

                        } else if (distance < 50 & fringeSuburbList.size() < 15) {
                            
                            if (fringeSuburbList.size() == 0) {
                                fringeSuburbList.add(targetSuburb);
                            } else {   
                                for (int i = 0; i < fringeSuburbList.size(); i++) {
                                    if (targetSuburb.getDistance() < fringeSuburbList.get(i).getDistance()) {
                                        fringeSuburbList.add(i,targetSuburb);
                                        break;
                                    }
                                }
                            }    
                        }
                    }
                }
                
            
                System.out.println("Origin: " + originSuburb.getLatitude() + " " + originSuburb.getLongitude());

                System.out.println("Close suburbs: ");
                for(Suburb suburb : closeSuburbList) {
                    System.out.println(suburb.getLocality() + " " + suburb.getLatitude() + " " + suburb.getLongitude() + " Distance: " + decimalFormatter.format(suburb.getDistance()) + "km");
                }

                System.out.println("\nFringe suburbs: ");
                for(Suburb suburb : fringeSuburbList) {
                    System.out.println(suburb.getLocality() + " " + suburb.getLatitude() + " " + suburb.getLongitude() + " Distance: " + decimalFormatter.format(suburb.getDistance()) + "km");
                }

                closeSuburbList = null;
                fringeSuburbList = null;

            } else {
                System.out.println(String.format("Nothing found for %s, %s!!\n", suburbName, postCodeInput));
            }
        }


        command.close();       
         
        
    }
  
 
    public static double CalculateDistance(double lat1, double lat2, double lon1, double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }


    public static Suburb findByLocalityAndPostCode(Collection<Suburb> suburbList, String targetLocality, int targetPostalCode) {
        return suburbList.stream().filter(suburb -> targetLocality.equals(suburb.getLocality())).filter(suburb -> targetPostalCode == suburb.getPcode()).findFirst().orElse(null);
    }

   
}
