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
        // Re-write this test with functions in AppController, see doc.txt :>  -Li
        /* 
        Recipe recipe = new Recipe("Chicken and Rice", "Dinner", "Its Chicken and Rice man...");
        // RecipeList lst = new RecipeList();
        RecipeList lst = new RecipeList(recipe);
        lst.addRecipeToList(recipe);
        assertEquals(recipe, lst.getMostRecentRecipe());
        Recipe recipe2 = new Recipe("Curry", "Dinner", "I'm hungry for curry");
        lst.addRecipeToList(recipe2);
        Recipe recipe3 = new Recipe("Ramen", "Lunch", "Instant Ramen cuz we college students");

        assertEquals(true, lst.containsRecipe(recipe2));
        assertEquals(false, lst.containsRecipe(recipe3));
        */
        AppController appController = new AppController();
        List<Recipe> recipeList = appController.getRecipeList();
        assertEquals(recipeList.size(), 0);
        appController.addNewRecipeToList(new Recipe("recipe 1", "meal type 1", "detail1"));
        recipeList = appController.getRecipeList();
        assertEquals(recipeList.size(), 1);
    }
}
