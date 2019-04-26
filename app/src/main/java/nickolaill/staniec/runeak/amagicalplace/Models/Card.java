package nickolaill.staniec.runeak.amagicalplace.Models;

import android.arch.persistence.room.Entity;

@Entity(tableName = "card_table")
public class Card {

    private int id;

    private String title;

    private String series;

    private String text;

    public Card(String title, String series, String text){
        this.title = title;
        this.series = series;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
