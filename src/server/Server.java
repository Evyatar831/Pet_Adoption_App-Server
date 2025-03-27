package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
    private final int port;
    private volatile boolean running;
    private ServerSocket serverSocket;
    private final ExecutorService executorService;

    public Server(int port) {
        this.port = port;
        this.running = true;
        this.executorService = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);

            while (running) {
                System.out.println("Waiting for client connections...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

                executorService.execute(new HandleRequest(clientSocket));

            }
        } catch (IOException e) {
            if (running) {
                System.err.println("Server error: " + e.getMessage());
                e.printStackTrace();
            }
        } finally {
            stop();
        }
    }

    public void stop() {
        running = false;
        executorService.shutdown();

        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }
    }
}
