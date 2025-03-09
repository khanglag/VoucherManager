
-- cơ sở dữ liệu  `voucher_management`;

-- Bảng Users
CREATE TABLE Users (
                       UserID INT PRIMARY KEY AUTO_INCREMENT,
                       Username VARCHAR(50) NOT NULL UNIQUE,
                       Password VARCHAR(255) NOT NULL,
                       FullName VARCHAR(100) NOT NULL,
                       Email VARCHAR(100) UNIQUE,
                       PhoneNumber VARCHAR(15) UNIQUE,
                       RoleID INT,
                       Status BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu vào bảng Users
INSERT INTO Users (Username, Password, FullName, Email, PhoneNumber, RoleID, Status) VALUES
                                                                                         ('admin', '123456', 'Admin ne', 'admin@gmail.com', '0987654321', 1, TRUE),
                                                                                         ('cuong', '123456', 'Nguyễn Kế Cường', 'cuong@gmail.com', '0912345678', 2, TRUE),
                                                                                         ('hoang', '123456', 'Võ Đinh Xuân Hoàng', 'hoang@gmail.com', '0933221100', 2, TRUE),
                                                                                         ('mkhang', '123456', 'Đỗ Minh Khang', 'mkhang@gmail.com', '0912345679', 2, TRUE),
                                                                                         ('thu', '123456', 'Nguyễn Thị Anh Thư', 'thu@gmail.com', '0912345680', 2, TRUE),
                                                                                         ('dkhang', '123456', 'Lê Duy Khang', 'dkhang@gmail.com', '0933221101', 2, TRUE);

-- Bảng Roles
CREATE TABLE Roles (
                       RoleID INT PRIMARY KEY AUTO_INCREMENT,
                       RoleName VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu vào bảng Roles
INSERT INTO Roles (RoleName) VALUES
                                 ('Admin'),
                                 ('User');

-- Bảng Voucher
CREATE TABLE Voucher (
                         VoucherCode VARCHAR(50) PRIMARY KEY,
                         DiscountType ENUM('PERCENTAGE', 'FIXED') NOT NULL,
                         DiscountValue DECIMAL(10,2) NOT NULL,
                         StartDate DATE NOT NULL,
                         EndDate DATE NOT NULL CHECK (EndDate >= StartDate),
                         MinimumOrderValue DECIMAL(10,2),
                         Status ENUM('ACTIVE', 'EXPIRED', 'CANCELLED') NOT NULL DEFAULT 'ACTIVE',
                         CreatedBy INT,
                         UsageCount INT NOT NULL DEFAULT 0,
                         MaxUsage INT NOT NULL DEFAULT 100,
                         CreatedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
                         ApplicableForAllProducts BOOLEAN NOT NULL DEFAULT FALSE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu vào bảng Voucher
INSERT INTO Voucher (VoucherCode, DiscountType, DiscountValue, StartDate, EndDate, MinimumOrderValue, Status, CreatedBy, MaxUsage) VALUES
                                                                                                                                       ('SALE10', 'PERCENTAGE', 10.00, '2025-03-01', '2025-12-31', 500.00, 'ACTIVE', 1, 200),
                                                                                                                                       ('FIXED50', 'FIXED', 50.00, '2025-03-01', '2025-09-30', 100.00, 'ACTIVE', 2, 150);

-- Bảng Orders
CREATE TABLE Orders (
                        OrderID INT PRIMARY KEY AUTO_INCREMENT,
                        UserID INT,
                        OrderDate DATETIME DEFAULT CURRENT_TIMESTAMP,
                        TotalAmount DECIMAL(10,2) NOT NULL,
                        FinalAmount DECIMAL(10,2) NOT NULL,
                        OrderStatus ENUM('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu vào bảng Orders
INSERT INTO Orders (UserID, TotalAmount, FinalAmount, OrderStatus) VALUES
                                                                       (2, 1600.00, 1440.00, 'COMPLETED'),
                                                                       (3, 75.00, 75.00, 'PENDING'),
                                                                       (2, 920.00, 870.00, 'COMPLETED');

-- Bảng OrderDetails
CREATE TABLE OrderDetails (
                              OrderDetailID INT PRIMARY KEY AUTO_INCREMENT,
                              OrderID INT,
                              ProductID INT,
                              Quantity INT NOT NULL,
                              UnitPrice DECIMAL(10,2) NOT NULL,
                              TotalPrice DECIMAL(10,2) NOT NULL,
                              FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu vào bảng OrderDetails
INSERT INTO OrderDetails (OrderID, ProductID, Quantity, UnitPrice, TotalPrice) VALUES
                                                                                   (1, 1, 1, 1500.00, 1500.00),
                                                                                   (1, 3, 1, 120.00, 120.00),
                                                                                   (2, 4, 1, 75.00, 75.00),
                                                                                   (3, 2, 1, 800.00, 800.00),
                                                                                   (3, 3, 1, 120.00, 120.00);

-- Bảng Products
CREATE TABLE Products (
                          ProductID INT PRIMARY KEY AUTO_INCREMENT,
                          ProductName VARCHAR(100) NOT NULL,
                          Price DECIMAL(10,2) NOT NULL,
                          Status BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu vào bảng Products
INSERT INTO Products (ProductName, Price, Status) VALUES
                                                      ('Product 1', 1500.00, TRUE),
                                                      ('Product 2', 800.00, TRUE),
                                                      ('Product 3', 120.00, TRUE),
                                                      ('Product 4', 75.00, TRUE);

-- Bảng VoucherUsage
CREATE TABLE VoucherUsage (
                              UsageID INT PRIMARY KEY AUTO_INCREMENT,
                              OrderID INT,
                              VoucherCode VARCHAR(50),
                              UsedDate DATETIME DEFAULT CURRENT_TIMESTAMP,
                              UNIQUE (OrderID, VoucherCode),
                              FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE,
                              FOREIGN KEY (VoucherCode) REFERENCES Voucher(VoucherCode) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Thêm dữ liệu vào bảng VoucherUsage
INSERT INTO VoucherUsage (OrderID, VoucherCode) VALUES
                                                    (1, 'SALE10'),
                                                    (3, 'FIXED50');

-- Ràng buộc khóa ngoại

-- Bảng Users
ALTER TABLE Users
    ADD CONSTRAINT FK_Users_Role FOREIGN KEY (RoleID) REFERENCES Roles(RoleID) ON DELETE SET NULL;

-- Bảng Voucher
ALTER TABLE Voucher
    ADD CONSTRAINT FK_Voucher_CreatedBy FOREIGN KEY (CreatedBy) REFERENCES Users(UserID) ON DELETE SET NULL;

-- Bảng Orders
ALTER TABLE Orders
    ADD CONSTRAINT FK_Orders_User FOREIGN KEY (UserID) REFERENCES Users(UserID) ON DELETE SET NULL;

-- Bảng OrderDetails
ALTER TABLE OrderDetails
    ADD CONSTRAINT FK_OrderDetails_Product FOREIGN KEY (ProductID) REFERENCES Products(ProductID) ON DELETE RESTRICT;

ALTER TABLE OrderDetails
    ADD CONSTRAINT FK_OrderDetails_Order FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE;

-- Bảng VoucherUsage
ALTER TABLE VoucherUsage
    ADD CONSTRAINT FK_VoucherUsage_Order FOREIGN KEY (OrderID) REFERENCES Orders(OrderID) ON DELETE CASCADE;

ALTER TABLE VoucherUsage
    ADD CONSTRAINT FK_VoucherUsage_Voucher FOREIGN KEY (VoucherCode) REFERENCES Voucher(VoucherCode) ON DELETE CASCADE;



-- Cập nhật giá trị đơn hàng

DELIMITER $$
CREATE TRIGGER trg_update_final_amount
    AFTER INSERT ON VoucherUsage
    FOR EACH ROW
BEGIN
    UPDATE Orders
    SET FinalAmount = GREATEST(TotalAmount - (
        SELECT COALESCE(SUM(V.DiscountValue), 0)
        FROM VoucherUsage VU
                 JOIN Voucher V ON VU.VoucherCode = V.VoucherCode
        WHERE VU.OrderID = NEW.OrderID
    ), 0)
    WHERE OrderID = NEW.OrderID;
END $$
DELIMITER ;

--Thêm cập nhật UsageCount tự động

DELIMITER $$
CREATE TRIGGER trg_update_voucher_usage
    AFTER INSERT ON VoucherUsage
    FOR EACH ROW
BEGIN
    UPDATE Voucher
    SET UsageCount = UsageCount + 1
    WHERE VoucherCode = NEW.VoucherCode;
END $$
DELIMITER ;
