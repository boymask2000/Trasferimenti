package database;

/**
 * Created by giovanni on 22/10/16.
 */
public class CommonAdapter {
    public String clean(String val) {
        String s = val.replace('\'', '_');
        s = s.replace(' ', '_');
        return s;
    }
}
