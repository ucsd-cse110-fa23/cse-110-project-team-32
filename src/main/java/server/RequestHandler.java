package server;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class RequestHandler implements HttpHandler {


    private final Map<String, String> data;


    public RequestHandler(Map<String, String> data) {
        this.data = data;
    }

    public void handle(HttpExchange httpExchange) throws IOException {
        dummyHandleGet(httpExchange);
        /*
        String response = "Request Received";
        String method = httpExchange.getRequestMethod();
        boolean isRequestValid = true;
        try {
            if (method.equals("POST")) {
                response = handlePost(httpExchange);
            } else if (method.equals("GET")) {
                response = handleGet(httpExchange);
            } else if (method.equals("PUT")) {
                response = handlePut(httpExchange);
            } else if (method.equals("DELETE")) {
                response = handleDelete(httpExchange);
            } else {
                isRequestValid = false;
                throw new Exception("Invalid Request Method");
            }
        } catch (Exception e) {
            System.out.println("There Is An Error In Request Received.");
            response = e.toString();
            e.printStackTrace();
        } finally {
            httpExchange.sendResponseHeaders(isRequestValid ? 200 : 404, response.length());
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response.getBytes());
            outStream.close();
        }
        */
    }
    public void dummyHandleGet(HttpExchange httpExchange) throws IOException {
        // this handle method can only handle the GET request
        String method = httpExchange.getRequestMethod();
        // give user an error if the method is not GET
        StringBuilder htmlBuilder = null;
        int code = 200;
        try {
            if (!method.equals("GET")) {
                code = 404;
                htmlBuilder = new StringBuilder();
                htmlBuilder
                .append("<html>")
                .append("<body>")
                .append("<h1>")
                .append("Sorry, this page can only handle a GET request")
                .append("</h1>")
                .append("</body>")
                .append("</html>");
            } else {
                // if the method is GET, extract the name field in the uri query and display the following page
                URI uri = httpExchange.getRequestURI();
                String query = uri.getRawQuery();
                String name = query.substring(query.indexOf('=')+1);
                htmlBuilder = new StringBuilder();
                htmlBuilder
                .append("<html>")
                .append("<body>")
                .append("<h1>")
                .append("Hello ")
                .append(name)
                .append("</h1>")
                .append("</body>")
                .append("</html>");
            }
        } catch (Exception e) {
                code = 404;
                htmlBuilder = new StringBuilder();
                htmlBuilder
                .append("<html>")
                .append("<body>")
                .append("<h1>")
                .append("Oops... Something went wrong")
                .append("</h1>")
                .append("</body>")
                .append("</html>");
        } finally {
            String response = htmlBuilder.toString();
            httpExchange.sendResponseHeaders(code, response.length());
            OutputStream outputStream = httpExchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.close();
        }
    }

    private String handleGet(HttpExchange httpExchange) {
        String response = "Invalid GET Request.";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        if (query != null) {
            String queryKey = query.substring(query.indexOf('=')+1);
            String queryResult = this.data.get(queryKey);
            if (queryResult == null) {
                response = "No data found for " + queryKey;
            } else {
                response = queryResult;
                System.out.println("Queried for " + queryKey + ", and found: " + queryResult);
            }
        }
        return response;
    }

    private String handlePost(HttpExchange httpExchange) {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String postData = scanner.nextLine();
        String languageKey = postData.substring(0, postData.indexOf(','));
        String yearValue = postData.substring(postData.indexOf(',')+1);
        this.data.put(languageKey, yearValue);

        String response = "Received POST request on lang = " + languageKey + " and year = " + yearValue;
        
        scanner.close();

        System.out.println(response);

        return response;
    }

    private String handlePut(HttpExchange httpExchange) {
        InputStream inStream = httpExchange.getRequestBody();
        Scanner scanner = new Scanner(inStream);
        String putData = scanner.nextLine();
        scanner.close();
        String languageKey = putData.substring(0, putData.indexOf(','));
        String languageYear = putData.substring(putData.indexOf(',') + 1);

        String response = "Added entry {" + languageKey + ", " + languageYear + "}";

        if (this.data.containsKey(languageKey)) {
            response = "Updated entry {" + languageKey + ", " + languageYear + "} (previous year: " + this.data.get(languageKey) + ")";
        }
        this.data.put(languageKey, languageYear);
        return response;
    }

    private String handleDelete(HttpExchange httpExchange) {
        String response = "Invalid DELETE Request";
        URI uri = httpExchange.getRequestURI();
        String query = uri.getRawQuery();
        String languageKey = query.substring(query.indexOf('=')+1);

        response = "No data found for " + languageKey;
        if (this.data.containsKey(languageKey)) {
            response = "Deleted entry {" + languageKey + ", " + this.data.get(languageKey) + "}";
            this.data.remove(languageKey);
        }
        return response;
    }
}
