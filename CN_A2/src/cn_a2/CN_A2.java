package cn_a2;
import java.io.*;
import java.net.*;

public class CN_A2 {

    public static void main(String[] args) throws IOException {
ServerSocket proxy=null;
int localport=80;
try
{
    proxy=new ServerSocket(localport);
    System.err.println("Connected and statred Listening");    
}
catch (IOException e)
{
    System.err.println("local port could not be connected");
}
for(int c=1;c<=100;c++)
{
            new thread_proxy(proxy.accept()).start();
}
    
proxy.close();
        
    }
}
