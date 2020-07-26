package com.kk.imsdk.base;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Map;


public interface IImRequestSender {

    void registerMessageListener(IResponseListenerContract.MessageListener listener);

    void registerGroupEventListener(IResponseListenerContract.GroupEventListener listener);

    void registerContactEventListener(IResponseListenerContract.ContactEventListener listener);

    void registerCommonEventListener(IResponseListenerContract.CommonEventListener listener);

    void unregisterMessageListener(IResponseListenerContract.MessageListener listener);

    void unregisterGroupEventListener(IResponseListenerContract.GroupEventListener listener);

    void unregisterContactEventListener(IResponseListenerContract.ContactEventListener listener);

    void unregisterCommonEventListener(IResponseListenerContract.CommonEventListener listener);

    /**
     * 用户注册
     */
    <T> void register(@NonNull String username,
                      @NonNull String password,
                      @Nullable Map<String, String> extra,
                      @Nullable ImCallback<T> callback);

    /**
     * 用户登录
     */
    <T> void login(@NonNull String username,
                   @NonNull String password,
                   @Nullable Map<String, String> extra,
                   @Nullable ImCallback<T> callback);

    /**
     * 用户退出登录
     */
    void logout();

    /**
     * 获取用户信息
     */
    void getUserInfo(String userId, ImCallback<IUser> callback);

    /**
     * 更新用户信息
     */
    <T> void updateUserInfo(Map<String, String> userInfo, @Nullable ImCallback<T> callback);

    /**
     * 添加黑名单
     */
    <T> void addUsersToBlacklist(List<String> userIdList, @Nullable ImCallback<T> callback);

    /**
     * 移除黑名单
     */
    <T> void deleteUsersFromBlacklist(List<String> userIdList, @Nullable ImCallback<T> callback);


    /**
     * 进入单聊
     */
    void enterConversation(String conversationId);

    /**
     * 进入群聊
     */
    void exitConversation();

    /**
     * 创建单聊
     */
    void createSingleConversation(String userId);

    /**
     * 创建群聊
     */
    void createGroupConversation(String groupId);

    /**
     * 获取会话列表
     */
    List<IConversation> getConversationList();

    /**
     * 删除会话
     */
    void deleteConversation(String conversationId);

    /**
     * 发送消息
     *
     * @param message  需要发送的消息
     * @param callback 发送消息的回调
     */
    void sendMessage(IMessage message, ImCallback<IMessage> callback);

    /**
     * 发送命令透传消息，
     * 命令透传：没有离线消息、不需要回执、需要对方在线才发送、对方收到之后也不用保存、没有通知
     * 例如：实时输入状态
     *
     * @param command command
     */
    void sendCommand(IMessage command);

    /**
     * 消息撤回
     */
    <T> void recallMessage(IMessage message, ImCallback<T> callback);

    /**
     * 转发消息
     */
    <T> void forwardMessage(IMessage message, IConversation conversation, ImCallback<T> callback);

    /**
     * 消息设置为已读，包括发送的消息和接收的消息
     */
    <T> void readMessage(IMessage message, ImCallback<T> callback);

    /**
     * 获取消息列表，消息按照时间升序排列
     *
     * @param offset  获取消息的起始位置
     * @param limit   获取消息的条数，0表示获取所有
     * @param reverse 降序
     * @return 消息列表
     */
    List<IMessage> getMessageList(int offset, int limit, boolean reverse);


    /**
     * 发起好友邀请
     */
    <T> void sendInvitationRequest(String userId, ImCallback<T> callback);

    /**
     * 同意好友邀请
     */
    <T> void acceptInvitation(String userId, ImCallback<T> callback);

    /**
     * 拒绝好友邀请
     */
    <T> void declineInvitation(String userId, ImCallback<T> callback);

    /**
     * 获取联系人信息
     */
    List<IUser> getContactList();

    /**
     * 删除联系人
     */
    <T> void deleteContact(String userId, ImCallback<T> callback);

    /**
     * 修改备注名
     */
    <T> void updateNoteName(String userId, String noteName, ImCallback<T> callback);


    /**
     * 发送进群申请
     */
    void sendJoinGroupRequest(String groupId);

    /**
     * 同意进群申请
     */
    void approvedJoinGroup(String groupId);

    /**
     * 拒绝进群申请
     */
    void refusedJoinGroup(String groupId);

    /**
     * 修改群名称
     */
    void updateGroupName(String groupId, String newName);

    /**
     * 修改群公告
     */
    void updateGroupNote(String groupId, String newNote);

    /**
     * 解散群组，需要管理员权限
     */
    void dissolveGroupConversation(String groupId);

    /**
     * 获取群成员列表
     */
    void getMemberList(String groupId, ImCallback<List<IUser>> callback);

    /**
     * 添加群成员
     */
    <T> void addGroupMembers(String groupId, List<String> userIdList, ImCallback<T> callback);

    /**
     * 移除群成员
     */
    <T> void removeGroupMembers(String groupId, List<String> userIdList, ImCallback<T> callback);

    /**
     * 添加管理员
     */
    <T> void addGroupKeeper(String groupId, List<String> userIdList, ImCallback<T> callback);

    /**
     * 移除管理员
     */
    <T> void removeGroupKeeper(String groupId, List<String> userIdList, ImCallback<T> callback);

    /**
     * 获取管理员列表
     */
    void getGroupKeepers(String groupId, List<String> userIdList, ImCallback<List<IUser>> callback);

    /**
     * 退出群聊
     */
    <T> void exitGroupGroup(String groupId, ImCallback<T> callback);

    /**
     * 转移群主
     */
    void changeGroupOwner(String groupId, String newOwner);

    /**
     * 群成员禁言
     *
     * @param groupId     群id
     * @param userList    用户列表
     * @param silenceTime 禁言时间
     */
    <T> void addGroupSilenceWithTime(String groupId,
                                     List<IUser> userList,
                                     long silenceTime,
                                     ImCallback<T> callback);

    /**
     * 设置群消息免打扰，可以收到消息，但无通知，无弹出
     */
    <T> void setNoDisturb(String groupId, ImCallback<T> callback);

    /**
     * 解除免打扰
     */
    <T> void unsetNoDisturb(String groupId, ImCallback<T> callback);

    /**
     * 是否已建立连接（登录）
     */
    boolean isConnected();

    /**
     * 是否与服务器联通
     */
    boolean isSocketConnected();
}
