package kasirmini.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

import kasirmini.model.Produk;
import kasirmini.service.ProdukService;
import kasirmini.service.TransaksiService; // pastikan ini di package 'service'

public class KasirGUI extends JFrame {
    private JTable tabelProduk, tabelKeranjang;
    private DefaultTableModel modelProduk, modelKeranjang;
    private JTextField tfJumlah, tfBayar, tfTotal, tfKembalian;
    private JButton btnTambah, btnBayar;

    private ArrayList<Produk> listProduk;
    private final ArrayList<Produk> keranjang = new ArrayList<>();

    public KasirGUI() {
        setTitle("Aplikasi Kasir Mini - Warung/UMKM");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponent();
        loadProduk();
        setupEvent();
        
       revalidate();
    repaint();

    }

   private void initComponent() {
    JPanel panelAtas = new JPanel(new GridLayout(1, 2));

    modelProduk = new DefaultTableModel(new String[]{"Nama", "Harga"}, 0);
    tabelProduk = new JTable(modelProduk);
    JScrollPane scrollProduk = new JScrollPane(tabelProduk);

    modelKeranjang = new DefaultTableModel(new String[]{"Nama", "Harga", "Jumlah", "Subtotal"}, 0);
    tabelKeranjang = new JTable(modelKeranjang);
    JScrollPane scrollKeranjang = new JScrollPane(tabelKeranjang);

    panelAtas.add(scrollProduk);
    panelAtas.add(scrollKeranjang);

    JPanel panelBawah = new JPanel(new GridLayout(6, 2, 10, 5));
    tfJumlah = new JTextField();
    tfBayar = new JTextField();
    tfTotal = new JTextField(); tfTotal.setEditable(false);
    tfKembalian = new JTextField(); tfKembalian.setEditable(false);
    btnTambah = new JButton("Tambah ke Keranjang");
    btnBayar = new JButton("Bayar");

    panelBawah.add(new JLabel("Jumlah:"));
    panelBawah.add(tfJumlah);
    panelBawah.add(btnTambah);
    panelBawah.add(new JLabel(""));

    panelBawah.add(new JLabel("Total:"));
    panelBawah.add(tfTotal);
    panelBawah.add(new JLabel("Uang Bayar:"));
    panelBawah.add(tfBayar);
    panelBawah.add(new JLabel("Kembalian:"));
    panelBawah.add(tfKembalian);
    panelBawah.add(btnBayar);
    panelBawah.add(new JLabel(""));

    add(panelAtas, BorderLayout.CENTER);
    add(panelBawah, BorderLayout.SOUTH);
}

        private void loadProduk() {
         listProduk = ProdukService.getAllProduk();
         for (Produk p : listProduk) {
             modelProduk.addRow(new Object[]{p.getNama(), p.getHarga()});
         }
    }
        private void setupEvent() {
    // Tombol TAMBAH KE KERANJANG
    btnTambah.addActionListener(e -> {
        int row = tabelProduk.getSelectedRow();
        if (row >= 0) {
            try {
                int jumlah = Integer.parseInt(tfJumlah.getText());
                Produk p = listProduk.get(row);
                double subtotal = jumlah * p.getHarga();

                modelKeranjang.addRow(new Object[]{
                        p.getNama(), p.getHarga(), jumlah, subtotal
                });

                keranjang.add(p); // disimpan jika perlu
                updateTotal();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih produk dulu!");
        }
    });

    // Tombol BAYAR
    btnBayar.addActionListener(e -> {
        try {
            double bayar = Double.parseDouble(tfBayar.getText());
            double total = Double.parseDouble(tfTotal.getText());

            if (bayar < total) {
                JOptionPane.showMessageDialog(this, "Uang bayar kurang!");
                return;
            }

            double kembalian = bayar - total;
            tfKembalian.setText(String.valueOf(kembalian));

            // Simpan transaksi ke database
            TransaksiService.simpanTransaksi(modelKeranjang, total, bayar, kembalian);
            JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Masukkan uang bayar yang valid!");
                }
            });
        }
        private void updateTotal() {
            double total = 0;
            for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                total += (double) modelKeranjang.getValueAt(i, 3); // kolom subtotal
            }
            tfTotal.setText(String.valueOf(total));
        }
}
