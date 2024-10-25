package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface DishService {


    /**
     * 新增菜品和口味数据
     *
     * @param dishDTO
     */
    public void saveWithFlavor(DishDTO dishDTO);


    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pegeQuery(DishPageQueryDTO dishPageQueryDTO);

    /**菜品批量删除
     *
     * @param ids
     */
    void deleteBatch(List<Long> ids);
}