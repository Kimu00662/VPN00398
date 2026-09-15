package com.vpnpro.hook;

import android.content.Context;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!"vpn.japan".equals(lpparam.packageName)) return;

        final ClassLoader cl = lpparam.classLoader;

        try {
            XposedHelpers.findAndHookMethod(
                "com.vpn.lib.data.repo.RepositoryImpl", cl, "l",
                XC_MethodReplacement.returnConstant(1));
            XposedBridge.log("[VpnProHook] Hook1 OK");
        } catch (Throwable t) {
            XposedBridge.log("[VpnProHook] Hook1 FAIL: " + t);
        }

        try {
            XposedHelpers.findAndHookMethod(
                "com.vpn.lib.pem.PemGenerationException", cl, "b",
                Context.class,
                XC_MethodReplacement.returnConstant(true));
            XposedBridge.log("[VpnProHook] Hook2 OK");
        } catch (Throwable t) {
            XposedBridge.log("[VpnProHook] Hook2 FAIL: " + t);
        }

        try {
            XposedHelpers.findAndHookMethod(
                "com.vpn.lib.feature.dashboard.DashboardPresenterImpl", cl, "v",
                String.class, Boolean.class, String.class, String.class,
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            Method a0 = param.thisObject.getClass().getDeclaredMethod("a0");
                            a0.setAccessible(true);
                            a0.invoke(param.thisObject);
                        } catch (Throwable t) {
                            XposedBridge.log("[VpnProHook] Hook3 a0 FAIL: " + t);
                        }
                        return null;
                    }
                });
            XposedBridge.log("[VpnProHook] Hook3 OK");
        } catch (Throwable t) {
            XposedBridge.log("[VpnProHook] Hook3 FAIL: " + t);
        }

        try {
            XposedHelpers.findAndHookMethod(
                "com.vpn.lib.feature.dashboard.DashboardPresenterImpl", cl, "a0",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            Field fN = XposedHelpers.findClass("com.vpn.lib.App", cl)
                                    .getField("N");
                            fN.setBoolean(null, true);
                        } catch (Throwable t) {
                            XposedBridge.log("[VpnProHook] Hook4 FAIL: " + t);
                        }
                    }
                });
            XposedBridge.log("[VpnProHook] Hook4 OK");
        } catch (Throwable t) {
            XposedBridge.log("[VpnProHook] Hook4 FAIL: " + t);
        }
    }
}
