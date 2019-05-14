package nickolaill.staniec.runeak.amagicalplace.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import io.magicthegathering.javasdk.api.CardAPI;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.Models.MagicDao;

@Database(entities = {Card.class, Collection.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CardDatabase extends RoomDatabase {
    private static CardDatabase instance;

    public abstract MagicDao magicDao();

    public static synchronized CardDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CardDatabase.class,
                    "magic_database")
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private MagicDao magicDao;

        private PopulateDbAsyncTask(CardDatabase db) {
            magicDao = db.magicDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // NOTE: This was nice for testing the app, but lets leave this empty or remove this now?

            return null;
        }
    }
}
