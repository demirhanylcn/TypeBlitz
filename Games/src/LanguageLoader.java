import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class LanguageLoader {

    private static final String DICTIONARY_PATH = "C:\\Users\\demir\\Documents\\codes\\TypeBlitz\\Games\\dictionary";

    public static List<String> loadLanguages() {
        List<String> languages = new ArrayList<>();

        File directory = new File(DICTIONARY_PATH);
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".txt")) {
                    String fileName = file.getName();
                    String languageName = fileName.substring(0, fileName.indexOf('.'));
                    languages.add(languageName);
                }
            }
        }

        return languages;
    }
}
