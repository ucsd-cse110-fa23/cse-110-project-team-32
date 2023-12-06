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

public class SortTest {
    private AppController appController;
    private RecipeListView recipeListView;

    @BeforeEach
    public void setUp() {
        // set up objects used for testing
        recipeListView = new RecipeListView(true);
        appController = new AppController();
    }

    @Test
    void testSortRecipesByTitle() {
        // Create a list of recipes in unsorted order
        addRecipesForTest();

        appController.sortRecipesByTitle(null);
        List<Recipe> sortedRecipes = appController.getRecipeList();

        // Assert that the recipes are sorted in ascending order by title
        assertEquals("Burger", sortedRecipes.get(0).getTitle());
        assertEquals("Omelette", sortedRecipes.get(1).getTitle());
        assertEquals("Pancakes", sortedRecipes.get(2).getTitle());
        assertEquals("Salad", sortedRecipes.get(3).getTitle());
        assertEquals("Spaghetti", sortedRecipes.get(4).getTitle());
        assertEquals("Steak", sortedRecipes.get(5).getTitle());
    }

    @Test
    void testSortRecipesByReversedTitle() {
        // Create a list of recipes in unsorted order
        addRecipesForTest();

        appController.reverseSortRecipesByTitle(null);
        List<Recipe> sortedRecipes = appController.getRecipeList();
        assertEquals("Steak", sortedRecipes.get(0).getTitle());
        assertEquals("Spaghetti", sortedRecipes.get(1).getTitle());
        assertEquals("Salad", sortedRecipes.get(2).getTitle());
        assertEquals("Pancakes", sortedRecipes.get(3).getTitle());
        assertEquals("Omelette", sortedRecipes.get(4).getTitle());
        assertEquals("Burger", sortedRecipes.get(5).getTitle());
    }

    @Test
    void testSortRecipesByDate() {
        // Create a list of recipes in unsorted order
        addRecipesForTest();

        appController.sortRecipesByDate(null);
        List<Recipe> sortedRecipes = appController.getRecipeList();
        assertEquals(1, sortedRecipes.get(0).getIndex());
        assertEquals(2, sortedRecipes.get(1).getIndex());
        assertEquals(3, sortedRecipes.get(2).getIndex());
        assertEquals(4, sortedRecipes.get(3).getIndex());
        assertEquals(5, sortedRecipes.get(4).getIndex());
        assertEquals(6, sortedRecipes.get(5).getIndex());
    }

    @Test
    void testSortRecipesByReversedDate() {
        // Create a list of recipes in unsorted order
        addRecipesForTest();

        appController.reverseSortRecipesByDate(null);
        List<Recipe> sortedRecipes = appController.getRecipeList();
        assertEquals(6, sortedRecipes.get(0).getIndex());
        assertEquals(5, sortedRecipes.get(1).getIndex());
        assertEquals(4, sortedRecipes.get(2).getIndex());
        assertEquals(3, sortedRecipes.get(3).getIndex());
        assertEquals(2, sortedRecipes.get(4).getIndex());
        assertEquals(1, sortedRecipes.get(5).getIndex());
    }

    // Method to create recipes
    private void addRecipesForTest() {
        appController.addNewRecipeToList(new Recipe("Pancakes", "Breakfast", "Description 1"));
        appController.addNewRecipeToList(new Recipe("Spaghetti", "Lunch", "Description 2"));
        appController.addNewRecipeToList(new Recipe("Burger", "Dinner", "Description 3"));
        appController.addNewRecipeToList(new Recipe("Omelette", "Breakfast", "Description 4"));
        appController.addNewRecipeToList(new Recipe("Salad", "Lunch", "Description 5"));
        appController.addNewRecipeToList(new Recipe("Steak", "Dinner", "Description 6"));
        List<Recipe> recipes = appController.getRecipeList();
        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);
            recipe.setIndex(i + 1); // Assuming index starts from 1
        }
    }

}
