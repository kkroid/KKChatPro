package com.kk.imsdk.base.type;


/**
 * 修改时不要修改ID的对应关系，否则会和现有数据冲突
 */
public enum ConvType {

    Single(0),
    Group(1),
    Room(2);

    public final int id;

    ConvType(int id) {
        this.id = id;
    }
}
