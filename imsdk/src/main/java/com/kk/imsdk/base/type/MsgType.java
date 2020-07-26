package com.kk.imsdk.base.type;

public enum MsgType {
    Text(0),
    Image(1),
    Audio(2),
    Video(3),
    Location(4),
    File(5),
    Event(6),
    Custom(7);

    public final int id;

    MsgType(int id) {
        this.id = id;
    }

    public static MsgType get(int id) {
        for (MsgType msgType : values()) {
            if (msgType.id == id) {
                return msgType;
            }
        }
        return Text;
    }
}
