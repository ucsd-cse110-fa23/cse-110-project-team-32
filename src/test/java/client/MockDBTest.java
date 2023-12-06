package client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

class mockDataBase{
    HashMap<String,String> accDetails;
    HashMap<String,List<Recipe>> recipes;
    String username;
    String password;
    Boolean autoLogin;
    List<Recipe> lst;
    public mockDataBase(){
        accDetails = new HashMap<>();
        recipes = new HashMap<String,List<Recipe>>();
        lst = new ArrayList<Recipe>();
        autoLogin = false;
    }

    public void setAutoLogIn(){
        this.autoLogin = true;
    }
    public HashMap<String,String> getAccDetails(){
        return accDetails;
    }

    public HashMap<String, List<Recipe>> getRecipeList(){
        return recipes;
    }

    public void setRecipeList(String username, List<Recipe> lst){
        recipes.replace(username, lst);
    }

  public String addAccount(String username, String password) {
    if (checkValidUsername(username) == true) {
      accDetails.put(username, password);
      return "Added Account successfully.";
    }
    // System.out.println("Username Already Exists.");
    return "Username Already Exists.";
  }

  public boolean checkValidUsername(String username) {
    if (accDetails.containsKey(username)) {
      return false;
    } else {
      return true;
    }
  }
    public String logIn(){
        if(this.autoLogin == true){
            return "Logged In Successfully";
        }
        else{
            return "Incorrect Username + Password";
        }
    }

    public String logIn(String username, String password, boolean auto){
        this.autoLogin = auto;
        if(password == accDetails.get(username)){
            //LoggedIn, maybe return list of recipes?
            this.username = username;
            return "Logged In Successfully";
        }
        else{
            // System.out.println("Incorrect Username + Password");
            return "Incorrect Username + Password";
        }
    }

    public String logOut(){
        if(this.username == null){
            return "User not currently logged in";
        }
        this.username = null;
        lst.clear();
        autoLogin = false;
        return "Logged Out Successfully";
    }
    
    public void storeRecipes(Recipe recipe){
        lst.add(recipe);
        recipes.put(username, lst);
    }

    public void editRecipe(Recipe recipe, String details){
        // recipes.(lst.get(lst.indexOf(recipe)))
        lst.get(lst.indexOf(recipe)).setRecipeDetail(details);
        recipes.replace(username, lst);
    }

    public void deleteRecipe(Recipe recipe){
        lst.remove(recipe);
    }

    public boolean containsRecipe(Recipe recipe){
        return lst.contains(recipe);
    }

    public String getRecipeTitle(Recipe recipe){
        return lst.get(lst.indexOf(recipe)).getTitle();
    }
    public String getRecipeMealType(Recipe recipe){
        return lst.get(lst.indexOf(recipe)).getMealType();
    }
    public String getRecipeDetails(Recipe recipe){
        return lst.get(lst.indexOf(recipe)).getRecipeDetail();
    }

    public String getRecipeImg(Recipe recipe){
        return lst.get(lst.indexOf(recipe)).getImgBase64Str();
    }

    public String shareRecipe(Recipe recipe){
        return recipe.getTitle()+ ".html";
    }
}

public class MockDBTest {

  //Fake Database

  @Test
  void mockCreateAccount() {
    mockDataBase mangoDB = new mockDataBase();
    //Add new Account
    String res = mangoDB.addAccount("Ryan", "is-a-Rizzer");
    assertEquals("Added Account successfully.", res);
  }

  @Test
  void addDuplicateUsername() {
    mockDataBase mangoDB = new mockDataBase();
    //Add new Account
    String res = mangoDB.addAccount("Joe", "test");
    String res1 = mangoDB.addAccount("Joe", "mama");
    assertEquals(false, mangoDB.checkValidUsername("Joe")); //, "mama"));
    assertEquals("Username Already Exists.", res1);
  }

  @Test
  void logInCorrectly() {
    mockDataBase mangoDB = new mockDataBase();
    //Add new Account
    String res = mangoDB.addAccount("Joe", "test");
    String loggedInRes = mangoDB.logIn("Joe", "test",false);
    assertEquals("Logged In Successfully", loggedInRes);
  }

