package client.AddRecipe;

import client.Recipe;
import client.RecipeList.RecipeList;
import client.RecipeList.RecipeListView;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.io.*;
import javax.sound.sampled.*;

public class AddRecipe extends VBox {
    private static final String audioFilePath = "recording.wav";
    private ChatGPT chatGPT = new ChatGPT();
    private Whisper whisper = new Whisper(audioFilePath);
    private Recipe newRecipe;

    private Button mealTypeStartButton;
    private Button mealTypeStopButton;
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private Label recordingLabel;
    private Text voiceInputPrompt;
    private Label generateRecipeErrorLabel;
    private Button generateRecipeButton;
    
    private Text recordedMealType;
    private Text recordedIngredients;
    private Text mealTypeErrorMsg;

    private String ingredients;
    private String mealType;
    String defaultButtonStyle = "-fx-border-color: #000000; -fx-font: 13 arial; -fx-pref-width: 175px; -fx-pref-height: 50px;";
    String defaultLabelStyle = "-fx-font: 13 arial; -fx-pref-height: 50px; -fx-text-fill: red; visibility: hidden";

    public AddRecipe() {
        // Get the audio format
        audioFormat = getAudioFormat();
        setUpAddRecipeForm();
    }

    private AudioFormat getAudioFormat() {
        // the number of samples of audio per second.
        // 44100 represents the typical sample rate for CD-quality audio.
        float sampleRate = 44100;
        // the number of bits in each sample of a sound that has been digitized.
        int sampleSizeInBits = 16;
        // the number of audio channels in this format (1 for mono, 2 for stereo).
        int channels = 1;
        // whether the data is signed or unsigned.
        boolean signed = true;
        // whether the audio data is stored in big-endian or little-endian order.
        boolean bigEndian = false;
        return new AudioFormat(
            sampleRate,
            sampleSizeInBits,
            channels,
            signed,
            bigEndian);
    }

    private void setUpAddRecipeForm() {
        this.getChildren().clear();
        // Add the buttons and text fields
        this.voiceInputPrompt = new Text("Please record the meal type from \"Breakfast\", \"Lunch\", and \"Dinner\"");
        this.mealTypeErrorMsg = new Text("Oops, please record the meal type again");
        this.mealTypeErrorMsg.setStyle("-fx-text-fill: red; visibility: hidden");
        
        this.recordedMealType = new Text("");
        this.recordedMealType.setVisible(false);
        this.recordedIngredients = new Text("");
        this.recordedIngredients.setVisible(false);
        this.getChildren().addAll(voiceInputPrompt, mealTypeErrorMsg, recordedMealType, recordedIngredients);
        mealTypeStartButton = new Button("Start");
        mealTypeStartButton.setStyle(defaultButtonStyle);
        mealTypeStartButton.setOnAction(e -> {
            startRecording();
        });
        mealTypeStopButton = new Button("Stop");
        mealTypeStopButton.setStyle(defaultButtonStyle);
        mealTypeStopButton.setOnAction(e -> {
            stopRecording();
        });
        recordingLabel = new Label("Recording...");
        recordingLabel.setStyle(defaultLabelStyle);
        HBox buttonGroup = new HBox(mealTypeStartButton, mealTypeStopButton);
        
        this.getChildren().addAll(buttonGroup, recordingLabel);

        generateRecipeButton = new Button("Generate Recipe");
        generateRecipeButton.setStyle(defaultButtonStyle);
        generateRecipeErrorLabel = new Label("Error! Please record the meal type and your ingredients!");
        generateRecipeErrorLabel.setStyle(defaultLabelStyle);
        generateRecipeButton.setOnAction(e -> {
            try {
                if (this.mealType == null || this.ingredients == null) {
                    generateRecipeErrorLabel.setVisible(true);
                    return;
                }
                this.newRecipe = this.chatGPT.generate(mealType, ingredients);
                // this.newRecipe = this.chatGPT.generate(true);
                this.getChildren().add(new Text(this.newRecipe.getRecipeDetail()));
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
            
        });
        
        this.getChildren().addAll(generateRecipeButton);
        this.getChildren().addAll(generateRecipeErrorLabel);
    }

    public Recipe createNewRecipe() {
        // Recipe recipe = new Recipe(getTitle(), getMealType(), getRecipeDetail());
        clearForm();
        // setUpAddRecipeForm();
        return this.newRecipe;
    }

    private void clearForm() {
        this.getChildren().clear();
    }

    private void startRecording() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // the format of the TargetDataLine
                    DataLine.Info dataLineInfo = new DataLine.Info(
                        TargetDataLine.class,
                        audioFormat);
                    // the TargetDataLine used to capture audio data from the microphone
                    targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                    targetDataLine.open(audioFormat);
                    targetDataLine.start();
                    recordingLabel.setVisible(true);

                    // the AudioInputStream that will be used to write the audio data to a file
                    AudioInputStream audioInputStream = new AudioInputStream(
                        targetDataLine);

                    // the file that will contain the audio data
                    File audioFile = new File(audioFilePath);
                    AudioSystem.write(
                        audioInputStream,
                        AudioFileFormat.Type.WAVE,
                        audioFile);
                    recordingLabel.setVisible(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.start();
    }

    private void stopRecording() {
        targetDataLine.stop();
        targetDataLine.close();
        String text = "";
        try {
            text = whisper.translateVoiceToText().toLowerCase();
            text = text.charAt(text.length()-1) == '.' ? text.substring(0, text.indexOf('.')) : text;
            if (this.mealType == null || this.mealType.equals("")) {
                System.out.println("0");
                System.out.println("|" + text + "|");

                if (!text.equals("breakfast") && !text.equals("lunch") && !text.equals("dinner")) {
                    System.out.println("1");
                    System.out.println("meal type error msg: " + this.mealTypeErrorMsg.getText());
                    this.mealTypeErrorMsg.setVisible(true);
                } else {
                    System.out.println("2");
                    System.out.println("recorded: " + text);
                    this.mealTypeErrorMsg.setVisible(false);
                    this.mealType = text;
                    this.voiceInputPrompt.setText("Please record your ingredients");
                    recordedMealType.setText("Recorded Meal Type: " + mealType);
                    this.recordedMealType.setVisible(true);
                }
            } else {
                System.out.println("3");
                this.ingredients = text;
                this.recordedIngredients.setText("Recorded ingredients: " + ingredients);
                this.recordedIngredients.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}

class Input extends HBox {
    private Text label;
    private TextField inputField;
    public Input(String label) {
        this.label = new Text(label);
        this.inputField = new TextField();
        this.getChildren().addAll(this.label, this.inputField);
    }
    public String getInput() {
        return this.inputField.getText();
    }
}