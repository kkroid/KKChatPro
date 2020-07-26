package com.kk.chatlist.mvp.model.entity;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class ChatItem {
    @Id
    public long id;
    public String avatar;
    public String name;
    public String lastMsg;
    public int unreadCount;
    public long lastMsgTime;
}
