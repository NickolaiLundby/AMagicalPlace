package nickolaill.staniec.runeak.amagicalplace.Utilities;

import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class InternetUtils {

    //https://stackoverflow.com/a/27312494
    public static boolean isOnline() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
            } catch (Exception e) {
            return false;
        }
    }

    public static ArrayList<Pair<String, Double>> extractJsonScryfall(JSONObject target){
        double priceUsd = 0.0;
        String cardName = "none";
        ArrayList<Pair<String, Double>> pairList = new ArrayList<Pair<String, Double>>();

        try{
            JSONArray data = target.getJSONArray("data");
            for(int i = 0; i < data.length(); i++){
                JSONObject jo = data.getJSONObject(i);
                cardName = jo.getString("name");
                priceUsd = jo.getJSONObject("prices").getDouble("usd");
                pairList.add(Pair.create(cardName, priceUsd));
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        if(pairList.isEmpty())
            return null;
        else
            return pairList;
    }
}
