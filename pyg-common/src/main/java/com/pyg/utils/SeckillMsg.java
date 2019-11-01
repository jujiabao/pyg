package com.pyg.utils;

import java.io.Serializable;

/**
 * @Title SeckillMsg
 * @ProjectName pyg
 * @Description 秒杀消息通知实体
 * @Author Hello.Ju
 * @Date 2019-11-01 10:09
 */
public class SeckillMsg implements Serializable {
    private Long goodsId;
    private Long userId;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "SeckillMsg{" +
                "goodsId=" + goodsId +
                ", userId=" + userId +
                '}';
    }
}
