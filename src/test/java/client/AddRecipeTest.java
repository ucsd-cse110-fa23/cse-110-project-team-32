package client;
import client.RecipeList.RecipeList;
import client.RecipeList.RecipeListView;
import client.RecipeList.RecipeListHeader;

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
}
