package nickolaill.staniec.runeak.amagicalplace.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import nickolaill.staniec.runeak.amagicalplace.Database.CardRepository;
import nickolaill.staniec.runeak.amagicalplace.Models.Card;

public class CollectionViewModel extends AndroidViewModel {
    private CardRepository repository;
    private LiveData<List<Card>> allCards;

    public CollectionViewModel(@NonNull Application application, int id) {
        super(application);
        repository = new CardRepository(application, id);
        allCards = repository.getAllCards();
    }

    public void insert(Card card) {
        repository.insertCardFromApi(card);
    }

    public void updateImage(Card card){
        repository.updateImage(card);
    }

    public void update(Card card) {
        repository.update(card);
    }

    public void delete(Card card) {
        repository.delete(card);
    }

    public void deleteAllCards() {
        repository.deleteAllCards();
    }

    public LiveData<List<Card>> getAllCards() {
        return allCards;
    }
}
