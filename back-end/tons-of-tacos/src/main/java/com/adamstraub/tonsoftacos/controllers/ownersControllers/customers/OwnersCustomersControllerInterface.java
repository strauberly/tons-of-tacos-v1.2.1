package com.adamstraub.tonsoftacos.controllers.ownersControllers.customers;

import com.adamstraub.tonsoftacos.entities.OrderItem;
import com.adamstraub.tonsoftacos.dto.businessDto.CustomerReturnedToOwner;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;




@Validated
@RequestMapping(
        value = "api/owners-tools/customers")
@OpenAPIDefinition(info = @Info(title = "Services pertaining to functions reserved for the owners of tons of tacos."),
        servers = {@Server(url="http://localhost:8080", description = "Local server")})
public interface OwnersCustomersControllerInterface {

    // get all customers
    @Operation(
            summary = "All customers returned.",
            description = """
                    Will  return an array of customer objects with fields for customer name, phone number,  email, and open orders. For owner use only with proper auth.
                    """
            + "\n" + "Example response: "
                    + "\n" +  "\n" +
                    """
                            [
                                {
                                    "customerId": 1,
                                    "name": "John Johnson",
                                    "email": "john@johnson.com",
                                    "phone": "555.555.5552",
                                    "orderIds": [
                                        2,
                                        3
                                    ]
                                },
                                {
                                    "customerId": 2,
                                    "name": "Tim Timson",
                                    "email": "tim@timson.com",
                                    "phone": "555.555.5553",
                                    "orderIds": [
                                        1
                                    ]
                                },
                                {
                                    "customerId": 3,
                                    "name": "Bob Bobson",
                                    "email": "bob@bobson.com",
                                    "phone": "555.555.5551",
                                    "orderIds": []
                                }
                            ]
                            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All customers returned."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No customers found."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occured."),
            }
    )
    @Transactional
    @GetMapping("/get-customers")
    List<CustomerReturnedToOwner> getAllCustomers();


//  get a customer by customer name
@Operation(
            summary = "A customer is returned by customer name.",
            description = """
                 A customers name is provided and if valid, will return a customer object. For owner use only with proper auth.
                 """
                    + "\n" + "Example query: "
                    + "\n" +  "\n" +
                    "localhost:8080/api/owners-tools/customers/get-customer/name?name=John Johnson"
                    + "\n" +
            "\n" + "Example response: "
                    + "\n" +  "\n" +
                    """
                            {
                                "customerUid": "jk34-h5j0",
                                "name": "John Johnson",
                                "email": "john@johnson.com",
                                "phone": "555.555.5552",
                                "orderIds": [
                                    "654654-4655-555",
                                    "654654-4657-555"
                                ]
                            }
                            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Customer is returned."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No customer found."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occured."),
            }
    )
    @Transactional
    @GetMapping("/get-customer-name/{name}")
    CustomerReturnedToOwner getCustomerByName(@RequestParam String name);

//get customer by uid
@Operation(
        summary = "A customer is returned by uid.",
        description = """

        A customers uid is provided and if valid, will return a customer object. For owner use only with proper auth.
        """
           + "\n" + "Example query: "
           + "\n" +  "\n" +
           "localhost:8080/api/owners-tools/customers/get-customer-uid/customerUid?customerUid=jk34-h5j0"
           + "\n" +
   "\n" + "Example response: "
           + "\n" +  "\n" +
           """
{
    "customerUid": "jk34-h5j0",
    "name": "John Johnson",
    "email": "john@johnson.com",
    "phone": "555.555.5552",
    "orderIds": [
        "654654-4655-555",
        "654654-4657-555"
    ]
}
                   """,
responses = {
@ApiResponse(
    responseCode = "200",
    description = "Customer is returned."),
@ApiResponse(
    responseCode = "400",
    description = "Request parameters invalid."),
@ApiResponse(
    responseCode = "404",
    description = "No customers found."),
@ApiResponse(
    responseCode = "500",
    description = "An unplanned error occured."),
}
)
@Transactional
@GetMapping("/get-customer-uid/{customerUid}")
CustomerReturnedToOwner getCustomerByUid(@RequestParam String customerUid);


