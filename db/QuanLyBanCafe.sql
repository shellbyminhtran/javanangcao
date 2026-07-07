create database QuanLyBanCafe;
go

use QuanLyBanCafe;
go

create table LOAI_HINH_LV (
MaLHLV int primary key,
TenLHLV nvarchar(50) not null unique,
MucLuongCoDinh decimal (18,0) null,
MucLuongTheoGio decimal (18,0) null
);
go

create table NHAN_VIEN (
MaNV varchar(10) primary key,
TenNV nvarchar(50) not null,
GioiTinh nvarchar(5) not null,
NgaySinh date,
NgayBatDauLam date not null default getdate(),
SDT varchar(15),
DiaChi nvarchar(100),
Email varchar(50),
MatKhau varchar(100) not null,
VaiTro int not null,
MaLHLV int not null,
TrangThai bit default 1,
foreign key (MaLHLV) references LOAI_HINH_LV(MaLHLV)
);
go

create table DANH_MUC (
MaDM int primary key identity(1,1),
TenDM nvarchar(50) not null unique
);
go

create table SAN_PHAM (
MaSP varchar(10) primary key,
TenSP nvarchar(50) not null unique,
MaDM int not null,
GiaBan decimal(18,2) not null,
TrangThai bit default 1,
foreign key (MaDM) references DANH_MUC(MaDM)
);
go

create table KHACH_HANG (
MaKH int identity(1,1) primary key,
TenKH nvarchar(100) not null,
Email varchar(100) unique,
MatKhau varchar(50),
DiemTichLuy int default 0,
NgayDangKy date default getdate()
);
go

insert into KHACH_HANG (TenKH, Email, MatKhau, DiemTichLuy) values
(N'Trần Tuấn Minh', 'minhtran@gmail.com', '1', 100)
go

create table HOA_DON (
MaHD varchar(15) primary key,
MaNV varchar(10) not null,
MaKH int,
NgayLap datetime default getdate(),
TongTien decimal(18,2) not null,
TrangThaiThanhToan nvarchar(20),
foreign key (MaNV) references NHAN_VIEN(MaNV),
foreign key (MaKH) references KHACH_HANG(MaKH)
);
go
alter table HOA_DON alter column TrangThaiThanhToan nvarchar(100);
alter table HOA_DON add DaTongKet bit default 0;
update HOA_DON set DaTongKet = 0 where DaTongKet is null;
go

create table Chi_Tiet_Hoa_Don (
MaCTHD int primary key identity(1,1),
MaHD varchar(15) not null,
MaSP varchar(10) not null,
SoLuong int not null,
DonGia decimal (18,2) not null,
ThanhTien decimal(18,2) not null,
foreign key (MaHD) references HOA_DON(MaHD),
foreign key (MaSP) references SAN_PHAM(MaSP)
);
go

create table CHAM_CONG (
MaCC int primary key identity(1,1),
MaNV varchar(10) not null,
Thang int not null,
Nam int not null,
SoGioLam float default 0,
GhiChu nvarchar(100),
foreign key (MaNV) references NHAN_VIEN(MaNV),
constraint Unique_ChamCong unique(MaNV, Thang, Nam)
);
go

create table KHO_NGUYEN_LIEU (
MaNL varchar(10) primary key,
TenNL nvarchar(50) not null unique,
DonViTinh nvarchar(20),
SoLuongTon float default 0,
DiemCanhBao float default 5
);
go

insert into LOAI_HINH_LV values
(1, N'Quản Lý (Full Time)', 10000000, null),
(2, N'STAFF (Full Time)', 6000000, null),
(3, N'STAFF (Part Time)', null, 26000),
(4, N'TRAINEE (Part Time)', null, 21000);
go

insert into NHAN_VIEN (MaNV, TenNV, GioiTinh, MatKhau, VaiTro, MaLHLV, NgayBatDauLam) values
('SM001', N'Trần Tuấn Minh (Quản Lý)', N'Nam', '1', 1, 1, '2020-01-01'),
('NV001', N'Nguyễn Nam Anh (Staff Full)', N'Nam', '1', 2, 2, '2022-01-29'),
('NV002', N'Đoàn Phương Linh (Staff Full)', N'Nữ', '1', 2, 2, '2022-09-26'),
('NV003', N'Nguyễn Tuấn Minh (Staff Part)', N'Nam', '1', 2, 3, '2022-07-16'),
('NV004', N'Nguyễn Phương Anh (Staff Part)', N'Nữ', '1', 2, 3, '2024-09-15'),
('NV005', N'Triệu Minh Ngọc (Trainee Part)', N'Nữ', '1', 2, 4, getdate());
go

insert into DANH_MUC values
(N'Espresso & Coffee'),
(N'Vietnam Only'),
(N'Frappuccino & More'),
(N'Tea & Refreshment');
go



select * from NHAN_VIEN
select * from Chi_Tiet_Hoa_Don
select * from KHACH_HANG
select * from CHAM_CONG
select * from DANH_MUC
select * from SAN_PHAM
select * from LOAI_HINH_LV
select * from KHO_NGUYEN_LIEU

select name from sys.tables
SELECT DB_NAME() AS CurrentDB;

SELECT OBJECT_DEFINITION(OBJECT_ID('KHO_NGUYEN_LIEU'));