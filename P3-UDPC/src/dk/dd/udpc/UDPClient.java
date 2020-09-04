package dk.dd.udpc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.nio.file.*;

/**
 * @author Magnus Albeck Klitmose, Mathias Baunsgaard Kristensen,Rasmus Lynge Jacobsen
 */
public class UDPClient {
    private static final int serverPort = 7777;

    // buffers for the messages
    public static String message;
    private static byte[] dataIn = new byte[25000000];
    private static byte[] dataOut = new byte[25000000];

    // In UDP messages are encapsulated in packages and sent over sockets
    private static DatagramPacket requestPacket;
    private static DatagramPacket responsePacket;
    private static DatagramSocket clientSocket;

    public static void main(String[] args) throws IOException {
        // Enter server's IP address as a parameter from Run/Edit Configuration/Application/Program Arguments
        clientSocket = new DatagramSocket();
        InetAddress serverIP = InetAddress.getByName("192.168.0.22");
        System.out.println(serverIP);
        Scanner scan = new Scanner(System.in);
        System.out.println("Type message: ");
        while ((message = scan.nextLine()) != null) {
            sendRequest(serverIP);
            receiveResponse();
        }
        clientSocket.close();
    }

    public static void sendRequest(InetAddress serverIP) throws IOException {
        //clientSocket = new DatagramSocket();
        byte[] data = imageToByteArray();
        dataOut = data;
        requestPacket = new DatagramPacket(dataOut, data.length, serverIP, serverPort);
        clientSocket.send(requestPacket);
    }


    private static byte[] imageToByteArray() throws IOException {
        BufferedImage image = ImageIO.read(new File("dankmann.jpg"));
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", bos);
        byte[] data = bos.toByteArray();
        return data;
    }

    public static void receiveResponse() throws IOException {
        //clientSocket = new DatagramSocket();
        responsePacket = new DatagramPacket(dataIn, dataIn.length);
        clientSocket.receive(responsePacket);
        //String message = new String(responsePacket.getData(), 0, responsePacket.getLength());
        saveResponseImage();

    }

    private static void saveResponseImage() throws IOException {
        try {
            Files.deleteIfExists(Paths.get("x.jpg"));
        } finally {
            ByteArrayInputStream bis = new ByteArrayInputStream(responsePacket.getData(), 0, requestPacket.getLength());
            BufferedImage imageIn = ImageIO.read(bis);
            ImageIO.write(imageIn, "jpg", new File("x.jpg"));
            System.out.println("Response from Server: \n" + imageIn.toString());
        }
    }
}