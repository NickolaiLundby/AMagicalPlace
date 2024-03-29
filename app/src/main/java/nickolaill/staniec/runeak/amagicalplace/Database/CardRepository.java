package nickolaill.staniec.runeak.amagicalplace.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Models.MagicDao;
import nickolaill.staniec.runeak.amagicalplace.Utilities.Constants;
import nickolaill.staniec.runeak.amagicalplace.Utilities.CustomVolleyRequest;

public class CardRepository {
    private int collectionId;
    private MagicDao magicDao;
    private LiveData<List<Card>> allCards;
    private RequestQueue requestQueue;
    private Context context;

    public CardRepository(Application app, int collectionId) {
        this.collectionId = collectionId;
        magicDao = CardDatabase.getInstance(app).magicDao();
        allCards = magicDao.getAllCards(this.collectionId);
        requestQueue = Volley.newRequestQueue(app.getBaseContext());
        this.context = app.getBaseContext();
    }

    public void insert(Card card){
        new InsertCardAsyncTask(magicDao).execute(card);
    }

    public void insertCardFromApi(Card card){
        new InsertCardFromApiAsyncTask(magicDao).execute(card);
    }

    public void update(Card card){
        new UpdateCardAsyncTask(magicDao).execute(card);
    }

    public void updateImage(Card card){
        new AddImageAsyncTask(magicDao).execute(card);
    }

    public void delete(Card card){
        new DeleteCardAsyncTask(magicDao).execute(card);
    }

    public void deleteAllCards(){
        new DeleteAllCardsAsyncTask(magicDao).execute();
    }

    public LiveData<List<Card>> getAllCards(){
        return allCards;
    }

    private static class InsertCardAsyncTask extends AsyncTask<Card, Void, Void> {
        private MagicDao magicDao;

        private InsertCardAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            magicDao.insertCard(cards[0]);
            return null;
        }
    }

    private static class UpdateCardAsyncTask extends AsyncTask<Card, Void, Void> {
        private MagicDao magicDao;

        private UpdateCardAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            magicDao.updateCard(cards[0]);
            return null;
        }
    }

    private class DeleteCardAsyncTask extends AsyncTask<Card, Void, Void> {
        private MagicDao magicDao;

        private DeleteCardAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            magicDao.deleteCard(cards[0]);
            deleteImageFromExternalStorage(cards[0]);
            return null;
        }
    }

    private static class DeleteAllCardsAsyncTask extends AsyncTask<Void, Void, Void> {
        private MagicDao magicDao;

        private DeleteAllCardsAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            magicDao.deleteAllCards();
            return null;
        }
    }

    private class InsertCardFromApiAsyncTask extends AsyncTask<Card, Void, Void> {
        private MagicDao magicDao;

        private InsertCardFromApiAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {

            Card existingCard = magicDao.getCardByCollectionAndMultiverseIdAndTitle(cards[0].getCollectionId(), cards[0].getMultiverseId(), cards[0].getTitle());
            if(existingCard == null){
                File file = getFile(createFileName(cards[0].getMultiverseId()));
                if(file.exists()){
                    Log.d(Constants.LOG_TAG_IMAGE, "Using existing image for: " + cards[0].getMultiverseId());
                    Card card = cards[0];
                    card.setImageUri(Uri.parse(file.getAbsolutePath()));
                    magicDao.insertCard(card);
                } else {
                    Log.d(Constants.LOG_TAG_IMAGE, "Downloading new image for: " + cards[0].getMultiverseId());
                    final Card card = cards[0];
                    if(card.getImageUrl() == null) {
                        card.setImageUrl(Constants.DEFAULT_IMG_URL);
                        Log.d(Constants.LOG_TAG_IMAGE, "Using default image url for: " + cards[0].getMultiverseId());
                    }
                    CustomVolleyRequest volleyRequest = new CustomVolleyRequest(convertHttpToHttps(card.getImageUrl()), new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            InputStream is = new ByteArrayInputStream(response.data);
                            Bitmap b = BitmapFactory.decodeStream(is);
                            saveImageToExternalStorage(false, b, card, magicDao);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });

                    requestQueue.add(volleyRequest);
                    }
            } else {
                existingCard.setQuantity(existingCard.getQuantity()+1);
                magicDao.updateCard(existingCard);
            }
            return null;
        }
    }

    private class AddImageAsyncTask extends AsyncTask<Card, Void, Void> {
        private MagicDao magicDao;

        private AddImageAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            final Card card = cards[0];
            if(card.getImageUrl() == null)
                card.setImageUrl(Constants.DEFAULT_IMG_URL);
            Log.d(Constants.LOG_TAG_IMAGE, "Url: " + card.getImageUrl());
            CustomVolleyRequest volleyRequest = new CustomVolleyRequest(convertHttpToHttps(card.getImageUrl()), new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    InputStream is = new ByteArrayInputStream(response.data);
                    Bitmap b = BitmapFactory.decodeStream(is);
                    saveImageToExternalStorage( true, b, card, magicDao);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

            requestQueue.add(volleyRequest);

            return null;
        }
    }

    private void saveImageToExternalStorage(Boolean existingCard, Bitmap bitmap, Card card, MagicDao magicDao){
        new SaveImageToStorageTask(existingCard, card, context.getCacheDir(), magicDao).execute(bitmap);
    }

    private class SaveImageToStorageTask extends AsyncTask<Bitmap, Void, Void> {
        private boolean existingCard;
        private Card card;
        private File parentFile;
        private MagicDao magicDao;

        private SaveImageToStorageTask(boolean existingCard, Card card, File parentFile, MagicDao magicDao){
            this.existingCard = existingCard;
            this.card = card;
            this.parentFile = parentFile;
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            boolean success = true;

            String imageFileName = createFileName(card.getMultiverseId());

            if(success){
                File imageFile = new File(parentFile, imageFileName);

                try{
                    imageFile.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    card.setImageUri(Uri.parse(imageFile.getAbsolutePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(existingCard)
                magicDao.updateCard(card);
            else
                magicDao.insertCard(card);
            return null;
        }
    }

    private void deleteImageFromExternalStorage(Card card){
        new DeleteImageFromStorageTask(magicDao).execute(card);
    }

    private class DeleteImageFromStorageTask extends AsyncTask<Card, Void, Void>{
        private MagicDao magicDao;

        private DeleteImageFromStorageTask(MagicDao magicDao){
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            List<Card> c = magicDao.getCardsByMultiverseId(cards[0].getMultiverseId());
            if(c.isEmpty()){
                try{
                    File file = new File(cards[0].getImageUri().toString());
                    boolean deleted = file.delete();
                    if(deleted)
                        Log.d(Constants.LOG_TAG_EXTERNAL_STORAGE, cards[0].getTitle() + " deleted from storage");
                    else
                        Log.e(Constants.LOG_TAG_EXTERNAL_STORAGE, cards[0].getTitle() + " was NOT deleted");
                } catch (Exception e) {
                    Log.e(Constants.LOG_TAG_EXTERNAL_STORAGE, "Exception while deleting file " + e.getMessage());
                }
            }
            return null;
        }
    }

    private String convertHttpToHttps(String uri){
        return uri.replace("http://", "https://");
    }

    private String createFileName(int multiverseId){
        return "JPEG_" + multiverseId + ".jpg";
    }

    public File getFile(String fileName){
        File file = new File(context.getCacheDir(), fileName);
        return file;
    }

}
