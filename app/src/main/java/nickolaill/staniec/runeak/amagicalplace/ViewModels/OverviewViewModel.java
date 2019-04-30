package nickolaill.staniec.runeak.amagicalplace.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Database.CollectionRepository;
import nickolaill.staniec.runeak.amagicalplace.Models.Collection;

public class OverviewViewModel extends AndroidViewModel {
    private CollectionRepository repository;
    private LiveData<List<Collection>> allCollections;

    public OverviewViewModel(@NonNull Application application) {
        super(application);
        repository = new CollectionRepository(application);
        allCollections = repository.getAllCollections();
    }

    public void insert(Collection collection) {
        repository.insert(collection);
    }

    public void update(Collection collection) {
        repository.update(collection);
    }

    public void delete(Collection collection) {
        repository.delete(collection);
    }

    public void deleteAllCollections() {
        repository.deleteAllCollections();
    }

    public LiveData<List<Collection>> getAllCollections() {
        return allCollections;
    }
}
