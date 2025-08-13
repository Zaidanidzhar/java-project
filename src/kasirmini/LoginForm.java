package kasirmini;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

import kasirmini.service.Database;

public class LoginForm extends JFrame {
    JTextField tfUser = new JTextField();
    JPasswordField tfPass = new JPasswordField();
    JButton btnLogin = new JButton("Login");

    public LoginForm() {
        setTitle("Login Kasir");
        setSize(300, 150);
        setLayout(new GridLayout(3, 2, 10, 5));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        add(new JLabel("Username:"));
        add(tfUser);
        add(new JLabel("Password:"));
        add(tfPass);
        add(new JLabel(""));
        add(btnLogin);

        btnLogin.addActionListener(e -> {
            String user = tfUser.getText().trim();
            String pass = new String(tfPass.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username dan password harus diisi!");
                return;
            }

            try {
                Connection conn = Database.getConnection();
                PreparedStatement pst = conn.prepareStatement(
                    "SELECT * FROM user WHERE username=? AND password=?"
                );
                pst.setString(1, user);
                pst.setString(2, pass);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("role");
                    JOptionPane.showMessageDialog(this, "Login berhasil sebagai " + role);
                    this.dispose();
                    new KasirGUI(role).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Username atau password salah!");
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Koneksi gagal: " + ex.getMessage());
            }
        });
    }
}
