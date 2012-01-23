/* Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package wslite.json;

import java.util.*;
import groovy.lang.GString;
import wslite.json.internal.JSONException;

public class JSONObject implements Map {

    private wslite.json.internal.JSONObject wrapped;
    
    public JSONObject() {
        wrapped = new wslite.json.internal.JSONObject();
    }
    
    public JSONObject(String s) throws JSONException {
        wrapped = new wslite.json.internal.JSONObject(s);
    }
    
    public JSONObject(Object o) throws JSONException {
        wrapped = new wslite.json.internal.JSONObject(wrap(o));
    }
    
    public JSONObject(Map m) {
        this();
        putAll(m);
    }

    protected JSONObject(wslite.json.internal.JSONObject o) {
        wrapped = o;
    }

    public int size() {
        return wrapped.length();
    }

    public boolean isEmpty() {
        return wrapped.length() == 0;
    }

    public boolean containsKey(Object o) {
        return (o != null) && wrapped.has(o.toString());
    }

    public boolean containsValue(Object o) {
        return values().contains(o);
    }

    public Object get(Object o) {
        return (o != null) ? wrap(wrapped.opt(o.toString())) : null;
    }

    public Object put(Object o, Object o1) {
        try {
            return wrapped.put(o.toString(), o1);
        } catch (JSONException e) {
            return null;
        }
    }

    public Object remove(Object o) {
        return (o != null) ? wrapped.remove(o.toString()) : null;
    }

    public void putAll(Map map) {
        Set entrySet = map.entrySet();
        for (Object e : entrySet) {
            Map.Entry entry = (Entry) e;
            put(wrap(entry.getKey()), wrap(entry.getValue()));
        }
    }

    public void clear() {
        wrapped = new wslite.json.internal.JSONObject();
    }

    public Set keySet() {
        if (size() == 0) {
            return Collections.EMPTY_SET;
        }
        Set keySet = new HashSet();
        Iterator keys = wrapped.keys();
        while (keys.hasNext()) {
            keySet.add(keys.next());
        }
        return keySet;
    }

    public Collection values() {
        if (size() == 0) {
            return Collections.EMPTY_LIST;
        }
        List values = new ArrayList();
        Set entries = entrySet();
        for (Object e : entries) {
            Map.Entry entry = (Entry) e;
            values.add(wrap(entry.getValue()));
        }
        return values;
    }

    public Set entrySet() {
        Set entrySet = new HashSet();
        for (Object k : keySet()) {
            Map e = new HashMap();
            e.put(k, wrap(get(k)));
            entrySet.add((Entry) e.entrySet().iterator().next());
        }
        return entrySet;
    }
    
    @Override
    public String toString() {
        return wrapped.toString();
    }

    protected static Object wrap(Object o) {
        if (o == null || o == wslite.json.internal.JSONObject.NULL) {
            return null;
        }
        if (o instanceof wslite.json.internal.JSONObject) {
            return new JSONObject((wslite.json.internal.JSONObject) o);
        }
        if (o instanceof GString) {
            return o.toString();
        }
        return o;
    }

}
