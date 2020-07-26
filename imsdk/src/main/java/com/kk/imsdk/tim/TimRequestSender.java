package com.kk.imsdk.tim;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kk.imsdk.base.BaseImRequestSender;
import com.kk.imsdk.base.IConversation;
import com.kk.imsdk.base.IMessage;
import com.kk.imsdk.base.IUser;
import com.kk.imsdk.base.ImCallback;
import com.kk.imsdk.tim.entity.EntityParser;
import com.kk.imsdk.tim.entity.TimConvImpl;
import com.kk.imsdk.tim.entity.TimMsgImpl;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConnListener;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMGroupTipsType;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMRefreshListener;
import com.tencent.imsdk.TIMSNSChangeInfo;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMMessageReceipt;
import com.tencent.imsdk.friendship.TIMFriendPendencyInfo;
import com.tencent.imsdk.friendship.TIMFriendRequest;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.tencent.imsdk.friendship.TIMFriendshipListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.kk.imsdk.base.IResponseListenerContract.CommonEventListener;
import static com.kk.imsdk.base.IResponseListenerContract.ContactEventListener;
import static com.kk.imsdk.base.IResponseListenerContract.GroupEventListener;
import static com.kk.imsdk.base.IResponseListenerContract.MessageListener;

public class TimRequestSender extends BaseImRequestSender {

    private boolean mConnected;
    private boolean mSocketConnected;
    private List<IConversation> mConversationList;

    public TimRequestSender(Context appContext) {
        TIMSdkConfig timSdkConfig = new TIMSdkConfig(UserSigGenerator.SDK_APP_ID);
        timSdkConfig.enableLogPrint(false);
        TIMManager.getInstance().init(appContext, timSdkConfig);

        TIMUserConfig userConfig = new TIMUserConfig()
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        Timber.i("onForceOffline");
                        for (CommonEventListener listener : mCommonEventListeners) {
                            listener.onForceOffline();
                        }
                    }

