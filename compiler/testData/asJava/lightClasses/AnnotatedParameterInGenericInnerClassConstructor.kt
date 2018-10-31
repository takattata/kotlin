// test.AnnotatedParameterInGenericInnerClassConstructor
package test

annotation class Anno(val x: String)

class AnnotatedParameterInGenericInnerClassConstructor {

    inner class Inner<T>(@Anno("a") a: T, @Anno("b") b: String) {

    }
}