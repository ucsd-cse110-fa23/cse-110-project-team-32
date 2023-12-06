package client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import client.HttpResponse.AuthResponse;
import client.HttpResponse.ServerResponse;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class E2ETest {
    public static final AppController appController = new AppController();


    @Test void JohnUsesPP2(){
        AppController app = new AppController();
        List<Recipe> currRecipes;
        mockChatGPT gpt = new mockChatGPT();
        mockWhisper whisp = new mockWhisper("lol.mp3");
        mockVoiceRecorder vRec = new mockVoiceRecorder();  
        vRec.setInput("lol.mp3");
        mockDataBase mangaDB = new mockDataBase();  
        ServerResponse<Boolean> logInResponse = new AuthResponse();
        logInResponse.setServerDownResponse();
        assertEquals(false, logInResponse.getResponse());
        assertEquals("The server is Down!", logInResponse.getErrorMsg());
        assertEquals(503, logInResponse.getStatusCode());       
        logInResponse.setValidResponse("Server operates!");
        assertEquals(true, logInResponse.getResponse());
        assertEquals(200, logInResponse.getStatusCode());

        
    }

    @Test
    public void EndToEndTest1(){
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
    assertEquals("Chicken and Rice", currRecipes.get(0).getTitle());
    assertEquals("Breakfast", currRecipes.get(0).getMealType());
    assertEquals("Bread, Sausages, and Eggs", currRecipes.get(0).getRecipeDetail());
    //Nows Lets edit this recipe:
    // RecipeDetailView rdv = new RecipeDetailView();
    r1.setRecipeDetail("Bacon, Cheese, Rice");
    assertEquals("Bacon, Cheese, Rice", currRecipes.get(0).getRecipeDetail());
    Recipe r2 = gpt.generate(mealType, ingredients);
    app.addNewRecipeToList(r2);
    currRecipes = app.getRecipeList();
    assertEquals("Bread, Sausages, and Eggs", currRecipes.get(0).getRecipeDetail());
    app.removeRecipeFromRecipeList(r2);
    currRecipes = app.getRecipeList();
    assertEquals("Bacon, Cheese, Rice", currRecipes.get(0).getRecipeDetail());
    app.removeRecipeFromRecipeList(r1);
    currRecipes = app.getRecipeList();
    assertEquals(0, currRecipes.size());
    // Recipe Detail View

    
    }
    
}
