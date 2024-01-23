<div align="center">
<p text-align = "center">
<pre>
%Y%}Y.%Y.%%%}%.%%}}]%YY%%%%
|Y%%Y}%%///////////////////////|
  |}}Y%%Y}Y&/////////////////////////||  
||Y}}}}}%&//////////////////////////||
||&&]&}%%&///////////////////////////||
||&&]]&&}}///////////////////////////||
||&}}%%}&////////////////////////////||
||&&&&&&//////////////////////////////
\\|||||////////////////////////////
</pre>
</p>

# tons-of-tacos-v1.2

</div>

---

## This application is based off of the [tons-of-tacos](https://github.com/strauberly/tons-of-tacos) project I created earlier. And is meant as a portfolio project in order to demonstrate some of my capability and comprehension as junior fullstack developer to potential employers.

## The prior application was a more simplistic command line application created to demonstrate logic solving and core Java knowledge. This version will be a fullstack application that feels as though Tons of Tacos actually exists.

## Tons of Tacos is a fictional food truck. This is an application designed for its owners and their customers in order to expedite the ordering process and increase sales.

## Progress can be monitored from the [Dev-Journal](https://github.com/strauberly/tons-of-tacos-v1.2/blob/main/DEV-JOURNAL.md).

## Thank you for checking out what I am working on!

## Adam Straub -- zeron30@hotmail.com

---

### Table of Contents

[I. General Design Specs](#i-general-design-specs)

[II. Rough Phase Outline](#ii-rough-phase-outline)

[III. Tech Stack](#iii-tech-stack)

[IV. ERD](#iv-erd)

---

### I. General Design Specs

- Focus is on getting functionality in place for customers to place orders. Then they can just come down to the truck pay in person and pick up food. After this we will implement accepting online payments and additional features.

- Create API whose primary focus will be retrieving menu items from a MySQL database in order to create an order.
  Additional functionality at some point likely to also include:

  - Allow the food truck owners to update and edit their menu, track orders, and track sales.
  - Allow a customer to create an account where they may have access to special deals and menu items. Also view past orders and repeat them in order to facilitate expedient orders if desired.

- Create front-end with a focus on ease of use, and reflective of owners open, kind, and hard working nature. Food has a folk with modern flair vibe.

- Build an owners portal into the front-end to facilitate owners ability to work with orders and customers.

[Table of Contents](#table-of-contents)

---

### II. Rough Phase Outline

- 1.2.1 - Return data dynamically from a database.

- 1.2.2 - Use data to create an order with customer provided data.

- 1.2.3 - Create tests for desired functionality of retrieving menu items and creating orders.

- 1.2.4 - Implement functionality for owners of Tons of Tacos to retrieve and alter data pertaining to orders and customers.

- 1.2.5 - Create tests for owners functions.

- 1.2.6 - Secure owners functions with spring security 6 and json web tokens.

- 1.2.7 - Tests rewritten for owners functions integrating security measures.

- 1.2.8 - Application proof read, cleaned, and tested.

===

Future phases:

- Incorporation of a notification system for when order ready.
- Incorporation of online payment systems with receipt.
- Build web application for customers.
- Build portal for owners.
- Ability for Customers to view past orders and order again.
- Ability to see food truck's location for the day and planned location for tomorrow.

[Table of Contents](#table-of-contents)

---

### III. Tech Stack

Back-End

- MySql Workbench
- MySql 8
- Spring 3.0
- Spring Security 6.0
- BCrypt
- Java 17
- Lombok
- Junit 5
- Mockito
- Swagger
- Logback
- Bash

[Table of Contents](#table-of-contents)

---

### IV. ERD

<p align="center">
  <img src="./back-end/supporting-files/erd-16Oct2023.png"/>
</p>

[Table of Contents](#table-of-contents)

---

---

© Adam Straub 2023
