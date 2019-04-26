package nickolaill.staniec.runeak.amagicalplace.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Models.CardDao;

@Database(entities = {Card.class}, version = 1)
public abstract class CardDatabase extends RoomDatabase {
    private static CardDatabase instance;

    public abstract CardDao cardDao();

    public static synchronized CardDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CardDatabase.class,
                    "card_database")
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
        private CardDao cardDao;

        private PopulateDbAsyncTask(CardDatabase db) {
            cardDao = db.cardDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            cardDao.insert(new Card("Lightning Bolt", "Alpha", "Lightning Bolt does 3 damage to one target."));
            cardDao.insert(new Card("Lightning Bolt", "Fourth Edition", "Lightning Bolt deals 3 damage to target creature or player."));
            cardDao.insert(new Card("Lightning Bolt", "Magic 2011", "Lightning Bolt deals 3 damage to target creature or player."));
            return null;
        }
    }
}
