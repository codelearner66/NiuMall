package com.hechi.niumall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hechi.niumall.entity.GoodsCategory;
import com.hechi.niumall.mapper.GoodsCategoryMapper;
import com.hechi.niumall.service.GoodsCategoryService;
import org.springframework.stereotype.Service;

/**
 * niumall商城-商品分类(GoodsCategory)表服务实现类
 *
 * @author ccx
 * @since 2022-07-14 21:48:55
 */
@Service("goodsCategoryService")
public class GoodsCategoryServiceImpl extends ServiceImpl<GoodsCategoryMapper, GoodsCategory> implements GoodsCategoryService {

}
