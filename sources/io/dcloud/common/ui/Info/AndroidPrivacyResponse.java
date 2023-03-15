package io.dcloud.common.ui.Info;

import io.dcloud.common.DHInterface.IReflectAble;

public class AndroidPrivacyResponse implements IReflectAble {
    public String buttonAccept;
    public String buttonRefuse;
    public disagreeModeDTO disagreeMode = new disagreeModeDTO();
    public String hrefLoader = "default";
    public String message;
    public String prompt;
    public SecondDTO second = new SecondDTO();
    public StylesDTO styles = new StylesDTO();
    public String title;
    public String version;

    public static class SecondDTO implements IReflectAble {
        public String buttonAccept;
        public String buttonRefuse;
        public String message;
        public String title;
    }

    public static class StylesDTO implements IReflectAble {
        public String backgroundColor;
        public String borderRadius;
        public ButtonAcceptDTO buttonAccept;
        public ButtonRefuseDTO buttonRefuse;
        public ButtonRefuseDTO buttonVisitor;
        public ContentDTO content;
        public TitleDTO title;

        public static class ButtonAcceptDTO implements IReflectAble {
            public String color;
        }

        public static class ButtonRefuseDTO implements IReflectAble {
            public String color;
        }

        public static class ContentDTO implements IReflectAble {
            public String color;
        }

        public static class TitleDTO implements IReflectAble {
            public String color;
        }
    }

    public static class disagreeModeDTO implements IReflectAble {
        public boolean loadNativePlugins = true;
        public boolean showAlways = false;
        public boolean support = false;
        public boolean visitorEntry = false;
    }
}
