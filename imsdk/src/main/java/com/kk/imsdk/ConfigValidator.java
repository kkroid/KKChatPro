package com.kk.imsdk;

import com.kk.imsdk.base.ImConfigValidator;
import com.kk.imsdk.tim.TimParamValidator;

import static com.jess.arms.utils.Preconditions.checkNotNull;

public final class ConfigValidator implements ImConfigValidator {
    private static class Holder {
        private static ConfigValidator sInstance = new ConfigValidator();
    }

    private ConfigValidator() {
    }

    public static void init(SdkType sdkType) {
        switch (sdkType) {
            default:
            case Tim:
                getInstance().mImConfigValidator = new TimParamValidator();
                break;
            case Custom:
                getInstance().mImConfigValidator = new ImConfigValidator() {
                };
                break;
        }
    }

    public static ConfigValidator getInstance() {
        return Holder.sInstance;
    }

    private ImConfigValidator mImConfigValidator;

    @Override
    public boolean isValidAccountName(String userName) {
        checkNotNull(mImConfigValidator, "Not init yet");
        return mImConfigValidator.isValidAccountName(userName);
    }

    @Override
    public boolean isValidPassword(String password) {
        checkNotNull(mImConfigValidator, "Not init yet");
        return mImConfigValidator.isValidPassword(password);
    }

    @Override
    public boolean isValidGroupName(String groupName) {
        checkNotNull(mImConfigValidator, "Not init yet");
        return mImConfigValidator.isValidGroupName(groupName);
    }

    @Override
    public boolean isValidNickname(String nickname) {
        checkNotNull(mImConfigValidator, "Not init yet");
        return mImConfigValidator.isValidNickname(nickname);
    }
}
