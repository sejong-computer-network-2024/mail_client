package email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class SMTPSender {

	public static void sendEmail(String smtpServer, int port, String username, String password, String toAddress,
			String cc, String subject, String body) throws IOException {

		Socket socket = new Socket(smtpServer, port);
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

		System.out.println("Response: " + reader.readLine());

		List<String> rcptList = parseRcpt(toAddress, cc);

		writer.write("HELO " + smtpServer + "\r\n");
		writer.flush();
		System.out.println("Response: " + reader.readLine());

		writer.write("MAIL FROM:<" + username + ">\r\n");
		writer.flush();
		System.out.println("Response: " + reader.readLine());
		for (String rcpt : rcptList) {
			writer.write("RCPT TO:<" + rcpt + ">\r\n");
			writer.flush();
			System.out.println("Response: " + reader.readLine());
		}

		writer.write("DATA\r\n");
		writer.flush();
		System.out.println("Response: " + reader.readLine());
		
		//메일 작성
		writer.write("Subject: " + subject + "\r\n");
		writer.write("From: " + username + "\r\n");
		writer.write("To: " + toAddress + "\r\n");
		writer.write("Cc: " + cc + "\r\n");
		SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
		writer.write("Date: " + formatter.format(new Date()).toString() + "\r\n");
		writer.write(body + "\r\n.\r\n");
		writer.flush();
		System.out.println("Response: " + reader.readLine());

		writer.write("QUIT\r\n");
		writer.flush();
		System.out.println("Response: " + reader.readLine());

		writer.close();
		reader.close();
		socket.close();
	}

	private static List<String> parseRcpt(String to, String cc) {
		List<String> rcptList = new LinkedList<>();
		for (String toStr : to.split(",")) {
			toStr = toStr.trim();
			if(!toStr.equals(""))
				rcptList.add(toStr);
		}
		for (String toStr : cc.split(",")) {
			toStr = toStr.trim();
			if(!toStr.equals(""))
				rcptList.add(toStr);
		}
		return rcptList;
	}
	
}
