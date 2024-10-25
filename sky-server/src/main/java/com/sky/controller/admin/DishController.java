package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 菜品管理
 */
@RestController
@RequestMapping("/admin/dish")
@Api(tags = "拆品相关接口")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    @ApiOperation( value = "新增菜品")
    public Result save(@RequestBody DishDTO dishDTO){
        dishService.saveWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        PageResult pageResult= dishService.pegeQuery(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 菜品批量删除
     * @return
     */
    @DeleteMapping
    @ApiOperation("菜品批量删除")
    private Result delete(@RequestParam List<Long> ids){
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询拆品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    private Result<DishVO> getById(@PathVariable Long id){
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }
}
