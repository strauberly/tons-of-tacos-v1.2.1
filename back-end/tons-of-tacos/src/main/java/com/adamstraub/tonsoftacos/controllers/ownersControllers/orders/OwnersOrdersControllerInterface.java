package com.adamstraub.tonsoftacos.controllers.ownersControllers.orders;
import com.adamstraub.tonsoftacos.dto.businessDto.DailySales;
import com.adamstraub.tonsoftacos.dto.businessDto.OrderReturnedToOwner;
import com.adamstraub.tonsoftacos.entities.OrderItem;
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
        value = "api/owners-tools/orders")
@OpenAPIDefinition(info = @Info(title = "Services pertaining to functions reserved for the owners of tons of tacos."),
        servers = {@Server(url="http://localhost:8080", description = "Local server")})
public interface OwnersOrdersControllerInterface {

//    get all orders
    @Operation(
            summary = "All orders returned.",
            description = """
                   Returns an array containing all orders.
                   For owner use only with proper auth.
                   """
            + "\n" + "Example array: " + "\n" +
                    """
                            
                            [
                                {
                                    "orderId": 1,
                                    "name": "Tim Timson",
                                    "email": "tim@timson.com",
                                    "phone": "555.555.5553",
                                    "orderUid": "654654-465465-555",
                                    "orderItems": [
                                        {
                                            "orderItemId": 3,
                                            "itemName": "cola",
                                            "quantity": 2,
                                            "total": 2.0
                                        },
                                        {
                                            "orderItemId": 8,
                                            "itemName": "cola",
                                            "quantity": 3,
                                            "total": 3.0
                                        },
                                        {
                                            "orderItemId": 9,
                                            "itemName": "cola",
                                            "quantity": 3,
                                            "total": 3.0
                                        }
                                    ],
                                    "orderTotal": 40.55,
                                    "created": "2023-08-05T23:54:52.000+00:00",
                                    "ready": "no",
                                    "closed": "no"
                                },
                                {
                                    "orderId": 2,
                                    "name": "John Johnson",
                                    "email": "john@johnson.com",
                                    "phone": "555.555.5552",
                                    "orderUid": "654654-4655-555",
                                    "orderItems": [
                                        {
                                            "orderItemId": 1,
                                            "itemName": "pound",
                                            "quantity": 3,
                                            "total": 3.0
                                        },
                                        {
                                            "orderItemId": 2,
                                            "itemName": "golden pound",
                                            "quantity": 4,
                                            "total": 4.0
                                        }
                                    ],
                                    "orderTotal": 25.55,
                                    "created": "2023-08-05T23:54:52.000+00:00",
                                    "ready": "no",
                                    "closed": "no"
                                },
                                {
                                    "orderId": 3,
                                    "name": "John Johnson",
                                    "email": "john@johnson.com",
                                    "phone": "555.555.5552",
                                    "orderUid": "654654-4657-555",
                                    "orderItems": [
                                        {
                                            "orderItemId": 4,
                                            "itemName": "cola",
                                            "quantity": 3,
                                            "total": 1.5
                                        }
                                    ],
                                    "orderTotal": 10.0,
                                    "created": "2023-08-05T23:54:52.000+00:00",
                                    "ready": "no",
                                    "closed": "no"
                                }
                            ]
                            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "All orders returned."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No orders found."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occured."),
            }
    )
    @Transactional
    @GetMapping("/get-orders")
    List<OrderReturnedToOwner> getAllOrders();


//get an order by uid
    @Operation(
            summary = "An order is returned by its uid.",
            description = """
                    When a customer creates an order a uid is generated. This end point allows for a customer to repeat the
                    uid to the owner and have the order returned with out exposing the order id used in backend.  For owner use only with proper auth.
                    """
                    + "\n" +
                    "Example request: localhost:8080/api/owners-tools/orders/get-order/orderUid?orderUid=654654-465465-555"
                    + "\n"
                    + "\n" + "Example response: " + "\n" +

                    """
                              {
                                "name": "Tim Timson",
                                "email": "tim@timson.com",
                                "phone": "555.555.5553",
                                "orderUid": "654654-465465-555",
                                "orderItems": [
                                    {
                                        "orderItemId": 3,
                                        "itemName": "cola",
                                        "quantity": 2,
                                        "total": 2.0
                                    },
                                    {
                                        "orderItemId": 8,
                                        "itemName": "cola",
                                        "quantity": 3,
                                        "total": 3.0
                                    },
                                    {
                                        "orderItemId": 9,
                                        "itemName": "cola",
                                        "quantity": 3,
                                        "total": 3.0
                                    }
                                ],
                                "orderTotal": 40.55,
                                "created": "2023-08-05T23:54:52.000+00:00",
                                "ready": "no",
                                "closed": "no"
                            }
                            """
            ,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order is returned."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No orders found."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occured."),
            }
    )
    @Transactional
    @GetMapping("/get-order-uid/{orderUid}")
    OrderReturnedToOwner getOrderByUid(@RequestParam String orderUid);



//get an order by customer name
    @Operation(
            summary = "Orders returned by customer name.",
            description = """ 
                 An array of open orders are returned to an owner by customer name.
                 For owner use only with proper auth."""
            + "\n" + "\n" + "Example response: " + "\n" + "\n"
            + """
                    [
                        {
                            "name": "John Johnson",
                            "email": "john@johnson.com",
                            "phone": "555.555.5552",
                            "orderUid": "654654-4655-555",
                            "orderItems": [
                                {
                                    "orderItemId": 1,
                                    "itemName": "pound",
                                    "quantity": 3,
                                    "total": 3.0
                                },
                                {
                                    "orderItemId": 2,
                                    "itemName": "golden pound",
                                    "quantity": 4,
                                    "total": 4.0
                                }
                            ],
                            "orderTotal": 25.55,
                            "created": "2023-08-05T23:54:52.000+00:00",
                            "ready": "no",
                            "closed": "no"
                        },
                        {
                            "name": "John Johnson",
                            "email": "john@johnson.com",
                            "phone": "555.555.5552",
                            "orderUid": "654654-4657-555",
                            "orderItems": [
                                {
                                    "orderItemId": 4,
                                    "itemName": "cola",
                                    "quantity": 3,
                                    "total": 1.5
                                }
                            ],
                            "orderTotal": 10.0,
                            "created": "2023-08-05T23:54:52.000+00:00",
                            "ready": "no",
                            "closed": "no"
                        }
                    ]
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Open orders for customer returned."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No orders found."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occured."),
            }
    )
    @Transactional
    @GetMapping("/get-order-customer/{customer}")
   List <OrderReturnedToOwner> getOpenOrderByCustomer(@RequestParam String customer);



