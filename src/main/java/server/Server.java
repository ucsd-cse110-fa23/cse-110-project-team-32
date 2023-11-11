package server;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.*;


// compile in root folder: $javac src/main/java/server/Server.java src/main/java/server/RequestHandler.java
// then navigate to src/main/java and run $java server.Server
public class Server {
  // initialize server port and hostname
  private static final int SERVER_PORT = 8100;
  private static final String SERVER_HOSTNAME = "localhost";


  public static void main(String[] args) throws IOException {
    // create a thread pool to handle requests
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    // create a map to store data
    Map<String, List<String>> data = new HashMap<>();

    // create a server
    HttpServer server = HttpServer.create(
      new InetSocketAddress(SERVER_HOSTNAME, SERVER_PORT),
    0
    );
    RequestHandler requestHandler = new RequestHandler(data);
    server.createContext("/", requestHandler);
    server.setExecutor(threadPoolExecutor);
    server.start();
    System.out.println("Server started on port " + SERVER_PORT);
  }
}