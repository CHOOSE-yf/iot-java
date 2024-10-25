package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /**
     * 切入点
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){

    }
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        //获取当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//获取方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获取方法上的注解对象
        OperationType operationType = autoFill.value();//获取数据库操作类型

        //获取当前被拦截的方法参数--实体对象
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0){
            return;
        }
        Object enticy  = args[0];
        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        //根据当前不同操作类型，为对应的属性反射来赋值
        if (operationType==OperationType.INSERT){
            //为4个公共字段赋值
            try {
                Method setCreateTime = enticy.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = enticy.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = enticy.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = enticy.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setCreateTime.invoke(enticy,now);
                setCreateUser.invoke(enticy,currentId);
                setUpdateTime.invoke(enticy,now);
                setUpdateUser.invoke(enticy,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else if ( operationType == OperationType.UPDATE){
            //为两个公共字段赋值
            try {

                Method setUpdateTime = enticy.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateUser = enticy.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                //通过反射为对象属性赋值
                setUpdateTime.invoke(enticy,now);
                setUpdateUser.invoke(enticy,currentId);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}