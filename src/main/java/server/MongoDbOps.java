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
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

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
      // System.out.println("pretty print user: \n" + user.toJson(prettyPrint));

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
      // System.out.println("pretty print user: \n" + user.toJson(prettyPrint));

      JSONObject userJson = new JSONObject(user);
      JSONArray dataJsonArray = userJson.getJSONArray("recipes");
      // System.out.println(dataJsonArray);

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
          o.getString("recipeDetail");
        if (o.has("imgBase64Str")) {
          response += ";" + o.getString("imgBase64Str") + "#";
        } else {
          response += "#";
        }
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
      // System.out.println("pretty print user: \n" + user.toJson(prettyPrint));

      JSONObject userJson = new JSONObject(user);
      JSONArray dataJsonArray = userJson.getJSONArray("recipes");
      // System.out.println(dataJsonArray);

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
        recipe.getString("recipeDetail") +
        ";" +
        recipe.getString("imgBase64Str")
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
    String recipeDetail,
    String imgBase64Str
  ) {
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
      MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(
        collection
      );
      // if userID doesn't exist in table (user's first recipe)
      // create a user with a recipe
      Document user = recipeCollection.find(eq("username", userID)).first();
      Document newRecipe = new Document("recipeID", recipeID)
        .append("title", title)
        .append("mealType", mealType)
        .append("recipeDetail", recipeDetail)
        .append("imgBase64Str", imgBase64Str);
      if (user == null) {
        user = new Document("_id", new ObjectId());
        List<Document> recipes = new ArrayList<>();
        recipes.add(newRecipe);
        user.append("username", userID).append("recipes", recipes);
        recipeCollection.insertOne(user);
      } else {
        // user already exists, so just append a recipe entry in the recipes list
        Bson filter = Filters.eq("username", userID);
        Bson update = Updates.push("recipes", newRecipe);
        recipeCollection.findOneAndUpdate(filter, update);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public boolean updateRecipeByUsername(
    String username,
    String recipeID,
    String recipeDetail
  ) {
    System.out.println("updated recipe detail: " + recipeDetail);
    try (MongoClient mongoClient = MongoClients.create(uri)) {
      MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
      MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(
        collection
      );

      Bson filter = eq("username", username);
      List<Bson> arrayFilter = new ArrayList<>();
      arrayFilter.add(eq("ele.recipeID", recipeID));
      UpdateOptions options = new UpdateOptions().arrayFilters(arrayFilter);
      Bson update = set("recipes.$[ele].recipeDetail", recipeDetail);
      UpdateResult result = recipeCollection.updateOne(filter, update, options);
      // System.out.println("result: " + result);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean deleteRecipeByUserIdRecipeId(
    String username,
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
      // Create the filter to match the document
      // Document filter = new Document("username", new ObjectId("5150a1199fac0e6910000002"));
      Bson filter = Filters.eq("username", username);
      // Create the update operation
      Document pullUpdate = new Document(
        "$pull",
        new Document("recipes", new Document("recipeID", recipeID))
      );

      // Perform the update
      recipeCollection.updateOne(filter, pullUpdate);

      // Close the MongoDB connection
      mongoClient.close();
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }
}
