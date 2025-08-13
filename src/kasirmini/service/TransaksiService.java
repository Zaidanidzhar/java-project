package kasirmini.service;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import kasirmini.service.Database;

public class TransaksiService {

    public static void simpanTransaksi(DefaultTableModel keranjang, double total, double bayar, double kembalian) {
        try {
            Connection conn = Database.getConnection();

            String sqlTransaksi = "INSERT INTO transaksi (total_harga, uang_bayar, uang_kembalian) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS);
            pst.setDouble(1, total);
            pst.setDouble(2, bayar);
            pst.setDouble(3, kembalian);
            pst.executeUpdate();

            ResultSet rs = pst.getGeneratedKeys();
            int idTransaksi = rs.next() ? rs.getInt(1) : 0;

            String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_produk, jumlah, subtotal) VALUES (?, ?, ?, ?)";
            PreparedStatement psDetail = conn.prepareStatement(sqlDetail);

            for (int i = 0; i < keranjang.getRowCount(); i++) {
                String nama = keranjang.getValueAt(i, 0).toString();
                int jumlah = Integer.parseInt(keranjang.getValueAt(i, 2).toString());
                double subtotal = Double.parseDouble(keranjang.getValueAt(i, 3).toString());

                ResultSet rsId = conn.createStatement().executeQuery(
                        "SELECT id_produk FROM produk WHERE nama_produk = '" + nama + "'"
                );
                int idProduk = rsId.next() ? rsId.getInt(1) : 0;

                psDetail.setInt(1, idTransaksi);
                psDetail.setInt(2, idProduk);
                psDetail.setInt(3, jumlah);
                psDetail.setDouble(4, subtotal);
                psDetail.addBatch();
            }

            psDetail.executeBatch();
        } catch (Exception e) {
            System.out.println("Gagal simpan transaksi: " + e.getMessage());
        }
    }
}
