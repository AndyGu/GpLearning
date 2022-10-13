package com.bard.arouter_api;

import java.util.Map;

/**
 * Group分组的领头 带头大哥
 *
 * TODO
 *  *    key:   app
 *  *    value:  ARouterPath
 */
public interface ARouterGroup {

    /**
     * 例如：order module分组下有一组这些信息，personal module分组下有一组这些信息
     * 例如："order" --- ARouterPath的实现类 -->（APT生成出来的 ARouter$$Path$$order）
     *
     * @return  key:"order/app/personal"      value : 系列的order组下面所有的（path---class）
     */
    Map<String, Class<? extends ARouterPath>> getGroupMap();
}
