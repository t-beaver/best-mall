package io.dcloud.common.DHInterface;

public interface ISysEventListener {

    public enum SysEventType {
        AllSystemEvent,
        onActivityResult,
        onCreateOptionMenu,
        onKeyDown,
        onKeyUp,
        onSaveInstanceState,
        onKeyLongPress,
        onStart,
        onResume,
        onWebAppStop,
        onWebAppReStart,
        onWebAppSaveState,
        onWebAppTrimMemory,
        onWebAppBackground,
        onWebAppPause,
        onWebAppForeground,
        onWebAppSrcUpZip,
        onStop,
        onPause,
        onDeviceNetChanged,
        onSimStateChanged,
        onNewIntent,
        onConfigurationChanged,
        onKeyboardShow,
        onKeyboardHide,
        onRequestPermissionsResult,
        onSplashclosed,
        onSizeChanged
    }

    boolean onExecute(SysEventType sysEventType, Object obj);
}
