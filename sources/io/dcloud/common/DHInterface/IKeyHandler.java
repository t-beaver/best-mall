package io.dcloud.common.DHInterface;

import android.view.KeyEvent;
import io.dcloud.common.DHInterface.ISysEventListener;

public interface IKeyHandler {
    boolean onKeyEventExecute(ISysEventListener.SysEventType sysEventType, int i, KeyEvent keyEvent);
}
