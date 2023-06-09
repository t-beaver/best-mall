package com.dcloud.android.v4.view;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import com.dcloud.android.v4.view.AccessibilityDelegateCompatIcs;
import com.dcloud.android.v4.view.AccessibilityDelegateCompatJellyBean;
import com.dcloud.android.v4.view.accessibility.AccessibilityNodeInfoCompat;
import com.dcloud.android.v4.view.accessibility.AccessibilityNodeProviderCompat;

public class AccessibilityDelegateCompat {
    private static final Object DEFAULT_DELEGATE = IMPL.newAccessiblityDelegateDefaultImpl();
    private static final AccessibilityDelegateImpl IMPL;
    final Object mBridge = IMPL.newAccessiblityDelegateBridge(this);

    static class AccessibilityDelegateIcsImpl extends AccessibilityDelegateStubImpl {
        AccessibilityDelegateIcsImpl() {
        }

        public boolean dispatchPopulateAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent) {
            return AccessibilityDelegateCompatIcs.dispatchPopulateAccessibilityEvent(obj, view, accessibilityEvent);
        }

        public Object newAccessiblityDelegateBridge(final AccessibilityDelegateCompat accessibilityDelegateCompat) {
            return AccessibilityDelegateCompatIcs.newAccessibilityDelegateBridge(new AccessibilityDelegateCompatIcs.AccessibilityDelegateBridge() {
                public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                    return accessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
                }

                public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
                }

                public void onInitializeAccessibilityNodeInfo(View view, Object obj) {
                    accessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(view, new AccessibilityNodeInfoCompat(obj));
                }

                public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
                }

