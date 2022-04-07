import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BudgetServer {
    public final static int PORT = 5555;


    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(5);
        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket connection = server.accept();
                    Callable<Void> task = new BudgetServerTask(connection);
                    pool.submit(task);
                } catch (IOException exception) {

                }
            }
        }catch (IOException exception) {
            System.err.println("Couldn't start server");
        }
    }

    private static class BudgetServerTask implements Callable<Void>{
        private Socket connection;

        BudgetServerTask(Socket connection) {
            this.connection = connection;
        }
        @Override
        public Void call(){
            try {
                Scanner scanner = new Scanner(new File("goals.csv"));
                Writer out = new OutputStreamWriter((connection.getOutputStream()));
                Date now = new Date();
                String test = "";
                while (scanner.hasNext()) {
                    test += scanner.nextLine();
                }
                out.write(test + "\r\n");
                out.flush();

            } catch (IOException exception) {
                System.err.println(exception);
            }finally{
                try {
                    connection.close();
                } catch (IOException e) {

                }
            }
            return null;

        }
    }
}
