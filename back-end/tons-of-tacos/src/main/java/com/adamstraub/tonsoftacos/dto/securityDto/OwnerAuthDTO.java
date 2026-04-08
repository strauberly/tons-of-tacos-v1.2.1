package com.adamstraub.tonsoftacos.dto.securityDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerAuthDTO {
    private String username;
    private String psswrd;
}

