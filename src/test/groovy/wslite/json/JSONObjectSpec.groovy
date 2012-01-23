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
package wslite.json

import spock.lang.*

class JSONObjectSpec extends Specification {

    void 'should be a Map'() {
        expect:
        new JSONObject() instanceof Map
    }

    void 'should answer to the Groovy truth'() {
        given:
        JSONObject json1 = new JSONObject()
        JSONObject json2 = new JSONObject('''{"foo":"bar"}''')
        JSONObject json3 = new JSONObject('{}')

        expect:
        !json1
        json2
        !json3
    }

    void 'can be constructed from a GString'() {
        given:
        def someVar = 'GStrings are cool'

        when:
        JSONObject result = new JSONObject("""{"foo":"Bar says ${someVar}"}""")

        then:
        'Bar says GStrings are cool' == result.foo
        '''{"foo":"Bar says GStrings are cool"}''' == result.toString()
    }

    void 'can be constructed from a Map with GString as a value'() {
        given:
        def someVar = 'bar'
        def someMap = [foo: "foo is ${someVar}"]

        when:
        JSONObject result = new JSONObject(someMap)

        then:
        'foo is bar' == result.foo
        '''{"foo":"foo is bar"}''' == result.toString()
    }

    void 'can be constructed from a Map with GString as a key'() {
        given:
        def someVar = "foo"
        def someMap = ["${someVar}": 'bar']

        when:
        JSONObject result = new JSONObject(someMap)

        then:
        '''{"foo":"bar"}''' == result.toString()
    }

    void 'map key containing null value will not exist'() {
        given:
        def someMap = [foo: 'bar', baz: null]

        when:
        def result = new JSONObject(someMap)

        then:
        'bar' == result.foo
        null == result.baz
        !result.containsKey('baz')
        1 == result.size()
        '''{"foo":"bar"}''' == result.toString()
    }

    void 'null values'() {
        given:
        JSONObject result = new JSONObject('''{"foo":"bar","baz":null}''')

        expect:
        result.containsKey('baz')
        null == result.baz
        !result.baz
    }

    void 'size'() {
        given:
        JSONObject result = new JSONObject()
        JSONObject resultEmpty = new JSONObject('{}')
        JSONObject resultOne = new JSONObject('''{"foo":"bar"}''')
        JSONObject resultThree = new JSONObject('''{"one":1,"two":2,"three":3}''')

        expect:
        0 == result.size()
        0 == resultEmpty.size()
        1 == resultOne.size()
        3 == resultThree.size()
    }

    void 'isEmpty'() {
        JSONObject result = new JSONObject()
        JSONObject resultEmpty = new JSONObject('{}')
        JSONObject resultOne = new JSONObject('''{"foo":"bar"}''')
        JSONObject resultThree = new JSONObject('''{"one":1,"two":2,"three":3}''')

        expect:
        result.isEmpty()
        resultEmpty.isEmpty()
        !resultOne.isEmpty()
        !resultThree.isEmpty()
    }

    void 'containsKey'() {
        given:
        def someKey = "two"
        JSONObject result = new JSONObject('''{"one":1,"two":2,"three":3}''')

        expect:
        result.containsKey("one")
        result.containsKey(someKey)
        !result.containsKey("foo")
    }

    void 'containsValue'() {
        given:
        def someValue = "2"
        JSONObject result = new JSONObject('''{"one":"1","two":"2","three":"3"}''')

        expect:
        result.containsValue("1")
        result.containsValue(someValue)
        !result.containsValue("foo")
    }

    void 'get'() {
        given:
        def someKey = "3"
        JSONObject result = new JSONObject('''{"one":"1","two":"2","three":"3"}''')

        expect:
        '1' == result.get("one")
        '3' == result.get("${someKey}")
    }

    void 'put'() {
        given:
        JSONObject result = new JSONObject()

        expect:
        !true
    }

    void 'remove'() {
        given:
        def someKey = "three"
        JSONObject result = new JSONObject('''{"one":"1","two":"2","three":"3"}''')
        result.remove(someKey)

        expect:
        2 == result.size()
        !result.containsKey("three")
        !result.containsValue("3")
    }

    void 'putAll'() {
        given:
        JSONObject result = new JSONObject()

        expect:
        !true
    }

    void 'clear'() {
        given:
        JSONObject result = new JSONObject('''{"one":"1","two":"2","three":"3"}''')

        when:
        result.clear()

        then:
        0 == result.size()
        result.isEmpty()
        !result.containsKey("one")
    }

    void 'keySet'() {
        given:
        JSONObject result = new JSONObject('''{"one":"1","two":"2","three":"3"}''')

        expect:
        ['one', 'two', 'three'] as Set == result.keySet()
    }

    void 'values'() {
        given:
        JSONObject result = new JSONObject('''{"one":"1","two":"2","three":"3"}''')

        expect:
        ['1', '2', '3'] as Set == result.values()
    }

    void 'entrySet'() {
        given:
        JSONObject result = new JSONObject()

        expect:
        !true
    }

}
