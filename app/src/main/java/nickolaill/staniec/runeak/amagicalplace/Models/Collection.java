package nickolaill.staniec.runeak.amagicalplace.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;
import java.util.List;

@Entity(tableName = "collection_table",  indices = @Index(value = "coId", unique = true))
public class Collection implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int coId;
    private String title;
    private String description;
    private double value;
    private Date lastEvaluated;

    public Collection(String title, String description) {
        this.title = title;
        this.description = description;
        this.value = 0;
        this.lastEvaluated = null;
    }

    public int getCoId() {
        return coId;
    }

    public void setCoId(int coId) {
        this.coId = coId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getLastEvaluated() {
        return lastEvaluated;
    }

    public void setLastEvaluated(Date lastEvaluated) {
        this.lastEvaluated = lastEvaluated;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.coId);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeDouble(this.value);
        dest.writeLong(this.lastEvaluated != null ? this.lastEvaluated.getTime() : -1);
    }

    protected Collection(Parcel in) {
        this.coId = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.value = in.readDouble();
        long tmpLastEvaluated = in.readLong();
        this.lastEvaluated = tmpLastEvaluated == -1 ? null : new Date(tmpLastEvaluated);
    }

    public static final Creator<Collection> CREATOR = new Creator<Collection>() {
        @Override
        public Collection createFromParcel(Parcel source) {
            return new Collection(source);
        }

        @Override
        public Collection[] newArray(int size) {
            return new Collection[size];
        }
    };
}
