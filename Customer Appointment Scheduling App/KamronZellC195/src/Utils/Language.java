package Utils;

import java.util.Locale;

/**
 * classed is used to handling language(ONLY USED FOR LOG IN SCREEN FOR NOW)
 */
public class Language {
    /**
     * used to get system's current defualt locale
     * @return default Locale
     */
    public static Locale getLanguage(){
        System.out.println(Locale.getDefault());
        return Locale.getDefault();
    }
}
