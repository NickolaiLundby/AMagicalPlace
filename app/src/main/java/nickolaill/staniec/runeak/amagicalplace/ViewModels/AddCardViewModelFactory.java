package nickolaill.staniec.runeak.amagicalplace.ViewModels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Models.Card;

public class AddCardViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private Application application;
    private List<Card> cards;

    public AddCardViewModelFactory(@NonNull Application application, List<Card> cards){
        super(application);
        this.application = application;
        this.cards = cards;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddCardViewModel(application, cards);
    }
}
