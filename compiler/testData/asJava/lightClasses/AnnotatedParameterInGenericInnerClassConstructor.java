public final class AnnotatedParameterInGenericInnerClassConstructor {
    public AnnotatedParameterInGenericInnerClassConstructor() { /* compiled code */ }

    public final class Inner <T> {
        public Inner(@test.Anno(x = "a") T $outer, @test.Anno(x = "b") @org.jetbrains.annotations.NotNull java.lang.String a) { /* compiled code */ }
    }
}