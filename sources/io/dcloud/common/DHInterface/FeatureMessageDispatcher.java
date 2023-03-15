package io.dcloud.common.DHInterface;

import io.dcloud.common.adapter.util.MessageHandler;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeatureMessageDispatcher {
    public static CopyOnWriteArrayList<MessageListener> sFeatureMessage = new CopyOnWriteArrayList<>();

    @Deprecated
    public interface MessageListener {
        void onReceiver(Object obj);
    }

    public static abstract class StrongMessageListener implements MessageListener {
        Object mFlag;

        public StrongMessageListener(Object obj) {
            this.mFlag = obj;
        }

        public abstract void onReceiver(Object obj);
    }

    public static boolean contains(Object obj) {
        try {
            Iterator<MessageListener> it = sFeatureMessage.iterator();
            while (it.hasNext()) {
                MessageListener next = it.next();
                if ((next instanceof StrongMessageListener) && obj.equals(((StrongMessageListener) next).mFlag)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void dispatchMessage(Object obj, final Object obj2) {
        try {
            Iterator<MessageListener> it = sFeatureMessage.iterator();
            while (it.hasNext()) {
                MessageListener next = it.next();
                if (next instanceof StrongMessageListener) {
                    if (obj.equals(((StrongMessageListener) next).mFlag)) {
                        MessageHandler.sendMessage(new MessageHandler.IMessages() {
                            public void execute(Object obj) {
                                ((MessageListener) obj).onReceiver(obj2);
                            }
                        }, next);
                    }
                } else if (obj2 != null) {
                    MessageHandler.sendMessage(new MessageHandler.IMessages() {
                        public void execute(Object obj) {
                            ((MessageListener) obj).onReceiver(obj2);
                        }
                    }, next);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerListener(MessageListener messageListener) {
        sFeatureMessage.add(messageListener);
    }

    public static void unregisterListener(MessageListener messageListener) {
        sFeatureMessage.remove(messageListener);
    }

    @Deprecated
    public static void dispatchMessage(Object obj) {
        dispatchMessage((Object) null, obj);
    }
}
