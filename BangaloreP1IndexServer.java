import java.net.*;
import java.io.*;
import java.util.*;

public class BangaloreP1IndexServer{
    public static void main(String[] args) throws IOException{

        BufferedReader input;
        PrintWriter output;
        String username="";
		String password ="";
        String filename ="";

        try
        {
        
            ServerSocket ss = new ServerSocket(4999);
            Socket socket = ss.accept();

            System.out.println("Client Connected \n Requesting Client to  log in\n");

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            username = input.readLine();
            password = input.readLine();
        
            //validating user name and password
            InputStream file = BangaloreP1IndexServer.class.getResourceAsStream("usrList.txt");
            try (Scanner sc = new Scanner(file)) 
            {
                while (sc.hasNextLine()) 
                {
                    String[] line = sc.nextLine().split(";");
                    if (line[0].equals(username) && line[1].equals(password)) 
                    {
                       System.out.println(username.toUpperCase() + " is connected to Index server \n");
                       
                       //sending success flag to client
                       output.println("no");
                       output.flush();
                       break;
                    }
                    else 
                    {   
                        output.println("yes");
				    	output.flush();
                        break;
                    }
                } 
            }


            //reading client request 
            filename = input.readLine();
            boolean flag = false;
            
            file = BangaloreP1IndexServer.class.getResourceAsStream("contentList.txt");
            try (Scanner sc = new Scanner(file)) 
            {
                while (sc.hasNextLine()) 
                {
                    String[] line = sc.nextLine().split(";");
                    if (line[0].equals(filename))
                    {
                        System.out.println("\n Sending file info to Client ");
                        output.println("filefound");
                        output.println(line[1]);
                        output.println(line[2]);
                        output.println(line[3]);
                        output.println(line[4]);
                        output.flush();
                        flag = true;
                    }
                }
                if(flag == false)
                 {
                    output.println("filenotfound");
                    output.flush();
                    System.out.println("\n Requested file not found in any of the server. Bye! ");
                    try{
                        Thread.sleep(2000000); 
                    }
                    catch (Exception e)
                    {
                        System.out.println(e);
                    }
                    System.exit(0);
                 }   

            } 
            
            //closing file stream
            file.close();
    }

    catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }
    }

}

    