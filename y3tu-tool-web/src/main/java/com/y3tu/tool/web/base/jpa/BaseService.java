package com.y3tu.tool.web.base.jpa;


public interface BaseService<T> {

    /**
     * 分页查询
     *
     * @param page 分页信息 包含每页多少条,当前页数,排序,查询条件
     * @return 分页查询数据
     */
    Page<T> page(Page<T> page);

}
