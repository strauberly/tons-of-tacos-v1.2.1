## This Journal tracks development progress, ideas and thoughts as proof I (Adam Straub) am the developer.

---

--- 17 Sep 2024 ---

- Discovered bug in validation of customer already existing or being new and 
determining if an order is to be created. Also a bug where order total was not 
being reset. Both issues have been resolved.

- Will also be working on error handling and responses.

---

--- 14 Sep 2024 ---

- Create an order function in backend now accepts simplified front end object 
as a dto and uses a converter method to build object sent to db.

- Putting in more work with catching errors, followed by test re-write followed 
by working on response.

---

--- 9 Sep 2024 ---

- Began refactor of order creation. Past structure would have required front 
end to use an over complicated object to achieve desired results.
- Customer name is received from order and could use try catch blocks as well 
as errors.

---

--- 6 Feb 2023 ---

- Adjusted database to better reflect standardized naming conventions.
- Adjusted order item table in order to make use of pricing adjustments determined for item sizing.
- Adjusted entities to reflect updated db.
- Ran test suite, located errors and cleaned those up.
- Updated DTOs where appropriate.
- Implemented pricing adjustment logic in orders service.
- Ran test suite and results are currently as desired.
- Updated controller documentation for creating orders to reflect new object required to create an order.

---

--- 22 Jan 2023 ---

- Updated sql scripts and database for menu categories.
- Updated security config for utility endpoints.
- Created controller and controller interface for utility endpoints.
- Created menu category entity, dto, and repository.
- Updated swagger docs for category endpoint.
- Created utility service and service interface.
- Wrote two functionality tests.
  - One for the repository correctly returning menu categories only if they are marked 'y' in the available column.
  - And another for returning menu categories with a 200 when reaching the correct endpoint and checking that classtype returned is our DTO.

---

--- 17 Jan 2023 ---

- Notes from front-end team implemented into database.
  - Removed img_url from menu items as images are able to be retrieved through just the menu item name and category. Changes needed in back-end application to reflect this.
  - Table created created for menu item categories to facilate menu navigation dynamic population. Also needs to be addressed in backend (ie. data modeling, controller and service implementation, tests created.)
  - Descriptions for menu items updated.
- Refactored menu item entity to properly reflect updated database.
  - Modified tests, and general methods as errors became apparent.
  - Found a small logic error in creating new order from details provided from customer.
    - Corrected and retested with desired results.

---

--- 16 Jan 2023 ---

- Implementation of CORS configuration allowing for connection to front-end application.
- Taking note of areas of improvement in overall design and structure. Will evaluate for impact, test new changes and then update documentation.

---

--- 17 Oct 2023 ---

- Application files scrubbed for outdated code and comments.
- Test files scrubbed for outdated code and comments.
- ERD updated.
- ReadMe updated.

---

-- 10 Oct 2023 --

Had a drive fail and lost a bit of work but was able to get drive cloned and then utilize git to repair damaged files merged from a cloned drive. Both drives appear to be synced and operational and program is chugging along. Next steps will include a cleanup from the file repairs.

---

-- 5 Oct 2023 --

- tests refactored and improved upon.
- noticed double data type utilized in application to describe currency variables. Updated to utilize big decimal instead for precision.

---

-- 3 Oct 2023 --

- Application wide refactor mostly focused on eliminating outdated comments and commenting out print lines in order to reduce noise. Commented out instead of eliminated as I appreciate not having to completely rewriting the line when trouble shooting is required.

---

-- 25 Sep 2023 --

- Updated all endpoint documentation for swagger with more detailed instructions and examples.
- Began work on tests with Mockito. Utilizing the framework to mock null instances of calls to the database and ensure global exception handler is operating as desired. These tests will serve as a refactor and replacement of earlier tests.

---

-- 13 Sep 2023 --

- Discovered bug linked to not enough variation in path names to differentiate between searching for a customer or order by uid or customer name. Appears to be remedied by adding uid or name to the paths concerned.

  (ie "/get-order-uid/{orderUid}", "/get-order-customer/{customer}")

---

-- 11 Sep 2023 --

- Updates to owners tools pertaining to customers for increased validation.
  - edit customer phone number updated.
  - edit customer name updated.
  - edit customer email updated.
  - delete customer updated.
- Updated tests for get all customers, delete customer by uid, get customer by uid, get customer by name, update customer email, update customer phone.

---

-- 10 Sep 2023 --

- Refactored dtos to exclude entities id in favor of uid and for returned order to utilize uids in lieu of ids.
- Updated owners tools customers requests to make use of uids.
- Encountered a bug while returning customer by name that appeared to stem from endpoint paths too similar to looking up a customer by uid. Rectified through alteration to naming convention.

---

-- 06 Sep 2023 --

