package client;

public interface API {
    //Maybe add a getInput method: That gets input
    //Change the getInfo Method()
    public API initializeAPI(String API, String tokens, String model);
    public String getInfo();
}