//edit customer name
@Operation(
summary = "Updates a customer's name.",
description = """
        Accepts the uid of the customer to be updated as a parameter along with the new name for the customer.
         Returned response is a message as a string that the customers name has been updated.
        For owner use only with proper auth."""
        + "\n" + "\n" + "Example request: " + "\n" +  "\n" +
        "localhost:8080/api/owners-tools/customers/edit-customer-name/09t8-g093/Gus Gusson"
        + "\n" + "\n" + "Example response: " + "\n" +  "\n" +
        "Previous customer name: Bob Bobson, updated to: Gus Gusson.",
responses = {
@ApiResponse(
        responseCode = "200",
        description = "Customer name updated."),
@ApiResponse(
        responseCode = "400",
        description = "Request parameters invalid."),
@ApiResponse(
        responseCode = "404",
        description = "No customer found for given uid."),
@ApiResponse(
        responseCode = "500",
        description = "An unplanned error occured."),
}
)
@Transactional
@PutMapping("/edit-customer-name/{customerUid}/{newCustomerName}")
String updateCustomerName(
@PathVariable
String customerUid,
@PathVariable
String newCustomerName);


//edit customer email
@Operation(
summary = "Updates a customer's email.",
description = """
    Accepts the uid of the customer to be updated as a parameter along with the new email for the customer.
    Returned response is a message as a string that the customers email has been updated.
    For owner use only with proper auth."""
        + "\n" + "\n" + "Example request: " + "\n" +  "\n" +
        "localhost:8080/api/owners-tools/customers/edit-customer-email/09t8-g093/gussy@gus.com"
        + "\n" + "\n" + "Example response: " + "\n" +  "\n" +
        "Previous customer email: bobby@bobert.com, updated to: gussy@gus.com.",
responses = {
@ApiResponse(
        responseCode = "200",
        description = "Customer email updated."),
@ApiResponse(
        responseCode = "400",
        description = "Request parameters invalid."),
@ApiResponse(
        responseCode = "404",
        description = "No customer found according to parameter."),
@ApiResponse(
        responseCode = "500",
        description = "An unplanned error occured."),
}
)
@Transactional
@PutMapping("/edit-customer-email/{customerUid}/{newCustomerEmail}")
String updateCustomerEmail(
@PathVariable
String customerUid,
@PathVariable
String newCustomerEmail);



//edit customer phone number
@Operation(
summary = "Updates a customer's phone number.",
description = """
 Accepts the uid of the customer to be updated as a parameter along with the new phone number for the customer.
 Returned response is a message as a string that the customers phone number has been updated.
For owner use only with proper auth.
"""
        + "\n" + "\n" + "Example request: " + "\n" +  "\n" +
        "localhost:8080/api/owners-tools/customers/edit-customer-phone/gd34-igjr/555.555.5558"
        + "\n" + "\n" + "Example response: " + "\n" +  "\n" +
        "Previous customer phone number: 555.555.5551, updated to: 555.555.5558.",
responses = {
@ApiResponse(
        responseCode = "200",
        description = "Customer phone number updated."),
@ApiResponse(
        responseCode = "400",
        description = "Request parameters invalid."),
@ApiResponse(
        responseCode = "404",
        description = "No customer found according to parameter."),
@ApiResponse(
        responseCode = "500",
        description = "An unplanned error occured."),
}
)
@Transactional
@PutMapping("/edit-customer-phone/{customerUid}/{newCustomerPhone}")
String updateCustomerPhone(
@PathVariable
String customerUid,
@PathVariable
String newCustomerPhone);


// delete customer by uid
@Operation(
summary = "Deletes a customer by uid.",
description = """
        Accepts the id of an existing customer and removes their information from the application permanently.
         Returned response is a message as a string that the customers records have been removed.
         For owner use only with proper auth."""
        + "\n" + "\n" + "Example request: " + "\n" +  "\n" +
        "localhost:8080/api/owners-tools/customers/delete-customer/{customerUid}"
        + "\n" + "\n" + "Example response: " + "\n" +  "\n" +
        "Customer Bob Bobson(09t8-g093), removed from application records.",
responses = {
@ApiResponse(
        responseCode = "200",
        description = "Customer deleted."),
@ApiResponse(
        responseCode = "400",
        description = "Request parameters invalid."),
@ApiResponse(
        responseCode = "404",
        description = "No customer found according to parameter."),
@ApiResponse(
        responseCode = "500",
        description = "An unplanned error occured."),

}
)
@Transactional
@DeleteMapping("/delete-customer/{customerUid}")
String deleteCustomer(@PathVariable String customerUid);
}
