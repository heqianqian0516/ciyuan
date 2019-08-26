package com.ciyuanplus.mobile.manager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;

import com.bumptech.glide.util.Util;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Alen on 2017/2/16.
 * <p>
 * 消息中心管理类
 * 需要在application 里面做一下init
 */

public class EventCenterManager {
    private static final int HANDLER_MESSAGE_ID = 0;
    private static final SparseArray<Set<OnHandleEventListener>> sMap = new SparseArray<>();
    private static Handler sHandler;

    public static void initEventCenterManager() throws RuntimeException {
        if (sHandler == null) {
            if (Looper.myLooper() != Looper.getMainLooper()) {
                throw new RuntimeException("please init in main ui thread !");
            }
            sHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg != null && msg.what == HANDLER_MESSAGE_ID) {
                        synSendEvent((EventMessage) msg.obj);
                    }
                }
            };
        }
    }

    @SuppressWarnings("unchecked")
    public static void synSendEvent(final EventMessage eventMessage) {
        if (eventMessage == null) {
            return;
        }

        Set<OnHandleEventListener> setTemp = null;
        synchronized (sMap) {
            HashSet<OnHandleEventListener> set = (HashSet<OnHandleEventListener>) sMap
                    .get(eventMessage.mEvent);
            if (set != null) {
                setTemp = (Set<OnHandleEventListener>) set.clone();
            }
        }
        if (setTemp != null) {
            for (OnHandleEventListener onHandleEventListener : setTemp) {
                if (Util.isOnMainThread() ) {
                    onHandleEventListener.onHandleEvent(eventMessage);
                }
                }

        }
    }

    public static void asynSendEvent(final EventMessage eventMessage) throws RuntimeException {
        if (eventMessage == null) {
            return;
        }
        if (sHandler == null) {
            throw new RuntimeException("please init first (initEventCenterManager()) !");
        } else {
            sHandler.sendMessage(sHandler.obtainMessage(HANDLER_MESSAGE_ID, eventMessage));
        }
    }

    public static void addEventListener(final int event,
                                        final OnHandleEventListener onHandleEventListener) {
        if (onHandleEventListener != null) {
            synchronized (sMap) {
                Set<OnHandleEventListener> set = sMap.get(event);
                if (set == null) {
                    set = new HashSet<>();
                    sMap.put(event, set);
                }
                set.add(onHandleEventListener);
            }
        }
    }

    public static void deleteEventListener(final int event,
                                           final OnHandleEventListener onHandleEventListener) {
        if (onHandleEventListener != null) {
            synchronized (sMap) {
                Set<OnHandleEventListener> set = sMap.get(event);
                if (set != null) {
                    set.remove(onHandleEventListener);
                }
            }
        }
    }

    public static void deleteAllEventListener() {
        synchronized (sMap) {
            sMap.clear();
        }
    }

    public interface OnHandleEventListener {
        void onHandleEvent(final EventMessage eventMessage);
    }

    public static class EventMessage {
        public final int mEvent;
        public final Object mObject;

        public EventMessage(final int event) {
            this(event, null);
        }

        public EventMessage(final int event, final Object object) {
            this.mEvent = event;
            this.mObject = object;
        }
    }
}
