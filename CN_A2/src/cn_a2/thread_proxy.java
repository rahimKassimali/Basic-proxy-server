package cn_a2;
import java.io.*;
import java.net.*;
import java.util.*;
public class thread_proxy extends Thread
{
    int MAXIMUM_BUFFER_SIZE = 650000;
    String user_agent_browser = "Mozilla/5.0";
    Socket proxy_socket =new Socket();
    
    public thread_proxy(Socket socket) 
    {
        this.proxy_socket = socket;
    }
              
 
    @Override
    public void run()
    {
        try {
            DataOutputStream respone_output =new DataOutputStream(proxy_socket.getOutputStream());
            // returing resposne to the browser
            
            BufferedReader URL_input = new BufferedReader(new InputStreamReader(proxy_socket.getInputStream()));
            // reading URL request and taking out the URL            
            InputStream checker=proxy_socket.getInputStream();
            
            String input="",URL="",full_request="";
            int count =0;
            boolean allow=false;
            
// 
            // 
            while ((input = URL_input.readLine())!=null)
            {
                try
                {
                    full_request+=input+"\n";
                    StringTokenizer part = new StringTokenizer(input);
                    part.nextToken();
                }
                catch(Exception e)
                {
                    break;
                }
                if (count == 0)  // for first line , checking it contains get or not
                {     
                    
                    String[] parts = input.split(" ");
                    URL = parts[1];
                    if(parts[0].equals("GET") && parts[2].equals("HTTP/1.1"))
                    {
                        allow=true;
                    }
                }
                if (count == 1) 
                {     
                 
                    String[] parts = input.split(" ");
                    if(!parts[0].equals("Host:"))
                    {
                        allow=false;
                    }
                }
              
                count++;
            }
           // System.out.println("Here is the request\n" + full_request);
          allow=lines_checker(full_request);
            try {
                if(allow == false)
                {
              respone_output.writeBytes("Error 501 : Not implemented");
              respone_output.flush();
                }
                else
                {
             InputStream respone_input=GET_REQUEST(URL);  
         
             byte response_msg[] = new byte[ MAXIMUM_BUFFER_SIZE ];
                int iterator = respone_input.read( response_msg, 0, MAXIMUM_BUFFER_SIZE );
                while ( iterator != -1 )
                {
                  respone_output.write( response_msg, 0, iterator );
                  iterator = respone_input.read( response_msg, 0, MAXIMUM_BUFFER_SIZE );
                }
                respone_output.flush();
            } 
            }
            catch (Exception e)
            {
                System.out.println("Exception  : " + e);
                respone_output.writeBytes("");
            }

            if (proxy_socket != null)
            {
                proxy_socket.close();
            }
            if (respone_output != null)
            {
                respone_output.close();
            }
            if (URL_input!= null) 
            {
                URL_input.close();
            }
        } 
        catch (IOException e)
        {
       System.out.println("Exception  : " + e);     
        }    
}
    private InputStream GET_REQUEST(String url) throws Exception 
       {	
		URL link = new URL(url);
		HttpURLConnection con = (HttpURLConnection) link.openConnection();
		con.setRequestMethod("GET"); // request method is "GET"   
		con.setRequestProperty("User-Agent", user_agent_browser);// specifiying the browser
		System.out.println("Sending http GET request to specified URL : " + url);
		System.out.println("Response  Code recieved : " + con.getResponseCode());
                return con.getInputStream(); //returing response
	}   
 public boolean lines_checker(String full_request)
            {
                boolean allow=true;
                
            String[] lines = full_request.split(System.getProperty("line.separator"));
                for(int c=0;c<lines.length;c++)
                {
                    System.out.println(lines[c]);
                      if (c == 0) 
                {                      
                    String[] parts = lines[c].split(" ");
                    if(!parts[0].equals("GET") && !parts[2].equals("HTTP/1.1") )
                    {
                    System.out.println("error");                        
                        allow=false;
                    }
                }
                      if (c == 1) 
                {                      
                    String[] parts = lines[c].split(" ");
                    if(!parts[0].equals("Host:"))
                    {
                        allow=false;
                    }
                }
                      if (c == 2) 
                {                      
                    String[] parts = lines[c].split(" ");
                    if(!parts[0].equals("User-Agent:"))
                    {
                        allow=false;
                    }
                }
                      if (c == 3) 
                {                      
                    String[] parts = lines[c].split(" ");
                    if(!parts[0].equals("Accept:"))
                    {
                        allow=false;
                    }
                }
                      if (c == 4) 
                {                      
                    String[] parts = lines[c].split(" ");
                    if(!parts[0].equals("Accept-Language:"))
                    {
                        allow=false;
                    }
                }
                      if (c == 5) 
                {                      
                    String[] parts = lines[c].split(" ");
                    if(!parts[0].equals("Accept-Encoding:"))
                    {
                        allow=false;
                    }
                }
                      if (c == 6) 
                {                      
                    String[] parts = lines[c].split(" ");
                    if(!parts[0].equals("Connection:"))
                    {
                        allow=false;
                    }
                }
                
                }
                return allow;
            }
}