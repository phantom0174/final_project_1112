package base;

/*


連結到排行榜提供平台 "dreamlo.com" 的簡易 API Wrapper

> source: https://youtu.be/KZuqEyxYZCc

*/


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.util.Pair;


public class ScoreAPI {
	public static String privateCode = "***REMOVED***";
	public static String publicCode = "64523f128f40bb6dec82d4c8";
	public static String webUrl = "http://dreamlo.com/lb/";
	
	public ArrayList<Pair<String, String>> getScores() throws IOException {
		String request = webUrl + publicCode + "/json";
		URL url = new URL(request);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.disconnect();
		
		JSONArray ja;
		try {
			InputStream inputStream = url.openStream();
			JSONObject jsonObject = (JSONObject) new JSONParser().parse(
			      new InputStreamReader(inputStream, "UTF-8")
			);
			
			jsonObject = (JSONObject) jsonObject.get("dreamlo");
			jsonObject = (JSONObject) jsonObject.get("leaderboard");
			ja = (JSONArray) jsonObject.get("entry");
		} catch (ParseException e) {
			System.out.println("TOO MANY REQUESTS");
			return null;
		}
		
		if (ja == null) return null;
		
		ArrayList<Pair<String, String>> scorePairs = new ArrayList<>();
		
		for (int i = 0; i < ja.size(); i++) {
			JSONObject jo = (JSONObject) ja.get(i);
		    String score = (String) jo.get("score");
		    String user = (String) jo.get("name");
		    
		    scorePairs.add(new Pair<String, String>(user, score));
		}
		
		return scorePairs;
	}
	
	public boolean uploadScore(String user, String score) throws IOException {
		String request = webUrl + privateCode + "/" + String.join("/", "add", user, score);
		URL url = new URL(request);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		con.disconnect();
		
		if (responseCode == 200) return true;
		return false;
	}
}
