package kasirmini;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Date;

import kasirmini.model.Produk;
import kasirmini.service.ProdukService;
import kasirmini.service.TransaksiService;
import kasirmini.service.Database;


public class KasirGUI extends JFrame {

    private JTable tabelProduk, tabelKeranjang;
    private DefaultTableModel modelProduk, modelKeranjang;
    private JTextField tfJumlah, tfBayar, tfTotal, tfKembalian;
    private JButton btnTambah, btnBayar, btnLaporan, btnLogout;
    private JButton btnEditKeranjang, btnHapusKeranjang, btnCetakStruk;
    private JButton btnTambahProduk, btnHapusProduk;
    private JTextField tfNamaProduk, tfHargaProduk;
    private JLabel labelJam;

    private ArrayList<Produk> listProduk;
    private ArrayList<Produk> keranjang = new ArrayList<>();
    private String userRole;
        public KasirGUI(String role) {
        this.userRole = role;

        setTitle("Aplikasi Kasir Mini - POS Sederhana");
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        initComponent();
        loadProduk();
        setupEvent();
        startClock();
    }


    private void initComponent() {
        JPanel panelTambahProduk = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        tfNamaProduk = new JTextField(10);
        tfHargaProduk = new JTextField(10);
        btnTambahProduk = new JButton("Simpan Produk");
        btnHapusProduk = new JButton("Hapus Produk");

        panelTambahProduk.add(new JLabel("Nama Produk:"));
        panelTambahProduk.add(tfNamaProduk);
        panelTambahProduk.add(new JLabel("Harga:"));
        panelTambahProduk.add(tfHargaProduk);
        panelTambahProduk.add(btnTambahProduk);
        if (userRole.equalsIgnoreCase("admin")) {
            panelTambahProduk.add(btnHapusProduk);
        }

        JPanel panelAtas = new JPanel(new GridLayout(1, 2, 10, 10));
        modelProduk = new DefaultTableModel(new String[]{"Nama", "Harga"}, 0);
        tabelProduk = new JTable(modelProduk);
        JScrollPane scrollProduk = new JScrollPane(tabelProduk);

        modelKeranjang = new DefaultTableModel(new String[]{"Nama", "Harga", "Jumlah", "Subtotal"}, 0);
        tabelKeranjang = new JTable(modelKeranjang);
        JScrollPane scrollKeranjang = new JScrollPane(tabelKeranjang);

        panelAtas.add(scrollProduk);
        panelAtas.add(scrollKeranjang);

        JPanel panelBawah = new JPanel();
        panelBawah.setLayout(new BoxLayout(panelBawah, BoxLayout.Y_AXIS));
        panelBawah.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tfJumlah = new JTextField(10);
        tfBayar = new JTextField(10);
        tfTotal = new JTextField(10); tfTotal.setEditable(false);
        tfKembalian = new JTextField(10); tfKembalian.setEditable(false);

        btnTambah = new JButton("Tambah ke Keranjang");
        btnBayar = new JButton("Bayar");
        btnEditKeranjang = new JButton("Edit Jumlah");
        btnHapusKeranjang = new JButton("Hapus dari Keranjang");
        btnCetakStruk = new JButton("Cetak Struk");
        btnLaporan = new JButton("Lihat Laporan");
        btnLogout = new JButton("Logout");

        if (!userRole.equalsIgnoreCase("admin")) {
            btnLaporan.setVisible(false);
        }


         JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        inputPanel.add(new JLabel("Jumlah:"));
        inputPanel.add(tfJumlah);
        inputPanel.add(btnTambah);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        totalPanel.add(new JLabel("Total:"));
        totalPanel.add(tfTotal);
        totalPanel.add(new JLabel("Bayar:"));
        totalPanel.add(tfBayar);
        totalPanel.add(new JLabel("Kembali:"));
        totalPanel.add(tfKembalian);

        JPanel tombolPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tombolPanel.add(btnBayar);
        tombolPanel.add(btnEditKeranjang);
        tombolPanel.add(btnHapusKeranjang);
        tombolPanel.add(btnCetakStruk);
        tombolPanel.add(btnLaporan);
        tombolPanel.add(btnLogout);

        labelJam = new JLabel("Jam: --:--:--");
        JPanel jamPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        jamPanel.add(labelJam);

        panelBawah.add(inputPanel);
        panelBawah.add(totalPanel);
        panelBawah.add(tombolPanel);
        panelBawah.add(jamPanel);

        add(panelTambahProduk, BorderLayout.NORTH);
        add(panelAtas, BorderLayout.CENTER);
        add(panelBawah, BorderLayout.SOUTH);
    }

    private void startClock() {
        Thread clockThread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            while (true) {
                String time = sdf.format(new Date());
                SwingUtilities.invokeLater(() -> labelJam.setText("Jam: " + time));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        clockThread.setDaemon(true);
        clockThread.start();
    }

    private void loadProduk() {
        modelProduk.setRowCount(0);
        listProduk = ProdukService.getAllProduk();
        for (Produk p : listProduk) {
            modelProduk.addRow(new Object[]{p.getNama(), p.getHarga()});
        }
    }

    private void setupEvent() {
        btnTambah.addActionListener(e -> {
            int row = tabelProduk.getSelectedRow();
            if (row >= 0) {
                try {
                    int jumlah = Integer.parseInt(tfJumlah.getText());
                    Produk p = listProduk.get(row);
                    double subtotal = jumlah * p.getHarga();
                    modelKeranjang.addRow(new Object[]{p.getNama(), p.getHarga(), jumlah, subtotal});
                    keranjang.add(p);
                    updateTotal();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Jumlah harus angka!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk terlebih dahulu.");
            }
        });

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

                TransaksiService.simpanTransaksi(modelKeranjang, total, bayar, kembalian);
                JOptionPane.showMessageDialog(this, "Transaksi berhasil disimpan!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Masukkan uang bayar yang valid!");
            }
        });

