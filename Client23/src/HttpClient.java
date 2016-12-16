import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
public class HttpClient {
     public static void main(String[] args) throws IOException {
    	 PrintWriter printWriter = null;   
    	 Socket socket = null;
    	 File file = null;   
            BufferedReader bufferedReader = null;
            DataInputStream dataInputStream = null;
            
            int content_length_to_int = 0;
            byte[] arrbytes = new byte[1024];
            System.out.println("Enter the Port Number of the server");
            Scanner cp = new Scanner(System.in);
            int clinet_request_port = cp.nextInt();
            System.out.println("Enter Server Name");
            Scanner cp1 = new Scanner(System.in);
            String client_request_host = cp1.next();
            InetAddress inetAddress = InetAddress.getByName(client_request_host);
            BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Please input file name which you want from server: ");
            String file_name = bufferedReader2.readLine();
            if(file_name!=null){
                try{
                    socket = new Socket(inetAddress,clinet_request_port);
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                }
                catch(IOException error1c){
                    System.out.println("File Error");
                }
                file_name = "/" + file_name;
                System.out.println("Sending A Bad Request For The Intial Test");
                System.out.print("SET " + file_name + " HTTP/1.0\r\n\r\n");
                printWriter.print("SET " + file_name + " HTTP/1.0\r\n\r\n");
                printWriter.flush();
                System.out.println("Receive response from server .....");
                String server_response =null;
                server_response = bufferedReader.readLine();
                if (server_response == null) {
                    System.err.println("Couldn't read response from server.");
                } else if (server_response.equalsIgnoreCase("HTTP/1.0 400 Bad Request")) {
                    System.out.println("\t" + server_response);
                } else {
                    System.out.println("\t Wrong response from the server:\n");
                    System.out.println("\t" + server_response);
                }
                System.out.println("\n\n\n");
                socket.close();
                printWriter.close();
                bufferedReader.close();
                try {
                    socket = new Socket(inetAddress, clinet_request_port);
                    printWriter = new PrintWriter(socket.getOutputStream(), true);
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                }
                catch (UnknownHostException error4c) {
                    System.err.println("Don't know about host: " + client_request_host);
                    System.exit(1);
                }
                System.out.println("Send out the right request..... ");
                System.out.println("GET " + file_name + " HTTP/1.0");
                printWriter.print("GET " + file_name + " HTTP/1.0\r\n");
                printWriter.flush();
                System.out.println("\nReceive response from server .....");
                server_response = bufferedReader.readLine();
                if (server_response == null) {
                    System.err.println("Couldn't read response from server.");
                } else {
                    if (server_response.equalsIgnoreCase("HTTP/1.0 200 OK") || server_response.equalsIgnoreCase("HTTP/1.1 200 OK")) {
                        System.out.println("\t" + server_response);
                        while (!server_response.isEmpty()) {
                            server_response = bufferedReader.readLine();
                            System.out.println("\t" + server_response);
                        }
                        
                        try{
                        	//System.out.println("Enter The File Name for storing sever content");
                        	//Scanner c1 = new Scanner(System.in);
                        	//String client_data_store =  c1.nextLine();
                        	file = new File("./" + file_name);
                        	FileOutputStream client_file = new FileOutputStream(file);
                            //String line;
                            BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            FileWriter fw = new FileWriter(file);
                            BufferedWriter bufferredWriter = new BufferedWriter(fw);
                            String line1;
                            //String line2;
                            System.out.println("Server's File Data");
                            while ((line1 = bufferedReader3.readLine()) != null) {
                                bufferredWriter.write(line1);
                                bufferredWriter.newLine();
                                System.out.println(line1);
                               
                                
                            }
                            bufferredWriter.close();
//                            FileReader fr = new FileReader("server_data.txt");
//                            BufferedReader bufferedReader4 = new BufferedReader(fr);
//                            System.out.println("Reading the file data sent by server");
//                            while((line2 = bufferedReader4.readLine())!=null){
//                            	System.out.println(line2);
//                            }
 
                            client_file.close();
                            bufferedReader3.close();
//                            bufferedReader4.close();
                        }
                        catch(IOException error2c){
                            System.out.println("Could Not Create A new file" + error2c);
                        }
                        
                    }

                }
            
            System.out.println("Closing and quiting client");
            printWriter.close();
            bufferedReader.close();
            bufferedReader2.close();
            socket.close();
            }
}
}