-- Sample customers
INSERT INTO customer ( name, email, phone_number, customer_uid)
VALUES( 'John Johnson', 'john@johnson.com', '555.555.5552', 'jk34-h5j0');
INSERT INTO customer ( name, email, phone_number, customer_uid)
VALUES('Tim Timson', 'tim@timson.com',  '555.555.5553', 'gd34-igjr' );
INSERT INTO customer ( name, email, phone_number, customer_uid)
VALUES('Bob Bobson', 'bob@bobson.com', '555.555.5551', '09t8-g093');


-- Sample items
-- menu-item taco
INSERT INTO menu_item (category, description, item_name, item_size, unit_price)
VALUES('tacos', 'The basic staple made with farm fresh ingredients, love, and unique seasonings.', 'pound', NULL, 2.25);
INSERT INTO menu_item (category, description, item_name, item_size, unit_price)
VALUES('tacos', 'Try our daily chefs choice gourmet offering. Features our signature fluffy shell', 'golden pound', NULL, 5.30);


-- menu-item drink
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('drinks', 'The classic offered with ingredients from down south.', 'cola', '16 oz.bottle', 1.00);
INSERT INTO menu_item (category, description , item_name, item_size, unit_price)
VALUES('drinks', 'Refreshing citrus zing in a bottle.', 'orange soda', '16 oz.bottle', 1.25);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('drinks', 'Purple drank!', 'grape soda', '16 oz.bottle', 1.25);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('drinks', 'Tropical waves of refreshment.', 'pineapple soda', '16 oz.bottle', 1.25);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('drinks', 'A light and smooth drinking creamy concoction infused with spices.', 'horchata', 'a', 1.00);
INSERT INTO menu_item (category, description , item_name, item_size, unit_price)
VALUES('drinks', 'Like momma makes it. Brewed in the sun and you can add your own sweetener of choice.', 'iced tea', 'a', 1.00);


-- menu-item  side
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('sides', 'A tangy and seasoned corn salad.', 'elote', NULL, 1.00);
INSERT INTO menu_item (category, description , item_name, item_size, unit_price)
VALUES('sides', 'A delectable diced salsa.', 'pico de gallo', NULL, 1.50);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('sides', 'A zesty and spicy cabbage salad.', 'slaw de mexicana', NULL, 1.00);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('sides', 'Seasoned and roasted potatoes.', 'papas', NULL, 2.15);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('sides', 'Highly nutritional black beans flavored with reduced fat milk and a pinch of magic.', 'frijoles', NULL, 1.50);


-- menu-item  toppings
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('toppings', 'Crisp and cool.', 'cabbage', NULL, .50);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('toppings', 'A fresh and bright addition.', 'cilantro', NULL, .50);
INSERT INTO menu_item (category, description, item_name, item_size,
unit_price)
VALUES('toppings', 'Sweet and spicy.', 'pickled jalapenos and onions', NULL, 1.00);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('toppings', 'Cool and creamy.', 'sour cream', NULL, 1.50);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('toppings', 'Treat yourself.', 'avocado', NULL, 1.50);
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('toppings', 'That little extra tang that adds so much.', 'lime', NULL, 1.50);


---- Sample orders
INSERT INTO orders (customer_fk, order_total, order_uid, customer_uid)
VALUES(2, 30.55, '654654-465465-555', 'gd34-igjr');
INSERT INTO orders (customer_fk, order_total, order_uid, customer_uid)
VALUES(1, 25.55, '654654-4655-555', 'jk34-h5j0');
INSERT INTO orders (customer_fk, order_total, order_uid, customer_uid)
VALUES(1, 10.00, '654654-4657-555', 'jk34-h5j0');
--
---- Sample order items
INSERT INTO order_items (item_fk, quantity, total, order_fk)
VALUES(1, 3, 3.00, 2);
INSERT INTO order_items (item_fk, quantity, total, order_fk)
VALUES(2, 4, 4.00, 2);
INSERT INTO order_items (item_fk, quantity, total, order_fk)
VALUES(3, 3, 1.50, 1);
INSERT INTO order_items (item_fk, quantity, total, order_fk)
VALUES(3, 3, 1.50, 3);

-- Sample owners
INSERT INTO owners (name, username, psswrd, contact, role)
VALUES('Jim Castillo', 'jcast22',
'$2a$10$MIGqdrc1JzTC1NWCfsWnsuY9L3OpAO4.gWmGiFKzUrq26Q1ejmjS2',
'jim@tonsoftacos.com', 'ADMIN');
INSERT INTO owners (name, username, psswrd, contact, role)
VALUES('Jenny Castillo', 'jcast33',
'$2a$10$oJU3y8przJfPudPzXAH8FOVNSeNl3YEZdckyPJzia0fq/lYIvJhWC',
'jenny@tonsoftacos.com', 'ADMIN');

-- Sample categories
INSERT INTO menu_categories(name, description, available)
VALUES('tacos', 'Delicious tacos built with ingredients from our own farm.', 
'y' ),
('sides', 'Delightful on their own, but our side dishes perfectly compliment our savory tacos.', 'y'),
('toppings', 'Add a little extra of that flavor you crave.', 'y'),
('drinks', 'Refreshing beverages to wet your whistle.', 'y'),
('test', 'should not be visible.', 'n');;
