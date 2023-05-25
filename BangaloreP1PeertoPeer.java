import java.net.*;
import java.io.*;
import java.util.*;

public class BangaloreP1PeertoPeer{
    public static void main(String[] args) throws IOException{
        
        
        //initializing threads
        Thread t1 = new Thread(new ClientThread());
        Thread t2 = new Thread(new ServerThread());

        //starting threads
        t1.start();
        t2.start();

    }
}


/******************************************************************************
 ************************CLIENT THREAD CLASS***********************************
 ******************************************************************************/
class ClientThread implements Runnable {

    BufferedReader input;
    PrintWriter output;
    Socket socket;
    String username;
    String login_flag;
    String ftype;
    String avail;
    String mport;
    String mIp;
    String fileflag;
    
    //Constructor for Client thread class
    public ClientThread(){}

    // Request index server for login
    public void ConnectToIndex() throws IOException
    {
        
        try{
            
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            //user name 
            System.out.println("\n Enter User Name:");
            Scanner sc= new Scanner(System.in); 
            output.println(sc.nextLine());
            System.out.println("\n Enter Password:");
            output.println(sc.nextLine());
            output.flush();

            login_flag = input.readLine();
            if(login_flag.equals("yes"))
            {
                System.out.println("\n Client credentials entered are wrong!!! \n Try Again \n");
                System.exit(0);
            }
        }
        catch(UnknownHostException u)
        {
            System.out.println(u);
        }
        catch(IOException i)
        {
            System.out.println(i);
        }

        System.out.println("Exiting ConnectToIndex method");
    }

    public void RequestFile() throws IOException
    {
        try
        {
            System.out.println("Entering RequestFile method");
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

            //Request file name 
            System.out.println("\n Enter File Name:");
            Scanner sc= new Scanner(System.in); 
            output.println(sc.nextLine());
            output.flush();

            //receiving response from index server
            fileflag = input.readLine();
            //exit if file not found
            if(fileflag.equals("filenotfound"))
            {
                System.out.println("\n Requested file not found in any of the server. Bye! ");
                System.exit(0);
            }

            ftype = input.readLine();
            avail = input.readLine();
            mIp = input.readLine();
            mport = input.readLine();
            
            System.out.println("receiving server info");
            System.out.println(" \n Information received from Index server");
            System.out.println(" File type requested is:"+ ftype);
            System.out.println(" Available:"+ avail);
            System.out.println(" media server IP:"+ mIp);
            System.out.println(" media server port number:"+ mport);

            if(avail == "No")
            {
                System.out.println("\n Requested media not available");
                System.exit(0);
            }
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

    public void ConnectToServer() throws IOException
    {
        try
        {
            
            InetAddress addr = InetAddress.getByName(mIp);
            Socket c_socket = new Socket(addr,Integer.parseInt(mport));
            //Socket c_socket = new Socket("localhost",5000);

            DataInputStream dataInputStream = new DataInputStream(c_socket.getInputStream());
            //DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            int bytes = 0;
            FileOutputStream fileOutputStream = new FileOutputStream("Aladdin.txt");

            long size = dataInputStream.readLong(); // read file size
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = dataInputStream.read( buffer, 0, (int)Math.min(buffer.length, size)))!= -1) 
            {
                // Here we write the file using write method
                fileOutputStream.write(buffer, 0, bytes);
                size -= bytes; // read upto file size
            }
            // Here we received file
            System.out.println("\n Client File is Received");
            fileOutputStream.close();

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

    @Override
    public void run() {
        
        //connecting to index server via socket
       try{
        socket = new Socket("172.29.76.14",4999);
        this.ConnectToIndex();
        this.RequestFile();
        this.ConnectToServer();
        
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


/******************************************************************************
 ************************SERVER THREAD CLASS***********************************
 ******************************************************************************/
class ServerThread implements Runnable { 
    
    //Constructor for Server thread class
    public ServerThread(){ }


    @Override
    public void run() {
        try{
            ServerSocket ss = new ServerSocket(5000);
            Socket s = ss.accept();

            System.out.println("\n Client Connected to media server");

            //DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
            
            //sending file to client
            System.out.println("\n Server: sending File");
            int bytes = 0;
            File file = new File("C:/Users/bssha/Documents/aos/mediaFiles/Aladdin.txt");
            FileInputStream fileInputStream = new FileInputStream(file);
            dataOutputStream.writeLong(file.length());
            byte[] buffer = new byte[4 * 1024];
            while ((bytes = fileInputStream.read(buffer))!= -1) 
            {
                System.out.println("Server: Writing File");
                dataOutputStream.write(buffer, 0, bytes);
                dataOutputStream.flush();
            }
            fileInputStream.close();
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