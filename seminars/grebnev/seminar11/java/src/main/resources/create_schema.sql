CREATE TABLE customers (
    id INT PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL
);

CREATE TABLE goods (
    id INT PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL
);

CREATE TABLE orders (
    id INT PRIMARY KEY,
    customer_id INT NOT NULL REFERENCES customers(id)
);

CREATE TABLE order_items (
    id SERIAL PRIMARY KEY NOT NULL,
    order_id INT NOT NULL REFERENCES orders(id),
    good_id INT NOT NULL REFERENCES goods(id),
    quantity INT NOT NULL,

    UNIQUE (order_id, good_id)
);