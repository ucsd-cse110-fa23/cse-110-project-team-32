package client.CreateRecipeScene;

import client.AppController;
import client.Recipe;
import java.io.File;
import javafx.event.ActionEvent;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.TargetDataLine;

public class CreateRecipeController {

  CreateRecipeView createRecipeView;
  CreateRecipeModel createRecipeModel;
  AppController appController;
  private AudioFormat audioFormat;
  private TargetDataLine targetDataLine;
  private static final String audioFilePath = "recording.wav";

  public CreateRecipeController(
    CreateRecipeView createRecipeView,
    CreateRecipeModel createRecipeModel,
    AppController appController
  ) {
    this.createRecipeView = createRecipeView;
    this.createRecipeModel = createRecipeModel;
    this.appController = appController;
    this.audioFormat = this.setUpAudioFormat();

    createRecipeView.setCancelButtonAction(this::handleCancelButtonAction);
    createRecipeView.setStartRecordingButtonAction(
      this::handleStartRecordingButtonAction
    );
    createRecipeView.setStopRecordingButtonAction(
      this::handleStopRecordingButtonAction
    );
    createRecipeView.setCreateDummyRecipeButtonAction(
      this::handleCreateDummyRecipeButtonAction
    );
  }

  private AudioFormat setUpAudioFormat() {
    // the number of samples of audio per second. 44100 represents the typical sample rate for CD-quality audio.
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
      bigEndian
    );
  }

  private void handleCancelButtonAction(ActionEvent event) {
    createRecipeView.reset();
    appController.changeToRecipeListScene();
  }

  private void handleStartRecordingButtonAction(ActionEvent event) {
    Thread t = new Thread(
      new Runnable() {
        @Override
        public void run() {
          try {
            // the format of the TargetDataLine
            DataLine.Info dataLineInfo = new DataLine.Info(
              TargetDataLine.class,
              audioFormat
            );
            // the TargetDataLine used to capture audio data from the microphone
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            targetDataLine.open(audioFormat);
            targetDataLine.start();
            createRecipeView.toggleDisplayToolTip(true);

            // the AudioInputStream that will be used to write the audio data to a file
            AudioInputStream audioInputStream = new AudioInputStream(
              targetDataLine
            );

            // the file that will contain the audio data
            File audioFile = new File(audioFilePath);
            AudioSystem.write(
              audioInputStream,
              AudioFileFormat.Type.WAVE,
              audioFile
            );
            createRecipeView.toggleDisplayToolTip(false);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    );
    t.start();
  }

  private void handleStopRecordingButtonAction(ActionEvent event) {
    targetDataLine.stop();
    targetDataLine.close();
    String text = "";
    try {
      text = createRecipeModel.translateByWhisper(audioFilePath).getResponse();
      if (
        text != null && text == "YIKES! You are not connected to the server!!"
      ) {
        createRecipeView.setRecordingErrorMsg(text);
        return;
      }
      if (text == null || text.length() == 0) {
        createRecipeView.setRecordingErrorMsg(
          "Oops... I didn't hear what you said. Please record again :>"
        );
        return;
      }

      if (
        createRecipeView.getMealType() == null ||
        createRecipeView.getMealType().equals("")
      ) {
        // System.out.println("0");
        if (
          !text.equals("breakfast") &&
          !text.equals("lunch") &&
          !text.equals("dinner")
        ) {
          // System.out.println("1");
          createRecipeView.setRecordingErrorMsg(
            "Oops... I couldn't recognize the meal type you said. Please try again :>"
          );
        } else {
          // System.out.println("2");
          // System.out.println("recorded: " + text);
          createRecipeView.switchPrompt();
          createRecipeView.setRecordedMealType(text);
        }
      } else {
        // System.out.println("3");
        createRecipeView.setRecordedIngredients(text);

        Recipe newRecipe = createRecipeModel.generateByChatGPT(
          createRecipeView.getMealType(),
          createRecipeView.getIngredients(),
          false
        );
        appController.changeToRecipeDetailScene(newRecipe, true);
        createRecipeView.reset();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleCreateDummyRecipeButtonAction(ActionEvent event) {
    Recipe dummy = createRecipeModel.generateByChatGPT(
      "lunch",
      "chicken thighs",
      true
    );
    // Recipe dummy = new Recipe("Dummy Recipe Title", "Dummy Recipe Meal Type", "Dummy Recipe Detail");
    appController.changeToRecipeDetailScene(dummy, true);
    createRecipeView.reset();
  }
}
