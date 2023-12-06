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
        //Checks for invalid server response
        assertEquals(false, logInResponse.getResponse());
        assertEquals("The server is Down!", logInResponse.getErrorMsg());
        assertEquals(503, logInResponse.getStatusCode());       
        logInResponse.setValidResponse("Server operates!");
        //Checks for valid server response
        assertEquals(true, logInResponse.getResponse());
        assertEquals(200, logInResponse.getStatusCode());

        String logInAttempt = mangaDB.logIn("John","password",false);
        assertEquals("Incorrect Username + Password", logInAttempt);
        //John creates his account now...
        String createAccSuccess = mangaDB.addAccount("John", "password");
        assertEquals("Added Account successfully.", createAccSuccess);
        logInAttempt = mangaDB.logIn("John","password",false);
        assertEquals("Logged In Successfully", logInAttempt);

        //Now he's at his recipeList and creates some recipes
        vRec.setInput("lol.mp3");

        String audioFile = vRec.getOutput();
        whisp.setInput(audioFile);
        String mealType = whisp.getOutput("Lunch", "Bacon Eggs");
        audioFile = vRec.getOutput();
        String ingredients = whisp.getOutput("Lunch", "Bacon Eggs");
        Recipe r1 = gpt.generate(mealType, ingredients);
        assertEquals("Lunch", r1.getMealType());
        assertEquals("Bacon Eggs", r1.getRecipeDetail());
        r1 = gpt.regenerate(mealType, ingredients);
        assertEquals("Rice and Chicken", r1.getTitle());
        assertEquals("Lunch", r1.getMealType());
        assertEquals("Bacon Eggs", r1.getRecipeDetail());
        List<Recipe> lst = app.getRecipeList();

        app.addNewRecipeToList(r1);
        mangaDB.setRecipeList("John", lst);
        mangaDB.storeRecipes(r1);
        mangaDB.editRecipe(r1, "Bacon Eggs, Cooks eggs then Bacon");
        assertEquals("Bacon Eggs, Cooks eggs then Bacon", mangaDB.getRecipeDetails(r1));
        whisp.setInput(audioFile);
        mealType = whisp.getOutput("Dinner", "Chicken and Rice");
        audioFile = vRec.getOutput();
        ingredients = whisp.getOutput("Dinner", "Chicken and Rice");
        Recipe r2 = gpt.generate(mealType, ingredients);
        app.addNewRecipeToList(r2);
        lst = app.getRecipeList();
        assertEquals(2, lst.size());
        // System.out.println("RECIPELIST after 2nd recipe insertion: " + lst.size());
        // System.out.println(lst.get(0).getRecipeDetail());
        mangaDB.setRecipeList("John", lst);
        mangaDB.storeRecipes(r2);
        assertEquals("Dinner", lst.get(lst.indexOf(r2)).getMealType());

        //BUGGY CODE
        // assertEquals(1,app.reverseSortRecipesByDate(null));
        // app.sortRecipesByDate(null);
        app.sortRecipesByDate(null);
        lst = app.getRecipeList();
        assertEquals("Rice and Chicken",lst.get(0).getTitle());
        assertEquals("Bacon Eggs, Cooks eggs then Bacon", lst.get(0).getRecipeDetail());
        assertEquals(2, lst.size());
        assertEquals(1, app.handleFilter("Dinner").size());
        assertEquals("Chicken and Rice", app.handleFilter("Dinner").get(0).getTitle());
        assertEquals(2,app.getRecipeList().size());
        app.reverseSortRecipesByDate(null);
        // app.handleFilter("reset filter");
        // assertEquals("Bacon Eggs, Cooks eggs then Bacon",app.handleFilter("Breakfast").get(0).getRecipeDetail());

        //Share Recipe 
        String shareURL = mangaDB.shareRecipe(r2);
        assertEquals("Chicken and Rice.html",shareURL);
        shareURL = mangaDB.shareRecipe(r1);
        assertEquals("Rice and Chicken.html", shareURL);
        mangaDB.logOut();
        //Assume account exists for user:<test> password:<123>
        mangaDB.logIn("test", "123", true);

        assertEquals("Logged In Successfully", mangaDB.logIn());
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
