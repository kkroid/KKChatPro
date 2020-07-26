package com.kk.imsdk.base;

import com.kk.imsdk.base.type.ConvType;

import java.util.List;


@SuppressWarnings("unused")
public interface IConversation {

    String getId();

    String getIcon();

    String getName();

    List<? extends IUser> getUsers();

    String getLastMsg();

    void setLastMsg(String msg);

    int getUnreadCount();

    long getLastMsgTime();

    ConvType getType();
}
