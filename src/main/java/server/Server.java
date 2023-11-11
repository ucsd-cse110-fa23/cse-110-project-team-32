package server;
import com.sun.net.httpserver.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


// to run the server compile it first, 
// then navigate to src/main/java and run $java server.Server
public class Server {


  // initialize server port and hostname
  private static final int SERVER_PORT = 8100;
  private static final String SERVER_HOSTNAME = "localhost";


  public static void main(String[] args) throws IOException {
    // create a thread pool to handle requests
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    // create a map to store data
    Map<String, String> data = new HashMap<>();


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

/*
Q1: Recall the ThreadingWithThread and ThreadingNoThread implementations. Did you observe any  differences when running the two programs and clicking on the buttons? If you did, why do you think this happens?
In the TreadingNoTread application, when I click record, the program freezes before outputing. In TreadingWithThread application, the program runs smoothly.
Both programs require the main thread to sleep for 5 seconds. But the threading program makes another thread and let it sleep before joining that thread to the main thread.

Q2: What is a REST API?
A type of web API that handles CRUD operations with separate APIs, returning a JSON/XML response

Q3: What is the desired action performed by the GET, POST, PUT and DELETE methods?
GET: retrieve some resource from server
POST: submitting something to the server
PUT: updating some resource on the server
DELETE: delete some resource from the server



Q4: Why is 127.0.0.1 a special IP address?
localhost

Q5: What is the MVC design pattern? Name one benefit of using it in your software engineering project.
Modal-View-Controller pattern, it practices separation of concern
It separates the rendering and the interaction logic of an application

Q6: Why do we use thread pools to handle incoming requests? What advantages does this provide compared to creating a new thread to handle every new request?
Creating a thread pool sets up n threads on standby to handle requests. In this way, we don't need to create
new threads every time when the server receives a request. When a request is handled by a thread
the thread is freed and returns to standby
 */