- Delete order tests updated to make use of order uid.
- Edit order item quantity tests updated to make use of order uid.
- Work on get order by id tests saved and removed from application as we will no longer be using them.
- Today's sales tests updated to make use of order uid.
- Order closed tests updated to make use of order uid.
- Order ready tests updated to make use of order uid.

---

-- 05 Sep 2023 --

- Mark an order as closed changed to use uid instead of id successfully.
- Delete an order changed to use uid instead of id successfully.
- Add to order changed to use uid instead of id successfully.
  - tests updated and functioning correctly.
- Update order item quantity.
- Addressed an issue with 404 not returning a satisfactory message.

---

-- 04 Sep 2023 --

- Initialized refactor of order endpoints to use the order uid in lieu of the order database index as a parameter for searches and alterations by the owner.
- Mark an order as ready changed from id to uid successfully. Will continue on with additional changes.

---

-- 03 Sep 2023 --

- All menu item endpoint documentation updated with examples of successful requests and responses. Documentation formatting updated.

---

-- 02 Sep 2023 --

- All owner endpoint documentation updated with examples of successful requests and responses.

---

-- 29 Aug 2023 --

- Updating docs with examples of successful request responses.
- Updated calling an order to include the uid in lieu of id.

---

-- 23 Aug 2023 --

- Updated documentation for owner tools pertaining to orders.
- Updated documentation for owner login.

---

-- 22 Aug 2023 --

- Updated documentation for orders endpoints.
- Updated documentation for owner tools pertaining to customers.

---

-- 21 Aug 2023 --

- Application updated for open api 3.0.
- Began updating docs.
- Updated documentation for menu endpoints.

---

-- 18 Aug 2023 --

- Logger set up and operating and currently operating as desired. Notes made for potential future implementations.
  - Appender for debug and one for errors.
- Issues updated and resolved in github.

---

-- 8 Aug 2023 --

- Implemented a basic logger for exceptions of interest. Refinement in progress.

---

-- 6 Aug 2023 --

- Refactor for security, exception handling, applicable tests and dtos in good place.
- Beginning implementation of logging exceptions with logback.

---

-- 1 Aug 2023 --

- Refactored for 401 responses for bad login attempt or trying to send request with fabricated payload.
- Removed a validation check that was hindering auth filter.
- Next steps will include a refactor of security files to remove redundant comments or theoretical code.

---

-- 26 Jul 2023 --

- customer to be refactored to include a uid.
- exception handler to ensure bad credentials throw a 401 and expired or bad tokens throw 403.
- ***

  -- 24 Jul 2023 --

- refactor of ownersServices customers and orders complete and tested.

---

-- 14 Jul 2023 --

- refactor of config folder executed and tested, still need annotation
- refactor of controllers folder executed and tested, still need annotation
- refactor of dao folder executed and tested
- refactor of dtos with new names executed and tested

---

-- 11 Jul 2023 --

- Encountered oddities while running test cases through postman in exception handling for un-found menu item entities. These have been resolved.
- Junit tests performing as expected for exception handling.
- Application test run through postman successful.
- Commencing clean up and refactor.

---

-- 6 Jul 2023 --

- Currently all 44 tests for input and output validation operating as expected for all current customer and owner functions. Whew!
- Next will be quick run through with postman to verify user experience followed by a refactor and clean up.

---

-- 5 Jul 2023 --

- Test for daily sales through postman reveals logic errors. Needs to be evaluated further.
- Need to edit response successful deletion of an order. Currently returning void.

---

-- 4 Jul 2023 --

- mark order ready and mark order closed refactored to return order dto.
- Moving onto daily sales. Dto to be created, returned and verified. Test will include marking two orders ready and closed in order to test calculations.

### To all that remains of freedom and liberty.

---

-- 3 Jul 2023 --

- Negative test case for attempt to close an order with an invalid order id complete.
- Negative test case for attempt to close an order before the order has been marked ready complete.

---

-- 2 Jul 2023 --

- Negative test cases for editing an order item quantity complete.
- Negative test case for no orders returned complete.
- Negative test case for calling an order by a bad order id complete.
- Negative test case for calling an order by an invalid uid complete.
- Negative test case for calling an order by an invalid customer name complete.
- Negative test case for attempting to change order status with invalid order id complete.
- Positive test case for closing an order complete.

---

-- 29 Jun 2023 --

- Completed validation for adding an item to an order, handling an exception and tests for negative use case.
- Completed validation for deleting an order, handling an exception and tests for negative use case.
- Began validation for editing an order item. Still need test for changing quantity to zero and being removed from the order .

---

-- 28 Jun 2023 --

- Completed validation for adding an item to an order, handling an exception and tests for negative use case.
- Completed validation for deleting an order, handling an exception and tests for negative use case.
- Began validation for editing an order item. Still need test for changing quantity to zero and being removed from the order .

---

-- 27 Jun 2023 --

