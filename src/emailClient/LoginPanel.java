package emailClient;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginPanel extends EmailClientPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    LoginPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        
        // Username label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);
        gbc.gridx = 2;
        add(new JLabel("@yeop.site"), gbc);
        
        // Password label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);
        
        // Login button
        gbc.gridx = 1;
        gbc.gridy = 2;
        loginButton = new JButton("Login");
        add(loginButton, gbc);

        // Add action listener to the login button
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    // Method to handle login when the button is clicked
    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        
        // Simulate login logic (for demonstration)
//        if (username.equals("user") && password.equals("password")) {
//            JOptionPane.showMessageDialog(this, "Login Successful!");
//        } else {
//            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
//        }
        
        EmailClientFrame frame = EmailClientFrame.getEmailClientFrame();
        username += "@yeop.site";
        frame.setUserId(username);
        frame.setUserPassword(password);
        frame.mailListPanel.init();
        frame.changePanel(frame.mailListPanel);
    }
}