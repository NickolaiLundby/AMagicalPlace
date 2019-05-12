package nickolaill.staniec.runeak.amagicalplace.Utilities;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.net.URI;
import java.net.URL;

import nickolaill.staniec.runeak.amagicalplace.Fragments.CardDetailFragment;

public class StorageUtils {
    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static boolean isUriValid(Uri uri){
        if(uri != null){
            File file = new File(uri.getPath());
            if(!file.exists()){
                return false;
            } else{
                return true;
            }
        } else {
            return false;
        }
    }

}
