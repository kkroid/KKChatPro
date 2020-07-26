package com.kk.imsdk.base;

import java.util.List;

public interface IResponseListenerContract {

    interface MessageListener {
        /**
         * 收到一条消息
         */
        void onMessageArrived(IMessage message);

        /**
         * 收到多条消息，如离线消息等
         */
        void onMessageArrived(List<IMessage> messageList);

        /**
         * 消息已送达服务器
         */
        void onMessageDelivered(String messageId);

        /**
         * 消息已被阅读
         */
        void onMessageRead(String messageId);

        /**
         * 消息已被撤回
         */
        void onMessageRecalled(String messageId);

        /**
         * 消息发送失败(服务器返回错误)
         */
        default void onMessageSentFailed(String messageId, int code, String desc) {
        }

        /**
         * 打字中
         */
        default void onTyping() {
        }
    }

    interface GroupEventListener {
        /**
         * 用户被添加进入群聊
         */
        void onMemberAdded(String groupId, List<String> newMembers, String operator);

        /**
         * 用户被移除群聊
         */
        void onMemberRemoved(String groupId, List<String> removedMembers, String operator);

        /**
         * 用户退出群聊
         */
        void onMemberExist(String groupId, List<String> existMembers);

        /**
         * 进群申请被同意
         */
        void onGroupInviteApproved(String groupId);

        /**
         * 进群申请被拒绝
         */
        void onGroupInviteDeclined(String groupId);

        void onAddAdmin(String groupId, List<String> newAdmin);

        void onCancelAdmin(String groupId, List<String> canceledAdmin);
    }

    interface ContactEventListener {
        /**
         * 好友申请被同意
         */
        void onInvitationAccept(String userId);

        /**
         * 好友申请被拒绝
         */
        void onInvitationDeclined(String userId);

        /**
         * 收到好友申请
         */
        void onInvitationReceived(String userId);

        /**
         * 联系人被移除
         */
        void onContactRemoved(String userId);
    }

    interface CommonEventListener {
        /**
         * 漫游消息刷新完毕
         */
        default void onRefreshed() {

        }

        /**
         * 漫游消息刷新完毕
         */
        default void onConversationListRefreshed(List<IConversation> conversationList) {
        }

        /**
         * 被踢下线，如：从其他终端登录、密码变更
         */
        default void onForceOffline() {
        }

        /**
         * 登录成功
         */
        default void onConnected() {
        }

        /**
         * 与服务器连接成功
         */
        default void onSocketConnected() {
        }

        /**
         * 连接断开
         */
        default void onDisconnected() {
        }
    }
}
