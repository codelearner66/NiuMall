package com.hechi;

import com.hechi.niumall.NiuMallApplication;
import com.hechi.niumall.entity.GoodsCategory;
import com.hechi.niumall.service.GoodsCategoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@SpringBootTest(classes = NiuMallApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class testapp {
    @Autowired
    GoodsCategoryService goodsCategoryService;

    @Test
    public void test01(){
        List<GoodsCategory> list = goodsCategoryService.list();
        list.forEach(System.out::println);
    }
}
