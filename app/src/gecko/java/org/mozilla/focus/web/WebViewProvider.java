/* -*- Mode: Java; c-basic-offset: 4; tab-width: 4; indent-tabs-mode: nil; -*-
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.mozilla.focus.web;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import org.mozilla.gecko.BaseGeckoInterface;
import org.mozilla.gecko.GeckoProfile;
import org.mozilla.gecko.GeckoThread;
import org.mozilla.gecko.GeckoView;

/**
 * WebViewProvider implementation for creating a Gecko based implementation of IWebView.
 */
public class WebViewProvider {
    public static void preload(final Context context) {
        // Nothing: there's no Gecko preloading (yet?).
    }

    public static View create(Context context, AttributeSet attrs) {
        final GeckoView geckoView = new GeckoWebView(context, attrs);

        GeckoView.setGeckoInterface(new BaseGeckoInterface(context));

        final GeckoProfile profile = GeckoProfile.get(context.getApplicationContext());

        GeckoThread.init(profile, /* args */ null, /* action */ null, /* debugging */ false);
        GeckoThread.launch();

        return geckoView;
    }

    public static void performCleanup(final Context context) {
        // Nothing: does Gecko need extra private mode cleanup?
    }

    public static class GeckoWebView extends NestedGeckoView implements IWebView {
        private Callback callback;

        public GeckoWebView(Context context, AttributeSet attrs) {
            super(context, attrs);

            setChromeDelegate(createChromeDelegate());
            setContentListener(createContentListener());
            setProgressListener(createProgressListener());

            // TODO: set long press listener, call through to callback.onLinkLongPress()
        }

        @Override
        public void setCallback(Callback callback) {
            this.callback =  callback;
        }

        @Override
        public void onPause() {

        }

        @Override
        public void onResume() {

        }

        @Override
        public void reload() {
            // TODO: Reload website
        }

        @Override
        public void stopLoading() {
            // TODO: Stop loading website
        }

        @Override
        public String getUrl() {
            // TODO: Get current URL
            return null;
        }

        @Override
        public void loadUrl(final String url) {
            // TODO: Load new URL
        }

        @Override
        public void cleanup() {
            // TODO: Remove browsing session/data
        }

        @Override
        public void restoreWebviewState(Bundle savedInstanceState) {
            // TODO: restore navigation history, and reopen previously opened page
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            // TODO: save anything needed for navigation history restoration.
        }

        private ChromeDelegate createChromeDelegate() {
            return new ChromeDelegate() {
                @Override
                public void onAlert(GeckoView geckoView, String s, PromptResult promptResult) {

                }

                @Override
                public void onConfirm(GeckoView geckoView, String s, PromptResult promptResult) {

                }

                @Override
                public void onPrompt(GeckoView geckoView, String s, String s1, PromptResult promptResult) {

                }

                @Override
                public void onDebugRequest(GeckoView geckoView, PromptResult promptResult) {

                }
            };
        }

        private ContentListener createContentListener() {
            return new ContentListener() {
                @Override
                public void onTitleChanged(GeckoView geckoView, String s) {

                }
            };
        }

        private ProgressListener createProgressListener() {
            return new ProgressListener() {
                @Override
                public void onPageStart(GeckoView geckoView, String url) {
                    if (callback != null) {
                        callback.onPageStarted(url);
                        callback.onProgress(25);
                    }
                }

                @Override
                public void onPageStop(GeckoView geckoView, boolean success) {
                    if (callback != null) {
                        callback.onProgress(100);
                        callback.onPageFinished(false);
                    }
                }

                @Override
                public void onSecurityChanged(GeckoView geckoView, int status) {
                    // TODO: Split current onPageFinished() callback into two: page finished + security changed
                }
            };
        }

        @Override
        public void goForward() {

        }

        @Override
        public void goBack() {

        }

        @Override
        public boolean canGoForward() {
            return false;
        }

        @Override
        public boolean canGoBack() {
            return false;
        }
    }
}
