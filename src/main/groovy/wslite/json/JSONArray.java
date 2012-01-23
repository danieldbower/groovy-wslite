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

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class JSONArray implements List {

    private wslite.json.internal.JSONArray wrapped;

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
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Iterator iterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object[] toArray() {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean add(Object o) {
        return wrapped.put(o) != null;
    }

    public boolean remove(Object o) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean containsAll(Collection objects) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean addAll(Collection collection) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean addAll(int i, Collection collection) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean removeAll(Collection objects) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean retainAll(Collection objects) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void clear() {
        wrapped = new wslite.json.internal.JSONArray();
    }

    public Object get(int i) {
        return wrapped.opt(i);
    }

    public Object set(int i, Object o) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void add(int i, Object o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object remove(int i) {
        return wrapped.remove(i);
    }

    public int indexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int lastIndexOf(Object o) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ListIterator listIterator() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ListIterator listIterator(int i) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public List subList(int i, int i1) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object[] toArray(Object[] objects) {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }
}
