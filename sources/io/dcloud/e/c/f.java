package io.dcloud.e.c;

import io.dcloud.common.DHInterface.IPdrModule;
import io.dcloud.common.DHInterface.IPdrModulesInfo;
import io.dcloud.e.e.a;
import java.util.HashMap;
import java.util.Map;

public class f implements IPdrModulesInfo {
    public Map<String, Class<? extends IPdrModule>> getPdrModuleMap() {
        HashMap hashMap = new HashMap();
        hashMap.put("commit", a.class);
        return hashMap;
    }
}
