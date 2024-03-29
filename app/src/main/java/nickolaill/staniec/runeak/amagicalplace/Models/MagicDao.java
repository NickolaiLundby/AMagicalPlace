package nickolaill.staniec.runeak.amagicalplace.Models;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface MagicDao {
    @Insert
    void insertCollection(Collection collection);

    @Update
    void updateCollection(Collection collection);

    @Delete
    void deleteCollection(Collection collection);

    @Query("DELETE FROM collection_table")
    void deleteAllCollections();

    @Insert
    void insertCard(Card card);

    @Update
    void updateCard(Card card);

    @Update
    void updateCards(List<Card> card);

    @Delete
    void deleteCard(Card card);

    @Query("SELECT * FROM card_table WHERE collectionId LIKE :collectionId AND multiverseId LIKE :multiverseId AND title LIKE :title")
    Card getCardByCollectionAndMultiverseIdAndTitle(int collectionId, int multiverseId, String title);

    @Query("SELECT * FROM card_table WHERE multiverseId LIKE :multiverseId")
    List<Card> getCardsByMultiverseId(int multiverseId);

    @Query("SELECT * FROM card_table WHERE collectionId LIKE :collectionId")
    List<Card> getAllCardsList(int collectionId);

    @Query("SELECT * FROM card_table WHERE collectionId LIKE :collectionId")
    LiveData<List<Card>> getAllCards(int collectionId);

    @Query("DELETE FROM card_table")
    void deleteAllCards();

    @Query("SELECT * FROM collection_table")
    LiveData<List<Collection>> getAllCollections();

}