        btnTambahProduk.addActionListener(e -> {
            String nama = tfNamaProduk.getText();
            String hargaStr = tfHargaProduk.getText();

            try {
                double harga = Double.parseDouble(hargaStr);
                Connection conn = Database.getConnection();

                PreparedStatement pst = conn.prepareStatement(
                        "INSERT INTO produk (nama_produk, harga) VALUES (?, ?)"
                );
                pst.setString(1, nama);
                pst.setDouble(2, harga);
                pst.executeUpdate();

                loadProduk();
                tfNamaProduk.setText("");
                tfHargaProduk.setText("");
                JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal tambah produk: " + ex.getMessage());
            }
        });

        btnHapusProduk.addActionListener(e -> {
            int selectedRow = tabelProduk.getSelectedRow();
            if (selectedRow >= 0) {
                String nama = modelProduk.getValueAt(selectedRow, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Hapus produk \"" + nama + "\"?", "Konfirmasi", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        Connection conn = Database.getConnection();
                        PreparedStatement pst = conn.prepareStatement(
                                "DELETE FROM produk WHERE nama_produk = ?"
                        );
                        pst.setString(1, nama);
                        int affected = pst.executeUpdate();

                        if (affected > 0) {
                            loadProduk();
                            JOptionPane.showMessageDialog(this, "Produk berhasil dihapus.");
                        } else {
                            JOptionPane.showMessageDialog(this, "Gagal menghapus produk.");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih produk yang ingin dihapus.");
            }
        });

        btnEditKeranjang.addActionListener(e -> {
            int row = tabelKeranjang.getSelectedRow();
            if (row >= 0) {
                String jumlahBaruStr = JOptionPane.showInputDialog(this, "Masukkan jumlah baru:");
                try {
                    int jumlahBaru = Integer.parseInt(jumlahBaruStr);
                    double harga = (double) modelKeranjang.getValueAt(row, 1);
                    double subtotal = jumlahBaru * harga;

                    modelKeranjang.setValueAt(jumlahBaru, row, 2);
                    modelKeranjang.setValueAt(subtotal, row, 3);
                    updateTotal();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Pilih item keranjang terlebih dahulu.");
            }
        });

        btnHapusKeranjang.addActionListener(e -> {
            int row = tabelKeranjang.getSelectedRow();
            if (row >= 0) {
                modelKeranjang.removeRow(row);
                updateTotal();
            } else {
                JOptionPane.showMessageDialog(this, "Pilih item keranjang yang ingin dihapus.");
            }
        });

            btnCetakStruk.addActionListener(e -> {
        try {
            StringBuilder struk = new StringBuilder();
            struk.append("===== STRUK PEMBELIAN =====\n");
            struk.append("Tanggal: ").append(new java.util.Date()).append("\n\n");

            struk.append(String.format("%-20s %-10s %-10s %-10s\n", "Nama", "Harga", "Jumlah", "Subtotal"));
            struk.append("-----------------------------------------------------\n");

            for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
                String nama = modelKeranjang.getValueAt(i, 0).toString();
                double harga = (double) modelKeranjang.getValueAt(i, 1);
                int jumlah = (int) modelKeranjang.getValueAt(i, 2);
                double subtotal = (double) modelKeranjang.getValueAt(i, 3);
                struk.append(String.format("%-20s %-10.0f %-10d %-10.0f\n", nama, harga, jumlah, subtotal));
            }

            struk.append("\nTotal    : Rp. ").append(tfTotal.getText());
            struk.append("\nBayar    : Rp. ").append(tfBayar.getText());
            struk.append("\nKembali  : Rp. ").append(tfKembalian.getText());
            struk.append("\n==============================\n");

            // Tampilkan di JTextArea dalam JDialog
            JTextArea area = new JTextArea(struk.toString());
            area.setEditable(false);
            area.setFont(new Font("Monospaced", Font.PLAIN, 12));
            JScrollPane scroll = new JScrollPane(area);

            JDialog dialog = new JDialog(this, "Struk Pembelian", true);
            dialog.setSize(400, 500);
            dialog.setLocationRelativeTo(this);
            dialog.add(scroll);
            dialog.setVisible(true);

            // Simpan juga ke file
            java.io.File dir = new java.io.File("struk");
            if (!dir.exists()) dir.mkdir();

            String filename = "struk/struk-" + System.currentTimeMillis() + ".txt";
            java.io.PrintWriter writer = new java.io.PrintWriter(filename);
            writer.println(struk.toString());
            writer.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal mencetak struk: " + ex.getMessage());
        }
    });

        btnLaporan.addActionListener(e -> new LaporanTransaksi().setVisible(true));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                this.dispose();
                new LoginForm().setVisible(true);
            }
        });
    }

    private void updateTotal() {
        double total = 0;
        for (int i = 0; i < modelKeranjang.getRowCount(); i++) {
            total += (double) modelKeranjang.getValueAt(i, 3);
        }
        tfTotal.setText(String.valueOf(total));
    }
}
