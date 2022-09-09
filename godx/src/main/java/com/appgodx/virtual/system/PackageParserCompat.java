/*
 * Copyright (C) 2017 Beijing Didi Infinity Technology and Development Co.,Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appgodx.virtual.system;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.appgodx.Reflector;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * @author
 */
public final class PackageParserCompat {

    public static final Object parsePackage(final Context context, final File apk, final int flags) {
        try {
            if (Build.VERSION.SDK_INT >= 28
                || (Build.VERSION.SDK_INT == 27 && Build.VERSION.PREVIEW_SDK_INT != 0)) { // Android P Preview
                return PackageParserPPreview.parsePackage(context, apk, flags);
            } else if (Build.VERSION.SDK_INT >= 24) {
                return PackageParserV24.parsePackage(context, apk, flags);
            } else if (Build.VERSION.SDK_INT >= 21) {
                return PackageParserLollipop.parsePackage(context, apk, flags);
            } else {
                return PackageParserLegacy.parsePackage(context, apk, flags);
            }
    
        } catch (Throwable e) {
            throw new RuntimeException("error", e);
        }
    }

    private static final class PackageParserPPreview {

        static final Object parsePackage(Context context, File apk, int flags) throws Throwable {
            Object parser = Reflector.on("android.content.pm.PackageParser").constructor().newInstance();
            Object pkg = Reflector.with(parser).method("parsePackage",File.class,int.class)
                            .call(apk,flags);
            Reflector.with(parser)
                .method("collectCertificates", Class.forName("android.content.pm.PackageParser$Package"), boolean.class)
                .call(pkg, false);
            return pkg;
        }
    }

    private static final class PackageParserV24 {

        static final Object parsePackage(Context context, File apk, int flags) throws Throwable {
            Object parser = Reflector.on("android.content.pm.PackageParser").constructor().newInstance();
            Object pkg = Reflector.with(parser).method("parsePackage",File.class,int.class)
                    .call(apk,flags);
            Reflector.with(parser)
                .method("collectCertificates", Class.forName("android.content.pm.PackageParser$Package"), int.class)
                .call(pkg, flags);
            return pkg;
        }
    }

    private static final class PackageParserLollipop {

        static final Object parsePackage(final Context context, final File apk, final int flags) throws Throwable {
            Object parser = Reflector.on("android.content.pm.PackageParser").constructor().newInstance();
            Object pkg = Reflector.with(parser).method("parsePackage",File.class,int.class)
                    .call(apk,flags);
            Reflector.with(parser)
                    .method("collectCertificates", Class.forName("android.content.pm.PackageParser$Package"), int.class)
                    .call(pkg, flags);
            return pkg;
        }

    }

    private static final class PackageParserLegacy {

        static final Object parsePackage(Context context, File apk, int flags) throws Throwable {
            Object parser = Reflector.on("android.content.pm.PackageParser").constructor(String.class).call(apk.getAbsolutePath());
            Object pkg = Reflector.with(parser).method("parsePackage",File.class,String.class, DisplayMetrics.class,int.class).call(
                    apk, apk.getAbsolutePath(), context.getResources().getDisplayMetrics(), flags);
            Reflector.with(parser)
                .method("collectCertificates", Class.forName("android.content.pm.PackageParser$Package"), int.class)
                .call(pkg, flags);
            return pkg;
        }

    }

}