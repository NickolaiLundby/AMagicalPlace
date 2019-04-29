package nickolaill.staniec.runeak.amagicalplace.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "card_table", foreignKeys = {@ForeignKey(entity= Collection.class,parentColumns = "coId",childColumns = "collectionId", onDelete = CASCADE)
}, indices = {@Index(value = {"caId"}, unique = true)})
public class Card {

    @PrimaryKey(autoGenerate = true)
    private int caId;

    private String title;

    private String series;

    private String text;

    private int quantity;

    private int collectionId;

    public Card(String title, String series, String text){
        this.title = title;
        this.series = series;
        this.text = text;
    }

    public int getCaId() {
        return caId;
    }

    public void setCaId(int caId) {
        this.caId = caId;
    }

    public String getTitle() {
        return title;
    }

    public String getSeries() {
        return series;
    }

    public String getText() {
        return text;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCollectionId() {
        return collectionId;
    }

    public void setCollectionId(int collectionId) {
        this.collectionId = collectionId;
    }
}
