# POS Sederhana (Kasir Mini)

Aplikasi kasir berbasis **Java** untuk membantu UMKM atau toko kecil mengelola penjualan. Dibuat dengan **NetBeans IDE** dan basis data **MySQL**. Proyek ini dikerjakan untuk mata kuliah **Pemrograman Berorientasi Objek (PBO)**.

<!-- TODO: tambahkan screenshot (login, transaksi, struk, laporan) di folder docs/ -->
<!-- ![Halaman Transaksi](docs/transaksi.png) -->

## Fitur

**Login multi-role**
- **Admin**: mengelola produk, melihat laporan penjualan, dan melakukan transaksi
- **Kasir (user)**: melakukan penjualan dan mencetak struk

**Manajemen produk**
- Tambah, ubah, dan hapus data produk
- Menyimpan harga dan stok barang

**Transaksi penjualan**
- Menambahkan barang ke keranjang
- Total harga dihitung otomatis
- Data penjualan disimpan ke database

**Cetak struk**
- Struk dibuat otomatis setelah transaksi selesai
- Hasilnya disimpan di folder `struk/`

**Laporan penjualan**
- Menampilkan data transaksi yang sudah dilakukan
- Memudahkan pemilik toko memantau penjualan harian

## Konsep OOP yang Digunakan

<!-- TODO: isi sesuai kode, contoh di bawah hanya kerangka. Hapus yang tidak dipakai. -->
- Class dan object (misalnya `Produk`, `Transaksi`, `User`)
- Enkapsulasi (atribut private dengan getter/setter)
- Pewarisan dan polimorfisme (jika dipakai, sebutkan class-nya)
- Koneksi database dengan JDBC

## Tech Stack

| Bagian | Teknologi |
|---|---|
| Bahasa | Java <!-- TODO: versi JDK --> |
| IDE / build | NetBeans, Apache Ant (`build.xml`) |
| Database | MySQL |
| Antarmuka | <!-- TODO: Java Swing / JavaFX --> |
| Library | <!-- TODO: sebutkan isi folder lib/, mis. MySQL Connector/J --> |

## Struktur Folder

```
.
├── src/kasirmini/   # Source code aplikasi
├── lib/             # Library eksternal (.jar)
├── struk/           # Hasil cetak struk transaksi
├── nbproject/       # Konfigurasi project NetBeans
├── build.xml        # Script build (Ant)
├── manifest.mf
└── README.md
```

## Database

<!-- TODO: lampirkan file .sql (struktur tabel) di folder database/ lalu tautkan di sini -->
Tabel utama (sesuaikan dengan skema yang sebenarnya):

| Tabel | Isi |
|---|---|
| `users` | Akun login dan role (admin / kasir) |
| `produk` | Nama, harga, dan stok barang |
| `transaksi` | Data penjualan |
| `detail_transaksi` | Item pada tiap transaksi |

## Cara Menjalankan

### Prasyarat
- JDK <!-- TODO: versi -->
- NetBeans IDE
- MySQL Server (XAMPP juga bisa)

### Langkah
1. Clone repository ini
   ```bash
   git clone https://github.com/Zaidanidzhar/java-project.git
   ```
2. Buat database di MySQL lalu impor file SQL
   <!-- TODO: nama database dan nama file .sql -->
3. Sesuaikan konfigurasi koneksi database (host, nama database, user, password) di source code
   <!-- TODO: sebutkan nama class/file koneksi -->
4. Buka project di NetBeans: **File > Open Project**, pilih folder hasil clone
5. Pastikan library di folder `lib/` sudah terdaftar, lalu jalankan dengan **Run Project (F6)**

### Akun awal
<!-- TODO: isi akun demo, misalnya admin / kasir. Jangan pakai kata sandi yang dipakai di tempat lain. -->

## Pengembangan Selanjutnya

- Validasi input dan penanganan error yang lebih lengkap
- Pencarian dan filter produk
- Ekspor laporan ke PDF atau Excel
- Enkripsi (hash) kata sandi pengguna

## Author

**Zaidan Idzharulhaq**
[LinkedIn](https://www.linkedin.com/in/zaidan-idzharulhaq/) | [GitHub](https://github.com/Zaidanidzhar)
