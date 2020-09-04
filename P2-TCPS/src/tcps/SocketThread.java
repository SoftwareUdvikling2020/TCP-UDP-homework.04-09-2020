package tcps;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketThread extends Thread{
    Socket openSocket;

    String request, response;
    Scanner in;   // Scanner is the incoming stream (requests from a client)
    PrintWriter out;

    // two I/O streams attached to the server socket:

    // Parameter true ensures automatic flushing of the output buffer

    // Server keeps listening for request and reading data from the Client,
    // until the Client sends "stop" requests


    public SocketThread(Socket openSocket) throws IOException {
            this.openSocket = openSocket;
            in = new Scanner(openSocket.getInputStream());
            out = new PrintWriter(openSocket.getOutputStream(), true);
    }

    public void run (){
        while (in.hasNextLine()) {
            request = in.nextLine();

            if (request.equals("stop")) {
                out.println("Good bye, client!");
                System.out.println("Log: " + request + " client!");


            } else {
                // server responses
                response = new StringBuilder(request).reverse().toString();
                out.println(response);
                // Log response on the server's console, too
                System.out.println("Log: " + response);
            }
        }
    }
}
