package com.hechi.niumall.utils;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象
        V v = null;
        try {
            //获取构造方法无参
            Constructor<V> constructor = clazz.getConstructor();
            //通过无参构造生成类
             v = constructor.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return v;
    }
    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }
    public static void main(String[] args) {

    }
}
