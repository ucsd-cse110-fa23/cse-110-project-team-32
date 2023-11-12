package server;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;

import client.Recipe;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
// import com.mongodb.client.MongoCollection;
// import com.mongodb.client.MongoCursor;
// import com.mongodb.client.MongoDatabase;
// import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.json.JsonWriterSettings;
// import org.bson.conversions.Bson;
// import org.bson.json.JsonWriterSettings;
// import org.bson.types.ObjectId;
// import com.mongodb.client.model.UpdateOptions;
import org.bson.types.ObjectId;
import org.checkerframework.checker.signature.qual.Identifier;
import org.json.JSONArray;
import org.json.JSONObject;


import static com.mongodb.client.model.Filters.eq;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.*;

// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileReader;
// import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import org.bson.json.JsonWriterSettings;


public class MongoDbOps {
    private static final String uri = "mongodb+srv://fangzhongli0:dm3DeEs1C7UGwbOn@cluster0.zd5oxwj.mongodb.net/?retryWrites=true&w=majority";
    private static final String database = "pantryPal";
    private static final String collection = "recipesByUserId";
    private JsonWriterSettings prettyPrint = JsonWriterSettings.builder().indent(true).build();


    public MongoDbOps() {
    }

    public String getRecipesByUserID(String userID) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
            MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(collection);

            Document user = recipeCollection.find(new Document("userID", userID)).first();
            // System.out.println(user.toJson(prettyPrint));
            if (user == null) {
                return null;
            }   
            JSONObject userJson = new JSONObject(user);
            JSONArray dataJsonArray = userJson.getJSONArray("recipes");
            String response = "";
            for (int i=0; i < dataJsonArray.length(); i++) {
                JSONObject o = dataJsonArray.getJSONObject(i);
                response += o.getString("recipeID") + ";" + o.getString("title") + ";" + o.getString("mealType") + ";" + o.getString("recipeDetail") + "#";
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean createRecipeByUserId(String userID, String recipeID, String title, String mealType, String recipeDetail) {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
            MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(collection);
            // if userID doesn't exist in table (user's first recipe)
            // create a user with a recipe
            Document user = recipeCollection.find(eq("userID", userID)).first();
            Document newRecipe = new Document("recipeID", recipeID).append("title", title).append("mealType", mealType).append("recipeDetail", recipeDetail);
            if (user == null) {
                user = new Document("_id", new ObjectId());
                List<Document> recipes = new ArrayList<>();
                recipes.add(newRecipe);
                user.append("userID", userID)
                    .append("recipes", recipes);
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

    public boolean updateRecipeByUserId(String userID, String recipeID, String recipeDetail) {
        // System.out.println("updated recipe detail: " + recipeDetail);
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
            MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(collection);
            
            Bson filter = eq("userID", userID);
            List<Bson> arrayFilter = new ArrayList<>();
            arrayFilter.add(eq("ele.recipeID", recipeID));
            UpdateOptions options = new UpdateOptions()
                                        .arrayFilters(arrayFilter);
            Bson update = set("recipes.$[ele].recipeDetail", recipeDetail);
            UpdateResult result = recipeCollection.updateOne(filter, update, options);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRecipeByUserIdRecipeId(String userID, String recipeID, String title, String mealType, String recipeDetail) {
        // System.out.println("userID: " + userID + " recipeID: " + recipeID);
        // System.out.println("title: " + title + " mealType: " + mealType + " recipeDetail: " + recipeDetail);
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase sampleTrainingDB = mongoClient.getDatabase(database);
            MongoCollection<Document> recipeCollection = sampleTrainingDB.getCollection(collection);
            Bson filter = Filters.eq("userID", userID);
            Bson delete = Updates.pull("recipes", new Document("recipeID", recipeID).append("title", title).append("mealType", mealType).append("recipeDetail", recipeDetail));
            recipeCollection.updateOne(filter, delete);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
