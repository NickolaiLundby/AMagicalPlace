package nickolaill.staniec.runeak.amagicalplace.Database;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

public class Converters {
    @TypeConverter
    public static Uri fromString(String value) {
        return Uri.parse(value);
    }

    @TypeConverter
    public static String uriToString(Uri uri) {
        return uri.toString();
    }
}
