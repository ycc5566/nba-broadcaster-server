
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.JSONObject;

/**
 *
 * @author eason
 */
public class GetPlayers {
    
    public JSONObject makeCallToAPI(String searchTerm) {
        JSONObject result = null;
        HttpURLConnection conn;
        int status = 0;
        
        try {
            // set URL connection and authentication key.
            URL url = new URL("https://api.mysportsfeeds.com/v2.0/pull/nba/players.json");
            String authString = "abb56610-f5f8-4afd-85b3-59eb4c:MYSPORTSFEEDS";  
            
            byte[] authEncBytes = Base64.getEncoder().encode(authString.getBytes());
            String encoding = new String(authEncBytes);
            
            // make a call to third party API.
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encoding);
            InputStream content = (InputStream) connection.getInputStream();
            BufferedReader in
                    = new BufferedReader(new InputStreamReader(content));
            String line = in.readLine();
            
            // get only one player information and return.
            result = cleanData(line, searchTerm);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    private JSONObject cleanData(String receivedData, String searchTerm) {
        
        // clean data
        Map<String, ArrayList<JSONObject>> playersGroup = new HashMap<>();
        // deal with received json object
        JSONObject jSONObject = new JSONObject(receivedData); // parse whole object

        // suppose to be around 882 data.
        int numOfPlayers = jSONObject.getJSONArray("players").length();

        for (int i = 0; i < numOfPlayers; i++) {
            String age = jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getInt("age") + "";
            // early exit to enhence performance.
            if (!age.equals(searchTerm)) {
                continue;
            }
            // instantiate jSon object.
            JSONObject eachPlayer = new JSONObject();
            // parse first name.
            eachPlayer.put("firstName", jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getString("firstName"));
            // parse last name.
            eachPlayer.put("lastName", jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getString("lastName"));
            // parse position.
            eachPlayer.put("primaryPosition", jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getString("primaryPosition"));
            // handle jersey number.
            if (!jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").get("jerseyNumber").equals(null)) {
                eachPlayer.put("jerseyNumber", jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getInt("jerseyNumber") + "");
            } else {
                eachPlayer.put("jerseyNumber", JSONObject.NULL);
            }
            // handle team info.
            if (!jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").get("currentTeam").equals(null)) {
                eachPlayer.put("abbreviation", jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getJSONObject("currentTeam").getString("abbreviation"));
            } else {
                eachPlayer.put("abbreviation", JSONObject.NULL);
            }
            // handle height.
            if (!jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").get("height").equals(null)) {
                eachPlayer.put("height", jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getString("height"));
            } else {
                eachPlayer.put("height", JSONObject.NULL);
            }
            // handle weight.
            if (!jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").get("weight").equals(null)) {
                eachPlayer.put("weight", jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getInt("weight") + "");
            } else {
                eachPlayer.put("weight", JSONObject.NULL);
            }
            // handle age.
            eachPlayer.put("age", age);
            // handle official picture.
            if (!jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").get("officialImageSrc").equals(null)) {
                eachPlayer.put("officialImageSrc", jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getString("officialImageSrc"));
            } else {
                eachPlayer.put("officialImageSrc", JSONObject.NULL);
            }
            // handle annual average salary.
            if (!jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").get("currentContractYear").equals(null)) {
                eachPlayer.put("annualAverageSalary", jSONObject.getJSONArray("players").getJSONObject(i).getJSONObject("player").getJSONObject("currentContractYear").getJSONObject("overallContract").getInt("annualAverageSalary")+"");
            } else {
                eachPlayer.put("annualAverageSalary", JSONObject.NULL);
            }

            // Put into map.
            if (playersGroup.containsKey(age)) {
                ArrayList<JSONObject> temp = playersGroup.get(age);
                temp.add(eachPlayer);
            } else {
                ArrayList<JSONObject> myList = new ArrayList<>();
                myList.add(eachPlayer);
                playersGroup.put(age, myList);
            }
        }

        // randomly select one from the group and return.
        JSONObject result = null;
        if (playersGroup.size() > 0) {
            Random r = new Random();
            int pickAnyOne = r.nextInt(playersGroup.get(searchTerm).size() + 1);
            result = playersGroup.get(searchTerm).get(pickAnyOne);
        }
        return result;
    }
}
