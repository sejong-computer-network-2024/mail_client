package email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IMAPReceiver {
	
	private static List<String> splitEmails(String fullMessage) {
		List<String> emailStrList = new LinkedList<String>();
		
		Pattern pattern = Pattern.compile("\\* \\d+ FETCH \\(BODY\\[.*?\\] \\{\\d+\\}(.*?)\n\\)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fullMessage);
        
        while (matcher.find()) {
            String emailStr = matcher.group(0).trim();
            emailStrList.add(emailStr);
        }
		
		return emailStrList;
	}
	
    public static List<Email> fetchEmails(String imapServer, int port, String username, String password) throws IOException {
        Socket socket = new Socket(imapServer, port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        System.out.println("Response: " + reader.readLine());

        writer.write("a001 LOGIN " + username + " " + password + "\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        writer.write("a002 SELECT INBOX\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        writer.write("a003 FETCH 1:* (BODY[HEADER.FIELDS (FROM SUBJECT DATE)])\r\n");
        writer.flush();

        String response;
        StringBuilder sb = new StringBuilder(10000);
        while ((response = reader.readLine()) != null) {
        	System.out.println("Response: " + response);
            if (response.equals("a003 OK FETCH completed")) break;
            sb.append(response);
            sb.append("\r\n");
        }
        String fullMessage = sb.toString();
        
        List<Email> emailList = new LinkedList<>();
        List<String> emailStrList = splitEmails(fullMessage);
        for(String emailStr : emailStrList) {
        	 Email email = new Email(emailStr);
             emailList.add(email);
        }

        writer.write("a004 LOGOUT\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        writer.close();
        reader.close();
        socket.close();
        
        return emailList;
    }
    public static Email fetchEmailDetail(String imapServer, int port, String username, String password, int no) throws IOException { 
    	Socket socket = new Socket(imapServer, port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        System.out.println("Response: " + reader.readLine());

        writer.write("a001 LOGIN " + username + " " + password + "\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());


        writer.write("a002 FETCH " + no + " (BODY[])\r\n");
        writer.flush();

        String response;
        StringBuilder sb = new StringBuilder(10000);
        while ((response = reader.readLine()) != null) {
        	System.out.println(response);
            if (response.equals("a002 OK FETCH completed")) break;
            sb.append(response);
            sb.append("\r\n");
        }
        String fullMessage = sb.toString();
        
        Email email = new Email(fullMessage);

        writer.write("a003 LOGOUT\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        writer.close();
        reader.close();
        socket.close();
        
        return email;
    }

	public static void deleteMail(String imapServer, int port, String username, String password, int no) throws IOException {
		Socket socket = new Socket(imapServer, port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        System.out.println("Response: " + reader.readLine());

        writer.write("a001 LOGIN " + username + " " + password + "\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        writer.write("a002 STORE " + no + " +FLAGS (\\Deleted)\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        writer.write("a003 LOGOUT\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        writer.close();
        reader.close();
        socket.close();
		
	}
    
}
