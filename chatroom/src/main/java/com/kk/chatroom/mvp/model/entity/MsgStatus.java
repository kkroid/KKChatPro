package com.kk.chatroom.mvp.model.entity;

public enum MsgStatus {
    Sending(0),
    Sent(1),
    SendFailed(2),
    Read(3),
    Recalled(4);

    public final int id;

    MsgStatus(int id) {
        this.id = id;
    }

    public static MsgStatus get(int id) {
        for (MsgStatus status : values()) {
            if (status.id == id) {
                return status;
            }
        }
        return Sent;
    }
}
