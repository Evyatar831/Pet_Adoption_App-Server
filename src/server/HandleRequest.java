package server;

import java.net.Socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.ControllerFactory;
import controller.IController;
import dm.Adopter;
import dm.Adoption;
import dm.Pet;
import util.LocalDateTimeAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Map;

public class HandleRequest implements Runnable {
    private final Socket clientSocket;
    private final Gson gson;
    private final ControllerFactory controllerFactory;

    public HandleRequest(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
        this.controllerFactory = ControllerFactory.getInstance();
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            String requestJson = reader.readLine();
            System.out.println("Received request: " + requestJson);

            if (requestJson == null || requestJson.isEmpty()) {
                sendErrorResponse(writer, "Empty request received");
                return;
            }

            processRequest(requestJson, writer);

        } catch (IOException e) {
            System.err.println("Error handling client request: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println("Error closing client socket: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private void processRequest(String requestJson, PrintWriter writer) {
        try {
            System.out.println("Processing request: " + requestJson);


            Type genericType = new TypeToken<Request<Object>>(){}.getType();
            Request<Object> genericRequest;

            try {
                genericRequest = gson.fromJson(requestJson, genericType);
            } catch (Exception e) {
                sendErrorResponse(writer, "Invalid JSON format: " + e.getMessage());
                return;
            }

            if (genericRequest == null || genericRequest.getHeaders() == null) {
                sendErrorResponse(writer, "Invalid request format: missing headers or malformed JSON");
                return;
            }

            String action = genericRequest.getAction();
            System.out.println("Processing action: " + action);

            if (action == null || action.isEmpty()) {
                sendErrorResponse(writer, "Missing action in request headers");
                return;
            }


            String[] actionParts = action.split("/");
            if (actionParts.length != 2) {
                sendErrorResponse(writer, "Invalid action format: " + action + ". Expected 'controller/method'");
                return;
            }

            String controllerName = actionParts[0];
            String methodName = actionParts[1];


            IController controller = controllerFactory.getController(controllerName);
            if (controller == null) {
                sendErrorResponse(writer, "Unknown controller: " + controllerName);
                return;
            }


            Request<?> typedRequest;
            try {
                typedRequest = parseRequestWithType(requestJson, controllerName);
            } catch (Exception e) {
                sendErrorResponse(writer, "Error parsing request body: " + e.getMessage());
                return;
            }


            Object result;
            try {
                result = controller.executeAction(methodName, typedRequest.getBody());
            } catch (IllegalArgumentException e) {
                sendErrorResponse(writer, "Invalid action method: " + methodName + ". Error: " + e.getMessage());
                return;
            } catch (Exception e) {
                sendErrorResponse(writer, "Error executing action: " + e.getMessage());
                e.printStackTrace();
                return;
            }


            Response<?> response = Response.success(result);
            String responseJson = gson.toJson(response);
            writer.println(responseJson);

        } catch (Exception e) {
            System.err.println("Critical error processing request: " + e.getMessage());
            e.printStackTrace();
            sendErrorResponse(writer, "Server error: " + e.getMessage());
        }
    }


    private Request<?> parseRequestWithType(String requestJson, String controllerName) {

        Type genericType = new TypeToken<Request<Object>>(){}.getType();
        Request<Object> genericRequest = gson.fromJson(requestJson, genericType);

        if (genericRequest.getHeaders() == null) {
            return gson.fromJson(requestJson, genericType);
        }

        String action = genericRequest.getAction();
        if (action == null) {
            return gson.fromJson(requestJson, genericType);
        }

        String[] actionParts = action.split("/");
        if (actionParts.length != 2) {
            return gson.fromJson(requestJson, genericType);
        }

        String methodName = actionParts[1];


        if (methodName.equals("delete") || methodName.equals("get") ||
                methodName.equals("getByStatus") || methodName.equals("match")) {
            System.out.println("Parsing request as Map for method: " + methodName);
            return gson.fromJson(requestJson, new TypeToken<Request<Map<String, Object>>>(){}.getType());
        }


        Type requestType;
        switch (controllerName) {

            case "pet":
                requestType = new TypeToken<Request<Pet>>(){}.getType();
                break;
            case "adopter":
                requestType = new TypeToken<Request<Adopter>>(){}.getType();
                break;
            case "adoption":
                requestType = new TypeToken<Request<Adoption>>(){}.getType();
                break;
            default:
                requestType = new TypeToken<Request<Object>>(){}.getType();
                break;
        }

        return gson.fromJson(requestJson, requestType);
    }

    private void sendErrorResponse(PrintWriter writer, String errorMessage) {
        System.err.println("Sending error response: " + errorMessage);
        Response<Object> errorResponse = Response.error(errorMessage);
        writer.println(gson.toJson(errorResponse));
    }
}