  @Test
    void wrongUsernameLogIn() {
        mockDataBase mangoDB = new mockDataBase();
        //Add new Account
        String res = mangoDB.addAccount("Joe", "test");
        String loggedInRes = mangoDB.logIn("Alex", "test",false);
        assertEquals("Incorrect Username + Password", loggedInRes);
    }

    @Test void wrongPasswordLogIn(){
        mockDataBase mangoDB = new mockDataBase();
        //Add new Account
        String res = mangoDB.addAccount("Joe", "test");
        String loggedInRes = mangoDB.logIn("Joe", "CORRECT",false);
        assertEquals("Incorrect Username + Password", loggedInRes);
    }

    @Test void bothUserAndPassWrongLogIn(){
        mockDataBase mangoDB = new mockDataBase();
        //Add new Account
        String res = mangoDB.addAccount("Joe", "test");
        String loggedInRes = mangoDB.logIn("JoeMama", "cool",false);
        assertEquals("Incorrect Username + Password", loggedInRes);
    }

    @Test void SuccessfulLogOut(){
        mockDataBase mangoDB = new mockDataBase();
        //Add new Account
        mangoDB.addAccount("Joe", "test");
        mangoDB.logIn("Joe", "test",false);
        assertEquals("Logged Out Successfully", mangoDB.logOut());
    }

    @Test void unsuccessfulLogOut(){
        mockDataBase mangoDB = new mockDataBase();
        assertEquals("User not currently logged in", mangoDB.logOut());
    }

    @Test void testAutoLogIn(){
        mockDataBase mangoDB = new mockDataBase();
        //Add new Account
        String res = mangoDB.addAccount("Joe", "test");
        String loggedInRes = mangoDB.logIn("Joe", "test",true);
        assertEquals("Logged In Successfully",mangoDB.logIn());
    }

    @Test void testLogOutWithAutoLogIn(){
                mockDataBase mangoDB = new mockDataBase();
        //Add new Account
        String res = mangoDB.addAccount("Joe", "test");
        String loggedInRes = mangoDB.logIn("Joe", "test",true);
        mangoDB.logOut();
        assertEquals("Incorrect Username + Password",mangoDB.logIn());
    }
    @Test void storeRecipesInDatabase(){
        mockDataBase mangoDB = new mockDataBase();
        mockChatGPT chatGPT = new mockChatGPT();

        String res = mangoDB.addAccount("Joe", "test");
        mangoDB.logIn("Joe", "test",false);
        Recipe r = chatGPT.generate("Dinner", "Chicken, Rice");
        mangoDB.storeRecipes(r);
        assertEquals("Chicken and Rice", mangoDB.getRecipeTitle(r));
        assertEquals("Chicken, Rice", mangoDB.getRecipeDetails(r));
        assertEquals("imageFileHere", mangoDB.getRecipeImg(r));
        assertEquals("Dinner", mangoDB.getRecipeMealType(r));
    }

    @Test void editRecipeInDataBase(){
        mockDataBase mangoDB = new mockDataBase();
        mockChatGPT chatGPT = new mockChatGPT();

        String res = mangoDB.addAccount("Joe", "test");
        mangoDB.logIn("Joe", "test",false);
        Recipe r = chatGPT.generate("Dinner", "Chicken, Rice");
        mangoDB.storeRecipes(r);
        mangoDB.editRecipe(r, "First cook rice, then pan-fry chicken");
        assertEquals("First cook rice, then pan-fry chicken", mangoDB.getRecipeDetails(r));
    }

    @Test void deleteRecipeinDataBase(){
        mockDataBase mangoDB = new mockDataBase();
        mockChatGPT chatGPT = new mockChatGPT();

        String res = mangoDB.addAccount("Joe", "test");
        mangoDB.logIn("Joe", "test",false);
        Recipe r = chatGPT.generate("Dinner", "Chicken, Rice");
        mangoDB.storeRecipes(r);
        mangoDB.deleteRecipe(r);
        assertEquals(false,mangoDB.containsRecipe(r));
    }
}
