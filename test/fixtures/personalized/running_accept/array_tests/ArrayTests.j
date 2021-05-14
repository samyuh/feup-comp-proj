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

bipush 10
istore_1

iload_1
newarray int
astore_2

iconst_1
istore 4

aload_2
iload 4
iconst_2
iastore

iload 4
invokestatic io.println(I)V
return

.end method
