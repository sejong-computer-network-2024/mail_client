package emailClient;

import java.net.SecureCacheResponse;

import javax.swing.JFrame;

public class EmailClientFrame extends JFrame {
	private static EmailClientFrame emailClientFrame = null;
	public final static int WIDTH = 600;
	public final static int HEIGHT = 800;

	public final static String SERVER = "mail.yeop.site";
	public final static int SMTP_PORT = 25;
	public final static int IMAP_PORT = 143;

	private String userId;
	private String userPassword;

	private EmailClientPanel currentPanel = null;
	public EmailClientPanel loginPanel = new LoginPanel();
	public EmailClientPanel mailEditPanel = new MailEditPanel();
	public EmailClientPanel mailListPanel = new MailListPanel();
	public EmailClientPanel mailDetailPanel = new MailDetailPanel();

	public static EmailClientFrame getEmailClientFrame() {
		if (emailClientFrame == null)
			emailClientFrame = new EmailClientFrame();
		return emailClientFrame;
	}

	private EmailClientFrame() {
		setSize(WIDTH, HEIGHT);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		changePanel(loginPanel);
		setVisible(true);
	}

	public void changePanel(EmailClientPanel nextPanel) {
		if (currentPanel != null) {
			remove(currentPanel);
		}
		add(nextPanel);
		currentPanel = nextPanel;

		revalidate();
		repaint();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}
}
