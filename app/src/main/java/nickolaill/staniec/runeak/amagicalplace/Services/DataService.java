package nickolaill.staniec.runeak.amagicalplace.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Utilities.DataStorage;

public class DataService extends Service {
    IBinder mBinder = new LocalBinder();
    DataStorage dataStorage;

    @Override
    public void onCreate() {
        dataStorage = new DataStorage();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public DataService getServiceInstance() {
            return DataService.this;
        }
    }

    // API to external storage
    public void SaveFileToStorage(Card card){

    }

    public void GetFileFromStorage(Card card){

    }
}
