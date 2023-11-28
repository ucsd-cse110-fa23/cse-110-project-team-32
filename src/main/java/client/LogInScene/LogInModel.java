package client.LogInScene;

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

public class LogInModel {

    private static final String URL = "http://localhost:8100/";
    private Boolean autoLogIn;

    public Boolean checkUserPass(String username, String password) {
        // TODO: Implement checking if username and password in database
        return false;
    }

    public Boolean checkRememberMe(Boolean checked) {
        // TODO: Use "checked" variable in LogInView to check if box is checked
        autoLogIn = checked;
        return autoLogIn;
    }

    public void autoLogIn() {
        autoLogIn = true;
        // TODO: 
    }

    public void serverStatusCheck () {
        //TODO
    }


}
