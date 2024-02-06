DROP DATABASE IF EXISTS tonsOfTacos;

CREATE DATABASE IF NOT EXISTS tonsOfTacos;

USE tonsOfTacos;

DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS order_item;
DROP TABLE IF EXISTS menu_item;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS menu_categories;

CREATE TABLE customer(
customer_pk INT unsigned NOT NULL AUTO_INCREMENT ,
name VARCHAR(40) NOT NULL,
email VARCHAR(40) NOT NULL,
phone_number VARCHAR(12) NOT NULL,
customer_uid VARCHAR(9) NOT NULL,
PRIMARY KEY (customer_pk)
);

CREATE TABLE menu_item(
item_pk INT unsigned NOT NULL AUTO_INCREMENT,
category VARCHAR(30) NOT NUll,
description VARCHAR(255) NOT NUll,
item_name VARCHAR(30) NOT NUll,
item_size VARCHAR(100) DEFAULT NULL,
unit_price DECIMAL(5, 2) NOT NUll,
PRIMARY KEY (item_pk)
);

CREATE TABLE orders(
order_pk INT unsigned NOT NULL AUTO_INCREMENT,
customer_fk INT unsigned,
order_total DECIMAL(5, 2) NOT NULL,
order_uid VARCHAR(255) NOT NULL,
created TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ready VARCHAR(30) DEFAULT 'no',
closed VARCHAR(30)DEFAULT 'no',
customer_uid VARCHAR(9) NOT NULL,
PRIMARY KEY (order_pk),
FOREIGN KEY (customer_fk) REFERENCES customer(customer_pk)ON DELETE SET NULL
);

CREATE TABLE order_items(
order_item_pk INT unsigned NOT NULL AUTO_INCREMENT,
item_fk INT unsigned NOT NULL,
quantity INT(2) NOT NULL,
total DECIMAL(3, 2) DEFAULT 0.00 NOT NULL,
order_fk INT unsigned NOT NULL,
PRIMARY KEY (order_item_pk),
FOREIGN KEY (item_fk) REFERENCES menu_item(item_pk),
FOREIGN KEY (order_fk) REFERENCES orders(order_pk)
);

CREATE TABLE owners(
owners_pk INT unsigned NOT NULL AUTO_INCREMENT,
name VARCHAR(20) NOT NULL,
username VARCHAR(20) NOT NULL,
psswrd VARCHAR(68) NOT NULL,
contact VARCHAR(44) NOT NULL,
role VARCHAR(12) NOT NULL,
PRIMARY KEY (owners_pk)
);

CREATE TABLE menu_categories(
menu_category_pk INT unsigned NOT NULL AUTO_INCREMENT,
name VARCHAR (20) NOT NULL,
description VARCHAR (255) NOT NULL,
available CHAR (1) NOT NULL,
PRIMARY KEY (menu_category_pk)
)
