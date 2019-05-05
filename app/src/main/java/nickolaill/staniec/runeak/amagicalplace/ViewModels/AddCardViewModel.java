package nickolaill.staniec.runeak.amagicalplace.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Database.CardRepository;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;

public class AddCardViewModel extends AndroidViewModel {

    private MutableLiveData<List<Card>> allCards;

    public AddCardViewModel(@NonNull Application application, List<Card> cards) {
        super(application);
        allCards = new MutableLiveData<>();
        allCards.setValue(cards);
    }


    public void setAllCards(List<Card> cards) {
        this.allCards.setValue(cards);
    }

    public LiveData<List<Card>> getAllCards() {
        return allCards;
    }
}
