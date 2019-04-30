package nickolaill.staniec.runeak.amagicalplace.ViewModels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class CollectionViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {
    private Application application;
    private int collectionId;

    public CollectionViewModelFactory(@NonNull Application application, int collectionId){
        super(application);
        this.application = application;
        this.collectionId = collectionId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new CollectionViewModel(application, collectionId);
    }
}
