package com.adamstraub.tonsoftacos.dao;


import com.adamstraub.tonsoftacos.dto.categoryDto.ReturnedCategory;
import com.adamstraub.tonsoftacos.entities.Category;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByAvailableLike(Character available);



    default List<ReturnedCategory> getByAvailable() {
        Iterable<Category> iterable = findByAvailableLike('y');
        return StreamSupport.stream(iterable.spliterator(), false).map(category -> {
            ReturnedCategory dto = new ReturnedCategory();
            BeanUtils.copyProperties(category, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
