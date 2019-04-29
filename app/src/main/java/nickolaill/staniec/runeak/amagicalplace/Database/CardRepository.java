package nickolaill.staniec.runeak.amagicalplace.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;
import nickolaill.staniec.runeak.amagicalplace.Models.MagicDao;

public class CardRepository {
    private MagicDao magicDao;
    private LiveData<List<Card>> allCards;

    public CardRepository(Application app) {
        magicDao = CardDatabase.getInstance(app).magicDao();
        allCards = magicDao.getAllCards();
    }

    public void insert(Card card){
        new InsertCardAsyncTask(magicDao).execute(card);
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

    private static class DeleteCardAsyncTask extends AsyncTask<Card, Void, Void> {
        private MagicDao magicDao;

        private DeleteCardAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Card... cards) {
            magicDao.deleteCard(cards[0]);
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
}
