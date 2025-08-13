package kasirmini.service;

import java.sql.*;
import java.util.ArrayList;
import kasirmini.service.Database;
import kasirmini.model.Produk;

public class ProdukService {
    public static ArrayList<Produk> getAllProduk() {
        ArrayList<Produk> list = new ArrayList<>();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM produk");
            while (rs.next()) {
                Produk p = new Produk(
                        rs.getString("nama_produk"),
                        rs.getDouble("harga")
                );
                list.add(p);
            }
        } catch (Exception e) {
            System.out.println("Gagal ambil produk: " + e.getMessage());
        }
        return list;
    }
}
