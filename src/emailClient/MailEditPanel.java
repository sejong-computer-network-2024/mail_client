package emailClient;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.Base64;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import email.SMTPSender;

public class MailEditPanel extends EmailClientPanel {
	private JTextField toField;
    private JTextField ccField;
    private JTextField subjectField;
    private JTextArea messageArea;
    private JButton sendButton;
    private JButton backToListBtn;
    
    private final static String PLAIN_TEXT_HEADER = "Content-Type: text/plain; charset=\"utf-8\"\r\n"
    		+ "Content-Transfer-Encoding: base64\r\n\r\n";
    
    private final static String OUT_HEADER = "Content-Type: multipart/alternative;\r\n\r\n";
    		
    		
	 public MailEditPanel() {
	        setLayout(new BorderLayout());

	        // 상단 패널: 수신자, 참조, 제목 입력
	        JPanel topPanel = new JPanel(new GridBagLayout());
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.insets = new Insets(5, 5, 5, 5);
	        gbc.fill = GridBagConstraints.HORIZONTAL;

	        // 받는 사람 입력
	        gbc.gridx = 0;
	        gbc.gridy = 0;
	        topPanel.add(new JLabel("To:"), gbc);
	        
	        gbc.gridx = 1;
	        toField = new JTextField(30);
	        topPanel.add(toField, gbc);

	        // 참조(CC) 입력
	        gbc.gridx = 0;
	        gbc.gridy = 1;
	        topPanel.add(new JLabel("CC:"), gbc);
	        
	        gbc.gridx = 1;
	        ccField = new JTextField(30);
	        topPanel.add(ccField, gbc);

	        // 제목 입력
	        gbc.gridx = 0;
	        gbc.gridy = 2;
	        topPanel.add(new JLabel("Subject:"), gbc);
	        
	        gbc.gridx = 1;
	        subjectField = new JTextField(30);
	        topPanel.add(subjectField, gbc);

	        add(topPanel, BorderLayout.NORTH);

	        // 내용 작성 영역
	        messageArea = new JTextArea(15, 30);
	        JScrollPane messageScrollPane = new JScrollPane(messageArea);
	        add(messageScrollPane, BorderLayout.CENTER);

	        // 하단 패널(전송 버튼, 메일 목록 버튼)
	        JPanel bottomPanel = new JPanel();
	        bottomPanel.setLayout(new GridLayout());
	        
	        sendButton = new JButton("Send");
	        bottomPanel.add(sendButton);
	        add(bottomPanel, BorderLayout.SOUTH);
	        sendButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	EmailClientFrame frame = EmailClientFrame.getEmailClientFrame();
	            	try {
	            		SMTPSender.sendEmail(EmailClientFrame.SERVER, EmailClientFrame.SMTP_PORT, frame.getUserId(), frame.getUserPassword(),
						        toField.getText(), ccField.getText(), subjectField.getText(), encodeBody());
						System.out.println("Email sent successfully.");
					} catch (IOException e1) {
						e1.printStackTrace();
					} finally {
						frame.mailListPanel.init();
						frame.changePanel(frame.mailListPanel);
					}
	            }
	        });
	        
	        backToListBtn = new JButton("메일 목록");
	        bottomPanel.add(backToListBtn);
	        backToListBtn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					EmailClientFrame frame = EmailClientFrame.getEmailClientFrame();
					frame.mailListPanel.init();
					frame.changePanel(frame.mailListPanel);
				}
			});
	        
	    }
	 private String encodeBody() throws IOException {
		    String boundary = generateBoundary(); // Boundary 생성

		    // MIME 멀티파트 헤더 설정
		    StringBuilder emailBody = new StringBuilder();
		    emailBody.append("MIME-Version: 1.0\r\n");
		    emailBody.append("Content-Type: multipart/alternative; boundary=").append("\"" + boundary + "\"").append("\r\n\r\n");

		    // PLAIN_TEXT_HEADER와 본문 추가
		    emailBody.append("--").append(boundary).append("\r\n"); // Boundary 시작
		    emailBody.append(PLAIN_TEXT_HEADER);
		    String plainTextBody = Base64.getEncoder().encodeToString(messageArea.getText().getBytes());
		    emailBody.append(plainTextBody).append("\r\n\r\n");

		    // 종료 Boundary
		    emailBody.append("--").append(boundary).append("--").append("\r\n\r\n");

		    return emailBody.toString();
		}
	 
	 private static String generateBoundary() {	//바운더리 생성 함수
	        byte[] randomBytes = new byte[16]; // 임의의 바이트 배열 크기 설정
	        new SecureRandom().nextBytes(randomBytes);
	        return "Boundary-" + Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
	    }
}
