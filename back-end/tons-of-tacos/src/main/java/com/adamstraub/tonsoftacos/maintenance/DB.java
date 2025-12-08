package com.adamstraub.tonsoftacos.maintenance;

import com.adamstraub.tonsoftacos.dao.OrdersRepository;
import com.adamstraub.tonsoftacos.entities.Orders;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DB {
    @Autowired
    OrdersRepository ordersRepository;

    public  void dailyScalesScrub() throws ParseException {
        List<Orders> orders = ordersRepository.findByClosed();
        System.out.println("closed orders: " + orders);
        Date closedDate;
        Date today = Date.from(Instant.now());

        DateFormat dateFormat = new SimpleDateFormat("MMM-dd-yy");
        for(Orders order: orders){
            closedDate = dateFormat.parse(order.getClosed());



            Long convertedClosed = closedDate.getTime();
            Long convertedToday = today.getTime();

            if( convertedToday >  convertedClosed){
                ordersRepository.deleteById(order.getOrderId());
            }
        }
        System.out.println("orders scrubbed: " +  ordersRepository.findAll().size()+ " : " +orders.size() );

    }
}
