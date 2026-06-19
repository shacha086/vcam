package com.yaahua.vcam;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import de.robv.android.xposed.XC_MethodHook;

public final class HookRegistry {
    private HookRegistry() {}

    private static final List<XC_MethodHook.Unhook> HOOKS = new CopyOnWriteArrayList<>();
    private static volatile boolean installed = false;

    public static void add(XC_MethodHook.Unhook unhook) {
        if (unhook != null) HOOKS.add(unhook);
    }

    public static synchronized void installCompleted() {
        installed = true;
    }

    public static synchronized boolean isInstalled() {
        return installed;
    }

    public static synchronized void unhookAll() {
        for (XC_MethodHook.Unhook unhook : new ArrayList<>(HOOKS)) {
            try {
                unhook.unhook();
            } catch (Throwable t) {
                LogUtil.log("【VCAM】unhook 失败: " + t);
            }
        }
        HOOKS.clear();
        installed = false;
    }
}

