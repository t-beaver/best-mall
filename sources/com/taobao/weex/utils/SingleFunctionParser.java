package com.taobao.weex.utils;

import com.taobao.weex.utils.FunctionParser;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SingleFunctionParser<V> extends FunctionParser<String, List<V>> {

    public interface FlatMapper<V> {
        V map(String str);
    }

    public interface NonUniformMapper<V> {
        List<V> map(List<String> list);
    }

    public SingleFunctionParser(String str, final FlatMapper<V> flatMapper) {
        super(str, new FunctionParser.Mapper<String, List<V>>() {
            public Map<String, List<V>> map(String str, List<String> list) {
                HashMap hashMap = new HashMap();
                LinkedList linkedList = new LinkedList();
                for (String map : list) {
                    linkedList.add(FlatMapper.this.map(map));
                }
                hashMap.put(str, linkedList);
                return hashMap;
            }
        });
    }

    public SingleFunctionParser(String str, final NonUniformMapper<V> nonUniformMapper) {
        super(str, new FunctionParser.Mapper<String, List<V>>() {
            public Map<String, List<V>> map(String str, List<String> list) {
                HashMap hashMap = new HashMap();
                hashMap.put(str, NonUniformMapper.this.map(list));
                return hashMap;
            }
        });
    }

    public List<V> parse(String str) {
        LinkedHashMap parse = parse();
        if (parse.containsKey(str)) {
            return (List) parse.get(str);
        }
        return null;
    }
}
