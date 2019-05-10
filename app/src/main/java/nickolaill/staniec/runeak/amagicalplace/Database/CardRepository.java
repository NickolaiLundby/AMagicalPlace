package nickolaill.staniec.runeak.amagicalplace.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Models.MagicDao;

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
            //TODO: Checks on this write operation
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
            Card existingCard = magicDao.getCardByCollectionAndMultiverseId(cards[0].getCollectionId(), cards[0].getMultiverseId());
            if(existingCard == null){
                final Card card = cards[0];
                // Initialize a new ImageRequest
                ImageRequest imageRequest = new ImageRequest(
                        cards[0].getImageUrl(), // Image URL
                        new Response.Listener<Bitmap>() { // Bitmap listener
                            @Override
                            public void onResponse(Bitmap response) {
                                // Do something with response
                                //mImageView.setImageBitmap(response);

                                // Save this downloaded bitmap to internal storage
                                saveImageToInternalStorage(response, card, magicDao);
                            }
                        },
                        0, // Image width
                        0, // Image height
                        ImageView.ScaleType.CENTER_CROP, // Image scale type
                        Bitmap.Config.RGB_565, //Image decode configuration
                        new Response.ErrorListener() { // Error listener
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Do something with error response
                                error.printStackTrace();
                            }
                        }
                );

                // Add ImageRequest to the RequestQueue
                requestQueue.add(imageRequest);
            } else {
                existingCard.setQuantity(existingCard.getQuantity()+1);
                magicDao.updateCard(existingCard);
            }
            return null;
        }
    }

    private void saveImageToInternalStorage(Bitmap bitmap, Card card, MagicDao magicDao){
        new SaveImageToStorageTask(card, context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), magicDao).execute(bitmap);
    }

    private class SaveImageToStorageTask extends AsyncTask<Bitmap, Void, Void> {
        private Card card;
        private File parentFile;
        private MagicDao magicDao;

        private SaveImageToStorageTask(Card card, File parentFile, MagicDao magicDao){
            this.card = card;
            this.parentFile = parentFile;
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            boolean success = true;
            String dirFileName = "DIR_" + collectionId;
            String imageFileName = "JPEG_" + card.getCaId() + ".jpg";

            File dir = new File(parentFile, dirFileName);
            if(!dir.exists())
                success = dir.mkdirs();

            if(success){
                File imageFile = new File(dir, imageFileName);

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
            magicDao.insertCard(card);
            return null;
        }
    }
}
