package nickolaill.staniec.runeak.amagicalplace.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.math.BigDecimal;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "card_table", foreignKeys = {@ForeignKey(entity= Collection.class,parentColumns = "coId",childColumns = "collectionId", onDelete = CASCADE)
}, indices = {@Index(value = {"caId"}, unique = true)})
public class Card {

    @PrimaryKey(autoGenerate = true)
    private int caId;
    private int quantity;
    //@ForeignKey(entity = Collection.class, parentColumns = "coId", childColumns = "collectionId")
    private int collectionId;

    private int multiverseId;
    private String title;
    private String series;
    private String text;
    private double price;
    private String types;
    private String type;
    private String rarity;
    private String toughness;
    private String power;
    private String manaCoast;
    private String imageUrl;
    private String colors;
    private String colorIdentity;

    // Should be deleted
    public Card(String title, String series, String text){
        this.title = title;
        this.series = series;
        this.text = text;
    }

    public Card(io.magicthegathering.javasdk.resource.Card card){
        this.multiverseId = card.getMultiverseid();
        this.title = card.getName();
        this.series = card.getSetName();
        this.text = card.getText();
        this.colorIdentity = TextUtils.join(",",card.getColorIdentity());
        this.colors  = TextUtils.join(",",card.getColors());
        this.imageUrl = card.getImageUrl();
        this.manaCoast = card.getManaCost();
        this.power = card.getPower();
        this.toughness = card.getToughness();
        this.rarity = card.getRarity();
        this.type = card.getType();
        this.types = TextUtils.join(",",card.getTypes());
        if(card.getOnlinePriceMid() != null){
            this.price = card.getOnlinePriceMid().doubleValue();
        }
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public void setText(String text) {
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

    public int getMultiverseId() {
        return multiverseId;
    }

    public void setMultiverseId(int multiverseId) {
        this.multiverseId = multiverseId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public String getToughness() {
        return toughness;
    }

    public void setToughness(String toughness) {
        this.toughness = toughness;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getManaCoast() {
        return manaCoast;
    }

    public void setManaCoast(String manaCoast) {
        this.manaCoast = manaCoast;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getColorIdentity() {
        return colorIdentity;
    }

    public void setColorIdentity(String colorIdentity) {
        this.colorIdentity = colorIdentity;
    }
}
