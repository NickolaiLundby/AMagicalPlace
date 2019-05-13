package nickolaill.staniec.runeak.amagicalplace.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Database.CardDatabase;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.Models.MagicDao;

public class PriceService extends Service {
    private final static String ScryfallURL = "https://api.scryfall.com/cards/collection";
    IBinder mBinder = new PriceBinder();
    private RequestQueue requestQueue;
    MagicDao magicDao;

    @Override
    public void onCreate() {
        super.onCreate();
        magicDao = CardDatabase.getInstance(getApplicationContext()).magicDao();
        requestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class PriceBinder extends Binder {
        public PriceService getServiceInstance() {
            return PriceService.this;
        }
    }

    //API
    public void testApi(){
        Toast.makeText(this, "Update handled by service", Toast.LENGTH_SHORT).show();
    }

    public void updateCollection(Collection collection){
        new UpdateCollectionAsyncTask(collection).execute();
    }

    //Async operations
    private class UpdateCollectionAsyncTask extends AsyncTask<Void, Void, Void> {
        private Collection collection;
        private List<Card> allCardsInCollection;

        private UpdateCollectionAsyncTask(Collection collection){
            this.collection = collection;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            allCardsInCollection = magicDao.getAllCardsList(collection.getCoId());
            JSONArray jsonArray = new JSONArray();
            try{
                for(Card c: allCardsInCollection){
                    JSONObject jsonObj = new JSONObject();
                    jsonObj.put("multiverse_id", c.getMultiverseId());
                    jsonArray.put(jsonObj);
                }
                JSONObject identifiersObj = new JSONObject();
                identifiersObj.put("identifiers", jsonArray);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, ScryfallURL, identifiersObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(PriceService.this, "Response: " + response.toString(), Toast.LENGTH_SHORT).show();
                        // TODO: Should add the price to each card in collection.
                        // TODO: Should add the total value to this collection, and the lastEvaluated with date.
                        collection.setLastEvaluated(Calendar.getInstance().getTime());
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonObjectRequest);

            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // TODO: Should make a broadcast so the UI can update.
        }
    }
}

