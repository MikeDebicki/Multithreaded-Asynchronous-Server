public class TestClass {

    public static void main(String[] args){
        Thread client, server, intermediateHost;

        try{
            System.out.println("Creating threads");
            client = new Thread(new Client(), "client");
            System.out.println("Created client thread");

            server = new Thread(new Server(), "server");
            intermediateHost = new Thread(new IntermediateHost(), "intermediateHost");
            System.out.println("Created host thread");

            server.start();
            client.start();
            intermediateHost.start();


        }catch(Exception IOException){
            System.out.println("Creating threads failed");
        }
    }


}
