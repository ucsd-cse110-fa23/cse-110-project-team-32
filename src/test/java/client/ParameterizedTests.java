package client;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import java.util.stream.Stream;

public class ParameterizedTests {

    static Stream<Arguments> provideRecipeData() {
    return Stream.of(
        Arguments.of("Chicken and Rice", "Dinner", "Its Chicken and Rice man"),
        Arguments.of("Pasta", "Lunch", "Pasta again for dinner??"),
        Arguments.of("Omelette", "Breakfast", "You can never be wrong by starting your day with eggs")
    );

    }
    @ParameterizedTest
    @MethodSource("provideRecipeData")
    void AddRecipeTest(String title, String mealType, String recipeDetail) {
        Recipe recipe = new Recipe (title, mealType, recipeDetail);
        assertEquals(recipe.getTitle(), title);
        assertEquals(recipe.getMealType(), mealType);
        assertEquals(recipe.getRecipeDetail(), recipeDetail);
    }

    // Parameterized test for the addStoredInRecipeListTest method
    @ParameterizedTest
    @MethodSource("provideRecipeData")
    void addStoredInRecipeListTest(String title, String mealType, String recipeDetail) {
        AppController appController = new AppController();
        List<Recipe> recipeList = appController.getRecipeList();
        assertEquals(recipeList.size(), 0);

        Recipe recipe = new Recipe(title, mealType, recipeDetail);
        appController.addNewRecipeToList(recipe);

        recipeList = appController.getRecipeList();
        assertEquals(recipeList.size(), 1);
        assertEquals(recipeList.get(0), recipe);
    }

    // Parameterized test for the recipeListFirstItemIsAddedRecipe method
    @ParameterizedTest
    @MethodSource("provideRecipeData")
    void recipeListFirstItemIsAddedRecipe(String title, String mealType, String recipeDetail) {
        AppController appController = new AppController();
        List<Recipe> recipeList;

        Recipe r1 = new Recipe("Title1", "MealType1", "Details1");
        Recipe r2 = new Recipe("Title2", "MealType2", "Details2");
        Recipe r3 = new Recipe("Title3", "MealType3", "Details3");

        appController.addNewRecipeToList(r1);
        appController.addNewRecipeToList(r3);

        recipeList = appController.getRecipeList();
        assertEquals(r3, recipeList.get(0));

        Recipe addedRecipe = new Recipe(title, mealType, recipeDetail);
        appController.addNewRecipeToList(addedRecipe);

        recipeList = appController.getRecipeList();
        assertEquals(addedRecipe, recipeList.get(0));
    }

    // Parameterized test for the deleteRecipeFromListTest method
    @ParameterizedTest
    @MethodSource("provideRecipeData")
    public void deleteRecipeFromListTest(String title, String mealType, String recipeDetail) {
        AppController appController = new AppController();
        List<Recipe> recipeList;

        Recipe r = new Recipe(title, mealType, recipeDetail);
        appController.addNewRecipeToList(r);
        recipeList = appController.getRecipeList();
        assertEquals(1, recipeList.size());
        
        appController.removeRecipeFromRecipeList(r);
    
        recipeList = appController.getRecipeList();
        assertEquals(0, recipeList.size());
    
    }
}