- Previously mentioned bug appears to be just that. Tried printing caught exception from repository and then commented it out and then new exception properly thrown and picked up. Seems a tad bizarre.
- Completed negative test cases for updating customer email.
- Completed negative test cases for updating customer phone number.
- Bug now appears to be as a result of needing to be instantiated (ie in the call to print customer) before the exception is thrown.
- Tests for owners functions pertaining to customers completed including validation.

---

-- 26 Jun 2023 --

- Implemented new tests for bad test cases for all owner functions relating to customers except update email and phone.
- Something bizarre with trying to call the repository to verify a customer id. Like it didn't close the connection properly.
- Will investigate further.

---

-- 20 Jun 2023 --

- Implemented new test for bad test case for delete customer by id.
- Implemented changes to get all customers for a more thorough test.

---

-- 19 Jun 2023 --

- Completed test for request sent with jwt containing invalid subject.
- Next phase will include verifying that all tests have a negative test case with expected exceptions thrown starting with delete customer by id.

---

-- 13 Jun 2023 --

- Began laying new jwt validation tests.
- Confirmed that if token expiration is before issued at jwt expiration exception thrown. Same as other test case where date is fixed as expired.
- Next will test where username or signature are invalid.

---

-- 12 Jun 2023 --

Something odd has taken place. Application and all tests were functioning as desired but something changed recently causing bearer token to not be generated so need to go back and and start at the root before further progress can be made towards testing for proper exception handling of bad jwts.

- Research reveals one version of auth filter allows for successful token creation but not allowing for successful error handling, the other version allows for error handling but not successful token creation.

- Removal of :

        assert expiration != null;

        if (!issuedAt.before(expiration)){
            throw new JwtException("invalid date") ;
        }

Appears to have rectified the issue.

- I believe the next endeavors will include cloning aspects of the jwt class in for our other test cases.

---

-- 7 Jun 2023 --

Still experimenting with where I want to catch certain errors and what makes most sense but completed test for an expired jwt. Next test will be for a jwt that isnt yet expired but the expiration is before the issued at.

---

-- 6 Jun 2023 --

Finally broke through. Moved auth filter in security config to the top of the stack, implemented suggestions from Nicolas Francisco Corvi and additional documentation to incorporate a Handler Exception Resolver, and also added a lombok config file in order to make it work.

Excited for this step and looking forward to the next.

---

-- 2 Jun 2023 --

- Bit stumped on getting global error handler to pick up exceptions related to jwt validation. Have submitted a question on stack trace to see if I can get more info from development community et al.

- Next attempts to include experimenting with catching the exception in a service layer, mostly out of curiosity.
- Continuing on current trajectory of customer exception for each jwt use case(seems inefficient in theory but seems to be best chance so far)
- As well, will re-attempt authentication entry point implementation and creating a returnable error response.

---

-- 29 May 2023 --

- Exception was being handled though not correctly. New efforts have demonstrated a more desirable result. Error code is correctly returned as well as the ability to manipulate the response into a useable message. This is not yet the full desired effect of having the exception picked up and handled by the global error handler. Morning research has brought me the following resource for further research.

https://github.com/erickrodrigs/musicflux/commit/c247e7f9212560c5f3e19c5b8667d7e97e22309f

---

-- 24 May 2023 --

- Research indicate jwt exceptions are coming from filter which is why the global error handler established is so far unable to pick up and handle the exception. Have been looking over other developers solutions and believe I may have located a jumping off point for a solution.

https://stackoverflow.com/questions/34595605/how-to-manage-exceptions-thrown-in-filters-in-spring

---

-- 23 May 2023 --

- Narrowing down relationship between jwt auth filter and extract all claims method. Specifically parseClaimsJws needs more understanding. Application is currently correctly throwing an expired jwt exception for our test data and returning a 403 but our exception is not currently being appropriately handled and returning useful information to the front end.

---

-- 22 May 2023 --

- While working through validating tokens it has been discovered that signature exceptions are being thrown but not handled. Research has lead me to the auth filter. I will be focused on this for a bit trying to achieve desired results. So I am currently advising to consider the auth filter a mess until further notice while tearing it apart and putting it back together. ^\_^.

---

-- 18 May 2023 --

