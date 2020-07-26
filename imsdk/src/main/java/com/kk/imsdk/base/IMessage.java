package com.kk.imsdk.base;

import com.kk.imsdk.base.type.MsgStatus;
import com.kk.imsdk.base.type.MsgType;

import java.util.Map;


@SuppressWarnings("unused")
public interface IMessage {

    /**
     * 获取服务器ID
     */
    String getServerId();

    /**
     * 获取本地ID
     */
    String getLocalId();

    /**
     * Returns message text
     *
     * @return the message text
     */
    String getText();

    /**
     * Returns message sender
     *
     * @return the message sender
     */
    String getSender();

    /**
     * send to
     */
    String getSendTo();

    /**
     * Returns message creation date
     *
     * @return the message creation date
     */
    long getTime();

    /**
     * 获取消息类型
     */
    MsgType getMsgType();

    /**
     * 获取消息状态
     */
    MsgStatus getStatus();

    /**
     * 获取额外信息，以Key-Value形式传入
     */
    Map<String, String> getExtras();
}