                    @Override
                    public void onUserSigExpired() {
                        Timber.i("onUserSigExpired");
                        for (CommonEventListener listener : mCommonEventListeners) {
                            listener.onForceOffline();
                        }
                    }
                }).setConnectionListener(new TIMConnListener() {
                    @Override
                    public void onConnected() {
                        Timber.i("Connection:onConnected");
                        mSocketConnected = true;
                        for (CommonEventListener listener : mCommonEventListeners) {
                            listener.onConnected();
                        }
                    }

                    @Override
                    public void onDisconnected(int code, String desc) {
                        mSocketConnected = false;
                        Timber.i("Connection:onDisconnected:code = %d, msg = %s", code, desc);
                        for (CommonEventListener listener : mCommonEventListeners) {
                            listener.onDisconnected();
                        }
                    }

                    @Override
                    public void onWifiNeedAuth(String name) {
                        Timber.i("onWifiNeedAuth:name = %s", name);
                    }
                }).setGroupEventListener(timGroupTipsElem -> {
                    Timber.i("");
                    TIMGroupTipsType type = timGroupTipsElem.getTipsType();
                    List<String> memberIdList = timGroupTipsElem.getUserList();
                    String opUser = timGroupTipsElem.getOpUser();
                    String groupId = timGroupTipsElem.getGroupId();
                    switch (type) {
                        case Join:
                            Timber.i("Join");
                            for (GroupEventListener listener : mGroupEventListeners) {
                                listener.onMemberAdded(groupId, memberIdList, opUser);
                            }
                            break;
                        case Kick:
                            Timber.i("Kick");
                            for (GroupEventListener listener : mGroupEventListeners) {
                                listener.onMemberRemoved(groupId, memberIdList, opUser);
                            }
                            break;
                        case Quit:
                            Timber.i("Quit");
                            for (GroupEventListener listener : mGroupEventListeners) {
                                listener.onMemberExist(groupId, Collections.singletonList(opUser));
                            }
                            break;
                        case SetAdmin:
                            Timber.i("SetAdmin");
                            for (GroupEventListener listener : mGroupEventListeners) {
                                listener.onAddAdmin(groupId, Collections.singletonList(opUser));
                            }
                            break;
                        case CancelAdmin:
                            Timber.i("CancelAdmin");
                            for (GroupEventListener listener : mGroupEventListeners) {
                                listener.onCancelAdmin(groupId, Collections.singletonList(opUser));
                            }
                            break;
                        case ModifyGroupInfo:
                            Timber.i("ModifyGroupInfo");
                            break;
                        case ModifyMemberInfo:
                            Timber.i("ModifyMemberInfo");
                            break;
                        default:
                            Timber.i("");
                            break;
                    }
                }).setRefreshListener(new TIMRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Timber.i("onRefresh");
                        for (CommonEventListener listener : mCommonEventListeners) {
                            listener.onRefreshed();
                        }
                    }

                    @Override
                    public void onRefreshConversation(List<TIMConversation> list) {
                        Timber.i("onRefreshConversation:%d", list.size());
                        List<IConversation> conversationList = new ArrayList<>();
                        for (TIMConversation conversation : list) {
                            TimConvImpl conv = TimConvImpl.from(conversation);
                            conversationList.add(conv);
                            Timber.i("onRefreshConversation:%s", conv.getLastMsg());
                        }
                        mConversationList = conversationList;
                        for (CommonEventListener listener : mCommonEventListeners) {
                            listener.onConversationListRefreshed(conversationList);
                        }
                    }
                }).setMessageReceiptListener(list -> {
                    Timber.i("onRecvReceipt");
                    for (MessageListener listener : mMessageListeners) {
                        for (TIMMessageReceipt receipt : list) {
                            long time = receipt.getTimestamp();
                            TIMConversation conversation = receipt.getConversation();
                            String conversationId = conversation.getPeer();
                            List<IMessage> msgList = getMessageByTime(conversationId, time);
                            for (IMessage msg : msgList) {
                                listener.onMessageRead(msg.getLocalId());
                            }
                        }
                    }
                }).setMessageRevokedListener(timMessageLocator -> {
                    Timber.i("MessageRevoked:%s", timMessageLocator);
                    String msgId = String.valueOf(timMessageLocator.getSeq());
                    for (MessageListener listener : mMessageListeners) {
                        listener.onMessageRecalled(msgId);
                    }
                });
        // 禁用SDK的消息存储机制，由上层自行处理
        userConfig.disableStorage();
        // 启用消息回执机制（已读状态）
        userConfig.enableReadReceipt(true);
        // 禁用自动发送消息回执
        // userConfig.disableAutoReport(true);
        userConfig.setFriendshipListener(new TIMFriendshipListener() {
            @Override
            public void onAddFriends(List<String> list) {
                Timber.i("onAddFriends");
                for (ContactEventListener listener : mContactEventListeners) {
                    for (String userId : list) {
                        listener.onInvitationAccept(userId);
                    }
                }
            }

            @Override
            public void onDelFriends(List<String> list) {
                Timber.i("onDelFriends");
                for (ContactEventListener listener : mContactEventListeners) {
                    for (String userId : list) {
                        listener.onContactRemoved(userId);
                    }
                }
            }

            @Override
            public void onFriendProfileUpdate(List<TIMSNSChangeInfo> list) {
                Timber.i("onFriendProfileUpdate");
            }

            @Override
            public void onAddFriendReqs(List<TIMFriendPendencyInfo> list) {
                Timber.i("onAddFriendReqs");
                for (ContactEventListener listener : mContactEventListeners) {
                    for (TIMFriendPendencyInfo info : list) {
                        listener.onContactRemoved(info.getFromUser());
                    }
                }
            }
        });

        TIMManager.getInstance().setUserConfig(userConfig);

        // 消息接收监听
        TIMManager.getInstance().addMessageListener(list -> {
            List<IMessage> msgList = new ArrayList<>();
            // 组装成基础消息
            for (TIMMessage timMsg : list) {
                TimMsgImpl msg = TimMsgImpl.from(timMsg);
                msgList.add(msg);
                Timber.i("onMessageArrived:%s", msg.getText());
            }
            for (MessageListener listener : mMessageListeners) {
                listener.onMessageArrived(msgList);
            }
            return false;
        });
    }

    private List<IMessage> getMessageByTime(String conversationId, long time) {
        // TODO 获取time之后的所有消息
        Timber.i("Find unread message by time:%d for:%s", time, conversationId);
        return new ArrayList<>();
    }

    @Override
    public <T> void register(@NonNull String username, @NonNull String password,
                             @Nullable Map<String, String> extra,
                             @Nullable ImCallback<T> callback) {
        Timber.i("register");
    }

    @Override
    public <T> void login(@NonNull String username,
                          @NonNull String password,
                          @Nullable Map<String, String> extra,
                          @Nullable ImCallback<T> callback) {
        if (extra != null && extra.containsKey("userId")) {
            String userId = extra.get("userId");
            TIMManager.getInstance().autoLogin(userId, new TIMCallBack() {
                @Override
                public void onError(int code, String msg) {
                    Timber.e("Failed to auto login, code = %s, msg = %s", code, msg);
                    if (callback != null) {
                        callback.callback(code, "Auto login failed", null);
                    }
                }

                @Override
                public void onSuccess() {
                    mConnected = true;
                    if (callback != null) {
                        callback.callback(0, "", null);
                    }
                }
            });
            return;
        }

        TIMManager.getInstance().login(username, UserSigGenerator.generate(username), new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                Timber.i("Login error:code = %d, msg = %s", code, desc);
                mConnected = false;
                if (callback != null) {
                    callback.callback(code, desc, null);
                }
            }

            @Override
            public void onSuccess() {
                Timber.i("Login onSuccess");
                mConnected = true;
                if (callback != null) {
                    callback.callback(0, "", null);
                }
            }
        });
    }

    @Override
    public void logout() {
        TIMManager.getInstance().logout(new TIMCallBack() {
            @Override
            public void onError(int code, String msg) {
                Timber.e("Logout failed:code = %d, msg = %s", code, msg);
            }

            @Override
            public void onSuccess() {
                Timber.i("");
            }
        });
    }

    @Override
    public void getUserInfo(String userId, ImCallback<IUser> callback) {
        Timber.i("");
    }

    @Override
    public <T> void updateUserInfo(Map<String, String> userInfo, @Nullable ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void addUsersToBlacklist(List<String> userIdList, @Nullable ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void deleteUsersFromBlacklist(List<String> userIdList, @Nullable ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public void enterConversation(String conversationId) {
        Timber.i("");
    }

    @Override
    public void exitConversation() {
        Timber.i("");
    }

    @Override
    public void createSingleConversation(String userId) {
        Timber.i("");
    }

    @Override
    public void createGroupConversation(String groupId) {
        Timber.i("");
    }

    @Override
    public List<IConversation> getConversationList() {
        return mConversationList;
    }

    @Override
    public void deleteConversation(String conversationId) {
        Timber.i("");
    }

    @Override
    public  void sendMessage(IMessage message, ImCallback<IMessage> callback) {
        String sendTo = message.getSendTo();
        TIMConversation conversation
                = TIMManager.getInstance().getConversation(TIMConversationType.C2C, sendTo);
        TIMMessage msg = EntityParser.toTimMsg(message);
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int code, String desc) {
                Timber.i("sendMessage:code = %d, msg = %s", code, desc);
                if (callback != null) {
                    callback.callback(-1, desc, null);
                }
                String msgId = message.getLocalId();
                for (MessageListener listener : mMessageListeners) {
                    listener.onMessageSentFailed(msgId, code, desc);
                }
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                Timber.i("sendMessage:onSuccess");
                if (callback != null) {
                    callback.callback(0, "", TimMsgImpl.from(timMessage));
                }
                for (MessageListener listener : mMessageListeners) {
                    listener.onMessageDelivered(timMessage.getMsgId());
                }
            }
        });
    }

    @Override
    public void sendCommand(IMessage command) {
        Timber.i("");
    }

    @Override
    public <T> void recallMessage(IMessage message, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void forwardMessage(IMessage message, IConversation conversation, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void readMessage(IMessage message, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public List<IMessage> getMessageList(int offset, int limit, boolean reverse) {
        Timber.i("");
        return null;
    }

    @Override
    public <T> void sendInvitationRequest(String userId, ImCallback<T> callback) {
        TIMFriendRequest request = new TIMFriendRequest(userId);
        TIMFriendshipManager.getInstance().addFriend(request, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int code, String desc) {
                Timber.i("sendInvitationRequest error:code = %d, msg = %s", code, desc);
                if (callback != null) {
                    callback.callback(code, desc);
                }
            }

            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                Timber.i("sendInvitationRequest success");
                if (callback != null) {
                    callback.callback(0, "");
                }
            }
        });
    }

    @Override
    public <T> void acceptInvitation(String userId, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void declineInvitation(String userId, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public List<IUser> getContactList() {
        Timber.i("");
        return null;
    }

    @Override
    public <T> void deleteContact(String userId, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void updateNoteName(String userId, String noteName, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public void updateGroupName(String groupId, String newName) {
        Timber.i("");
    }

    @Override
    public void updateGroupNote(String groupId, String newNote) {
        Timber.i("");
    }

    @Override
    public void dissolveGroupConversation(String groupId) {
        Timber.i("");
    }

    @Override
    public void getMemberList(String groupId, ImCallback<List<IUser>> callback) {
        Timber.i("");
    }

    @Override
    public <T> void addGroupMembers(String groupId, List<String> userIdList, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void removeGroupMembers(String groupId, List<String> userIdList, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void addGroupKeeper(String groupId, List<String> userIdList, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void removeGroupKeeper(String groupId, List<String> userIdList, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public void getGroupKeepers(String groupId, List<String> userIdList, ImCallback<List<IUser>> callback) {
        Timber.i("");
    }

    @Override
    public <T> void exitGroupGroup(String groupId, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public void sendJoinGroupRequest(String groupId) {
        Timber.i("");
    }

    @Override
    public void approvedJoinGroup(String groupId) {
        Timber.i("");
    }

    @Override
    public void refusedJoinGroup(String groupId) {
        Timber.i("");
    }

    @Override
    public void changeGroupOwner(String groupId, String newOwner) {
        Timber.i("");
    }

    @Override
    public <T> void addGroupSilenceWithTime(String groupId,
                                            List<IUser> userList,
                                            long silenceTime,
                                            ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void setNoDisturb(String groupId, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public <T> void unsetNoDisturb(String groupId, ImCallback<T> callback) {
        Timber.i("");
    }

    @Override
    public boolean isConnected() {
        return mConnected;
    }

    @Override
    public boolean isSocketConnected() {
        return mSocketConnected;
    }
}
