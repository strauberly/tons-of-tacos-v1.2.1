package com.adamstraub.tonsoftacos.dto.businessDto;

import lombok.Data;
import java.util.List;

@Data
public class CustomerReturnedToOwner {
    private String customerUid;
    private String name;
    private String email;
    private String phone;
    private List<String> orderIds;
}
