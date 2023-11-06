package client.AddRecipe;
import java.io.*;
import java.net.*;
import org.json.*;
public class Whisper implements API{
    private String API_ENDPOINT;// = "https://api.openai.com/v1/audio/transcriptions";
    private String TOKEN;// = "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";
    private String MODEL; //= "whisper-1";
    private String FILE_PATH;
    private String text;

    public Whisper(String path) {
        this.FILE_PATH = path;
        API_ENDPOINT = "https://api.openai.com/v1/audio/transcriptions";
        TOKEN = "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";
        MODEL = "whisper-1";
    }

    public API initializeAPI(String API_ENDPOINT,String API_TOKEN,String MODEL){
        this.API_ENDPOINT = API_ENDPOINT;
        this.TOKEN = API_TOKEN;
        this.MODEL = MODEL;
        return this;
    }

    public String getInfo(){
        return text;
    }
    // Helper method to write a parameter to the output stream in multipart form data format
    private static void writeParameterToOutputStream(
            OutputStream outputStream,
            String parameterName,
            String parameterValue,
            String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                (
                        "Content-Disposition: form-data; name=\"" + parameterName + "\"\r\n\r\n"
                ).getBytes()
        );
        outputStream.write((parameterValue + "\r\n").getBytes());
    }

    // Helper method to write a file to the output stream in multipart form data format
    private static void writeFileToOutputStream(
            OutputStream outputStream,
            File file,
            String boundary
    ) throws IOException {
        outputStream.write(("--" + boundary + "\r\n").getBytes());
        outputStream.write(
                (
                        "Content-Disposition: form-data; name=\"file\"; filename=\"" +
                                file.getName() +
                                "\"\r\n"
                ).getBytes()
        );
        outputStream.write(("Content-Type: audio/mpeg\r\n\r\n").getBytes());


        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();
    }

    // Helper method to handle a successful response
    private String handleSuccessResponse(HttpURLConnection connection)
            throws IOException, JSONException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
        );
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONObject responseJson = new JSONObject(response.toString());
        String generatedText = responseJson.getString("text");
        // Print the transcription result
        // System.out.println("Transcription Result: " + generatedText);
        return generatedText;
    }
    // Helper method to handle an error response
    private String handleErrorResponse(HttpURLConnection connection)
            throws IOException, JSONException {
        BufferedReader errorReader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream())
        );
        String errorLine;
        StringBuilder errorResponse = new StringBuilder();
        while ((errorLine = errorReader.readLine()) != null) {
            errorResponse.append(errorLine);
        }
        errorReader.close();
        String errorResult = errorResponse.toString();
        // System.out.println("Error Result: " + errorResult);
        return "Error Result: " + errorResult;
    }

    public String translateVoiceToText() throws IOException, URISyntaxException {
        // Create file object from file path
        File file = new File(FILE_PATH);

        //        for (String s : args) {
        //            System.out.println(s);
        //        }

        // Set up HTTP connection

        URL url = new URI(API_ENDPOINT).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);


// Set up request headers
        String boundary = "Boundary-" + System.currentTimeMillis();
        connection.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=" + boundary
        );
        connection.setRequestProperty("Authorization", "Bearer " + TOKEN);


// Set up output stream to write request body
        OutputStream outputStream = connection.getOutputStream();


// Write model parameter to request body
        writeParameterToOutputStream(outputStream, "model", MODEL, boundary);


// Write file parameter to request body
        writeFileToOutputStream(outputStream, file, boundary);


// Write closing boundary to request body
        outputStream.write(("\r\n--" + boundary + "--\r\n").getBytes());


// Flush and close output stream
        outputStream.flush();
        outputStream.close();


// Get response code
        int responseCode = connection.getResponseCode();


// Check response code and handle response accordingly
        String resultText;
        if (responseCode == HttpURLConnection.HTTP_OK) {
            resultText = handleSuccessResponse(connection);
        } else {
            resultText = handleErrorResponse(connection);
        }
        text = resultText;


// Disconnect connection
        connection.disconnect();
        return resultText;
    }

}
// /Users/fangzhongli/Music/Music/Media/Music/(c)Taira Komori/Unknown Album/enemyshoot.mp3"

/*
lab questions
Q1: In your own words, explain what the three layer architecture is. Explain what each layer is for.
client: render information for users, interact with users
server: calculate, run business logic, generate output based on users inputs
database: store data, give data to server to calculate
Q2: In your own words, explain what the benefits of using the APIs are.
so that the client and server and communicate
more generally, support communication between software
Q3: Would the company need to reimplement the whole app from scratch for each version? Explain your reasoning
No, because the server side logic, aka what the API calls would return only needs to be written once.
The client rendering probly needs to be changed based on different platforms
 */