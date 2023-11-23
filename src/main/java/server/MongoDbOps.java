package server;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileReader;
// import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonObject;
import org.bson.json.JsonWriterSettings;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

public class MongoDbOps {

  private static final String uri =
    "mongodb+srv://fangzhongli0:dm3DeEs1C7UGwbOn@cluster0.zd5oxwj.mongodb.net/?retryWrites=true&w=majority";
  private static final String database = "pantryPal";
  private static final String collection = "recipesByUserId";
  private JsonWriterSettings prettyPrint = JsonWriterSettings
    .builder()
    .indent(true)
    .build();

  public static final MongoDbOps MONGO_DB_OPS = new MongoDbOps();

  private MongoDbOps() {}

  public static MongoDbOps getInstance() {
    return MONGO_DB_OPS;
  }

  public String getUserPasswordByUsername(String username) {
    /* 
            take in a username, 
            if username is in userByUserID table
                return password field
            else 
                return null;
        */
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase db = mongoClient.getDatabase(database);
      MongoCollection<Document> userByUserID = db.getCollection(collection);

      Document user = userByUserID
        .find(new Document("username", username))
        .first();
      if (user == null) {
        return null;
      }
      System.out.println("pretty print user: \n" + user.toJson(prettyPrint));

      JSONObject userJson = new JSONObject(user);
      String password = userJson.getString("password");
      System.out.println(password);
      return password;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getRecipesByUserID(String username) {
    /*
        given a String username, 
        return all the recipes in the recipeIDs array in the user table

        username should be in the user table, if not return null
        get the recipeID array
        for each recipeID, get the recipe from recipe table
        ; separate each recipe field,
        # separate 2 recipes
    */
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase db = mongoClient.getDatabase(database);
      MongoCollection<Document> recipeCollection = db.getCollection(collection);

      Document user = recipeCollection
        .find(new Document("username", username))
        .first();
      if (user == null) {
        return null;
      }
      System.out.println("pretty print user: \n" + user.toJson(prettyPrint));

      JSONObject userJson = new JSONObject(user);
      JSONArray dataJsonArray = userJson.getJSONArray("recipes");
      System.out.println(dataJsonArray);

      String response = "";
      for (int i = 0; i < dataJsonArray.length(); i++) {
        JSONObject o = dataJsonArray.getJSONObject(i);
        response +=
          o.getString("recipeID") +
          ";" +
          o.getString("title") +
          ";" +
          o.getString("mealType") +
          ";" +
          o.getString("recipeDetail") +
          "#";
      }
      // System.out.println("Mongo's response: " + response);
      return response;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  public String getRecipeByUsernameAndRecipeID(
    String username,
    String recipeID
  ) {
    /*
        given a username and a recipe ID,
        return the information of that specific recipe
        if recipe does not exist, retur null;
     */
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase db = mongoClient.getDatabase(database);
      MongoCollection<Document> recipeCollection = db.getCollection(collection);

      Document user = recipeCollection
        .find(new Document("username", username))
        .first();
      if (user == null) {
        return null;
      }
      System.out.println("pretty print user: \n" + user.toJson(prettyPrint));

      JSONObject userJson = new JSONObject(user);
      JSONArray dataJsonArray = userJson.getJSONArray("recipes");
      System.out.println(dataJsonArray);

      JSONObject recipe = null;
      for (int i = 0; i < dataJsonArray.length(); i++) {
        JSONObject o = dataJsonArray.getJSONObject(i);
        if (o.getString("recipeID").equals(recipeID)) {
          recipe = o;
          break;
        }
      }
      if (recipe == null) return null;
      return (
        recipe.getString("recipeID") +
        ";" +
        recipe.getString("title") +
        ";" +
        recipe.getString("mealType") +
        ";" +
        recipe.getString("recipeDetail")
      );
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public boolean createUser(String username, String password) {
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
      MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(
        collection
      );
      // if userID doesn't exist in table (user's first recipe)
      // create a user with a recipe
      Document user = recipeCollection.find(eq("username", username)).first();
      if (user == null) {
        user = new Document("_id", new ObjectId());
        List<Document> recipes = new ArrayList<>();
        user
          .append("username", username)
          .append("password", password)
          .append("recipes", recipes);
        recipeCollection.insertOne(user);
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean createRecipeByUserId(
    String userID,
    String recipeID,
    String title,
    String mealType,
    String recipeDetail
  ) {
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
      MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(
        collection
      );
      // if userID doesn't exist in table (user's first recipe)
      // create a user with a recipe
      Document user = recipeCollection.find(eq("userID", userID)).first();
      Document newRecipe = new Document("recipeID", recipeID)
        .append("title", title)
        .append("mealType", mealType)
        .append("recipeDetail", recipeDetail);
      if (user == null) {
        user = new Document("_id", new ObjectId());
        List<Document> recipes = new ArrayList<>();
        recipes.add(newRecipe);
        user.append("userID", userID).append("recipes", recipes);
        recipeCollection.insertOne(user);
      } else {
        // user already exists, so just append a recipe entry in the recipes list
        Bson filter = Filters.eq("userID", userID);
        Bson update = Updates.push("recipes", newRecipe);
        Document oldVersion = recipeCollection.findOneAndUpdate(filter, update);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean updateRecipeByUserId(
    String userID,
    String recipeID,
    String recipeDetail
  ) {
    // System.out.println("updated recipe detail: " + recipeDetail);
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
      MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(
        collection
      );

      Bson filter = eq("userID", userID);
      List<Bson> arrayFilter = new ArrayList<>();
      arrayFilter.add(eq("ele.recipeID", recipeID));
      UpdateOptions options = new UpdateOptions().arrayFilters(arrayFilter);
      Bson update = set("recipes.$[ele].recipeDetail", recipeDetail);
      UpdateResult result = recipeCollection.updateOne(filter, update, options);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteRecipeByUserIdRecipeId(
    String userID,
    String recipeID,
    String title,
    String mealType,
    String recipeDetail
  ) {
    // System.out.println("userID: " + userID + " recipeID: " + recipeID);
    // System.out.println("title: " + title + " mealType: " + mealType + " recipeDetail: " + recipeDetail);
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
      MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(
        collection
      );
      Bson filter = Filters.eq("userID", userID);
      Bson delete = Updates.pull(
        "recipes",
        new Document("recipeID", recipeID)
          .append("title", title)
          .append("mealType", mealType)
          .append("recipeDetail", recipeDetail)
      );
      recipeCollection.updateOne(filter, delete);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
