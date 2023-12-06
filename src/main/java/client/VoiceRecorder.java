package client;

import client.CreateRecipeScene.*;
import client.RecipeDetailScene.*;
import client.RecipeListScene.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sound.sampled.*;
import server.ChatGPT;
import server.Whisper;

public class VoiceRecorder implements API {

  private AudioFormat audioFormat;
  private TargetDataLine targetDataLine;
  // private static final String audioFilePath = "recording.wav";
  private String path;
  private CreateRecipeView crv;
  private ChatGPT chatGPT;
  private Whisper whisper;

  public VoiceRecorder(CreateRecipeView crv) {
    this.crv = crv;
    audioFormat = setUpAudioFormat();
    chatGPT = new ChatGPT();
    whisper = new Whisper(path);
  }

  @Override
  public void setInput(String inputFile) {
    this.path = inputFile;
  }

  @Override
  public String getOutput() {
    startRecording();
    stopRecording();
    return this.path;
  }

  // Setter for action when audio recording start button is clicked
  public void startRecording() {
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
            crv.toggleDisplayToolTip(true);

            // the AudioInputStream that will be used to write the audio data to a file
            AudioInputStream audioInputStream = new AudioInputStream(
              targetDataLine
            );

            // the file that will contain the audio data
            File audioFile = new File(path);
            AudioSystem.write(
              audioInputStream,
              AudioFileFormat.Type.WAVE,
              audioFile
            );
            crv.toggleDisplayToolTip(false);
          } catch (Exception ex) {
            ex.printStackTrace();
          }
        }
      }
    );
    t.start();
  }

  // Setter for action when audio recording stop button is clicked
  public void stopRecording() {
    targetDataLine.stop();
    targetDataLine.close();
  }

  private AudioFormat setUpAudioFormat() {
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
      bigEndian
    );
  }
}
