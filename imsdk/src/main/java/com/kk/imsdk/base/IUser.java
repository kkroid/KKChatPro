package com.kk.imsdk.base;

/**
 * For implementing by real user model
 */
@SuppressWarnings("unused")
public interface IUser {

    /**
     * ID
     */
    String getId();

    /**
     * 用户名
     */
    String getUserName();

    /**
     * 昵称
     */
    String getNickName();

    /**
     * 备注名
     */
    String getNoteName();

    /**
     * 头像
     */
    String getAvatar();
}
