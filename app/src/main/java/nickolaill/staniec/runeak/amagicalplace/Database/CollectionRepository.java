package nickolaill.staniec.runeak.amagicalplace.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Models.Collection;
import nickolaill.staniec.runeak.amagicalplace.Models.MagicDao;

public class CollectionRepository {
    private MagicDao magicDao;
    private LiveData<List<Collection>> allCollections;

    public CollectionRepository(Application app) {
        magicDao = CardDatabase.getInstance(app).magicDao();
        allCollections = magicDao.getAllCollections();
    }

    public void insert(Collection collection){
        new InsertCollectionAsyncTask(magicDao).execute(collection);
    }

    public void update(Collection collection){
        new UpdateCollectionAsyncTask(magicDao).execute(collection);
    }

    public void delete(Collection collection){
        new DeleteCollectionAsyncTask(magicDao).execute(collection);
    }

    public void deleteAllCollections(){
        new DeleteAllCollectionsAsyncTask(magicDao).execute();
    }

    public LiveData<List<Collection>> getAllCollections(){
        return allCollections;
    }

    private static class InsertCollectionAsyncTask extends AsyncTask<Collection, Void, Void> {
        private MagicDao magicDao;

        private InsertCollectionAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Collection... collections) {
            magicDao.insertCollection(collections[0]);
            return null;
        }
    }

    private static class UpdateCollectionAsyncTask extends AsyncTask<Collection, Void, Void> {
        private MagicDao magicDao;

        private UpdateCollectionAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Collection... collections) {
            magicDao.updateCollection(collections[0]);
            return null;
        }
    }

    private static class DeleteCollectionAsyncTask extends AsyncTask<Collection, Void, Void> {
        private MagicDao magicDao;

        private DeleteCollectionAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Collection... collections) {
            magicDao.deleteCollection(collections[0]);
            return null;
        }
    }

    private static class DeleteAllCollectionsAsyncTask extends AsyncTask<Void, Void, Void> {
        private MagicDao magicDao;

        private DeleteAllCollectionsAsyncTask(MagicDao magicDao) {
            this.magicDao = magicDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            magicDao.deleteAllCollections();
            return null;
        }
    }
}
