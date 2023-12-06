package client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.logging.*;

import client.RecipeListScene.RecipeListView;

public class FilterByMealTypeTest {
    private AppController appController;
    private RecipeListView recipeListView;

    @BeforeEach
    public void setUp() {
        // set up objects used for testing
        recipeListView = new RecipeListView(true);
        appController = new AppController();
    }

    @Test
    public void testFilterRecipesByMealType() {
        // add recipes
        addRecipesForTest();

        // test filter for breakfast
        verifyFilteredRecipes(appController.handleFilter("Breakfast"),"Breakfast");
        // test filter for lunch
        verifyFilteredRecipes(appController.handleFilter("Lunch"), "Lunch");
        // test filter for dinner
        verifyFilteredRecipes(appController.handleFilter("Dinner"), "Dinner");
        // test for reset filter (should show all)
        List <Recipe> reset = appController.handleFilter("Reset Filter");
        reset.equals(appController.getRecipeList());

    }

    // Method to create recipes
    private void addRecipesForTest() {
        appController.addNewRecipeToList(new Recipe("Pancakes", "Breakfast", "Description 1"));
        appController.addNewRecipeToList(new Recipe("Spaghetti", "Lunch", "Description 2"));
        appController.addNewRecipeToList(new Recipe("Burger", "Dinner","Description 3"));
        appController.addNewRecipeToList(new Recipe("Omelette", "Breakfast", "Description 4"));
        appController.addNewRecipeToList(new Recipe("Salad", "Lunch", "Description 5"));
        appController.addNewRecipeToList(new Recipe("Steak", "Dinner", "Description 6"));
    }

    // Method to check if a list of recipes follow a certain meal type
    private void verifyFilteredRecipes(List<Recipe> displayedRecipes, String mealType) {
        for (Recipe recipe : displayedRecipes) {
            if (mealType != null) {
                assertEquals(mealType, recipe.getMealType(), "Mismatch in filtered recipes");
            } else {
            assertNull(recipe.getMealType(), "Mismatch in filtered recipes");
            }
        }
    }

}
