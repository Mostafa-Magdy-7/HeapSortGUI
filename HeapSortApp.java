package com.example.heapsortgui;

import java.util.Arrays;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HeapSortApp extends Application {

    private int[] arr;
    private int n;
    private GridPane grid;
    private Rectangle[] rectangles;
    private VBox blocksBox;
    private int currentStep = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Label sizeLabel = new Label("Enter the size of the array:");
        TextField sizeTextField = new TextField();
        Label elementsLabel = new Label("Enter the elements of the array:");
        TextField elementsTextField = new TextField();
        Button sortButton = new Button("Sort");
        Button startOverButton = new Button("Start Over");
        Button nextButton = new Button("Next");
        Label originalLabel = new Label();
        final Label sortedLabel = new Label();

        grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        sortButton.setOnAction(e -> {
            int size = Integer.parseInt(sizeTextField.getText());
            arr = new int[size];
            String[] elements = elementsTextField.getText().split(" ");
            for (int i = 0; i < size; i++) {
                arr[i] = Integer.parseInt(elements[i]);
            }
            n = arr.length;
            originalLabel.setText("Original array: " + Arrays.toString(arr));
            displayArray(); // Display the initial state of the array
            heapSort();
            sortedLabel.setText("Sorted array:");
            displaySortedArray();
            currentStep = 0; // Reset the current step
            nextButton.setDisable(false); // Enable the "next" button
        });

        startOverButton.setOnAction(e -> {
            arr = null;
            n = 0;
            grid.getChildren().clear();
            originalLabel.setText("");
            sortedLabel.setText("");
            rectangles = null; // Clear the rectangles array
            blocksBox.getChildren().clear(); // Clear the blocks box
            nextButton.setDisable(true); // Disable the "next" button
        });

        nextButton.setOnAction(e -> {
            if (currentStep < blocksBox.getChildren().size() - 1) {
                // Highlight the current block
                HBox currentBlock = (HBox) blocksBox.getChildren().get(currentStep);
                for (int i = 0; i < n; i++) {
                    Rectangle rect = (Rectangle) currentBlock.getChildren().get(i);
                    rect.setFill(Color.RED);
                }
                // Un-highlight the previous block
                if (currentStep > 0) {
                    HBox previousBlock = (HBox) blocksBox.getChildren().get(currentStep - 1);
                    for (int i = 0; i < n; i++) {
                        Rectangle rect = (Rectangle) previousBlock.getChildren().get(i);
                        rect.setFill(Color.WHITE);
                    }
                }
                currentStep++;
            }
            if (currentStep == blocksBox.getChildren().size() - 1) {
                // Disable the "next" button when we reach the end of the steps
                nextButton.setDisable(true);
            }
        });

        HBox buttonBox = new HBox(10, sortButton, startOverButton, nextButton);
        buttonBox.setAlignment(Pos.CENTER);

        blocksBox = new VBox(5);
        blocksBox.setAlignment(Pos.CENTER);

        VBox vbox = new VBox(10, sizeLabel, sizeTextField, elementsLabel, elementsTextField, buttonBox, originalLabel, grid, sortedLabel, blocksBox);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 500, 600);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void heapify(int[] arr, int heapSize, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < heapSize && arr[left] > arr[largest]) {
            largest = left;
        }

        if (right < heapSize && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            swap(arr, i, largest);
            displayArray(i, largest); // Display the updated state of the array
            // Create a new block to show the updated state of the array
            HBox block = new HBox(5);
            for (int k = 0; k < n; k++) {
                Rectangle rect = new Rectangle(30, 30, Color.WHITE);
                if (k == i || k == largest) {
                    rect.setFill(Color.RED);
                }
                Text text = new Text(Integer.toString(arr[k]));
                StackPane stackPane = new StackPane(rect, text);
                block.getChildren().add(stackPane);
            }
            blocksBox.getChildren().add(block);
            heapify(arr, heapSize, largest);
        }
    }

    public void heapSort() {
        blocksBox.getChildren().clear(); // Clear the blocks box
        int heapSize = n;
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, heapSize, i);
        }
        for (int i = n - 1; i >= 0; i--) {
            swap(arr, 0, i);
            displayArray(0, i); // Display the updated state of the array
            // Create a new block to show the updated state of the array
            HBox block = new HBox(5);
            for (int k = 0; k < n; k++) {
                Rectangle rect = new Rectangle(30, 30, Color.WHITE);
                if (k == 0 || k == i) {
                    rect.setFill(Color.RED);
                }
                Text text = new Text(Integer.toString(arr[k]));
                StackPane stackPane = new StackPane(rect, text);
                block.getChildren().add(stackPane);
            }
            blocksBox.getChildren().add(block);
            heapSize--;
            heapify(arr, heapSize, 0);
        }
    }

    public void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Display the initial state of the array
    public void displayArray() {
        rectangles = new Rectangle[n];
        for (int i = 0; i < n; i++) {
            rectangles[i] = new Rectangle(30, 30, Color.WHITE);
            Text text = new Text(Integer.toString(arr[i]));
            StackPane stackPane = new StackPane(rectangles[i], text);
            grid.add(stackPane, i, 0);
        }
    }

    // Display the updated state of the array after a swap
    public void displayArray(int i, int j) {
        Rectangle rect1 = rectangles[i];
        Rectangle rect2 = rectangles[j];
        double x1 = rect1.getLayoutX();
        double x2 = rect2.getLayoutX();
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(rect1.layoutXProperty(), x2),
                        new KeyValue(rect2.layoutXProperty(), x1)));
        timeline.play();
        rectangles[i] = rect2;
        rectangles[j] = rect1;
    }

    // Display the final sorted array
    public void displaySortedArray() {
        grid.getChildren().clear();
        for (int i = 0; i < n; i++) {
            Rectangle rect = new Rectangle(30, 30, Color.WHITE);
            Text text = new Text(Integer.toString(arr[i]));
            StackPane stackPane = new StackPane(rect, text);
            grid.add(stackPane, i, 0);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
