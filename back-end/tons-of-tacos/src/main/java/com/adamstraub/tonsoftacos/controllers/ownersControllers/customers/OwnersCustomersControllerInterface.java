package com.adamstraub.tonsoftacos.controllers.ownersControllers.customers;

import com.adamstraub.tonsoftacos.dto.businessDto.CustomerReturnedToOwner;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RequestMapping(
        value = "api/owners-tools/customers")
@OpenAPIDefinition(info = @Info(title = "Services pertaining to functions reserved for the owners of tons of tacos."),
        servers = {@Server(url = "http://localhost:8080", description = "Local server")})

public interface OwnersCustomersControllerInterface {


    //edit customer name
    @Operation(
            summary = "Updates a customer's name.",
            description = """
                    Accepts the uid of the customer to be updated as a parameter along with the new name for the customer.
                     Returned response is a message as a string that the customers name has been updated.
                    For owner use only with proper auth."""
                    + "\n" + "\n" + "Example request: " + "\n" + "\n" +
                    "localhost:8080/api/owners-tools/customers/edit-customer-name/09t8-g093/Gus Gusson"
                    + "\n" + "\n" + "Example response: " + "\n" + "\n" +
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
    ResponseEntity<ResponseMessage> updateCustomerName(
            @PathVariable
            String customerUid,
            @PathVariable
            String newCustomerName);


//    //edit customer email
    @Operation(
            summary = "Updates a customer's email.",
            description = """
                    Accepts the uid of the customer to be updated as a parameter along with the new email for the customer.
                    Returned response is a message as a string that the customers email has been updated.
                    For owner use only with proper auth."""
                    + "\n" + "\n" + "Example request: " + "\n" + "\n" +
                    "localhost:8080/api/owners-tools/customers/edit-customer-email/09t8-g093/gussy@gus.com"
                    + "\n" + "\n" + "Example response: " + "\n" + "\n" +
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
    ResponseEntity<ResponseMessage> updateCustomerEmail(
            @PathVariable
            String customerUid,
            @PathVariable
            String newCustomerEmail);
//
//
//    //edit customer phone number
//    @Operation(
//            summary = "Updates a customer's phone number.",
//            description = """
//                     Accepts the uid of the customer to be updated as a parameter along with the new phone number for the customer.
//                     Returned response is a message as a string that the customers phone number has been updated.
//                    For owner use only with proper auth.
//                    """
//                    + "\n" + "\n" + "Example request: " + "\n" + "\n" +
//                    "localhost:8080/api/owners-tools/customers/edit-customer-phone/gd34-igjr/555.555.5558"
//                    + "\n" + "\n" + "Example response: " + "\n" + "\n" +
//                    "Previous customer phone number: 555.555.5551, updated to: 555.555.5558.",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "Customer phone number updated."),
//                    @ApiResponse(
//                            responseCode = "400",
//                            description = "Request parameters invalid."),
//                    @ApiResponse(
//                            responseCode = "404",
//                            description = "No customer found according to parameter."),
//                    @ApiResponse(
//                            responseCode = "500",
//                            description = "An unplanned error occured."),
//            }
//    )
//    @Transactional
//    @PutMapping("/edit-customer-phone/{customerUid}/{newCustomerPhone}")
//    String updateCustomerPhone(
//            @PathVariable
//            String customerUid,
//            @PathVariable
//            String newCustomerPhone);

//
////edit customer customer
//@Operation(
//        summary = "Update customer contact info",
//        description = """
//                    Accepts the uid of the customer to be updated as a parameter along with all necessary data."""
//                + "\n" + "\n" + "Example request: " + "\n" + "\n" +
//                "localhost:8080/api/owners-tools/customers/edit-customer-email/09t8-g093/gussy@gus.com"
//                + "\n" + "\n" + "Example response: " + "\n" + "\n" +
//                "Previous customer email: bobby@bobert.com, updated to: gussy@gus.com.",
//        responses = {
//                @ApiResponse(
//                        responseCode = "200",
//                        description = "Customer email updated."),
//                @ApiResponse(
//                        responseCode = "400",
//                        description = "Request parameters invalid."),
//                @ApiResponse(
//                        responseCode = "404",
//                        description = "No customer found according to parameter."),
//                @ApiResponse(
//                        responseCode = "500",
//                        description = "An unplanned error occured."),
//        }
//)
////@Transactional
////@PutMapping("/edit-customer-name/{customerUid}/{newCustomerName}")
////ResponseEntity<String> updateCustomerName(
////        @PathVariable
////        String customerUid,
////        @PathVariable
////        String newCustomerName);
////@Value("${origin}")
//// String origin;
////@CrossOrigin(origins = "http://localhost:3000")
//@Transactional
//@PutMapping("/update-customer/{customerUid}/{name}/{phone}/{email}")
//ResponseMessage updateCustomer(
//        @PathVariable
//        String customerUid,
//        @PathVariable
//        String name,
//        @PathVariable
//        String phone,
//        @PathVariable
//        String email
//        );


//    // delete customer by uid
//    @Operation(
//            summary = "Deletes a customer by uid.",
//            description = """
//                    Accepts the id of an existing customer and removes their information from the application permanently.
//                     Returned response is a message as a string that the customers records have been removed.
//                     For owner use only with proper auth."""
//                    + "\n" + "\n" + "Example request: " + "\n" + "\n" +
//                    "localhost:8080/api/owners-tools/customers/delete-customer/{customerUid}"
//                    + "\n" + "\n" + "Example response: " + "\n" + "\n" +
//                    "Customer Bob Bobson(09t8-g093), removed from application records.",
//            responses = {
//                    @ApiResponse(
//                            responseCode = "200",
//                            description = "Customer deleted."),
//                    @ApiResponse(
//                            responseCode = "400",
//                            description = "Request parameters invalid."),
//                    @ApiResponse(
//                            responseCode = "404",
//                            description = "No customer found according to parameter."),
//                    @ApiResponse(
//                            responseCode = "500",
//                            description = "An unplanned error occured."),
//
//            }
//    )
//    @Transactional
//    @DeleteMapping("/delete-customer/{customerUid}")
//    String deleteCustomer(@PathVariable String customerUid);
}