//mark food ready by uid
    @Operation(
            summary = "Marks an order by its uid as having food ready for pick up.",
            description = """
                    A successful request will return a json object containing details of the order for the customer and the
                    the ready property will now contain the time when the order was marked as ready. For owner use only with proper auth.
                    """ + "\n" +"Example object :" + "\n" +
                    """
                            {
                                "orderId": 4,
                                "name": "bill billson",
                                "email": "billy@bolly.com",
                                "phone": "555.555.5959",
                                "orderUid": "NP0DQ",
                                "orderItems": [
                                    {
                                        "orderItemId": 5,
                                        "itemName": "golden pound",
                                        "quantity": 2,
                                        "total": 10.6
                                    },
                                    {
                                        "orderItemId": 6,
                                        "itemName": "iced tea",
                                        "quantity": 1,
                                        "total": 2.0
                                    },
                                    {
                                        "orderItemId": 7,
                                        "itemName": "cola",
                                        "quantity": 3,
                                        "total": 3.0
                                    }
                                ],
                                "orderTotal": 15.6,
                                "created": "2023-08-22T13:42:46.000+00:00",
                                "ready": "2023-08-25 07:11:52",
                                "closed": "no"
                            }
                            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order ready."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No order-items found according to input."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occured."),

            }
    )
    @Transactional
    @PutMapping("/order-ready/{orderUid}")
    OrderReturnedToOwner orderReady(@PathVariable String orderUid);


//close order by uid
    @Operation(
            summary = "Closes an order by its uid.",
            description = """
            Allows for marking an order as closed once payment received and food has been picked up by customer.
            A successful request returns an updated order object with the closed field containing the time an order was marked as closed. For owner use only with proper auth.
            """ + "\n"
                    +"Example object :" + "\n" +
                    """
                            {
                                "orderId": 4,
                                "name": "bill billson",
                                "email": "billy@bolly.com",
                                "phone": "555.555.5959",
                                "orderUid": "NP0DQ",
                                "orderItems": [
                                    {
                                        "orderItemId": 5,
                                        "itemName": "golden pound",
                                        "quantity": 2,
                                        "total": 10.6
                                    },
                                    {
                                        "orderItemId": 6,
                                        "itemName": "iced tea",
                                        "quantity": 1,
                                        "total": 2.0
                                    },
                                    {
                                        "orderItemId": 7,
                                        "itemName": "cola",
                                        "quantity": 3,
                                        "total": 3.0
                                    }
                                ],
                                "orderTotal": 15.6,
                                "created": "2023-08-22T13:42:46.000+00:00",
                                "ready": "2023-08-25 07:11:52",
                                "closed": "2023-08-25 07:26:16"
                            }
                            """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order closed."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No order-items found according to input."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occurred."),

            }
    )
    @Transactional
    @PutMapping("/close-order/{orderUid}")
    OrderReturnedToOwner closeOrder(@PathVariable String orderUid);


    //delete order by uid
    @Operation(
            summary = "Deletes an order by its uid.",
            description = " For owner use only with proper auth.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Order deleted."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No order-items found according to input."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occurred."),

            }
    )
    @Transactional
    @DeleteMapping("/delete-order/{orderUid}")
    String deleteOrder(@PathVariable String orderUid);

//add menu item to order
    @Operation(
            summary = "A menu item is added to an open order by respective uid and id.",
            description = "If request is successful then a message string is returned indicating as such (ex. 'cola x 3 added to order.')." +
                    " For owner use only with proper auth.",
            responses = {

                    @ApiResponse(
                            responseCode = "200",
                            description = "A menu item is added to an order.",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = OrderItem.class))),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Either the order to be altered or the menu item to be altered can not be found and is invalid."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occurred."),
            }
    )
    @Transactional
    @PutMapping("/add-to-order/{orderUid}/{menuItemId}/{quantity}")
    String addToOrder(
    @PathVariable
    String orderUid,
    @PathVariable
    Integer menuItemId,
    @PathVariable
    Integer quantity);

//edit order item
    @Operation(
            summary = " Updates an order item quantity and corresponding order total.",
            description = """ 
                    Updates the quantity of an item in an order and if quantity equals zero the item is removed from order.
                     The order total is updated at the end of each successful request. A successful request returns a message as a string that the 
                     update was successful (ex.'Item quantity updated, cart updated.').  For owner use only with proper auth.""",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Quantity updated."),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Request parameters invalid."),
                    @ApiResponse(
                            responseCode = "404",
                            description = "No order-items found according to input. Or no order found for submitted order id."),
                    @ApiResponse(
                            responseCode = "500",
                            description = "An unplanned error occurred."),
            }
    )
    @PutMapping("/update-order-item/{orderUid}/{orderItemId}/{newQuantity}")
    String updateOrderItemQuantity(
    @PathVariable
    String orderUid,
    @PathVariable
    Integer orderItemId,
    @PathVariable
    Integer newQuantity);


//get todays sales
    @Operation(
            summary = "Calculates and returns sales for today's closed orders.",
            description = """
                    A successful request will return a sales object with fields indicating today's date,
                     the number of sales for the day, and the total amount accrued for the day. For owner use only with proper auth.
                    """ +
             "\n" +
                    "Example object :" + "\n" +
                    """
                                            {
                                                "date": "2023-08-28",
                                                "numberOfSales": 2,
                                                "total": 22.35
                                            }
                            """,
responses = {
    @ApiResponse(
            responseCode = "200",
            description = "Order deleted."),
    @ApiResponse(
            responseCode = "400",
            description = "Request parameters invalid."),
    @ApiResponse(
            responseCode = "404",
            description = "No order-items found according to input."),
    @ApiResponse(
            responseCode = "500",
            description = "An unplanned error occurred."),

}
)
@GetMapping("/sales")
DailySales todaysSales();

}
