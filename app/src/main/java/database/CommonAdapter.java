package database;

/**
 * Created by giovanni on 22/10/16.
 */
public class CommonAdapter {
    public String clean(String val) {
        String out="";
        for( int i=0; i<val.length(); i++){
            char c = val.charAt(i);
            out += c;
            if( c=='\'')
                out +=c;


        }

        return out;
    }
}
