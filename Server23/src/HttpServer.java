import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
public class HttpServer {

public static void main(String[] args){
	BufferedReader bufferedReader = null;
	ServerSocket serverSocket = null;
	PrintWriter printWriter = null;
    OutputStream outputStream = null;
    DataOutputStream dataOutputStream = null;
    Socket socket = null;
    File file = null;
    FileInputStream fileInputStream = null; 
    int number_of_clients=0;
    System.out.println("Enter Port Number to be fixed for server");
    Scanner sc = new Scanner(System.in);
    int port_number = sc.nextInt();
    sc.close();
    System.out.println("The port for communicating with the sever is" + " "+ port_number);
    try {
      serverSocket = new ServerSocket(port_number);
      System.out.println("User can now connect server through "+ port_number);
    }
    catch (IOException error1) {
        System.err.println("Server Socket Unable to Create" + error1);
        System.exit(1);
    }    
    
   while(true){
     try {
           System.out.println(number_of_clients + " "+ " connections served. Accepting new client...");
           socket = serverSocket.accept();
           bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           outputStream = socket.getOutputStream();
           printWriter = new PrintWriter(outputStream, true);
           dataOutputStream = new DataOutputStream(new BufferedOutputStream(outputStream));
       }
       catch (IOException error2) {
           System.err.println("socket communciation failed to achieve from client " + error2);
           System.exit(1);
       }
     try{
       String string = bufferedReader.readLine();
       System.out.println(string);
       if(string!=null){
         String[] string_split = string.split("\\s+",15);
         if (string_split.length != 3) {
                   printWriter.print("HTTP/1.0 400 Bad Request\r\n\r\n");
                   printWriter.flush();
               } else if (!string_split[0].equalsIgnoreCase("get")) {
                   printWriter.print("HTTP/1.0 400 Bad Request\r\n\r\n");
                   printWriter.flush();
               } else if (!string_split[2].equalsIgnoreCase("http/1.0") && !string_split[2].equalsIgnoreCase("http/1.1")) {
                   printWriter.print("HTTP/1.0 400 Bad Request\r\n\r\n");
                   printWriter.flush();
               } else if (string_split[1].charAt(0) != '/') {
                   printWriter.print("HTTP/1.0 400 Bad Request\r\n\r\n");
                   printWriter.flush();
               } else {
                   fileInputStream = null;
                   try {
                       file = new File("." + string_split[1]);//Creates a new File instance 
                       //by converting the given pathname string into an abstract pathname.
                       fileInputStream = new FileInputStream(file); 
                   }
                   catch (FileNotFoundException error3) {
                       printWriter.print("HTTP/1.0 404 Not File Found\r\n\r\n" + error3);
                       printWriter.flush();
                   }
                   if(fileInputStream!=null){
                     printWriter.print("HTTP/1.0 ");
                       printWriter.flush();
                       printWriter.print("200 OK\r\n");
                       printWriter.flush();
                       printWriter.print("Content-Length: " + file.length() + "\r\n\r\n");
                       printWriter.flush();
                       byte[] array_of_bytes = new byte[1024];
                       int bytes_read_buffer = 0;
                       //bytes_read_buffer = fileInputStream.read(array_of_bytes);
                       //if(number_of_clients==0){
                       //bytes_read_buffer = fileInputStream.read(array_of_bytes);	    
                       bytes_read_buffer = fileInputStream.read(array_of_bytes,0,1024);
                       //}
                       //else{
                       //System.out.println(bytes_read_buffer);
                       //bytes_read_buffer = fileInputStream.read(array_of_bytes);
                       //}
                       dataOutputStream.write(array_of_bytes, 0, 1024);
                       dataOutputStream.flush();
                 }
                   if (fileInputStream != null) {
                       fileInputStream.close();
                       fileInputStream = null;
                   }
                   if (bufferedReader != null) {
                       bufferedReader.close();
                       bufferedReader = null;
                   }
                   if (printWriter != null) {
                       printWriter.close();
                       printWriter = null;
                   }
                   if (socket != null) {
                       socket.close();
                       socket = null;
                   }
                   number_of_clients = number_of_clients + 1;
                   
       }
     }
   }
       catch (IOException error4) {
           System.err.println("Server has Crashed" + error4);
           System.exit(1);
           continue;}
       

}
}
}
