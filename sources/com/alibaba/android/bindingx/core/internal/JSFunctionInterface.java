package com.alibaba.android.bindingx.core.internal;

import java.util.ArrayList;
import org.json.JSONException;

public interface JSFunctionInterface extends JSObjectInterface {
    Object execute(ArrayList<Object> arrayList) throws NumberFormatException, JSONException;
}
