package client;
import org.junit.jupiter.api.Test;
import client.AddRecipe.AddRecipe;
import client.AddRecipe.API;
import javafx.scene.text.Text;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class mockChatGPT implements API{
    String API;
    String TOKEN;
    String Model;
    String chatGPTAnswer;
    public mockChatGPT(String prompt){
        chatGPTAnswer = prompt;
    }

    @Override
    public API initializeAPI(String API, String tokens, String model) {
        this.API = API;
        this.TOKEN = tokens;
        this.Model = model;
        return this;
    }

    public Recipe generate(String mealType, String ingredients){
        return new Recipe("Chicken and Rice",mealType,ingredients);
    }

    public String getInfo(){
        return "Chicken and Rice";
    }
}

class mockWhisper implements API{
    String API;
    String TOKEN;
    String Model;
    String FILE;
    public mockWhisper(String audioFilePath){
        FILE = audioFilePath;
    }
    @Override
    public API initializeAPI(String API, String tokens, String model) {
        this.API = API;
        this.TOKEN = tokens;
        this.Model = model;
        return this;
    }

    public String translateVoiceToText(){
        return "I'm Bad at Coding!!!";
    }
    
    public String getInfo(){
        return translateVoiceToText();
    }
}
public class MockAPITest {
    @Test void testChatGPT(){
        mockChatGPT budgetChatGPT = new mockChatGPT("Create a dinner recipe with Chicken, Rice");
        budgetChatGPT.initializeAPI("API-Link", "token", "model");
        Recipe curr = budgetChatGPT.generate("Dinner", "Chicken, Rice");
        assertEquals("Dinner", curr.getMealType());
        assertEquals("Chicken and Rice", curr.getTitle());
        assertEquals("Chicken, Rice", curr.getRecipeDetail());
    }

    @Test void testWhisper(){
        mockWhisper whisper = new mockWhisper("lol.mp3");
        whisper.initializeAPI("API-Link", "token", "model");
        assertEquals("I'm Bad at Coding!!!", whisper.translateVoiceToText());
    }

    @Test void addRecipeWithMockAPI(){
        mockChatGPT budgetChatGPT = new mockChatGPT("Create a dinner recipe with Chicken, Rice");
        budgetChatGPT.initializeAPI("API-Link", "token", "model");
        mockWhisper whisper = new mockWhisper("lol.mp3");
        whisper.initializeAPI("API-Link", "token", "model");
        AddRecipe test = new AddRecipe(budgetChatGPT,whisper);
        assertEquals("Chicken and Rice",((Text) test.getChildren().get(0)).getText());
        assertEquals("I'm Bad at Coding!!!", ((Text) test.getChildren().get(1)).getText());
    }
}
