package emailClient;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import email.Email;
import email.IMAPReceiver;

public class MailDetailPanel extends EmailClientPanel {
	private Email email;

	// UI 컴포넌트들 선언
	private JLabel subjectLabel;
	private JLabel fromLabel;
	private JLabel toLabel;
	private JLabel ccLabel;
	private JLabel dateLabel;
	private JTextArea bodyTextArea;
	private JPanel bottomPanel;
	private JButton backToListBtn;
	private JButton deleteBtn;

	public MailDetailPanel() {
		setLayout(new BorderLayout());

		// 상단 정보 패널 (제목, 보낸 사람, 받는 사람 등)
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(5, 1)); // 5개의 행을 가진 그리드 레이아웃

		// 레이블 초기화 및 추가
		subjectLabel = new JLabel();
		fromLabel = new JLabel();
		toLabel = new JLabel();
		ccLabel = new JLabel();
		dateLabel = new JLabel();

		infoPanel.add(new JLabel("제목:"));
		infoPanel.add(subjectLabel);
		infoPanel.add(new JLabel("보낸 사람:"));
		infoPanel.add(fromLabel);
		infoPanel.add(new JLabel("받는 사람:"));
		infoPanel.add(toLabel);
		infoPanel.add(new JLabel("참조:"));
		infoPanel.add(ccLabel);
		infoPanel.add(new JLabel("날짜:"));
		infoPanel.add(dateLabel);

		add(infoPanel, BorderLayout.NORTH);

		// 본문 표시를 위한 텍스트 영역
		bodyTextArea = new JTextArea();
		bodyTextArea.setLineWrap(true);
		bodyTextArea.setWrapStyleWord(true);
		bodyTextArea.setEditable(false); // 편집 불가
		JScrollPane bodyScrollPane = new JScrollPane(bodyTextArea);
		add(bodyScrollPane, BorderLayout.CENTER);

		bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridLayout(1, 2));
		add(bottomPanel, BorderLayout.SOUTH);
		backToListBtn = new JButton("메일 목록");
		backToListBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EmailClientFrame frame = EmailClientFrame.getEmailClientFrame();
				frame.mailListPanel.init();
				frame.changePanel(frame.mailListPanel);
			}
		});
		bottomPanel.add(backToListBtn);

		deleteBtn = new JButton("삭제");
		deleteBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				EmailClientFrame frame = EmailClientFrame.getEmailClientFrame();
				try {
					IMAPReceiver.deleteMail(EmailClientFrame.SERVER, EmailClientFrame.IMAP_PORT, frame.getUserId(), frame.getUserPassword(), email.getNo());
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				frame.mailListPanel.init();
				frame.changePanel(frame.mailListPanel);
			}
		});
		bottomPanel.add(deleteBtn);
	}

	// 특정 이메일의 상세 내용을 로드하고 UI 업데이트
	public void init(int no) {
		EmailClientFrame frame = EmailClientFrame.getEmailClientFrame();
		try {
			// 이메일 세부 정보 가져오기
			email = IMAPReceiver.fetchEmailDetail(EmailClientFrame.SERVER, EmailClientFrame.IMAP_PORT,
					frame.getUserId(), frame.getUserPassword(), no);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// UI에 이메일 세부 정보 표시
		if (email != null) {
			subjectLabel.setText(email.getSubject());
			fromLabel.setText(email.getFrom());
			toLabel.setText(email.getTo());
			ccLabel.setText(email.getCc() != "" ? email.getCc() : "없음"); // 참조가 없을 경우 "없음" 표시
			dateLabel.setText(email.getDate().toString());
			bodyTextArea.setText(email.getBody());
		}

		System.out.println(email);
	}

	@Override
	public void init() {
		// init(int no) 호출 전 초기화가 필요한 경우 구현
	}

}
