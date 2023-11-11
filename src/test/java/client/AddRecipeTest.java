package client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class AddRecipeTest {
    @Test void addRecipeTest(){
        Recipe recipe = new Recipe("Chicken and Rice", "Dinner", "Its Chicken and Rice man...");
        assertEquals(recipe.getTitle(), "Chicken and Rice");
        assertEquals(recipe.getMealType(), "Dinner");
        assertEquals(recipe.getRecipeDetail(), "Its Chicken and Rice man...");
    }

    //RecipeList basic test 
    @Test void addStoredInRecipeListTest(){
        AppController appController = new AppController();
        List<Recipe> recipeList = appController.getRecipeList();
        assertEquals(recipeList.size(), 0);
        appController.addNewRecipeToList(new Recipe("recipe 1", "meal type 1", "detail1"));
        recipeList = appController.getRecipeList();
        assertEquals(recipeList.size(), 1);
    }

    //Test if RecipeList's first Item is most Recently added Recipe()
    @Test void recipeListFirstItemIsAddedRecipe(){
        AppController appController = new AppController();
        List<Recipe> recipeList;
        Recipe r1 = new Recipe("Title1", "MealType1", "Details1");
        Recipe r2 = new Recipe("Title2", "MealType2", "Details2");
        Recipe r3 = new Recipe("Title3", "MealType3", "Details3");

        appController.addNewRecipeToList(r1);
        appController.addNewRecipeToList(r3);
        recipeList = appController.getRecipeList();
        assertEquals(r3, recipeList.get(0));
        appController.addNewRecipeToList(r2);
        recipeList = appController.getRecipeList();
        assertEquals(r2, recipeList.get(0));
    }

    @Test
    public void deleteRecipeFromListTest() {
        // Implement here :) there is a addNewRecipe and a deleteRecipeFromList function in appController
        AppController appController = new AppController();
        List<Recipe> recipeList;
        Recipe r1 = new Recipe("Title1", "MealType1", "Details1");
        Recipe r2 = new Recipe("Title2", "MealType2", "Details2");
        Recipe r3 = new Recipe("Title3", "MealType3", "Details3");  
        
        appController.addNewRecipeToList(r1);
        appController.addNewRecipeToList(r2);
        appController.addNewRecipeToList(r3);
        recipeList =  appController.getRecipeList();
        assertEquals(r3, recipeList.get(0));
        appController.removeRecipeFromRecipeList(r3);
        recipeList =  appController.getRecipeList();
        assertEquals(r2, recipeList.get(0));
        appController.removeRecipeFromRecipeList(r1);
        recipeList = appController.getRecipeList();
     }

     //TODO: implement Edit Recipe 
     
}
