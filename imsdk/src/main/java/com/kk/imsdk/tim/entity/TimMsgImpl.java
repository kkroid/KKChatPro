package com.kk.imsdk.tim.entity;

import com.kk.imsdk.base.IMessage;
import com.kk.imsdk.base.type.MsgStatus;
import com.kk.imsdk.base.type.MsgType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMTextElem;

import java.util.Map;

public class TimMsgImpl implements IMessage {

    private TIMMessage mRealMessage;
    private TIMElem mFirstElem;
    private TIMElemType mFirstElemType;

    private TimMsgImpl(TIMMessage realMessage) {
        mRealMessage = realMessage;
        mFirstElem = realMessage.getElement(0);
        mFirstElemType = mFirstElem.getType();
    }

    public static TimMsgImpl from(TIMMessage realMessage) {
        return new TimMsgImpl(realMessage);
    }

    @Override
    public String getServerId() {
        return String.valueOf(mRealMessage.getMsgUniqueId());
    }

    @Override
    public String getLocalId() {
        return mRealMessage.getMsgId();
    }

    @Override
    public String getText() {
        switch (mFirstElemType) {
            case Text:
                TIMTextElem textElem = (TIMTextElem) mFirstElem;
                return textElem.getText();
            case Image:
                return "[图片]";
            case Location:
                return "[位置]";
            default:
                return "[未知]";
        }
    }

    @Override
    public String getSender() {
        return mRealMessage.getSender();
    }

    @Override
    public String getSendTo() {
        return mRealMessage.getConversation().getPeer();
    }

    @Override
    public long getTime() {
        return mRealMessage.timestamp() * 1000;
    }

    @Override
    public MsgType getMsgType() {
        return EntityParser.timElemType2MsgType(mFirstElemType);
    }

    @Override
    public MsgStatus getStatus() {
        return EntityParser.timMessageStatus2MsgStatus(mRealMessage);
    }

    @Override
    public Map<String, String> getExtras() {
        return null;
    }
}
