INSERT INTO customers (id, first_name, last_name, address) VALUES (1, 'Иван', 'Иванов', 'ул. Ленина');
INSERT INTO customers (id, first_name, last_name, address) VALUES (2, 'Петр', 'Петров', 'пр. Мира');

INSERT INTO goods (id, name, description, price) VALUES (1, 'Стол', NULL, 100.05);
INSERT INTO goods (id, name, description, price) VALUES (2, 'Стул', NULL, 25.37);
INSERT INTO goods (id, name, description, price) VALUES (3, 'Комод', NULL, 25.37);

INSERT INTO orders (id, customer_id) VALUES (1, 1);
INSERT INTO orders (id, customer_id) VALUES (2, 1);
INSERT INTO orders (id, customer_id) VALUES (3, 2);

INSERT INTO order_items (order_id, good_id, quantity) VALUES (1, 2, 5);
INSERT INTO order_items (order_id, good_id, quantity) VALUES (2, 1, 1);
INSERT INTO order_items (order_id, good_id, quantity) VALUES (2, 2, 2);
INSERT INTO order_items (order_id, good_id, quantity) VALUES (3, 1, 1);
INSERT INTO order_items (order_id, good_id, quantity) VALUES (3, 2, 4);
INSERT INTO order_items (order_id, good_id, quantity) VALUES (3, 3, 1);