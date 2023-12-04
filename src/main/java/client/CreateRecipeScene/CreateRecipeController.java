package client.CreateRecipeScene;

import client.AppController;
import client.HttpResponse.CreateRecipeResponse;
import client.HttpResponse.ServerResponse;
import client.HttpResponse.WhisperResponse;
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
    createRecipeView.setLogOutButtonAction(this::handleLogOutButton);
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

  private void handleLogOutButton(ActionEvent event) {
    appController.changeToLogInScene();
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
    ServerResponse<String> whisperResponse = new WhisperResponse();
    try {
      whisperResponse = createRecipeModel.translateByWhisper(audioFilePath);
      if (whisperResponse.getStatusCode() == 503) {
        appController.handleServerDown();
        return;
      }
      if (whisperResponse.getStatusCode() != 200) {
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
          !whisperResponse.getResponse().equals("breakfast") &&
          !whisperResponse.getResponse().equals("lunch") &&
          !whisperResponse.getResponse().equals("dinner")
        ) {
          // System.out.println("1");
          createRecipeView.setRecordingErrorMsg(
            "Oops... I couldn't recognize the meal type you said. Please try again :>"
          );
        } else {
          // System.out.println("2");
          // System.out.println("recorded: " + text);
          createRecipeView.switchPrompt();
          createRecipeView.setRecordedMealType(whisperResponse.getResponse());
        }
      } else {
        // System.out.println("3");
        createRecipeView.setRecordedIngredients(whisperResponse.getResponse());

        ServerResponse<Recipe> createRecipeRes = createRecipeModel.generateByChatGPT(
          createRecipeView.getMealType(),
          createRecipeView.getIngredients(),
          false
        );
        System.out.println(createRecipeRes);
        if (createRecipeRes.getStatusCode() == 503) {
          // server is suddenly down, nav to log in and display message

          System.out.println("!!!!!!!!!!!");
          System.out.println("Server down!");
          System.out.println(createRecipeRes.getErrorMsg());
          System.out.println("!!!!!!!!!!!");
          appController.handleServerDown();
          return;
        }
        if (createRecipeRes.getStatusCode() == 200) {
          appController.changeToRecipeDetailScene(
            createRecipeRes.getResponse(),
            true
          );
          createRecipeView.reset();
        } else {
          System.out.println("!!!!!!!!!!!");
          System.out.println("Server ERROR!");
          System.out.println(createRecipeRes.getErrorMsg());
          System.out.println("!!!!!!!!!!!");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void handleCreateDummyRecipeButtonAction(ActionEvent event) {
    ServerResponse<Recipe> dummyRes = createRecipeModel.generateByChatGPT(
      "lunch",
      "chicken thighs",
      true
    );
    // Recipe dummy = new Recipe("Dummy Recipe Title", "Dummy Recipe Meal Type", "Dummy Recipe Detail");
    appController.changeToRecipeDetailScene(dummyRes.getResponse(), true);
    createRecipeView.reset();
  }
}
