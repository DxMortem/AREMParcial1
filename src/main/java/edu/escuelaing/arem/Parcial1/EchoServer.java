package edu.escuelaing.arem.Parcial1;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        while (true) {
            ServerSocket serverSocket = null;
            try {
                serverSocket = new ServerSocket(getPort());
            } catch (IOException e) {
                System.err.println("Could not listen on port: " + getPort());
                System.exit(1);
            }
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
                System.out.println("Estoy Listo");
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            clientSocket.getInputStream()));
            String inputLine, outputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Mensaje:" + inputLine);
                outputLine = results(inputLine);
                out.println(outputLine);
                if (outputLine.equals("Respuestas: Bye."))
                    break;
            }
            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();
        }
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 4567; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    private static String results(String req) {
        String[] numbstr = req.split(",");
        List<String> numberstr = Arrays.asList(numbstr);
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i=0; i<numberstr.size();i++) {
            nums.add(Integer.valueOf(numberstr.get(i)));
        }
        int sum = 0;
        int mult = 1;
        for (Integer number : nums) {
            sum += number;
            mult *= number;
        }
        String response = "\"response\":{ " +
                "\"list\": " + nums.toString() + "," +
                "\"max\":" + Collections.max(nums).toString() + "," +
                "\"min\":" + Collections.min(nums).toString() + "," +
                "\"sum\":" + sum + "," +
                "\"mult\":" + mult + "}";
        return "{" + response + "}";
    }

}