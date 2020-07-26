package com.kk.imsdk.tim.entity;

import com.kk.imsdk.base.IMessage;
import com.kk.imsdk.base.type.ConvType;
import com.kk.imsdk.base.type.MsgStatus;
import com.kk.imsdk.base.type.MsgType;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMImageElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageStatus;
import com.tencent.imsdk.TIMTextElem;

/**
 * TODO 完成之后，已接口形式定义所有parser
 */
@SuppressWarnings("WeakerAccess")
public final class EntityParser {
    private EntityParser() {
    }

    public static MsgType timElemType2MsgType(TIMElemType timElemType) {
        switch (timElemType) {
            case Text:
                return MsgType.Text;
            case Image:
                return MsgType.Image;
            default:
                return MsgType.Custom;
        }
    }

    public static MsgStatus timMessageStatus2MsgStatus(TIMMessage message) {
        if (!message.isSelf() && message.isPeerReaded()) {
            return MsgStatus.Read;
        }
        TIMMessageStatus status = message.status();
        switch (status) {
            case Sending:
                return MsgStatus.Sending;
            case SendFail:
                return MsgStatus.SendFailed;
            case SendSucc:
            default:
                return MsgStatus.Sent;
        }
    }

    public static TIMMessage toTimMsg(IMessage message) {
        // TODO 其他消息待完成
        MsgType type = message.getMsgType();
        TIMMessage msg = new TIMMessage();
        switch (type) {
            case Text:
                TIMTextElem textElem = new TIMTextElem();
                textElem.setText(message.getText());
                msg.addElement(textElem);
                break;
            case Image:
                TIMImageElem imageElem = new TIMImageElem();
                msg.addElement(imageElem);
                break;
        }
        return msg;
    }

    public static ConvType timConvType2ConvType(TIMConversationType timConversationType) {
        switch (timConversationType) {
            default:
            case C2C:
                return ConvType.Single;
            case Group:
                return ConvType.Group;
        }
    }

}
