// IS_APPLICABLE: false
// ERROR: Class 'C' is not abstract and does not implement abstract base class member public abstract fun foo(): Int defined in B
interface A {
    fun <caret>foo(): Int
}

class X : A {
    override fun foo() = 1
}

abstract class B : A {
    abstract override fun foo(): Int
}

class C: B() {

}