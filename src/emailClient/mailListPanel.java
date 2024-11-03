package emailClient;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import email.Email;
import email.IMAPReceiver;

public class mailListPanel extends EmailClientPanel {
	private JButton newMailBtn;
	private JButton refreshBtn;
	private JTextField searchField;
	private JTable mailTable;
	private DefaultTableModel tableModel;

	public mailListPanel() {
		// 레이아웃 설정
		setLayout(new BorderLayout());

		// 상단 패널에 버튼 및 검색 필드 추가
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		newMailBtn = new JButton("메일 쓰기");
		refreshBtn = new JButton("새로 고침");
		searchField = new JTextField(15);
		searchField.setToolTipText("메일 검색");

		topPanel.add(newMailBtn);
		topPanel.add(refreshBtn);
		topPanel.add(searchField);
		add(topPanel, BorderLayout.NORTH);

		// 메일 목록 테이블 설정
		String[] columnNames = { "보낸 사람", "제목", "받은 시간" };
		tableModel = new DefaultTableModel(columnNames, 0);
		mailTable = new JTable(tableModel);

		// 테이블 스크롤 설정
		JScrollPane scrollPane = new JScrollPane(mailTable);
		add(scrollPane, BorderLayout.CENTER);

		// 이벤트 리스너 설정
		setButtonListeners();

	}

	private void setButtonListeners() {
		newMailBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EmailClientFrame frame = EmailClientFrame.getEmailClientFrame();
				frame.changePanel(frame.mailEditPanel);
			}
		});

		refreshBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 새로 고침 기능 추가
				JOptionPane.showMessageDialog(mailListPanel.this, "메일 목록 새로 고침");
				refreshMailList();
			}
		});
	}

	// 메일 목록 새로 고침 메서드 (임시 데이터 추가 예시)
	private void refreshMailList() {
		List<Email> emailList = null;
		EmailClientFrame frame = EmailClientFrame.getEmailClientFrame();
		try {
			emailList = IMAPReceiver.fetchEmails(EmailClientFrame.SERVER, EmailClientFrame.IMAP_PORT, frame.getUserId(),
					frame.getUserPassword());
			System.out.println("Emails fetched successfully.");
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 기존 데이터 지우기
		tableModel.setRowCount(0);

		// 새로운 메일 목록 데이터 추가 (임시 데이터 예시)
//		Object[][] sampleData = { { "Alice", "Hello!", "2024-11-01 10:30" },
//				{ "Bob", "Meeting update", "2024-11-01 09:15" },
//				{ "Charlie", "Invoice attached", "2024-11-01 08:45" } };
//
//		for (Object[] row : sampleData) {
//			tableModel.addRow(row);
//		}
		
		if (emailList != null) {
			for (Email email : emailList) {
				tableModel.addRow(new String[] { email.getFrom(), email.getBody(), email.getDate() });
			}
		}

	}

	public void init() {
		refreshMailList();
	}
}
