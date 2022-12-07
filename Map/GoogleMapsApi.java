package Map;

import com.dlsc.gmapsfx.GoogleMapView;
import com.dlsc.gmapsfx.javascript.object.LatLong;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Used to make Google Maps Api requests
 *
 * Reference from:
 * https://developers.google.com/maps/documentation/directions/get-directions#maps_http_directions_toronto_montreal-java
 * https://www.baeldung.com/java-read-json-from-url
 * @author Mico
 */

public class GoogleMapsApi {

    /**
     * Uses the Google Maps Directions Api to get a written set of instructions to get from origin to destination via
     * mode.
     *
     * The instructions are formatted with connecting words to be more readable
     *
     * @param origin a lat long seperated by a comma, ex. "43.66384509183421,-79.39576369024316"
     * @param destination a lat long seperated by a comma, ex. "43.66384509183421,-79.39576369024316"
     * @param mode either "walking", "bicycling", or "driving"
     * @return Written instructions how to get from origin to destination or "No route found" if there is no route,
     * or ("Error making URL in getDirections" or "Error in parsing URL in getDirections") if the params are not formatted
     * properly
     *
     */
    public static String getDirections(String origin, String destination, String mode){

        String apiKey = "AIzaSyCPfTsYtKOIcTNmhPGUrDphHTI5giH5X9s";

        //Makes Google Maps directions Api url for given params
        URL url = null;
        try {
            url = new URL("https://maps.googleapis.com/maps/api/directions/json?origin=" + origin +
                    "&destination=" + destination +
                    "&mode=" + mode +
                    "&key=" + apiKey);
        } catch (MalformedURLException e) {
            return "Error making URL in getDirections";
        }

        //Puts data from url into jsonObject
        JSONObject jsonObject = null;
        try {
            String text = IOUtils.toString(url, Charset.forName("UTF-8"));
            jsonObject = new JSONObject(text);
        } catch (IOException e) {
            return "Error in parsing URL in getDirections";
        }

        //Checks for valid route
        JSONArray jsonArray =  jsonObject.getJSONArray("routes");
        if(jsonArray.length() == 0){
            return "No route found";
        }

        //Sets jsonArray to where the directions are stored
        jsonObject=  jsonArray.getJSONObject(0);
        jsonArray = jsonObject.getJSONArray("legs");
        jsonObject = jsonArray.getJSONObject(0);
        jsonArray = jsonObject.getJSONArray("steps");

        String output = "";

        //Iterates through the directions formatting them and appending them to output

        for(int i = 0; i < jsonArray.length() - 1; i++){
            output += jsonArray.getJSONObject(i).get("html_instructions");
            output += " and continue for ";
            output += jsonArray.getJSONObject(i).getJSONObject("duration").get("text");
            output += ". Then, ";
        }
        output += jsonArray.getJSONObject(jsonArray.length() - 1).get("html_instructions");
        String string = "<div style=\\\"font-size:0.9em\\\">";
        output = output.replaceAll(string, " and your ");
        return output.replaceAll("<.*?>", "") + ".";
    }



    /**
     * Uses Google Maps Geocode Api to search for an address and find its latitude and longitude.
     * Names of buildings or incomplete address can be used as an address as Google Maps will still search for them,
     * but this will decrease reliability.
     *
     * @param address Do not include special characters and replace spaces with "+"
     * @return a LatLong of the top result on Google Maps for address. Or null if none is found

     */
    public static LatLong getLatLongFromAddress(String address){

        //Makes Google geocode API url for the address
        String apiKey = "AIzaSyCPfTsYtKOIcTNmhPGUrDphHTI5giH5X9s";
        URL url = null;
        try {
            url = new URL("https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + apiKey);
        } catch (MalformedURLException e) {
            return null;
        }

        //Puts data from url into jsonObject
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        try {
            String text = IOUtils.toString(url, Charset.forName("UTF-8"));
            jsonObject = new JSONObject(text);
        } catch (IOException e) {
            return null;
        }

        if(!(jsonObject.get("status").equals("OK"))){
            return null;
        }

        //Sets jsonObject to where the lat and long are stored
        jsonArray = jsonObject.getJSONArray("results");
        jsonObject = jsonArray.getJSONObject(0);
        jsonObject = jsonObject.getJSONObject("geometry");
        jsonObject = jsonObject.getJSONObject("location");

        //Gets lat and long and returns them.

        BigDecimal bigLat = (BigDecimal) jsonObject.get("lat");
        double smallLat = bigLat.doubleValue();

        BigDecimal bigLong = (BigDecimal) jsonObject.get("lng");
        double smallLong = bigLong.doubleValue();

        return new LatLong(smallLat, smallLong);

    }
}
