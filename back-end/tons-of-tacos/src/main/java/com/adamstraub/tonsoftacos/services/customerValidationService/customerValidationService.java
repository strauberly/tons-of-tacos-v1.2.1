package com.adamstraub.tonsoftacos.services.customerValidationService;

import com.adamstraub.tonsoftacos.dto.customerDto.Customer.Customer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Service
public class customerValidationService {

    private boolean customerNameValid = false;
    private boolean customerPhoneNumberValid = false;
    private boolean customerEmailValid = false;
    public boolean validateCustomerName(String customerName) {
        try {
            byte[] nameChars = customerName.getBytes(StandardCharsets.UTF_8);
            int spaces = 0;
            for (Byte nameChar : nameChars) {
                if (Objects.equals(nameChar, (byte) 32)) {
                    spaces += 1;
                }
            }
//        possibly alter for just ^[a-zA-Z]$+ [a-zA-Z]+. currently accepting letters from any language.
            if (customerName.matches("^\\p{L}+[\\p{L}\\p{Pd}\\p{Zs}']*\\p{L}+$|^\\p{L}+$") &&
                    spaces == 1) {
                customerNameValid = true;
            }
            if (!customerNameValid) {
                throw new IllegalArgumentException("Customer name incorrectly formatted. Please consult the documentation.");
            }
        } catch (Exception e) {
            log.error("error: ", e);
        }
        return customerNameValid;
    }

    public boolean validateCustomerPhone(String customerPhone){
        try{
            if (customerPhone.matches("[0-9.]*")
                    && customerPhone.charAt(3) == (char) 46
                    && customerPhone.charAt(7) == (char) 46
                    && customerPhone.length()==12){
                customerPhoneNumberValid = true;
            }
            if(!customerPhoneNumberValid) {
                throw new IllegalArgumentException("Customer phone number incorrectly formatted. Please consult the documentation.");
            }
        } catch (Exception e) {
            log.error("error: " , e);
        }
        return customerPhoneNumberValid;
    }

    public boolean validateCustomerEmail(String customerEmail){
        try{
            if (customerEmail.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,}")) customerEmailValid = true;
            if (!customerEmailValid){
                throw new IllegalArgumentException("Customer e-mail incorrectly formatted. Please consult the documentation.");
            }
        } catch (Exception e) {
            log.error("error: " , e);
        }
        return customerEmailValid;
    }
}
