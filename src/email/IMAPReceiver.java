package email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//IMAP를 통한 이메일 수신
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
        // IMAP 서버에 연결 설정
        Socket socket = new Socket(imapServer, port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // 서버 응답 읽기
        System.out.println("Response: " + reader.readLine());

        // LOGIN 명령어로 로그인
        writer.write("a001 LOGIN " + username + " " + password + "\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // INBOX 열기
        writer.write("a002 SELECT INBOX\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // 메일 제목 읽기 요청 (가장 최근 10개만)
        writer.write("a003 FETCH 1:* (BODY[HEADER.FIELDS (FROM SUBJECT DATE)])\r\n");
        writer.flush();

        // 서버로부터 메일 제목을 읽고 출력
        String response;
        StringBuilder sb = new StringBuilder(10000);
        while ((response = reader.readLine()) != null) {
            if (response.equals("a003 OK FETCH completed")) break;
            sb.append(response);
            sb.append("\r\n");
        }
        String fullMessage = sb.toString();
        System.out.println(fullMessage);
        
        List<Email> emailList = new LinkedList<>();
        List<String> emailStrList = splitEmails(fullMessage);
        for(String emailStr : emailStrList) {
        	 Email email = new Email(emailStr);
             emailList.add(email);
        }

        // 로그아웃
        writer.write("a004 LOGOUT\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // 리소스 해제
        writer.close();
        reader.close();
        socket.close();
        
        return emailList;
    }
    public static Email fetchEmailDetail(String imapServer, int port, String username, String password, int no) throws IOException { 
    	Socket socket = new Socket(imapServer, port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // 서버 응답 읽기
        System.out.println("Response: " + reader.readLine());

        // LOGIN 명령어로 로그인
        writer.write("a001 LOGIN " + username + " " + password + "\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // INBOX 열기
        writer.write("a002 SELECT INBOX\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // 메일 제목 읽기 요청 (가장 최근 10개만)
        writer.write("a003 FETCH " + no + " (BODY[])");
        writer.flush();

        // 서버로부터 메일 제목을 읽고 출력
        String response;
        StringBuilder sb = new StringBuilder(10000);
        //이슈 탈출 안됨 response가 뭔지 모르겠음.
        while ((response = reader.readLine()) != null) {
        	System.out.println(response);
            if (response.equals("a003 OK FETCH completed")) break;
            sb.append(response);
            sb.append("\r\n");
        }
        String fullMessage = sb.toString();
        System.out.println(fullMessage);
        
        Email email = new Email(fullMessage);

        // 로그아웃
        writer.write("a004 LOGOUT\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // 리소스 해제
        writer.close();
        reader.close();
        socket.close();
        
        return email;
    }
    

//    public static void main(String[] args) {
//        String imapServer = "imap.example.com";
//        int port = 143;  // 보통 포트 143 (보안 사용 안함)
//        String username = "your_email@example.com";
//        String password = "your_password";
//
//        try {
//            fetchEmails(imapServer, port, username, password);
//            System.out.println("Emails fetched successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
