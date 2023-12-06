package client;

import static org.junit.jupiter.api.Assertions.*;

import client.RecipeDetailScene.RecipeDetailView;
import java.util.*;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

class mockChatGPT implements API{
    String API;
    String TOKEN;
    String Model;
    String chatGPTAnswer;
    String prompt;
    // mockDallE imgGen;

    //Recipe: Title, mealType, Ingredients, img64

    public mockChatGPT(){
        chatGPTAnswer = "Chicken and Rice";
        prompt = "Chicken and Rice";
    }

  public void setInput(String input) {
    this.prompt = input;
  }

    public Recipe generate(String mealType, String ingredients){
        Recipe r = new Recipe("Chicken and Rice",mealType,ingredients);
        r.setImgBase64Str("imageFileHere");
        return r;
    }

    public Recipe regenerate(String mealType, String ingredients){
        chatGPTAnswer = "Rice and Chicken";
        Recipe r = new Recipe("Rice and Chicken",mealType,ingredients);
        r.setImgBase64Str("imageFileHere");
        return r;
    }

    public String getOutput(){
        return chatGPTAnswer;
    }

    public String getPrompt(){
        return prompt;
    }
}

class mockWhisper implements API {

  String API;
  String TOKEN;
  String Model;
  String FILE;
  boolean MealType;

  public mockWhisper(String audioFilePath) {
    FILE = audioFilePath;
    MealType = true;
  }

  public void setInput(String input) {
    FILE = input;
  }

  public String translateVoiceToText() {
    if (MealType == true) {
      MealType = false;
      return "Breakfast";
    } else {
      MealType = true;
      return "Bread, Sausages, and Eggs";
    }
  }

  public String endToEnd(){
    if (MealType == true) {
      MealType = false;
      return "Lunch";
    } else {
      MealType = true;
      return "bacon and eggs";
    }
  }

  public String getOutput() {
    return translateVoiceToText();
  }

  public String getOutput(String mealType, String ingredients){
    if (MealType == true) {
      MealType = false;
      return mealType;
    } else {
      MealType = true;
      return ingredients;
    }
  }
}

class mockVoiceRecorder implements API {

  String FILE;

  public mockVoiceRecorder() {
    //Init Stuff
  }

  public void setInput(String input) {
    FILE = input;
  }

    public String getOutput(){
        //Doesn't actually do voice recording
        return FILE;
    }
}

class mockDallE implements API{
    String prompt;
    String imageFile;
    public mockDallE(){

    }

    public void setInput(String input){
        this.prompt = input;
        imageFile = "imageFileHere";
    }

    public String getOutput(){
        //Doesn't actually do voice recording
        return imageFile;
    }
}
public class MockAPITest {

  @Test
  void testChatGPT() {
    mockChatGPT budgetChatGPT = new mockChatGPT();
    budgetChatGPT.setInput("Create a dinner recipe with Chicken, Rice");
    Recipe curr = budgetChatGPT.generate("Dinner", "Chicken, Rice");
    assertEquals("Dinner", curr.getMealType());
    assertEquals("Chicken and Rice", curr.getTitle());
    assertEquals("Chicken, Rice", curr.getRecipeDetail());
  }

  @Test
  void testWhisper() {
    mockWhisper whisper = new mockWhisper("lol.mp3");
    assertEquals("Breakfast", whisper.translateVoiceToText());
  }

    @Test void testVoiceRecorder(){
        mockVoiceRecorder vReg = new mockVoiceRecorder();
        vReg.setInput("recording.wav");
        assertEquals("recording.wav", vReg.getOutput());
    }

    @Test void testDallEAPI(){
        mockDallE imgCreator = new mockDallE();
        imgCreator.setInput("Give me a banana image");
        assertEquals("imageFileHere", imgCreator.getOutput());
    }

    @Test void addRecipeWithMockAPI(){
        mockChatGPT budgetChatGPT = new mockChatGPT();
        budgetChatGPT.setInput("Create a dinner recipe with Chicken, Rice");
        mockWhisper whisper = new mockWhisper("lol.mp3");
        mockDallE dallE = new mockDallE();
        dallE.setInput("Give me an image of Chicken and Rice");
        Recipe dummyRecipe = budgetChatGPT.generate(whisper.getOutput(), whisper.getOutput());
        assertEquals("Chicken and Rice", dummyRecipe.getTitle());
        assertEquals("Bread, Sausages, and Eggs", dummyRecipe.getRecipeDetail());
    }

    @Test void addRecipeWithRegeneration(){
        mockChatGPT budgetChatGPT = new mockChatGPT();
        budgetChatGPT.setInput("Create a dinner recipe with Chicken, Rice");
        mockWhisper whisper = new mockWhisper("lol.mp3");
        mockDallE dallE = new mockDallE();
        dallE.setInput("Give me an image of Chicken and Rice");
        Recipe dummyRecipe = budgetChatGPT.generate(whisper.getOutput(), whisper.getOutput());
        dummyRecipe.setImgBase64Str(dallE.getOutput());
        assertEquals("Chicken and Rice", dummyRecipe.getTitle());
        assertEquals("Bread, Sausages, and Eggs", dummyRecipe.getRecipeDetail());
        assertEquals("imageFileHere", dummyRecipe.getImgBase64Str());
        dallE.setInput(budgetChatGPT.getPrompt());
        Recipe regenRecipe = budgetChatGPT.regenerate(whisper.getOutput(), whisper.getOutput());
        regenRecipe.setRecipeID("12345");
        regenRecipe.setImgBase64Str(dallE.getOutput());
        assertEquals("Rice and Chicken", regenRecipe.getTitle()); //regenRecipe just changes Title
    }
}
