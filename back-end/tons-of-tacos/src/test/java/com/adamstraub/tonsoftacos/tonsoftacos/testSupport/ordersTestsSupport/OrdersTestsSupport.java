package com.adamstraub.tonsoftacos.tonsoftacos.testSupport.ordersTestsSupport;
import com.adamstraub.tonsoftacos.services.ordersServices.OrdersService;
import com.adamstraub.tonsoftacos.tonsoftacos.testSupport.TestUris;
import org.springframework.http.*;

public class OrdersTestsSupport extends TestUris {
    OrdersService ordersService;

    protected String validOrderBody() {
        return """
                {
                    "customer": {
                        "name": "billy billson",
                        "email": "billy@bolly.com",
                        "phoneNumber": "555.555.5959"
                    },
                    "order": {
                        "orderUid": "223-44-444",
                        "orderItems": [
                            {
                                "item": {
                                    "id": 2
                                },
                                "quantity": 2
                            },
                            {
                                "item": {
                                    "id": 12
                                },
                                "quantity": 1
                            },
                            {
                                "item": {
                                    "id": 3
                                },
                                "quantity": 3
                            }
                        ]
                    }
                }
                        
                """;
    }


    protected String genUidBody() {

        return """
                {
                    "customer": {
                        "name": "billy billson",
                        "email": "billy@bolly.com",
                        "phoneNumber": "555.555.5959"
                    },
                    "order": {
                        "orderItems": [
                            {
                                "item": {
                                    "id": 2
                                },
                                "quantity": 2
                            },
                            {
                                "item": {
                                    "id": 12
                                },
                                "quantity": 1
                            },
                            {
                                "item": {
                                    "id": 3
                                },
                                "quantity": 3
                            }
                        ]
                    }
                }
                """;

    }


    protected String noOrderBody() {
        return """
                {
                    "customer": {
                        "name": "bob smith",
                        "email": "billy@bolly.io",
                        "phoneNumber": "555-555-5999"
                    },
                    "order": {
                    }
                }
                """;
    }


    protected String improperlyFormattedOrder() {
        return """
                {
                    "customer": {
                        "name": "bobsmith",
                        "email": "billy@bolly.io",
                        "phoneNumber": "555-555-5999"
                    },
                    "order": {
                        "orderItems": [
                            {
                                "itemId": {
                                    "id": 2
                                },
                                "quantity": 2
                            },
                            {
                                "itemId": {
                                    "id": 12
                                },
                                "quantity": 1
                            },
                            {
                                "itemId": {
                                    "id": 3
                                },
                                "quantity": 3
                            }
                        ]
                    }
                }
                """;
    }



        protected String validCredentials(){
            return """
               {
               "username": "jcast22",
               "psswrd": "tacoocat"
               }""";
        }

        protected String validToken(){
            String body = validCredentials();
            System.out.println(body);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> tokenEntity = new HttpEntity<>(body, headers);

            String uri = getBaseUriForOwnersLogin();
            ResponseEntity<String> response = getRestTemplate().exchange(uri, HttpMethod.POST, tokenEntity,
                    String.class);
            return response.getBody();
        }

    protected String encryptedCredentials(){
        return """
               {
               "username":"m)Km7y{f0~nd$,hvNLOw0.F5FlP5u?5",
               "psswrd":"?aNwlfCd7glf(E&r)lLr}W?fT#Ld?aFw_ic"
               }""";
    }

    protected String encryptedToken(){
        String body = encryptedCredentials();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> tokenEntity = new HttpEntity<>(body, headers);

        String uri = getBaseUriForOwnersLogin();
        ResponseEntity<String> response = getRestTemplate().exchange(uri, HttpMethod.POST, tokenEntity,
                String.class);
        return response.getBody();
    }
    }

