package com.adamstraub.tonsoftacos.services.utilityService.salesService;

import com.adamstraub.tonsoftacos.dto.businessDto.DailySalesDTO;

import com.adamstraub.tonsoftacos.entities.OrderItem;
import com.adamstraub.tonsoftacos.entities.Orders;
import com.adamstraub.tonsoftacos.repository.OrdersRepository;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class SalesService {
    @Autowired
    private OrdersRepository ordersRepository;


    public String largestSale(){
        Orders order = ordersRepository.findTopByOrderByOrderTotalDesc();
        String orderId = order.getOrderUid();
        int itemCount = order.getOrderItems().size();

        DecimalFormat df = new DecimalFormat("#.00");
        String formattedTotal = df.format(order.getOrderTotal());

        String orderItemList = getItemList(order);
        String template = """
                <p>id =  {0}</p>
                <p>number of items = {1}</p>
                <p> items = {2}</p>
                <p> order total = {3}</p>
                """;
        return MessageFormat.format(template, orderId, itemCount, orderItemList, formattedTotal);
    }

    private static @NotNull String getItemList(Orders order) {
        List<OrderItem> items = order.getOrderItems();
        String orderItemString;
        String orderItemList = "";
        for(OrderItem orderItem : items){
        orderItemString = orderItem.getItem().getItemName()
                + ", size: "
                + orderItem.getSize()
                + ", qauntity: "
                + orderItem.getQuantity() + "; ";
        orderItemList = orderItemList.concat(orderItemString);
        }

        orderItemList = orderItemList.substring(0, orderItemList.length()-2);
        return orderItemList;
    }

    public String ordersStats(){
        List<Orders> orders = ordersRepository.findAll();
        List<Orders> openOrders = new ArrayList<>();
        List<Orders> readyOrders = new ArrayList<>();
        List<Orders> closedOrders = new ArrayList<>();

        for(Orders order: orders){
            if (!order.getClosed().equals("no")){
                closedOrders.add(order);
            } else if (!order.getReady().equals("no")) {
                readyOrders.add(order);
            }else{
                openOrders.add(order);
            }
        }
        int openEndOfDay = openOrders.size();
        int readyEndOfDay = readyOrders.size();
        int closedEndOfDay = closedOrders.size();

        String template = """
                <p>orders left open at end of day = {0}</p>
                <p>orders left ready at end of the day = {1}</p>
                <p>orders closed = {2}</p>
                """;
        return MessageFormat.format(template, openEndOfDay, readyEndOfDay, closedEndOfDay);
    }


    @Transactional
    public List<Orders> findClosedOrders(){
        List<Orders> closedOrders = new ArrayList<>();
        List<Orders> orders = ordersRepository.findAll();
        System.out.println(orders);
        for (Orders order: orders){
            if (!order.getClosed().equals("no")){
                closedOrders.add(order);
            }
        }
        return closedOrders;
    }

    public BigDecimal calcItemPriceWithSize(Integer quantity, String size, BigDecimal unitPrice){
        BigDecimal sizeSurcharge = BigDecimal.ZERO;
        BigDecimal adjPrice;
        if (size.equalsIgnoreCase("M")) {
            sizeSurcharge = BigDecimal.valueOf(0.5);
        } else if (size.equalsIgnoreCase( "L")) {
            sizeSurcharge = BigDecimal.valueOf(1.0);
        }
        adjPrice = BigDecimal.valueOf(quantity).multiply(unitPrice.add(sizeSurcharge));
        return adjPrice;
    }

        @Transactional
    public ResponseEntity<DailySalesDTO> salesToday() {
        System.out.println("service");
        DailySalesDTO salesToday = new DailySalesDTO();
        String formattedSales;
        LocalDate todaysDate = LocalDate.now();
        LocalDate dbDate;
        DateTimeFormatter closedDateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy hh:mm a");
        BigDecimal salesTotal = BigDecimal.valueOf(0.00);
        List<Orders> todaysOrders = new ArrayList<>();

        try {
            findClosedOrders();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

//        find orders by if they closed meaning transaction complete
        List<Orders> completedOrders = findClosedOrders();
        for (Orders completedOrder: completedOrders){
            LocalDateTime closedDateTime = LocalDateTime.parse(completedOrder.getClosed(), closedDateFormatter);
            LocalDate closed = closedDateTime.toLocalDate();
            dbDate = completedOrder.getCreated().toLocalDateTime().toLocalDate();
            if(closed.equals(dbDate)){
                todaysOrders.add(completedOrder);
            }
        }
//                looks for all orders with today's timestamp
        for (Orders order:todaysOrders){
            salesTotal = salesTotal.add(new BigDecimal(String.valueOf(order.getOrderTotal())));
        }

        salesToday.setDate(todaysDate);
        salesToday.setNumberOfSales(todaysOrders.size());
        salesToday.setTotal(salesTotal);

        return ResponseEntity.ok(salesToday);
    }
}
