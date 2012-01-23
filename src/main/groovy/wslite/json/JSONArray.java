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

import wslite.json.internal.JSONException;

import java.util.*;

public class JSONArray implements List {

    private wslite.json.internal.JSONArray wrapped;

    public JSONArray() {
        wrapped = new wslite.json.internal.JSONArray(); 
    }

    public JSONArray(String source) throws JSONException {
        wrapped = new wslite.json.internal.JSONArray(source);
    }

    public JSONArray(Collection c) {
        this();
        for (Object o : c) {
            add(o);
        }
    }
    
    public JSONArray(Object[] objects) {
        this();
        for (Object o : objects) {
            add(o);
        }
    }

    protected JSONArray(wslite.json.internal.JSONArray o) {
        wrapped = o;
    }

    public int size() {
        return wrapped.length();
    }

    public boolean isEmpty() {
        return wrapped.length() == 0;
    }

    public boolean contains(Object o) {
        return asList().contains(wrap(o));
    }

    public Iterator iterator() {
        return null;  // TODO: implement me
    }

    public Object[] toArray() {
        return asList().toArray();
    }

    public boolean add(Object o) {
        return wrapped.put(wrap(o)) != null;
    }

    public boolean remove(Object o) {
        int idx = asList().indexOf(wrap(o));
        if (idx == -1) {
            return false;
        }
        return wrapped.remove(idx) != null;
    }

    public boolean containsAll(Collection objects) {
        for (Object o : objects) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection collection) {
        boolean success = true;
        for (Object o : collection) {
            if (!add(o)) {
                success = false;
            }
        }
        return success;
    }

    public boolean addAll(int i, Collection collection) {
        List ary = asList();
        List wrappedCollection = new ArrayList();
        for (Object o : collection) {
            wrappedCollection.add(wrap(o));
        }
        if (ary.addAll(i, collection)) {
            wrapped = new wslite.json.internal.JSONArray(ary);
            return true;
        } else {
            return false;
        }
    }

    public boolean removeAll(Collection objects) {
        boolean success = true;
        for (Object o : objects) {
            if (!remove(wrap(o))) {
                success = false;
            }
        }
        return success;
    }

    public boolean retainAll(Collection objects) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        wrapped = new wslite.json.internal.JSONArray();
    }

    public Object get(int i) {
        return wrapped.opt(i);
    }

    public Object set(int i, Object o) {
        try {
            return wrapped.put(i, wrap(o));
        } catch (JSONException e) {
            return null;
        }
    }

    public void add(int i, Object o) {
        List ary = asList();
        ary.add(i, wrap(o));
        wrapped = new wslite.json.internal.JSONArray(ary);
    }

    public Object remove(int i) {
        return wrapped.remove(i);
    }

    public int indexOf(Object o) {
        return asList().indexOf(wrap(o));
    }

    public int lastIndexOf(Object o) {
        return asList().lastIndexOf(wrap(o));
    }

    public ListIterator listIterator() {
        throw new UnsupportedOperationException(); // TODO: implement me
    }

    public ListIterator listIterator(int i) {
        throw new UnsupportedOperationException(); // TODO: implement me
    }

    public List subList(int i, int i1) {
        throw new UnsupportedOperationException(); // TODO: implement me
    }

    public Object[] toArray(Object[] objects) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }

    protected static Object wrap(Object o) {
        if (o instanceof wslite.json.internal.JSONArray) {
            return new JSONArray((wslite.json.internal.JSONArray) o);
        }
        return JSONObject.wrap(o);
    }

    private List asList() {
        List ary = new ArrayList();
        for (int i = 0; i < wrapped.length(); i++) {
            ary.add(wrapped.opt(i));
        }
        return ary;
    }

}
