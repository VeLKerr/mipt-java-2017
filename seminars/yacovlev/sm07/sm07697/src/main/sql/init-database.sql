--- clean tables
drop table if EXISTS customers;
drop table if EXISTS goods;
drop table if EXISTS orders;

--- create schema
CREATE TABLE customers (
  id INTEGER PRIMARY KEY AUTOINCREMENT ,
  name VARCHAR(100) NOT NULL ,
  address TEXT NOT NULL
);

CREATE TABLE goods (
  id INTEGER PRIMARY KEY AUTOINCREMENT ,
  name VARCHAR(100) NOT NULL ,
  description TEXT , -- optional
  price DECIMAL(8, 2) NOT NULL
);

CREATE TABLE orders (
  id INTEGER PRIMARY KEY AUTOINCREMENT ,
  customers_id INTEGER NOT NULL,
  goods INTEGER NOT NULL,
  quantity INTEGER NOT NULL
);