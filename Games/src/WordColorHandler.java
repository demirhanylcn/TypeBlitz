package Games.src;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.List;

public class WordColorHandler {

    private static String allWords;
    private static String[] allWordsArr;
    private static String currentWord;
    private static int allWordsArrCurrentIndex;
    private static int LetterIndexInAllWords = 0;
    private static int LetterIndexInAllText = 0;
    private static boolean isRedundant = false;
    private static char[] allWordsCharArray;
    private static boolean isLastCharSpace = false;


    public static void setAllWords(List<String> allWordsList) {

        allWords = String.join(String.valueOf(' '), allWordsList);
        allWordsCharArray = allWords.toCharArray();
        allWordsArr = new String[allWordsList.size()];
        allWordsList.toArray(allWordsArr);
        currentWord = allWordsArr[0];
        allWordsArrCurrentIndex = 0;
    }


    public static void checkCorrectness(KeyEvent keyEvent) {

        try {
            if (keyEvent.getCode().toString().equals("BACK_SPACE")) {
                handleBackspace();
                return;
            }

            char pressedChar;
            char currentChar = allWordsCharArray[LetterIndexInAllWords];

            currentWord = allWordsArr[allWordsArrCurrentIndex];
            Text textNode = new Text(String.valueOf(currentChar));
            addUnderLineAnimation(textNode);

            if (keyEvent.getCode().toString().equals("SPACE")) {
                pressedChar = ' ';
            } else {
                pressedChar = keyEvent.getText().charAt(0);
            }

            isLastCharSpace = false;
            if (isRedundant) {
                if (pressedChar == ' ') {
                    GraphCreator.keyEventStatus(status.Correct, true);
                    Utilities.getTextFlow().getChildren().set(LetterIndexInAllText, textNode);
                    LetterIndexInAllWords++;
                    LetterIndexInAllText++;
                    isRedundant = false;
                    GraphCreator.addToWordList(currentWord + " -> " + GraphCreator.calculateCurrentWPM() + " wpm");
                    allWordsArrCurrentIndex++;

                } else {
                    textNode = new Text(String.valueOf(pressedChar));
                    textNode.setFill(Color.ORANGE);
                    GraphCreator.keyEventStatus(status.Redundant, true);
                    addUnderLineAnimation(textNode);
                    Utilities.getTextFlow().getChildren().set(LetterIndexInAllText, textNode);
                    LetterIndexInAllText++;
                    removeTextNodes(LetterIndexInAllText, Utilities.getTextFlow().getChildren().size() - 1);
                    addTextNode(allWordsCharArray);
                }
            } else if (currentChar == ' ' && pressedChar != ' ') {
                isRedundant = true;

                textNode = new Text(String.valueOf(pressedChar));
                textNode.setFill(Color.ORANGE);
                GraphCreator.keyEventStatus(status.Redundant, true);
                addUnderLineAnimation(textNode);
                Utilities.getTextFlow().getChildren().set(LetterIndexInAllText, textNode);

                LetterIndexInAllText++;
                removeTextNodes(LetterIndexInAllText, Utilities.getTextFlow().getChildren().size() - 1);
                addTextNode(allWordsCharArray);


            } else if (currentChar != ' ' && pressedChar == ' ') {
                textNode.setFill(Color.BLACK);
                GraphCreator.keyEventStatus(status.Omitted, true);
                addUnderLineAnimation(textNode);
                Utilities.getTextFlow().getChildren().set(LetterIndexInAllText, textNode);
                LetterIndexInAllWords++;
                LetterIndexInAllText++;
            } else {
                if (pressedChar == currentChar) {
                    if (currentChar == ' ') {
                        GraphCreator.addToWordList(currentWord + " -> " + GraphCreator.calculateCurrentWPM() + " wpm");
                        allWordsArrCurrentIndex++;
                    }


                    textNode.setFill(Color.GREEN);
                    GraphCreator.keyEventStatus(status.Correct, true);

                } else {
                    textNode.setFill(Color.RED);
                    GraphCreator.keyEventStatus(status.Wrong, true);
                }

                Utilities.getTextFlow().getChildren().set(LetterIndexInAllText, textNode);
                LetterIndexInAllWords++;
                LetterIndexInAllText++;

            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }


    }

    private static void addTextNode(char[] allWordsChars) {
        try {
            for (int i = LetterIndexInAllText, j = LetterIndexInAllWords; i < allWordsChars.length + (LetterIndexInAllText - LetterIndexInAllWords); i++, j++) {
                Text remainingTextNode = new Text(String.valueOf(allWordsChars[j]));
                remainingTextNode.setFill(Color.LIGHTGREY);
                Utilities.getTextFlow().getChildren().add(i, remainingTextNode);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }


    public static void addWaveAnimation() {

        try {


            for (int i = 0; i < Utilities.getTextFlow().getChildren().size(); i++) {
                Text each = (Text) Utilities.getTextFlow().getChildren().get(i);

                double delay = 0.1 * i;

                TranslateTransition waveAnimation = new TranslateTransition(Duration.seconds(0.5), each);
                waveAnimation.setFromY(0.0);
                waveAnimation.setToY(-8.0);
                waveAnimation.setCycleCount(2);
                waveAnimation.setAutoReverse(true);
                waveAnimation.setDelay(Duration.seconds(delay));

                waveAnimation.play();
                if (i == Utilities.getTextFlow().getChildren().size() / 2) {
                    waveAnimation.setOnFinished(event -> addWaveAnimation());
                }
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }


    public static void addUnderLineAnimation(Text textNode) {
        try {
            Timeline fadeInAndOutAnimation = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(textNode.opacityProperty(), 1.0)),
                    new KeyFrame(Duration.seconds(0.5), new KeyValue(textNode.opacityProperty(), 0.6))
            );

            if (textNode.getText().equals(" ")) ;
            else textNode.setUnderline(true);

            fadeInAndOutAnimation.setOnFinished(event -> {
                textNode.setUnderline(false);
            });

            fadeInAndOutAnimation.play();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

    private static void removeTextNodes(int startIndex, int endIndex) {
        if (startIndex >= 0 && endIndex < Utilities.getTextFlow().getChildren().size() && startIndex <= endIndex) {
            Utilities.getTextFlow().getChildren().remove(startIndex, endIndex + 1);
        } else {
            System.out.println("Invalid index range.");
        }
    }

    public static void handleBackspace() {
        try {
            if (LetterIndexInAllWords > 0) {
                LetterIndexInAllText--;

                Text charAtIndex = (Text) Utilities.getTextFlow().getChildren().get(LetterIndexInAllText);

                if (isLastCharSpace) {
                    isLastCharSpace = false;
                }
                if (charAtIndex.getText().charAt(0) == ' ') isLastCharSpace = true;

                if (charAtIndex.getFill() == Color.ORANGE) {
                    Utilities.getTextFlow().getChildren().remove(LetterIndexInAllText);
                    GraphCreator.keyEventStatus(status.Redundant, false);
                } else {
                    if (charAtIndex.getFill() == Color.RED) GraphCreator.keyEventStatus(status.Wrong, false);
                    else if (charAtIndex.getFill() == Color.GREEN) GraphCreator.keyEventStatus(status.Correct, false);
                    else GraphCreator.keyEventStatus(status.Omitted, false);
                    isRedundant = false;
                    LetterIndexInAllWords--;
                    Text textNode = new Text(String.valueOf(allWordsCharArray[LetterIndexInAllWords]));
                    textNode.setFill(Color.LIGHTGRAY);

                    Utilities.getTextFlow().getChildren().set(LetterIndexInAllText, textNode);
                }


            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }


    }


    public static void resetIndex() {
        LetterIndexInAllWords = 0;
        LetterIndexInAllText = 0;
        isRedundant = false;
        allWords = "";
    }

    public static int getLetterIndexInAllWords() {
        return LetterIndexInAllWords;
    }


    public static String[] getAllWordsArr() {
        return allWordsArr;
    }
}
