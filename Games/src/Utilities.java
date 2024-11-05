package Games.src;

import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class Utilities {

    private static final VBox root = new VBox(10);
    private static final int[] times = {15, 20, 45, 60, 90, 120, 300};
    private static final BooleanProperty gameFinished = new SimpleBooleanProperty(false);
    public static WordGenerator wordGenerator;
    private static ChoiceBox<String> languageChoiceBox;
    private static TextFlow textFlow;
    private static Slider slider;
    private static VBox shortcutsBox;
    private static boolean enter = false;
    private static boolean tab = false;
    private static boolean isEditable = true;
    private static int INTsecond = 0;
    private static int selectedTime = 15;
    private static Label timerLabel;
    private static Timeline timeline;
    private static boolean timelineBool = false;
    private static int keyCount = 0;
    private static List<String> newWords;
    private static int lengthOfNewWords;
    private static boolean isTimerRunning = false;

    public static VBox createUtilities(Stage primaryStage) {
        try {
            root.setAlignment(Pos.TOP_CENTER);
            root.getStyleClass().add("app-background");

            addSliderToRoot();
            addLanguageChoiceToRoot();
            addCountDownToRoot();
            addTextPanelToRoot();
            addShortCutFieldToRoot();

            primaryStage.setResizable(false);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

        return root;
    }

    private static void createShortCutField() {
        try {
            shortcutsBox = new VBox(10);
            shortcutsBox.setAlignment(Pos.CENTER);
            shortcutsBox.getStyleClass().add("shortcut-box");

            Label stopLabel = new Label("CTRL + SHIFT + P: Stop");
            Label restartLabel = new Label("TAB + ENTER: Restart");
            Label finishLabel = new Label("ESC: Finish Test");
            stopLabel.setStyle("-fx-text-fill: red;");
            restartLabel.setStyle("-fx-text-fill: LIGHTGREEN;");
            finishLabel.setStyle("-fx-text-fill: blue;");

            animateLabels(stopLabel, restartLabel, finishLabel);

            shortcutsBox.getChildren().addAll(stopLabel, restartLabel, finishLabel);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void animateLabels(Label stopLabel, Label restartLabel, Label quitLabel) {
        try {
            animateLabel(stopLabel);
            animateLabel(restartLabel);
            animateLabel(quitLabel);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void animateLabel(Label label) {
        try {
            Path path = new Path();
            path.getElements().add(new MoveTo(-50, 0));
            path.getElements().add(new LineTo(200, 0));

            PathTransition pathTransition = new PathTransition(Duration.seconds(5), path, label);
            pathTransition.setCycleCount(PathTransition.INDEFINITE);

            pathTransition.setAutoReverse(true);
            pathTransition.play();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void addShortCutFieldToRoot() {
        try {
            createShortCutField();
            root.getChildren().add(shortcutsBox);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void addSliderToRoot() {
        try {
            createSlider();
            slider.valueProperty().addListener((observable, oldSliderValue, newSliderValue) ->
                    handleSliderValueChange(oldSliderValue, newSliderValue));

            root.getChildren().add(slider);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void createSlider() {
        try {
            slider = new Slider();
            slider.getStyleClass().add("custom-slider");
            slider.setMin(0);
            slider.setMax(6);
            slider.setValue(0);
            slider.setMajorTickUnit(1);
            slider.setMinorTickCount(0);
            slider.setSnapToTicks(true);
            slider.setShowTickLabels(true);
            slider.setMaxSize(200, 100);

            slider.setLabelFormatter(new StringConverter<>() {
                @Override
                public String toString(Double value) {
                    return switch (value.intValue()) {
                        case 0 -> "15";
                        case 1 -> "20";
                        case 2 -> "45";
                        case 3 -> "60";
                        case 4 -> "90";
                        case 5 -> "120";
                        case 6 -> "300";
                        default -> "";
                    };
                }

                @Override
                public Double fromString(String s) {
                    return null;
                }
            });
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void addLanguageChoiceToRoot() {
        try {
            List<String> languages = LanguageLoader.loadLanguages();
            createLanguageChoiceBox(languages);

            languageChoiceBox.valueProperty().addListener((observable, oldValue, newValue) ->
                    handleLanguageChange(newValue));

            root.getChildren().add(languageChoiceBox);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public static void restartApplication() {
        try {
            if (isTimerRunning) {
                timeline.stop();
                isTimerRunning = false;
                slider.setDisable(false);
                languageChoiceBox.setDisable(false);
                updateTextFlow();
                timerLabel.setText(formatTime(times[(int) slider.getValue()]));
                selectedTime = times[(int) slider.getValue()];
            } else {
                updateTextFlow();
                timerLabel.setText(formatTime(times[(int) slider.getValue()]));
                selectedTime = times[(int) slider.getValue()];
            }

            WordColorHandler.resetIndex();
            keyCount = 0;
            isEditable = true;
            INTsecond = 0;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void createLanguageChoiceBox(List<String> languages) {
        try {
            languageChoiceBox = new ChoiceBox<>(
                    FXCollections.observableArrayList(languages)
            );
            languageChoiceBox.getStyleClass().add("custom-choice-box");
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public static void createTextPanel() {
        try {
            textFlow = new TextFlow();
            textFlow.getStyleClass().add("custom-text-flow");
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public static void updateTextFlow() {
        try {
            newWords = wordGenerator.getRandom30Words();

            textFlow.getChildren().clear();
            List<Text> textNodes = new ArrayList<>();

            for (String word : newWords) {
                for (char letter : word.toCharArray()) {
                    Text textNode = new Text(String.valueOf(letter));
                    textNode.getStyleClass().add("text-fill");
                    textNodes.add(textNode);
                }
                Text space = new Text(" ");
                textNodes.add(space);
            }

            textFlow.getChildren().addAll(textNodes);
            WordColorHandler.setAllWords(newWords);
            WordColorHandler.addWaveAnimation();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void addTextPanelToRoot() {
        try {
            createTextPanel();
            root.getChildren().add(textFlow);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    static void handleKeyPressed(KeyEvent keyEvent) {
        try {
            if (WordColorHandler.getLetterIndexInAllWords() == lengthOfNewWords - 1) {
                updateTextFlow();
                WordColorHandler.resetIndex();
                return;
            }

            handleShortCutPressed(keyEvent);
            if (isEditable) {
                String keyText = keyEvent.getText();
                if (!(keyEvent.isControlDown() && keyEvent.isShiftDown())) {
                    if (!keyText.isEmpty() && keyText.matches("[a-zA-Z0-9!@#$%^&*()_+{}|:\"<>?`\\-=[\\\\];',./]") || keyEvent.getCode().toString().equals("BACK_SPACE") ||
                            keyEvent.getCode().toString().equals("SPACE")) {
                        tab = false;
                        enter = false;

                        if (getSelectedLanguage() == null) {
                            return;
                        }

                        if (keyCount == 0) {
                            GraphCreator.setStartTime(System.currentTimeMillis());
                            startCountDown();
                        }
                        WordColorHandler.checkCorrectness(keyEvent);

                        keyCount++;
                    }
                }
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void handleShortCutPressed(KeyEvent event) {
        try {
            if (isCtrlShiftPCombination(event)) {
                toggleTimeline();
            }

            if (event.getCode() == KeyCode.ENTER) enter = true;
            if (event.getCode() == KeyCode.TAB) tab = true;
            if (enter && tab) {
                enter = false;
                tab = false;
                restartApplication();

            }

            if (event.getCode() == KeyCode.ESCAPE) {
                timeline.stop();
                endTest();
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static boolean isCtrlShiftPCombination(KeyEvent event) {
        try {
            return event.isControlDown() && event.isShiftDown() && event.getCode() == KeyCode.P;
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            return false;
        }
    }

    private static void toggleTimeline() {
        try {
            if (!timelineBool) {
                timeline.stop();
                timelineBool = true;
                isEditable = false;
            } else {
                timeline.play();
                timelineBool = false;
                isEditable = true;
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void endTest() {
        try {
            GraphCreator.saveTestResults();
            gameFinished.set(true);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public static BooleanProperty gameFinishedProperty() {
        return gameFinished;
    }

    public static void resetGameFinished() {
        gameFinished.set(false);
    }

    private static void addCountDownToRoot() {
        try {
            timerLabel = new Label(formatTime(selectedTime));
            root.getChildren().add(timerLabel);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    private static void startCountDown() {
        try {
            isTimerRunning = true;
            languageChoiceBox.setDisable(true);
            isEditable = true;
            slider.setDisable(true);
            timeline = new Timeline(
                    new KeyFrame(Duration.seconds(1), event -> {
                        if (selectedTime > 0) {
                            GraphCreator.updateGraphInfo(INTsecond, GraphCreator.calculateCurrentWPM());
                            INTsecond++;
                        }
                        updateTimerLabel();
                        if (selectedTime < 0) {
                            timeline.stop();
                            isEditable = false;
                            isTimerRunning = false;
                            slider.setDisable(false);
                            languageChoiceBox.setDisable(false);
                            endTest();
                        }
                    })
            );
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

    private static void updateTimerLabel() {
        timerLabel.setText(formatTime(selectedTime));
        selectedTime--;
    }

    private static String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private static void handleSliderValueChange(Number oldSliderValue, Number newSliderValue) {

        try {
            if (isTimerRunning) {
                return;
            }
            if (oldSliderValue.intValue() != newSliderValue.intValue()) {
                int selectedTimeInSeconds = times[newSliderValue.intValue()];

                System.out.println("Slider value changed: " + selectedTimeInSeconds);

                selectedTime = selectedTimeInSeconds;
                updateTimerLabel();
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }


    }

    public static String getSelectedLanguage() {

        if (languageChoiceBox != null) {
            return languageChoiceBox.getValue();
        } else {
            return null;
        }
    }

    private static void handleLanguageChange(String selectedLanguage) {
        wordGenerator = new WordGenerator(selectedLanguage);
        WordColorHandler.resetIndex();
        restartApplication();
    }

    public static void setLengthOfNewWords(int length) {
        lengthOfNewWords = length;
    }

    public static TextFlow getTextFlow() {
        return textFlow;
    }

}