- Began laying out procedures for tests concerning owner functions.
- Also need to take a look at when tests are run in succession there is a repeating pattern of 1 fail and two pass, 2 fail and one pass, and all pass. This behavior appears to mostly originate in the bad password test. My hunch is that something is related to the random chars created for encryption.
  - Will look into excluding specific chars. Will start with (", ^, <, >).

---

-- 17 May 2023 --

- Test case for an unsuccessful login attempt with a bad username returning a 401 successful and in place. Seems to be a spring bug with a fair bit of history. Solution came from research and after attempting multiple possibilities. The following dependency resolved the issue.

      	<groupId>org.apache.httpcomponents.client5</groupId>
      	<artifactId>httpclient5</artifactId>
      	<version>5.2.1</version>
      	<scope>test</scope>

- Nest step will be replicating test for a correct username but bad password.
- Bad password test successfully tests for 401 and error message contains the appropriate fields.

---

-- 16 May 2023 --

- Invalid credentials test is accurately looking for whatever response code is determined. Currently application throws a 403 if login fails and desired effect is a 401 for a bad login and then a 403 if a user was to say try and access a function with a bad jwt. Will attempt try catch at auth service for resolution.

---

-- 15 May 2023 --

- Continuing with bad login and validation tests.

---

-- 11 May 2023 --

- Began laying out negative test case for login endpoint.

---

-- 10 May 2023 --

- Updated createOrder() to reset validation flags after an order is saved.
- Updated database and entity to use status instead of closed as a data column for an order. Should make data more digestible. By default status is set as "open" and when order is closed will now read "Closed : + timestamp".
- Create order tests reformat completed at the moment.

---

-- 9 May 2023 --

- Order validation refactored for a clean up and integration test for negative test case operating as expected.
- Valid order test refactored for updated validation and is successful.
- Cleaned up code and comments in auth service, order creation test, and jwt service.

---

-- 8 May 2023 --

- customer email validation in place.
- test correctly returns 400 bad request if customer name, phone number or email invalid.

---

-- 4 May 2023 --

- validateCustomerName method implemented and functioning as intended.
- validateCustomerPhoneNumber method implemented and functioning as intended.

---

-- 3 May 2023 --

- Valid order test updated as well as orders service incorporating validation for a customer name.
- Research lead to regex solution from user Nick Sikrier on stack overflow https://dev.library.kiwix.org/content/stackoverflow_en_nopic_2021-08/questions/15805555/java-regex-to-validate-full-name-allow-only-spaces-and-letters.

  - solution as  ^\\p{L}+[\p{L}\p{Pd}\p{Zs}']\\\*\p{L}+\$|^\p{L}+$

    - ^\p{L}+ &nbsp; - &nbsp; It should start with 1 or more letters.
    - [\p{Pd}\p{Zs}'\p{L}]\* &nbsp; - &nbsp; It can have letters, space character (including invisible), dash or hyphen characters and ' in any order 0 or more times.
    - \p{L}+$ &nbsp; - &nbsp; It should finish with 1 or more letters.
    - |^\p{L}+$ &nbsp; - &nbsp; Or it just should contain 1 or more letters (It is done to support single letter names).

- My addition and incorporation simply adds converting our received customer name from the order, converting to a byte array and counting how many spaces. So if the name format doesnt match regex or has too many or not enough spaces it will not be valid. Nick's above regex allows any letter in any language and may be overkill for our little local food truck, but thought it might be nice to have the option. To be evaluated if issues arise.
- The idea being that front end should receive customer first and last name trimmed and then concat with a single space in between. Will be noted in the documentation.
- Added skeleton methods to order service to hold validation means for an order in order to help increase readability.

---

-- 2 May 2023 --

- Created test for negative use case of creating and verifying validation is operating as desired.

- Fine tuning regex.

---

-- 1 May 2023 --

- Validation in place for returning a menu item by id, or menu items by category. Tests updated for positive and negative use cases.
- Initialization of laying out validation for creating an order. Most likely evaluating that each field for customer is not null and of valid type, as well as ensuring order is not nut and the total is reflective of the cost of each item in the order.

---

-- 27 Apr 2023 --

- Quite possibly found my solution through simple creation of a body field returned in create message field. The actual error itself will be logged through the creation of a log method called in the error handler.
- Error handler updated for number format exception and entity not found exception.
- Menu item test successful for positive and negative use test cases.

---

-- 26 Apr 2023 --

- Development has slowed dramatically due to returning to old job in order to pay bills while trying to change careers.
- Working at trying to create a more detailed form of feed back for front end development when an exception is encountered.

---

--- 21 Apr 2023 --

- Appear to have rectified algorithm for random Uid generated on order creation
- Create order gives us a return order dto for order confirmation.
- Test successful however dtos were not being set correctly for response message but now are.
- Test is satisfactory.
- Order generates uid correctly on order creation.

---

-- 20 Apr 2023 --

- Beginning implementation of generating a random uid for an order on checkout.
- Trying for a smaller number so easy to read and repeat to customer or owner.

---

-- 19 Apr 2023 --

- Tests functioning as desired after encryption implementation for both username and password. This would indicate a means for protection from MITM. Algorithm can always be improved.
- Encryption algorithm keys moved offsite.
- Development code cleaned and saved offsite to my files for proof of work if requested but so that old algorithm views are not sitting waiting to be picked up.

---

-- 18 Apr 2023 --

- Basic implementation of encryption and decryption created
- Discovered char being generated causes issue with json string
- Research into random int generation with exclusions
- Random char method updated to compare generated random with a list of excluded
- Test for jwt creation with encryption successful
- Majority of program expected to be broken until full integration.

---

-- 17 Apr 2023 --

- Encryption research.
- Basic operable functionality written out in separate file.

---

-- 13 Apr 2023 --

- All patch methods updated to put. Spring, Java, Modern Http... ><
- Orders tests successful carrying out protected functions for owners.

---

-- 12 Apr 2023 --

- Created successful test for generating token and that it is valid.
- Research on JWt(JWS) continues and brings much to contemplate.
- Jwt now integrated now integrated into unit tests for owners tools customers functions. Successful test cases positive.

---

-- 10 Apr 2023 --

- Test db updated.
- Began writing unit tests for successful owner login function.
- Base Uri implemented.
- Test body created.
- Test asserts correct status code for successful call to login endpoint
  - Currently working through logic for verifying that an appropriate jwt is returned.

---

-- 6 Apr 2023 --

- Refactored files pertaining to security.
- Functionality tested in postman.
- Mapping out integration tests and means for token refresh and invalidation of token.

---

-- 5 Apr 2023 --

- Corrected circular dependencies.
- Application is correctly locking off desired endpoints and granting access if a valid jwt accompanies the request in the header and is only created if entered credentials for the owners match in the database.

---

-- 4 Apr 2023 --

- Jwtauth filter in place for validation by user name and date.
- Refactored -> past implementation generated token but would token was not accepted. Possibly due to time lited in postman vs. the amount of time before expiration.
- Current state creates toke. It is apparently accepted with a 200 but further testing is required still as we are no longer receive the expected objects back in postman accessing the secured endpoint.
- Returning a 200 for the token passing the filter but request not continuing on past to the endpoint. Next will include feedback for each step and comparing passing nothing and passing everything.

---

-- 3 Apr 2023 --

- Implemented jwt dependencies.
- Created controller and controller interface for login.
- Created ownerAuthDto.
- Created JWT Service and Jwt Service interface.
- Document where security config came from.
- Chain of actions so far
  - Hit controller and call service >> call generate token and pass username >> gen token calls create token >> create token sets the sign key, issues at, expires at, subject and claims.
- Currently returning a token through postman if creds correct.
- Have removed form login as we were mostly just receiving 302s for the spring redirect when hitting our endpoint for the login form.
- Currently receiving 403s though browser and postman.

---

-- 31 Mar 2023 --

- Application updated to Spring 3.0.01 in order to implement spring 6 and the latest features of spring security.
- After studying multiple tutorials online and learning what changed I am pleased to have an in initial implementation up and running.
- DB updated to include a user roles column for our owners table.
- Owners passwords were encrypted and fed into the tables on execution of sql script.
- Created security config in config package.
- Implemented beans for user detail service, filter chain, password encoder, authentication provider.
- Touched up owner entity and implemented user details in order to remove the need for an additional class.
- Created Owner repository and method for finding owner by username.
- Created the user details service in a new package under services package.
- End-points successfully tested in browser after logging in.
- Repo will be updated after more work and potentially sensitive information in the code secured.

---

-- 30 Mar 2023 --

- Broke out owners-tools controllers and services to owners-tools/orders and owners-tools/customers.
- Further testing demonstrated an error in calculating daily sales.
  - Refactored DB from status column to closed column in order to replicate function of ready column. ie(default no and then when changed logs time).
  - Refactored method in orders repository to find order by closed.
  - Refactored close order method in OwnersOrdersService and daily sales method.
  - Updated logic of daily sales to reflect changes.
    - Test successful.
- Base test uris updated for owner functions relating to customers.
- Customer test classes created.
- Successful test for deleting a customer from db.
- Successful test for getting all customers.
- Successful test for get customer by id.
- Successful test for update customer name.
- Successful test for update customer email.
- Successful test for update customer email.

- Discovered assertThat() not necessarily allowing tests to fail. Dove into the Assertions class for solutions and appear to have rectified the past issue.

### Initial tests for owners functions pertaining to customers in place!

---

-- 29 Mar 2023 --

- Successful test for getting an order by id.
- Successful test for deleting an order by id.
- Functionality for owner to add a menu item to an order implemented.
- Functionality for owner to edit an item on an order implemented.
- Successful test for editing an item on an order.
- Successful test for adding an item to an order.
- Successful test for returning days sales and verifying the equal a summed total of orders
  marked closed in the db.

### Initial tests for owners functions pertaining to orders in place!

---

-- 28 Mar 2023 --

- Added verification clause to order created test by immediately retrieving the created order from db.
- Successful test for owners to retrieve an order by order uid.
- Successful test for owners to get all orders.
- Successful test for retrieving open orders by customer name.
- Successful test for marking an order ready.
- Successful test for marking an or order closed.
- Test case for marking an order closed exposed a bug where orders were unable to be called after being marked closed.
  As customer id field was null the requirements were unfilled for creating the necessary dto. This was fixed by setting a default null value for the
  fields of interest. If the incoming order has null for a customer id then there is no point in setting the fields and the requirements of the dto are satisfied.

---

-- 27 Mar 2023 --

- Determined means for obfuscating customer details.
- More time spent researching hashing data for owner auth will work on it more after initial tests successful with restructure.
- Refactored reflecting structure change and removed unnecessary tests though their frames may be utilized in future contexts.
- Menu item tests successful
- Order creation test successful.

---

=======

-- 21 Mar 2023 --

- Customer and order saved in same transaction.
- Customer may place an additional order while past order is still open, eliminating duplicate customer entries.
- Upon closing last open customer order, customer information removed. Ensures information not in use is not sitting idly waiting to be exploited.
- Began work on securing owners tools endpoint.
  - Created owners table and loaded sample data.
  - Created owner entity and respository.

---

=======

-- 20 Mar 2023 --

- Get customer dto refactored for the desired use case and function is currently operating as desired.
- Implemeneted returning a customer by customer name.
- Implemented returning a customer by id.
- Implemented updating a customers details.
- Implement customer delete a customer without deleting orders associated to them so that information can still be used for accounting purposes.

- Refactor notes:

  - Removed unused code and comments app wide due to relocation of functionality between order item, orders and owners.
  - Relocated order item dto converter to orders service.

- Began combining order and customer dto into a new order dto that more closely reflects what the order object will consist of.

- refactor notes
  → removed unused code and comments app wide due to relocation of functionality between order item, orders and owners
  → relocated order item dto converter to orders service

  =======

  -- 17 Mar 2023 --

- Get customer dto refactored for the desired use case and function is currently operating as desired.
- Implemeneted returning a customer by customer name.
- Implemented returning a customer by id.
- Implemented updating a customers details.
- Implement customer delete a customer without deleting orders associated to
  them so that information can still be used for accounting purposes.

---

-- 16 Mar 2023 --

- Return order by uid for owners implemented.
- Return open order by customer for owners implemented.
- Get the days sales migrated to owners endpoint.
- Mark an order ready migrated to owners endpoint.
- Ability to delete an order by id migrated to owners endpoint.

---

-- 15 Mar 2023 --

- Controllers restructured to share the orders endpoint.
- Order can be edited to add an order item and order total is adjusted.
- Order item can be edited to change quantity and order item total and order total is adjusted.
  - If quantity = 0 the item is removed from order and the order total is adjusted.
- Functions reduced from order item controller as they are now implemented through orders controller
- Began creating packages for functions specific to owners, ie returning entities instead of DTOs so that they are able to have ease in manipulating data.
- Created owners controller, service, and dto layers.
- Implemented owners getting all orders in conjunction with their customized dto.

---

-- 14 Mar 2023 --

- Implemented sales endpoint which tallies number of completed sales and total for the current day for owners use.
- Implemented deletion of an order.

---

-- 13 Mar 2023 --

- Return order by uid implemented.
- Create order now correctly generates totals.
- Restructured get-order endpoint to be searchable by uid or customer name.
- Return order by customer name implemented.
- Order able to be marked with a time ready.
- Order status can be marked closed to indicate an order is complete.

---

-- 9 Mar 2023 --

- Status column added to orders.
- New erd available.
- Json syntax for creating order figured.
- Initial test for creating an order successful.
- Ready column added to orders.
- get orders returned to working off of dto, stills needs to be cleaned.
- New erd available.

<p align="center">
  <img src="./back-end/supporting-files/erd-09Mar2023.png"/>
</p>

---

-- 8 Mar 2023 --

- Setting up dto for order response. Interested to see if this will solve recursion as we will be wanting to work with dto most likely anyways.
- Dto implemented and relationships currently behaving as expected.
- Next steps will include implementing tests, and reformatting database to include a column for status with a default value of open, and to be changed manually to closed after customer pays and picks up food by tons of tacos owners. May need to also include field for contact details but they will be encrypted. To be worked on at a later date.
- test sql scripts updated
- Initial Test for all orders returned successful.

---

-- 7 Mar 2023 --

- Checkout endpoint initial implementation successful.
- Changing cart implementation to be conducted from front end and lighten server load. Not that this app should be undergoing a heavy rush but logic and research seems to suggest this is the way.
- SQL scripts updated.
- Refactor implemented.
- Initial test for getting all orders and their associated values through postman successful.
- Will get a new erd up soon.

---

-- 6 Mar 2023 --

- Branch schema changed where I will just stay on a branch for all work pertaining to in lieu of jumping back and forth to tests and end points branch. Please note development there has stopped and is continued on other branches.
- orders controller interface implemented.
- orders dto created.
- created packages and initial files for orders tests
- first function for endpoint currently operating as intended.

- Initial files for orders created.

---

-- 5 Mar 2023 --

- Updated order item entity to cart item as it better describes the purpose.
- Order controller interface implemented with swagger for documentation.
- Branch schema changed where I will just stay on a branch for all work pertaining to in lieu of jumping back and forth to tests and end points branch. Please note development there has stopped and is continued on other branches.

---

-- 3 Mar 2023 --

- Remove cart item tests successful.
- Create order item no longer returns the item added to cart in a response so that selected items remained cloaked. May be overkill or create other issues down the road. If so check there.
- Console messages created for verification of reaching service, controller and action completed.
- Order item tests functioning as designed.
- Code reformatted, cleaned up and ready to start working on orders.

---

-- 2 Mar 2023 --

- Successful tests for utilizing dto to return list of order items with 200 and
  if uuid not valid returning 404.
- Created dto for returning order items with specified parameters, tests successful.
- Updating cart tests successful.
- Composite indexes may be the key to what I was originally envisioning for link between order items and order item. Will take much more research and time.
- The two previously mentioned tables are no longer linked and sql scripts have been altered.

---

-- 28 Feb 2023 --

- initial order item response formatted.
- test with dto successful and good data for cart successful.
- test with dto and bad data for add to cart successful.
- total is now auto generated when an order item is created based on order items base price and the quantity selected.

---

-- 27 Feb 2023 --

- Now using HSQLDB as in memory for testing, way to many issues with H2 being restrictive and not truly modeling database.
- All tests successfully functioned from returned non linked entities.
- Established succesful relationship between order items and menu items with jpa.
  Still lots of work to be done formatting the data.
- Began Implementaion of dto for order items. New tests to be written as progress made.

---

-- 23 Feb 2023 --

- Successful test that order item is deleted with a 200 status code
  and cannot be deleted again because it can not be found because it has been deleted.
- Successful test for order item not deleted if order item id invalid.
- Implemented deleting an order item if if quantity is zero and tests verify desired functionality.
- Re-instantiated orders and customer entities.

---

-- 22 Feb 2023 --

- Get order items by uuid completed including handling bad input.
- Restored update quantity function to service and controller.
- Restored delete order item function to service and controller.
- Created successful test order item quantity is updated as well as order item total.
- Created successful test to return 404 if order item does not exist.

---

-- 21 Feb 2023 --

- Revised create Order Item tests to produce desired and satisfactory results for successful and unsuccessful scenarios.
- Began structuring tests for getting order items through a common uuid.

---

-- 20 Feb 2023 --

- Working through customer and order endpoint correlation, logic, and encryption.

---

-- 16 Feb 2023 --

- initial logic of testing if all order item fields are valid completed.
- need to possibly redefine test order item through a builder instead of json string values.
- right now cant compare as the database is in memory and closes after writing to the db so trying to read if value is valid against the repository is mute.
- need to refine regular expressions and test again after return from break.

---

-- 15 Feb 2023 --

- completed test for returning menu items by category.
- properties files split out between application and test.
- tests for successful call of menu items by category and unsuccessful call created.
- documentation updated.
- began piecing order endpoint back together including testing operation for a successful addition to a cart.

---

-- 14 Feb 2023 --

- Getting menu items by id tests completed and began organizing tests for getting menu items by category.
- Documentation to follow.

---

-- 13 Feb 2023 --

- Test environment refactored for full function of in memory h2 in order to preserve db image.

- Menu item endpoint restored and tested for calling a menu item by id number successfully. Will test for a 404 and 400 next.

---

-- 10 Feb 2023 --

- test env config restored.
- connections and base endpoint functionality re-established.
- turned off default exposure through config file.
- menu controller back online.
- order controller back online.
- all endpoints functioning as desired.
- tested in browser and postman.
- next will be error handler integration and unit testing for each endpoint and feature.
- updated erd below. Clockwise cascade.

  &nbsp;

<p align="center">
  <img src="./back-end/supporting-files/erdV1.2.png"/>
</p>

## &nbsp;

-- 9 Feb 2023 --

- Initial refactor complete. Means for adding a new order item to the database restored. Meaning that it should be feasible to create a cart. From the front end.
- Test environment scripts need to be restored however as they appear to be belly up at this point. Lots learned through the process.
- Also created new erd that represents changes. Will work on getting it up tomorrow.

---

-- 8 Feb 2023 --

- Started coming across abnormalities while conducting tests and so spent the day refactoring. Hoping the end result is for the better.

---

-- 7 Feb 2023 --

- DB refactored. Table 'order_item' is now 'cart' as this better reflects its purpose.
- Initial completion of update quantity of a cart item. Also updates the total.
- Initial completion of remove cart item. Needs some refactoring to reflect the new schema but methods were all tested and functioning in the browser and posted before changes made.
- Full crud functions for cart!

Lots to do still but starting to see light. Updated to do list.

---

-- 6 Feb 2023 --

- Refactored order item controllers and services.
- Refactored packages for organizational purposes.
- Began working out logic for updating a menu item.
- Updated documentation for menu item and order item controllers.
- Will be refactoring data structure in future. Order items will become cart items as this is more descriptive of what is actually happening.
- Order item endpoints test in browser and with postman for desired functionality. Tests will be written with JUnit following establishment of remaining endpoints for crud functionality of cart items.

---

-- 3 Feb 2023 --

- Refactored menu item controllers and created services that now provides desired functionality of shorthand or jpa search.
  -Refactored Controller naming convention .
- Working on functionality of order/cart item in regards to crud operations and then transferring to an order with making these end points open for queries. Will resume next week.

---

-- 2 Feb 2023 --

- added jira project - need to research issue keys.
- updated read me to include purpose of application and link to dev journal.
- created endpoint for creating orders and order items.
- created test for 201 response upon successful creation of order item.
- tested endpoint with valid data in postman.
- cleaned up tests.
- created controller and documentation for order endpoint.
- cleaned up config file.

Tomorrow's goals

- Refactor Controllers.
- Continue Crud Operations for order items.

---

-- 1 Feb 2023 --

- Built global error handler for future implementation.
- Began fleshing out functionality to be associated with adding items to a cart.
  - In summary:
    - click on item >> order uuid is generated >> relevant information is written to DB for the purpose of populating a cart for customer to alter and observe.
    - On checkout >> uuid and cart will clear after using the data in the cart to create an order and write to DB.
- Created order item controllers and repository classes.
- Refactored database based on needs new phase will bring.
- Update project on GitHub.

Tomorrow's goals

- Start project management with Jira.
- Update README description.
- Begin work on controllers and and tests to facilitate writing order items to DB.

---

-- 31 Jan 2023 --

- Test built for creating a new menu item for when that feature is implemented for the owners.
- Test built for getting menu items by category.
- Sprint concluded on target.

Tomorrow's goals

- Build error handler.

---

-- 30 Jan 2023 --

- Re-organizing menu item test for http responses.
- Organizing ideas on migrating menu item to order item.

---

-- 27 Jan 2023 --
Not as far as I wanted to get today but I'll take the small victory.

- Created test for retrieving data and comparing it against an expected set of data.
- Also tested for proper response code from the query.

---

-- 26 Jan 2023 --

- Created a configuration that works for testing in memory as well as against the actual database.
- Successfully created a test to create a new item in the database. I am not completely pleased with it however and intend to do some more work on it. Right now we create an entity to send through a constructor but I would like for the data to come from a builder instead.

Tomorrow's goals

- Try and complete means for full crud testing with in memory database.

---

-- 25 Jan 2023 --

- Revealed entity ids in order to aid in ease of working with data with method constructed in rest config
- Refined test for CRUD operations against the data base.
- Calling items by category will remain as simply a query on our menu-item endpoint.
- Considerable research into configuring H2 in memory database for testing purposes. Have possibly locked on to a configuration that will yield desired results.

Tomorrow's goals

- Try and complete means for full crud testing with in memory database.

---

-- 24 Jan 2023 --

Starting to get a bit more dialed with my procedure and what needs to come next. The pieces are falling together. I do find that I am prone to losing time because I worry about best practice and simply questioning if I'm doing this right in order to save work down the road. But sometimes you just gotta fool about and find out ^\_-.

- Created resources for searching menu items by id and category.
- Refined swagger document a bit more
- Created test file working with menu items by category
- Refined menu item path a bit so that a bit more precision is required to call an item and all items do not just become available upon endpoint entry.
- Updated DB with columns for descriptions and image urls.
- Added more DB entries to work with.

Tomorrow's goals

- More work on menu item category endpoint.
- Work on menu item test and error handling.
- Possibly work on logging.

---

-- 23 Jan 2023 --

- Created successful route test for a menu item.
- Created API documentation with swagger for menu-item end point.
- Added as project on github and linked to repository. Don't know how well I'll be able to keep up with project tracking while simultaneously developing application solo but going to give it a try.

Tomorrow's goals

- Create and Test menu-item endpoint for returning items by category.
- Add documentation for end point.
- Update DB to add columns 'description' and 'img-url' to menu-item table.
- Begin work on error handling.

---

-- 19 Jan 2023 --

- Inserted test data to local instance db.
- Created connection configuration.
- Created base entity classes.
- Created Menu Item repository as part of dao.
- Created Data Rest Config and limited access to read only.
- Tested endpoint in browser and with Postman.
- Created basic test classes.
- Updated README with a rough phase list.

Next tasks will include fleshing out the test classes. Project management with github and jira.

---

-- 18 Jan 2023 --

- Created script based on an updated EDR.
- Established local database for testing.
- Initialized SpringBoot application.
- Created base packages for further development.

---

-- 17 Jan 2023 --

Created initial EDR for consideration of database layout. This diagram will be updated as improvements are made.

&nbsp;

<p align="center">
  <img src="./back-end/supporting-files/TonsOfTacosErd.drawio2.svg"/>
</p>

&nbsp;

---

-- 16 Jan 2023 --

Created repositories, updated readme and pushed all to readme-update branch.

---

© Adam Straub 2023
