package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Controller {
    @FXML private FlowPane letterpane;
    @FXML private HBox useranswer;
    private String[] wordlist = {"APPLE","BALL","CAT","ORANGES","DOLL","AVOCADO","ELEPHANTS","EGG","FISH","FAN","FROG","HORSE","HAIR","ICE","IGLOO",
    		"JUG","JADE","JELLY","KANGAROO","KERNEL","LEMON","MANGO","NAIL","PENGUIN","QUEEN","ROSE","RAT","SNAKE","SNOW",
    		"TRAIN","TAIL","UMBRELLA","VIOLET","WAX","XYLOPHONE","ZOO"};
    private String correct_word;
    private StringBuilder currentGuess = new StringBuilder();
    private boolean guessAttempted = false;
    
    public void initialize() {
        setupgame();
    }
    
    private void setupgame() {
        currentGuess.setLength(0);
        guessAttempted = false;
        Random random = new Random();
        correct_word = wordlist[random.nextInt(wordlist.length)];
        
        List<Character> letters = new ArrayList<>();
        for (char c : correct_word.toCharArray()) {
            letters.add(c);
        }
        Collections.shuffle(letters);
        letterpane.getChildren().clear();
        
        for (char c : letters) {
            Button letterButton = new Button(String.valueOf(c));
            letterButton.setOnAction(e -> handleLetterClick(letterButton));
            letterpane.getChildren().add(letterButton);
        }
        updateanswerdisplay();
    }
    
    private void updateanswerdisplay() {
        useranswer.getChildren().clear();
        String guess = currentGuess.toString();

        for (int i = 0; i < correct_word.length(); i++) {
            javafx.scene.control.Label letterLabel = new javafx.scene.control.Label();
            if (i < guess.length()) {
                letterLabel.setText(String.valueOf(guess.charAt(i)));
            } else {
                letterLabel.setText(" ");
            }
            letterLabel.setStyle("-fx-font-size: 24; -fx-min-width: 20; -fx-alignment: center;");
            useranswer.getChildren().add(letterLabel);
        }
    }
    
    private void handleLetterClick(Button button) {
        currentGuess.append(button.getText());
        updateanswerdisplay();
        button.setDisable(true);

        if (currentGuess.length() == correct_word.length()) {
            if (currentGuess.toString().equalsIgnoreCase(correct_word)) {
                showCongratsPopup();
            } else {
                if (!guessAttempted) {
                    guessAttempted = true;
                    showTryAgainPopup();
                } else {
                    // Second wrong attempt - maybe show game over?
                    showTryAgainPopup(); // Or create a game over popup
                }
            }
        }
    }

    private void showCongratsPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("congrats.fxml"));
            Parent root = loader.load();
            
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(letterpane.getScene().getWindow());
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Congratulations!");
            popupStage.setResizable(false);
            popupStage.showAndWait();
            
            // Reset the game after popup closes
            setupgame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showTryAgainPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tryAgain.fxml"));
            Parent root = loader.load();
            
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initOwner(letterpane.getScene().getWindow());
            popupStage.setScene(new Scene(root));
            popupStage.setTitle("Try Again");
            popupStage.setResizable(false);
            popupStage.showAndWait();
            
            // After Try Again popup closes, reset for another attempt
            currentGuess.setLength(0);
            updateanswerdisplay();
            letterpane.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    ((Button) node).setDisable(false);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}