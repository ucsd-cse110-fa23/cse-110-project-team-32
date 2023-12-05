package client;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import org.junit.jupiter.api.Test;

class mockDataBase {

  HashMap<String, String> accDetails;

  public mockDataBase() {
    accDetails = new HashMap<>();
  }

  public String addAccount(String username, String password) {
    if (checkValidUsername(username) == true) {
      accDetails.put(username, password);
      return "Added Account successfully.";
    }
    System.out.println("Username Already Exists.");
    return "Username Already Exists.";
  }

  public boolean checkValidUsername(String username) {
    if (accDetails.containsKey(username)) {
      return false;
    } else {
      return true;
    }
  }

  public String logIn(String username, String password) {
    if (password == accDetails.get(username)) {
      //LoggedIn, maybe return list of recipes?
      return "Logged In Successfully";
    } else {
      System.out.println("Incorrect Username + Password");
      return "Incorrect Username + Password";
    }
  }
}

public class AccountTest {

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
    String loggedInRes = mangoDB.logIn("Joe", "test");
    assertEquals("Logged In Successfully", loggedInRes);
  }

  @Test
  void wrongUsernameLogIn() {
    mockDataBase mangoDB = new mockDataBase();
    //Add new Account
    String res = mangoDB.addAccount("Joe", "test");
    String loggedInRes = mangoDB.logIn("Alex", "test");
    assertEquals("Incorrect Username + Password", loggedInRes);
  }

  @Test
  void wrongPasswordLogIn() {
    mockDataBase mangoDB = new mockDataBase();
    //Add new Account
    String res = mangoDB.addAccount("Joe", "test");
    String loggedInRes = mangoDB.logIn("Joe", "CORRECT");
    assertEquals("Incorrect Username + Password", loggedInRes);
  }
}