                public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
                    return accessibilityDelegateCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
                }

                public void sendAccessibilityEvent(View view, int i) {
                    accessibilityDelegateCompat.sendAccessibilityEvent(view, i);
                }

                public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
                }
            });
        }

        public Object newAccessiblityDelegateDefaultImpl() {
            return AccessibilityDelegateCompatIcs.newAccessibilityDelegateDefaultImpl();
        }

        public void onInitializeAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityDelegateCompatIcs.onInitializeAccessibilityEvent(obj, view, accessibilityEvent);
        }

        public void onInitializeAccessibilityNodeInfo(Object obj, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            AccessibilityDelegateCompatIcs.onInitializeAccessibilityNodeInfo(obj, view, accessibilityNodeInfoCompat.getInfo());
        }

        public void onPopulateAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityDelegateCompatIcs.onPopulateAccessibilityEvent(obj, view, accessibilityEvent);
        }

        public boolean onRequestSendAccessibilityEvent(Object obj, ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            return AccessibilityDelegateCompatIcs.onRequestSendAccessibilityEvent(obj, viewGroup, view, accessibilityEvent);
        }

        public void sendAccessibilityEvent(Object obj, View view, int i) {
            AccessibilityDelegateCompatIcs.sendAccessibilityEvent(obj, view, i);
        }

        public void sendAccessibilityEventUnchecked(Object obj, View view, AccessibilityEvent accessibilityEvent) {
            AccessibilityDelegateCompatIcs.sendAccessibilityEventUnchecked(obj, view, accessibilityEvent);
        }
    }

    interface AccessibilityDelegateImpl {
        boolean dispatchPopulateAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent);

        AccessibilityNodeProviderCompat getAccessibilityNodeProvider(Object obj, View view);

        Object newAccessiblityDelegateBridge(AccessibilityDelegateCompat accessibilityDelegateCompat);

        Object newAccessiblityDelegateDefaultImpl();

        void onInitializeAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent);

        void onInitializeAccessibilityNodeInfo(Object obj, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat);

        void onPopulateAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent);

        boolean onRequestSendAccessibilityEvent(Object obj, ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent);

        boolean performAccessibilityAction(Object obj, View view, int i, Bundle bundle);

        void sendAccessibilityEvent(Object obj, View view, int i);

        void sendAccessibilityEventUnchecked(Object obj, View view, AccessibilityEvent accessibilityEvent);
    }

    static class AccessibilityDelegateJellyBeanImpl extends AccessibilityDelegateIcsImpl {
        AccessibilityDelegateJellyBeanImpl() {
        }

        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(Object obj, View view) {
            Object accessibilityNodeProvider = AccessibilityDelegateCompatJellyBean.getAccessibilityNodeProvider(obj, view);
            if (accessibilityNodeProvider != null) {
                return new AccessibilityNodeProviderCompat(accessibilityNodeProvider);
            }
            return null;
        }

        public Object newAccessiblityDelegateBridge(final AccessibilityDelegateCompat accessibilityDelegateCompat) {
            return AccessibilityDelegateCompatJellyBean.newAccessibilityDelegateBridge(new AccessibilityDelegateCompatJellyBean.AccessibilityDelegateBridgeJellyBean() {
                public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                    return accessibilityDelegateCompat.dispatchPopulateAccessibilityEvent(view, accessibilityEvent);
                }

                public Object getAccessibilityNodeProvider(View view) {
                    AccessibilityNodeProviderCompat accessibilityNodeProvider = accessibilityDelegateCompat.getAccessibilityNodeProvider(view);
                    if (accessibilityNodeProvider != null) {
                        return accessibilityNodeProvider.getProvider();
                    }
                    return null;
                }

                public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.onInitializeAccessibilityEvent(view, accessibilityEvent);
                }

                public void onInitializeAccessibilityNodeInfo(View view, Object obj) {
                    accessibilityDelegateCompat.onInitializeAccessibilityNodeInfo(view, new AccessibilityNodeInfoCompat(obj));
                }

                public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.onPopulateAccessibilityEvent(view, accessibilityEvent);
                }

                public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
                    return accessibilityDelegateCompat.onRequestSendAccessibilityEvent(viewGroup, view, accessibilityEvent);
                }

                public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                    return accessibilityDelegateCompat.performAccessibilityAction(view, i, bundle);
                }

                public void sendAccessibilityEvent(View view, int i) {
                    accessibilityDelegateCompat.sendAccessibilityEvent(view, i);
                }

                public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
                    accessibilityDelegateCompat.sendAccessibilityEventUnchecked(view, accessibilityEvent);
                }
            });
        }

        public boolean performAccessibilityAction(Object obj, View view, int i, Bundle bundle) {
            return AccessibilityDelegateCompatJellyBean.performAccessibilityAction(obj, view, i, bundle);
        }
    }

    static class AccessibilityDelegateStubImpl implements AccessibilityDelegateImpl {
        AccessibilityDelegateStubImpl() {
        }

        public boolean dispatchPopulateAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent) {
            return false;
        }

        public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(Object obj, View view) {
            return null;
        }

        public Object newAccessiblityDelegateBridge(AccessibilityDelegateCompat accessibilityDelegateCompat) {
            return null;
        }

        public Object newAccessiblityDelegateDefaultImpl() {
            return null;
        }

        public void onInitializeAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent) {
        }

        public void onInitializeAccessibilityNodeInfo(Object obj, View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        }

        public void onPopulateAccessibilityEvent(Object obj, View view, AccessibilityEvent accessibilityEvent) {
        }

        public boolean onRequestSendAccessibilityEvent(Object obj, ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
            return true;
        }

        public boolean performAccessibilityAction(Object obj, View view, int i, Bundle bundle) {
            return false;
        }

        public void sendAccessibilityEvent(Object obj, View view, int i) {
        }

        public void sendAccessibilityEventUnchecked(Object obj, View view, AccessibilityEvent accessibilityEvent) {
        }
    }

    static {
        int i = Build.VERSION.SDK_INT;
        if (i >= 16) {
            IMPL = new AccessibilityDelegateJellyBeanImpl();
        } else if (i >= 14) {
            IMPL = new AccessibilityDelegateIcsImpl();
        } else {
            IMPL = new AccessibilityDelegateStubImpl();
        }
    }

    public boolean dispatchPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        return IMPL.dispatchPopulateAccessibilityEvent(DEFAULT_DELEGATE, view, accessibilityEvent);
    }

    public AccessibilityNodeProviderCompat getAccessibilityNodeProvider(View view) {
        return IMPL.getAccessibilityNodeProvider(DEFAULT_DELEGATE, view);
    }

    /* access modifiers changed from: package-private */
    public Object getBridge() {
        return this.mBridge;
    }

    public void onInitializeAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        IMPL.onInitializeAccessibilityEvent(DEFAULT_DELEGATE, view, accessibilityEvent);
    }

    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        IMPL.onInitializeAccessibilityNodeInfo(DEFAULT_DELEGATE, view, accessibilityNodeInfoCompat);
    }

    public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
        IMPL.onPopulateAccessibilityEvent(DEFAULT_DELEGATE, view, accessibilityEvent);
    }

    public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
        return IMPL.onRequestSendAccessibilityEvent(DEFAULT_DELEGATE, viewGroup, view, accessibilityEvent);
    }

    public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        return IMPL.performAccessibilityAction(DEFAULT_DELEGATE, view, i, bundle);
    }

    public void sendAccessibilityEvent(View view, int i) {
        IMPL.sendAccessibilityEvent(DEFAULT_DELEGATE, view, i);
    }

    public void sendAccessibilityEventUnchecked(View view, AccessibilityEvent accessibilityEvent) {
        IMPL.sendAccessibilityEventUnchecked(DEFAULT_DELEGATE, view, accessibilityEvent);
    }
}
