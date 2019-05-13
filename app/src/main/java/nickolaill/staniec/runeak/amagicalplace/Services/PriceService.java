package nickolaill.staniec.runeak.amagicalplace.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

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
import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Database.CardDatabase;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.Models.MagicDao;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.Utilities.InternetUtils;
import nickolaill.staniec.runeak.amagicalplace.Utilities.ValueCalculator;

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
    public void updateCollectionPrices(Collection collection){
        new UpdateCollectionPricesAsyncTask(collection).execute();
    }

    //Async operations
    private class UpdateCollectionPricesAsyncTask extends AsyncTask<Void, Void, Void> {
        private Collection collection;
        private List<Card> allCardsInCollection;

        private UpdateCollectionPricesAsyncTask(Collection collection){
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
                        // TODO: Should add the price to each card in collection.
                        for(int i = 0; i < allCardsInCollection.size(); i++){
                            String cardName = allCardsInCollection.get(i).getTitle();
                            String scryFallName = InternetUtils.extractJsonScryfall(response).get(i).first;
                            Double value = InternetUtils.extractJsonScryfall(response).get(i).second;
                            if(cardName.equalsIgnoreCase(scryFallName)){
                                allCardsInCollection.get(i).setPrice(value);
                                allCardsInCollection.get(i).setLastEvaluated(Calendar.getInstance().getTime());
                                new UpdateAllCardsIncollectionAsyncTask(magicDao).execute(allCardsInCollection);
                            }
                        }
                        // TODO: Should add the total value to this collection, and the lastEvaluated with date.
                        collection.setLastEvaluated(Calendar.getInstance().getTime());
                        collection.setValue(ValueCalculator.calculateCollectionValue(allCardsInCollection, InternetUtils.extractJsonScryfall(response)));
                        sendMyBroadcast(collection);
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
    }

    //Broadcasting
    private void sendMyBroadcast(Collection collection){
        try{
            Intent broadCastIntent = new Intent();
            broadCastIntent.putExtra(Constants.COLLECTION_TAG, collection);
            broadCastIntent.setAction(Constants.BROADCAST_DATABASE_UPDATED);
            sendBroadcast(broadCastIntent);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private static class UpdateAllCardsIncollectionAsyncTask extends AsyncTask<List<Card>, Void, Void> {
        private MagicDao magicDao;

        private UpdateAllCardsIncollectionAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(List<Card>... cards) {
            magicDao.updateCards(cards[0]);
            return null;
        }
    }
}

