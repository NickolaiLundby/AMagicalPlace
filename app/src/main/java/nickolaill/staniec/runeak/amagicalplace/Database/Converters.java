package nickolaill.staniec.runeak.amagicalplace.Database;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Uri fromString(String value) {
        return Uri.parse(value);
    }

    @TypeConverter
    public static String uriToString(Uri uri) {
        return uri.toString();
    }

    @TypeConverter
    public Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
