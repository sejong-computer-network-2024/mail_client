package email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
//IMAP를 통한 이메일 수신
public class IMAPReceiver {

    public static void fetchEmails(String imapServer, int port, String username, String password) throws IOException {
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
        writer.write("a003 FETCH 1:10 BODY[HEADER.FIELDS (SUBJECT)]\r\n");
        writer.flush();

        // 서버로부터 메일 제목을 읽고 출력
        String response;
        while ((response = reader.readLine()) != null) {
            if (response.equals("a003 OK FETCH completed")) break;
            System.out.println(response);
        }

        // 로그아웃
        writer.write("a004 LOGOUT\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // 리소스 해제
        writer.close();
        reader.close();
        socket.close();
    }
    public static void fetchEmailDetail(String imapServer, int port, String username, String password) throws IOException { 
    	
    	return;
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
