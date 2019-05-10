package nickolaill.staniec.runeak.amagicalplace.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Models.Collection;

// Reference:
// https://developer.android.com/training/data-storage/files
public class DataStorage {
    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void ReadFromStorage(Collection collection, Card card, Context context){
        // TODO: The bitmap should be passed somehow and received in .execute(bitmap);
        if(isExternalStorageReadable())
            new ReadFileFromStorageTask(collection, card, context).execute();
    }

    public void WriteToStorage(Collection collection, Card card, Context context){
        // TODO: The bitmap should be passed somehow and received in .execute(bitmap);
        if(isExternalStorageWritable())
            new WriteFileToStorageTask(collection, card, context).execute();
    }

    private static class ReadFileFromStorageTask extends AsyncTask<Void, Void, Void> {
        private Collection collection;
        private Card card;
        private Context context;

        private ReadFileFromStorageTask(Collection collection, Card card, Context context) {
            this.collection = collection;
            this.card = card;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            String dirFileName = "DIR_" + collection.getCoId();
            String imageFileName = "JPEG_" + card.getCaId() + ".jpg";
            File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), dirFileName);
            File imageFile = new File(dir, imageFileName);
            try {
                FileInputStream inputStream = new FileInputStream(imageFile);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // TODO: Do something with bitmap
            return null;
        }
    }

    private static class WriteFileToStorageTask extends AsyncTask<Bitmap, Void, Void> {
        private Collection collection;
        private Card card;
        private Context context;

        private WriteFileToStorageTask(Collection collection, Card card, Context context) {
            this.collection = collection;
            this.card = card;
            this.context = context;
        }

        @Override
        protected Void doInBackground(Bitmap... bitmaps) {
            boolean success = true;
            String dirFileName = "DIR_" + collection.getCoId();
            String imageFileName = "JPEG_" + card.getCaId() + ".jpg";

            File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), dirFileName);
            if(!dir.exists())
                success = dir.mkdirs();

            if(success){
                File imageFile = new File(dir, imageFileName);
                try{
                    imageFile.createNewFile();
                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    bitmaps[0].compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // TODO: Alert success
            return null;
        }
    }
}
