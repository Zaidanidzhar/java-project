package kasirmini;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import kasirmini.service.Database;

public class LaporanTransaksi extends JFrame {
    public LaporanTransaksi() {
        setTitle("Laporan Transaksi Penjualan");
        setSize(700, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Tanggal", "Total", "Bayar", "Kembalian"}, 0
        );
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM transaksi ORDER BY id_transaksi ASC");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("id_transaksi"),
                    rs.getTimestamp("tanggal"),
                    rs.getDouble("total_harga"),
                    rs.getDouble("uang_bayar"),
                    rs.getDouble("uang_kembalian")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data laporan: " + e.getMessage());
        }
    }
}
