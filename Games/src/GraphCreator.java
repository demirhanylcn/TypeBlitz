package Games.src;

import javafx.animation.PathTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class GraphCreator {

    private static final int[] statusArr = {0, 0, 0, 0};
    private static final XYChart.Series<Number, Number> seriesOverall = new XYChart.Series<>();
    private static final XYChart.Series<Number, Number> seriesCurrent = new XYChart.Series<>();
    private static final List<String> wordList = new ArrayList<>();
    private static long startTime;

    // [0] Correct, [1] Wrong, [2] Omitted, [3] Redundant

    public static void keyEventStatus(status status, boolean IncreaseOrDecrease) {
        if (IncreaseOrDecrease) {
            switch (status) {
                case Correct -> statusArr[0]++;
                case Wrong -> statusArr[1]++;

                case Omitted -> statusArr[2]++;

                case Redundant -> statusArr[3]++;

                default -> throw new IllegalStateException("Unexpected value: " + status);
            }
        } else {
            switch (status) {
                case Correct -> statusArr[0]--;

                case Wrong -> statusArr[1]--;

                case Omitted -> statusArr[2]--;

                case Redundant -> statusArr[3]--;

                default -> throw new IllegalStateException("Unexpected value: " + status);
            }
        }


    }

    public static void setStartTime(long givenStartTime) {
        startTime = givenStartTime;
    }

    public static double calculateCurrentWPM() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double elapsedTimeInMinutes = elapsedTime / 60000.0;
        return ((double) statusArr[0] / calculateAvgWordSize()) / elapsedTimeInMinutes;
    }

    public static void saveTestResults() {
        try {

            String savePath = "Games/src/testResults/";
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");
            String fileName = dateFormat.format(new Date()) + ".txt";

            try (PrintWriter writer = new PrintWriter(new FileWriter(savePath + fileName))) {
                for (String result : wordList) {
                    writer.println(result);
                }

                writer.println("\nOverall Statistics:");
                writer.println("Language selected: " + Utilities.getSelectedLanguage());
                writer.println("Average WPM: " + calculateOverallWPM());
                writer.println("Character Statistics:");
                writer.println("Correct: " + statusArr[0]);
                writer.println("Incorrect: " + statusArr[1]);
                writer.println("Omitted: " + statusArr[2]);
                writer.println("Redundant: " + statusArr[3]);
                writer.println("% Correct Characters: " + calculatePercentageCorrect());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }

    public static double calculateOverallWPM() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double elapsedTimeInMinutes = elapsedTime / 60000.0;
        return ((double) statusArr[0] / calculateAvgWordSize()) / elapsedTimeInMinutes;
    }

    private static double calculatePercentageCorrect() {
        int totalCharacters = statusArr[0] + statusArr[1] + statusArr[2] + statusArr[3];
        int correctCharacters = statusArr[0];
        return (double) correctCharacters / totalCharacters * 100.0;
    }

    private static double calculateAvgWordSize() {
        int size = 0;
        for (String each : WordColorHandler.getAllWordsArr()) {
            size += each.length();
        }
        return (double) size / WordColorHandler.getAllWordsArr().length;
    }

    public static void resetForNew() {
        statusArr[0] = 0;
        statusArr[1] = 0;
        statusArr[2] = 0;
        statusArr[3] = 0;
        seriesOverall.getData().clear();
        seriesCurrent.getData().clear();
        wordList.clear();
    }

    public static void showTheGraph(Stage primaryStage) {
        try {
            Stage newStage = new Stage();
            NumberAxis x = new NumberAxis();
            NumberAxis y = new NumberAxis();
            x.setLabel("Time");
            y.setLabel("WPM");

            LineChart<Number, Number> lineChart = new LineChart<>(x, y);
            lineChart.getStyleClass().add("monkey-type-chart");

            Label overallWPMLabel = new Label("Overall WPM: ");
            Label percentageCorrectLabel = new Label("Percentage Correct: ");
            Label statusArrLabel = new Label("Status: ");

            overallWPMLabel.setText("Overall WPM: " + (int) calculateOverallWPM());
            overallWPMLabel.setTextFill(Color.ORANGE);
            percentageCorrectLabel.setText("Percentage Correct: %" + (int) calculatePercentageCorrect());
            percentageCorrectLabel.setTextFill(Color.RED);
            statusArrLabel.setText("Status: " + Arrays.toString(statusArr));
            statusArrLabel.setTextFill(Color.GREEN);


            VBox labelsVBox = new VBox(20);
            labelsVBox.getChildren().addAll(overallWPMLabel, percentageCorrectLabel, statusArrLabel);

            VBox mainVBox = new VBox(10);
            mainVBox.setAlignment(Pos.CENTER);
            mainVBox.getChildren().addAll(lineChart, labelsVBox);

            seriesOverall.setName("Correctness of typing");
            seriesCurrent.setName("WPM");

            lineChart.getData().add(seriesOverall);
            lineChart.getData().add(seriesCurrent);
            seriesOverall.getNode().setStyle("-fx-stroke: RED;");
            seriesCurrent.getNode().setStyle("-fx-stroke: ORANGE;");
            lineChart.setHorizontalGridLinesVisible(false);
            lineChart.setVerticalGridLinesVisible(false);
            lineChart.setCreateSymbols(false);

            Scene scene = new Scene(mainVBox, 800, 600);
            animateLabels(overallWPMLabel, percentageCorrectLabel, statusArrLabel, scene);
            scene.getStylesheets().add("Games/src/styles/styles.css");
            newStage.getIcons().add(new Image("Games/src/images/monkeytype.png"));

            newStage.setTitle("MonkeyType Graph");
            newStage.setScene(scene);
            newStage.setMinWidth(400);
            newStage.setMinHeight(300);

            newStage.show();

            newStage.setOnCloseRequest(event -> {
                primaryStage.show();
                Utilities.restartApplication();
                Utilities.resetGameFinished();
                resetForNew();
                newStage.close();
            });
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }


    private static void animateLabels(Label overallWPMLabel, Label percentageCorrectLabel, Label statusArrLabel, Scene scene) {
        animateLabel(overallWPMLabel, scene);
        animateLabel(percentageCorrectLabel, scene);
        animateLabel(statusArrLabel, scene);
    }

    private static void animateLabel(Label label, Scene scene) {
        try {
            Path path = new Path();
            path.getElements().add(new MoveTo(scene.getWidth() - scene.getWidth() / 4, 0));
            path.getElements().add(new LineTo(scene.getWidth() - (scene.getWidth() * ((double) 3 / 4)), 0));

            PathTransition pathTransition = new PathTransition(Duration.seconds(5), path, label);
            pathTransition.setCycleCount(PathTransition.INDEFINITE);

            pathTransition.setAutoReverse(true);
            pathTransition.play();
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }


    public static void updateGraphInfo(int second, double WPM) {
        seriesCurrent.getData().add(new XYChart.Data<>(second, WPM));
        seriesOverall.getData().add(new XYChart.Data<>(second, calculatePercentageCorrect()));
    }


    public static void addToWordList(String string) {
        wordList.add(string);
    }
}
