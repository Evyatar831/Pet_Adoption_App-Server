package server;



public class ServerDriver {
    public static void main(String[] args) {
        int port = 34567;

        Server server = new Server(port);
        System.out.println("Starting Pet Adoption Server on port " + port);

        new Thread(server).start();
    }
}
