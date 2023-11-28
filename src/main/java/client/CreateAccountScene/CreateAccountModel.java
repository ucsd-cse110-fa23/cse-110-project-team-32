package client.CreateAccountScene;

import client.HttpResponse.CreateRecipeResponse;
import client.HttpResponse.ServerResponse;
import client.HttpResponse.WhisperResponse;
import client.Recipe;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;
import org.bson.io.OutputBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

public class CreateAccountModel {
    
    public void storeAccountDetails(String user, String pass) {
        //TODO
    }

    public Boolean checkValidUsername(String username) {
        //TODO
        return false;
    }
}
