package com.raving.ebsystem.modular.system.service;

/**
 * 字典服务
 */
public interface IDictService {

    /**
     * 添加字典
     */
    void addDict(String dictName, String dictValues);

    void editDict(Integer dictId, String dictName, String dicts);

    /**
     * 编辑字典
    void editDict(Integer dictId, String dictName, String dicts);

    /**
     * 删除字典
     */
    void delteDict(Integer dictId);

}
