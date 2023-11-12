package client;
import org.junit.jupiter.api.Test;

import javafx.scene.text.Text;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class mockChatGPT implements API{
    String API;
    String TOKEN;
    String Model;
    String chatGPTAnswer;
    String prompt;
    public mockChatGPT(){
        // chatGPTAnswer = prompt;
    }

    public void setInput(String input){
        this.prompt = input;
    }

    public Recipe generate(String mealType, String ingredients){
        return new Recipe("Chicken and Rice",mealType,ingredients);
    }

    public String getOutput(){
        return "Chicken and Rice";
    }
}

class mockWhisper implements API{
    String API;
    String TOKEN;
    String Model;
    String FILE;
    boolean MealType;
    public mockWhisper(String audioFilePath){
        FILE = audioFilePath;
        MealType = true;
    }

    public void setInput(String input){
        FILE = input;
    }

    public String translateVoiceToText(){
        if(MealType == true){
            MealType = false;
            return "Breakfast";
        }
        else {
            MealType = true;
            return "Bread, Sausages, and Eggs";
        }
    }
    
    public String getOutput(){
        return translateVoiceToText();
    }
}

class mockVoiceRecorder implements API{
    String FILE;

    public mockVoiceRecorder(){
        //Init Stuff
    }

    public void setInput(String input){
        FILE = input;
    }

    public String getOutput(){
        //Doesn't actually do voice recording
        return FILE;
    }


}
public class MockAPITest {
    @Test void testChatGPT(){
        mockChatGPT budgetChatGPT = new mockChatGPT();
        budgetChatGPT.setInput("Create a dinner recipe with Chicken, Rice");
        Recipe curr = budgetChatGPT.generate("Dinner", "Chicken, Rice");
        assertEquals("Dinner", curr.getMealType());
        assertEquals("Chicken and Rice", curr.getTitle());
        assertEquals("Chicken, Rice", curr.getRecipeDetail());
    }

    @Test void testWhisper(){
        mockWhisper whisper = new mockWhisper("lol.mp3");
        assertEquals("Breakfast", whisper.translateVoiceToText());
    }

    @Test void addRecipeWithMockAPI(){
        mockChatGPT budgetChatGPT = new mockChatGPT();
        budgetChatGPT.setInput("Create a dinner recipe with Chicken, Rice");
        mockWhisper whisper = new mockWhisper("lol.mp3");
        Recipe dummyRecipe = budgetChatGPT.generate(whisper.getOutput(), whisper.getOutput());
        assertEquals("Chicken and Rice", dummyRecipe.getTitle());
        assertEquals("Breakfast", dummyRecipe.getRecipeDetail());
    }

    @Test void EndToEndTest1(){
    AppController app = new AppController();
    List<Recipe> currRecipes;
    mockChatGPT gpt = new mockChatGPT();
    mockWhisper whisp = new mockWhisper("lol.mp3");
    mockVoiceRecorder vRec = new mockVoiceRecorder();
    vRec.setInput("lol.mp3");

    String audioFile = vRec.getOutput();
    whisp.setInput(audioFile);
    String mealType = whisp.getOutput();

    audioFile = vRec.getOutput();
    String ingredients = whisp.getOutput();

    Recipe r1 = gpt.generate(mealType, ingredients);

    app.addNewRecipeToList(r1);
    //Checks for Recipe Title
    currRecipes = app.getRecipeList();
    assertEquals("Chicken and Rice", r1.getTitle());
    assertEquals("Breakfast",r1.getMealType());
    assertEquals("Bread, Sausages, and Eggs", r1.getRecipeDetail());
    
    Recipe r2 = gpt.generate(mealType, ingredients);

    app.addNewRecipeToList(r2);
    }
}
