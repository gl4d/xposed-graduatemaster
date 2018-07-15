/*
 * Copyright (c) 2018 The sky Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.aweme.hook.base;

import android.app.ActivityThread;
import android.content.Context;

import com.sky.xposed.aweme.data.CachePreferences;
import com.sky.xposed.aweme.data.ObjectManager;
import com.sky.xposed.aweme.data.UserConfigManager;
import com.sky.xposed.aweme.hook.HookManager;
import com.sky.xposed.aweme.hook.VersionManager;
import com.sky.xposed.aweme.util.Alog;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by sky on 18-3-10.
 */

public abstract class BaseHook {

    private XC_LoadPackage.LoadPackageParam mParam;

    protected HookManager mHookManager = HookManager.getInstance();
    protected Context mContext = mHookManager.getContext();
    protected CachePreferences mCachePreferences = mHookManager.getCachePreferences();
    protected UserConfigManager mUserConfigManager = mHookManager.getUserConfigManager();
    protected ObjectManager mObjectManager = mHookManager.getObjectManager();
    protected VersionManager mVersionManager = mHookManager.getVersionManager();

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        this.mParam = lpparam;

        try {
            // 处理
            onHandleLoadPackage(lpparam);
        } catch (Throwable tr) {
            Alog.e("handleLoadPackage异常", tr);
        }
    }

    public abstract void onHandleLoadPackage(XC_LoadPackage.LoadPackageParam param);

    public Context getSystemContext() {
        return ActivityThread.currentActivityThread().getSystemContext();
    }

    public XC_LoadPackage.LoadPackageParam getLoadPackageParam() {
        return mParam;
    }

    public String getProcessName() {
        return mParam.processName;
    }

    public Class findClass(String className) {
        return findClass(className, mParam.classLoader);
    }

    public Class findClass(String className, ClassLoader classLoader) {
        return XposedHelpers.findClass(className, classLoader);
    }

    public XC_MethodHook.Unhook findAndHookMethod(String className, String methodName, Object... parameterTypesAndCallback) {
        return XposedHelpers.findAndHookMethod(className, mParam.classLoader, methodName, parameterTypesAndCallback);
    }
}
