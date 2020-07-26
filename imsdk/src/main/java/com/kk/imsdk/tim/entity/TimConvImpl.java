package com.kk.imsdk.tim.entity;

import com.kk.imsdk.base.IConversation;
import com.kk.imsdk.base.IUser;
import com.kk.imsdk.base.type.ConvType;
import com.tencent.imsdk.TIMConversation;

import java.util.List;

public class TimConvImpl implements IConversation {

    private TIMConversation mRealConversation;
    private TimMsgImpl mLastMsg;

    private TimConvImpl(TIMConversation timConversation) {
        this.mRealConversation = timConversation;
        mLastMsg = TimMsgImpl.from(mRealConversation.getLastMsg());
    }

    public static TimConvImpl from(TIMConversation timConversation) {
        return new TimConvImpl(timConversation);
    }

    @Override
    public String getId() {
        return mRealConversation.getPeer();
    }

    @Override
    public String getIcon() {
        return null;
    }

    @Override
    public String getName() {
        ConvType convType = getType();
        switch (convType) {
            case Group:
                return mRealConversation.getGroupName();
            default:
            case Single:
                return mRealConversation.getPeer();
        }
    }

    @Override
    public List<? extends IUser> getUsers() {
        return null;
    }

    @Override
    public String getLastMsg() {
        if (mLastMsg == null) {
            return "";
        }
        return mLastMsg.getText();
    }

    @Override
    public void setLastMsg(String msg) {
    }

    @Override
    public int getUnreadCount() {
        return Long.valueOf(mRealConversation.getUnreadMessageNum()).intValue();
    }

    @Override
    public long getLastMsgTime() {
        if (mLastMsg == null) {
            return 0;
        }
        return mLastMsg.getTime();
    }

    @Override
    public ConvType getType() {
        return EntityParser.timConvType2ConvType(mRealConversation.getType());
    }
}
