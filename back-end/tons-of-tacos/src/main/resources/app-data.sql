USE tonsOfTacos;

-- Sample customers
INSERT INTO customer ( name, email, phone_number, customer_uid)
VALUES( 'John Johnson', 'john@johnson.com', '555.555.5552', 'jk34-h5j0'),
('Tim Timson', 'tim@timson.com',  '555.555.5553', 'gd34-igjr' ),
('Bob Bobson', 'bob@bobson.com', '555.555.5551', '09t8-g093');


-- Sample menu

-- menu-item taco
INSERT INTO menu_item (category, description, item_name, item_size, unit_price)
VALUES('tacos', 'The basic staple made with farm fresh ingredients, love, and 
unique seasonings.', 'pound', 'na', 2.25),
('tacos', 'Try our daily chefs choice gourmet offering. Features our signature 
fluffy shell', 'golden pound', 'na', 5.30);


-- menu-item drink
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('drinks', 'The classic.', 'cola', 
'na', 1.00),
('drinks', 'Refreshing citrus zing in a bottle.', 'orange soda',
'na', 1.25),
('drinks', 'Purple drank!', 'grape soda', 'na', 1.25),
('drinks', 'Tropical waves of refreshment.', 'pineapple soda', 'na', 
1.25),
('drinks', 'A light and smooth drinking creamy concoction infused with 
spices.', 
'horchata', 'a', 1.00),
('drinks', 'Like momma makes it. Brewed in the sun and you can add your own 
sweetener of choice.', 'iced tea', 'a', 1.00);


-- menu-item  side
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('sides', 'A tangy and seasoned corn salad.', 'elote', 'na', 1.00),
('sides', 'A delectable diced salsa.', 'pico de gallo', 'na', 1.50),
('sides', 'A zesty and spicy cabbage salad.', 'slaw de mexicana', 'na', 1.00),
('sides', 'Seasoned and roasted potatoes.', 'papas', 'na', 2.15),
('sides', 'Highly nutritional black beans flavored with reduced fat milk and a 
pinch of magic.', 'frijoles', 'na', 1.50);


-- menu-item  toppings
INSERT INTO menu_item (category, description , item_name, item_size,
unit_price)
VALUES('toppings', 'Crisp and cool.', 'cabbage', 'na', .50),
('toppings', 'A fresh and bright addition.', 'cilantro', 'na', .50),
('toppings', 'Sweet and spicy.', 'pickled jalapenos and onions', 'na', 1.00),
('toppings', 'Cool and creamy.', 'sour cream', 'na', 1.50),
('toppings', 'Treat yourself.', 'avocado', 'na', 1.50),
('toppings', 'That little extra tang that adds so much.', 'lime', 'na', 1.50),
('toppings', 'Made fresh from our home adding that authentic goodness.', 'queso 
fresco', 'na', 1.50),
('toppings', 'Four cheese blend fiesta mix.', 'mixed queso', 'na', .50),
('toppings', 'Picked fresh each morning.', 'diced tomato', 'na', .50),
('toppings', 'A little heat, more tang and incredible amounts of delicious.', 
'salsa verde', 'na', .50),
('toppings', 'Delicious spicy roasted tomato and garlic flavors. ', 'salsa 
roja', 'na', .50);




---- Sample orders
INSERT INTO orders (customer_fk, order_total, order_uid, customer_uid)
VALUES(2, 3.00, '654654-465465-555', 'gd34-igjr'),
(1, 25.70, '654654-4655-555', 'jk34-h5j0'),
(1, 3.75, '654654-4657-555', 'jk34-h5j0');

---- Sample order items
INSERT INTO order_item(item_fk, quantity, total, order_item_size, order_fk)
VALUES(7, 3, 4.50, 'm',  2),
(2, 4, 21.20, 'na', 2),
(3, 3, 3.00, 'na', 1),
(4, 3, 3.75, 'na', 3);

-- Sample owners
INSERT INTO owner (name, username, psswrd, contact, role)
VALUES('Jim Castillo', 'jcast22', 
'$2a$10$MIGqdrc1JzTC1NWCfsWnsuY9L3OpAO4.gWmGiFKzUrq26Q1ejmjS2', 
'jim@tonsoftacos.com', 'ADMIN'),
('Jenny Castillo', 'jcast33',
'$2a$10$oJU3y8przJfPudPzXAH8FOVNSeNl3YEZdckyPJzia0fq/lYIvJhWC', 
'jenny@tonsoftacos.com', 'ADMIN');

-- Sample categories
INSERT INTO menu_category(name, description, available)
VALUES('tacos', 'Delicious tacos built with ingredients from our own farm.', 
'y' ),
('sides', 'Delightful on their own, but our side dishes perfectly compliment our savory tacos.', 'y'),
('toppings', 'Add a little extra of that flavor you crave.', 'y'),
('drinks', 'Refreshing beverages to wet your whistle.', 'y'),
('test', 'should not be visible.', 'n');;


