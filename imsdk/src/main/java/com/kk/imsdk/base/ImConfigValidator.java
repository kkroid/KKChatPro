package com.kk.imsdk.base;

import android.text.TextUtils;


public interface ImConfigValidator {
    /**
     * 数字、字母、下划线、减号，长度4-24
     */
    String ACCOUNT_NAME_PATTERN = "^[.-_a-zA-Z0-9]{4,24}$";
    /**
     * 数字、字母、特殊字符，其中必须有大写字母、小写字母和数字混合，特殊字符暂时不强制要求，长度6-32
     */
    String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)[A-Za-z\\d$@!%*#?&-_]{6,32}$";
    /**
     * 中文、数字、字母、下划线、减号，长度4-16
     */
    String NICKNAME_PATTERN = "^[.-_a-zA-Z0-9\u4e00-\u9fa5]{4,16}$";

    default boolean isValidAccountName(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return false;
        }
        return userName.matches(ACCOUNT_NAME_PATTERN);
    }

    default boolean isValidPassword(String password) {
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        return password.matches(PASSWORD_PATTERN);
    }

    default boolean isValidNickname(String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            return false;
        }
        return nickname.matches(NICKNAME_PATTERN);
    }

    default boolean isValidGroupName(String groupName) {
        if (TextUtils.isEmpty(groupName)) {
            return false;
        }
        return groupName.matches(NICKNAME_PATTERN);
    }
}
