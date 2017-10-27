-- Очистка БД
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS goods;
DROP TABLE IF EXISTS orders;

CREATE TABLE customers (
  id INTEGER PRIMARY KEY AUTOINCREMENT ,
  first_name VARCHAR(50) NOT NULL ,
  last_name VARCHAR(50) NOT NULL
);

CREATE TABLE goods (
  id INTEGER PRIMARY KEY AUTOINCREMENT ,
  name VARCHAR(100) NOT NULL ,
  description TEXT ,
  price DECIMAL(8,2) NOT NULL
);

CREATE TABLE orders(
  id INTEGER PRIMARY KEY AUTOINCREMENT ,
  goods_id INTEGER NOT NULL ,
  customers_id INTEGER NOT NULL ,
  quantity INTEGER NOT NULL,
  address TEXT NOT NULL

  -- Specific for MySQL
--   CONSTRAINT FOREIGN KEY goods_id ON goods(id),
--   CONSTRAINT FOREIGN KEY customers_id ON customers(id)

);