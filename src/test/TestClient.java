package test;

import com.google.gson.Gson;
import dm.Pet;
import server.Request;
import server.Response;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class TestClient {
    public static void main(String[] args) {
        String host = "localhost";
        int port = 34567;

        try (
                Socket socket = new Socket(host, port);
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            Pet pet = new Pet("test123", "Buddy", "Dog", "Golden Retriever", 3, "Male");


            Map<String, String> headers = new HashMap<>();
            headers.put("action", "pet/add");

            Request<Pet> request = new Request<>(headers, pet);

            Gson gson = new Gson();
            String requestJson = gson.toJson(request);

            System.out.println("Sending request: " + requestJson);
            writer.println(requestJson);

            String responseJson = reader.readLine();
            System.out.println("Received response: " + responseJson);

            Response<?> response = gson.fromJson(responseJson, Response.class);
            System.out.println("Success: " + response.isSuccess());
            System.out.println("Message: " + response.getMessage());

        } catch (Exception e) {
            System.err.println("Error in test client: " + e.getMessage());
            e.printStackTrace();
        }
    }
}