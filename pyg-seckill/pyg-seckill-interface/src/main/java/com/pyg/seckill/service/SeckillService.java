package com.pyg.seckill.service;

import com.pyg.utils.PygResult;

/**
 * @Title SeckillService
 * @ProjectName pyg
 * @Description
 * @Author Hello.Ju
 * @Date 2019-11-01 10:12
 */
public interface SeckillService {
    PygResult executeSeckill(Long goodsId, Long userId, String md5);
}
