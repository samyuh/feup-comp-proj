.class ArrayTests
.super java/lang/Object

.method public <init>()V
aload_0
invokespecial java/lang/Object.<init>()V

return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 99
.limit locals 99

new ArrayTests
dup
invokespecial ArrayTests.<init>()V
astore_1

aload_1
invokevirtual ArrayTests.getNumber()I


return

.end method

.method public getNumber()I
.limit stack 99
.limit locals 99

iconst_4
istore_1

aload_0
invokevirtual ArrayTests.funcao2()I
istore_1

iload_1
invokestatic io.println(I)V

iconst_0
ireturn

.end method

.method public funcao2()I
.limit stack 99
.limit locals 99

iconst_2
invokestatic io.println(I)V

iconst_3
ireturn

.end method
