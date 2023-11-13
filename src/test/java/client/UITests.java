package client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.Scene;
import client.RecipeListScene.RecipeListView;
import client.RecipeDetailScene.RecipeDetailView;
import client.CreateRecipeScene.CreateRecipeView;

public class UITests extends AppController {

    /* To address the issue of the JavaFX Toolkit still running between tests,
    we restart the application per test method. */ 
    @BeforeAll
    static void initJavaFX() {
        // Start the JavaFX Application Thread
        Platform.startup(() -> {
        });
    }
    @AfterAll
    static void closeJavaFX() {
        // Run later to ensure that it is executed on the JavaFX Application Thread
        Platform.runLater(() -> {
            Platform.exit();
            // Ensure the JavaFX platform is fully closed
            System.exit(0);
        });
    }


    @Test
    void backFunctionTest() {
        // Create necessary components for the test
        RecipeListView recipeListView = new RecipeListView();
        RecipeDetailView recipeDetailView = new RecipeDetailView();
        Scene recipeListScene = new Scene(recipeListView.getBorderPane(), 500, 500);
        Scene recipeDetailScene = new Scene(recipeDetailView.getBorderPane(), 500, 500);

        // Use reflection to make the method accessible
        try {
            Method method = AppController.class.getDeclaredMethod("changeToRecipeListScene");
            method.setAccessible(true);

            // Change the scene to the recipe detail view
            getStage().setScene(recipeDetailScene);
            assertEquals(recipeDetailScene, getStage().getScene());

            // Invoke the private method to simulate the "Back" button click
            method.invoke(this);

            // Check if the scene has been changed back to the recipe list view
            assertEquals(recipeListScene, getStage().getScene());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void cancelFunctionTest() {
        // Create necessary components for the test
        RecipeListView recipeListView = new RecipeListView();
        CreateRecipeView createRecipeView = new CreateRecipeView();
        Scene recipeListScene = new Scene(recipeListView.getBorderPane(), 500, 500);
        Scene createRecipeScene = new Scene(createRecipeView.getBorderPane(), 500, 500);

        // Use reflection to make the method accessible
        try {
            // Method to switch back to the recipe list view
            Method switchToRecipeListScene = AppController.class.getDeclaredMethod("changeToRecipeListScene");
            switchToRecipeListScene.setAccessible(true);

            // Method to cancel the recipe creation
            Method cancelRecipeCreation = CreateRecipeView.class.getDeclaredMethod("handleCrvCancelButtonAction");
            cancelRecipeCreation.setAccessible(true);

            // Change the scene to the create recipe view
            getStage().setScene(createRecipeScene);
            assertEquals(createRecipeScene, getStage().getScene());

            // Invoke the private method to simulate the "Cancel" button click
            cancelRecipeCreation.invoke(createRecipeView);

            // Check if the scene has been changed back to the recipe list view
            assertEquals(recipeListScene, getStage().getScene());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void deleteFunctionTest() {
        // Create necessary components for the test
        RecipeListView recipeListView = new RecipeListView();
        RecipeDetailView recipeDetailView = new RecipeDetailView();
        Scene recipeListScene = new Scene(recipeListView.getBorderPane(), 500, 500);
        Scene recipeDetailScene = new Scene(recipeDetailView.getBorderPane(), 500, 500);

        // Use reflection to make the method accessible
        try {
            Method method = AppController.class.getDeclaredMethod("handleRdvDeleteButtonAction");
            method.setAccessible(true);

            // Change the scene to the recipe detail view
            getStage().setScene(recipeDetailScene);
            assertEquals(recipeDetailScene, getStage().getScene());

            // Invoke the private method to simulate the "Delete" button click
            method.invoke(this);

            // Check if the scene has been changed back to the recipe list view
            assertEquals(recipeListScene, getStage().getScene());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}