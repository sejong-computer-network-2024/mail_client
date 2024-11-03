package emailClient;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import email.SMTPSender;

public class mailEditPanel extends EmailClientPanel {
	private JTextField toField;
    private JTextField ccField;
    private JTextField subjectField;
    private JTextArea messageArea;
    private JButton sendButton;
    
	
	 public mailEditPanel() {
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

	        // 메인 패널에 상단 패널 추가
	        add(topPanel, BorderLayout.NORTH);

	        // 내용 작성 영역
	        messageArea = new JTextArea(15, 30);
	        JScrollPane messageScrollPane = new JScrollPane(messageArea);
	        add(messageScrollPane, BorderLayout.CENTER);

	        // 하단 패널: 전송 버튼
	        JPanel bottomPanel = new JPanel();
	        sendButton = new JButton("Send");
	        bottomPanel.add(sendButton);
	        add(bottomPanel, BorderLayout.SOUTH);

	        // 전송 버튼 이벤트 처리
	        sendButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	            	EmailClientFrame frame = EmailClientFrame.getEmailClientFrame();
	            	try {
	            		SMTPSender.sendEmail(EmailClientFrame.SERVER, EmailClientFrame.SMTP_PORT, frame.getUserId(), frame.getUserPassword(),
						        toField.getText(), subjectField.getText(), messageArea.getText());
						System.out.println("Email sent successfully.");
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	            }
	        });
	    }
}
