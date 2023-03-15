package io.dcloud.common.DHInterface;

import java.util.Map;

public interface IPdrModulesInfo {
    Map<String, Class<? extends IPdrModule>> getPdrModuleMap();
}
