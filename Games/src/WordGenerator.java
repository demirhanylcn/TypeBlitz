import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordGenerator {
    private final List<String> wordList;

    public WordGenerator(String selectedLanguage) {
        wordList = new ArrayList<>();


        String filePath = "C:\\Users\\demir\\Documents\\codes\\TypeBlitz\\Games\\dictionary\\   " + selectedLanguage + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                wordList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getRandom30Words() {
        List<String> random30 = new ArrayList<>(wordList);
        Collections.shuffle(random30);

        int length = 0;
        List<String> returnList = random30.subList(0, 30);
        for (String str : returnList) {
            length += str.length();
        }
        length += returnList.size() - 1;
        Utilities.setLengthOfNewWords(length);

        return returnList;
    }

}
