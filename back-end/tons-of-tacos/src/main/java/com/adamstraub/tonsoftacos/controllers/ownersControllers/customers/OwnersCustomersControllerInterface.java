package com.adamstraub.tonsoftacos.controllers.ownersControllers.customers;

import com.adamstraub.tonsoftacos.dto.businessDto.CustomerReturnedToOwner;
import com.adamstraub.tonsoftacos.dto.businessDto.ResponseMessage;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
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

@Transactional
@PutMapping("/update-customer/{customerUid}/{name}/{phone}/{email}")
ResponseMessage updateCustomer(
        @PathVariable
        String customerUid,
        @PathVariable
        String name,
        @PathVariable
        String phone,
        @PathVariable
        String email
        );
}
