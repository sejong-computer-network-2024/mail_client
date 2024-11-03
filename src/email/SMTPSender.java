package email;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Base64;
//SMTP를 통한 이메일 전송
public class SMTPSender {

    public static void sendEmail(String smtpServer, int port, String username, String password,
                                 String toAddress, String subject, String body) throws IOException {
        // SMTP 서버에 연결 설정
        Socket socket = new Socket(smtpServer, port);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        // 서버 응답 읽기
        System.out.println("Response: " + reader.readLine());

        // HELO 명령어 보내기
        writer.write("HELO " + smtpServer + "\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

// 로그인 및 인증 생략
//        // 인증 정보 전송 (AUTH LOGIN)
//        writer.write("AUTH LOGIN\r\n");
//        writer.flush();
//        System.out.println("Response: " + reader.readLine());
//
//        // 사용자 이름과 비밀번호를 Base64로 인코딩하여 전송
//        writer.write(Base64.getEncoder().encodeToString(username.getBytes()) + "\r\n");
//        writer.flush();
//        System.out.println("Response: " + reader.readLine());
//
//        writer.write(Base64.getEncoder().encodeToString(password.getBytes()) + "\r\n");
//        writer.flush();
//        System.out.println("Response: " + reader.readLine());

        // 보내는 주소와 받는 주소 설정
        writer.write("MAIL FROM:<" + username + ">\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        writer.write("RCPT TO:<" + toAddress + ">\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // 메일 전송 시작 (DATA 명령)
        writer.write("DATA\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // 메일 제목, 본문 작성 후 종료 (.을 포함하는 줄로 종료)
        writer.write("Subject: " + subject + "\r\n");
        writer.write("From: " + username + "\r\n");
        writer.write("To: " + toAddress + "\r\n");
        writer.write("\r\n" + body + "\r\n.\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // QUIT 명령으로 연결 종료
        writer.write("QUIT\r\n");
        writer.flush();
        System.out.println("Response: " + reader.readLine());

        // 리소스 해제
        writer.close();
        reader.close();
        socket.close();
    }
}
