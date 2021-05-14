.class FindMaximum
.super java/lang/Object
.field private test_arr [I

.method public <init>()V
aload_0
invokespecial java/lang/Object.<init>()V

return
.end method

.method public build_test_arr()I
.limit stack 99
.limit locals 99

iconst_5
istore_1

iload_1
newarray int
astore_2

aload_0
aload_2
putfield FindMaximum/test_arr [I

aload_0
getfield FindMaximum/test_arr [I
astore 4

iconst_0
istore 5

aload 4
iload 5
bipush 14
iastore

aload 4
iload 5
iaload
invokestatic io.println(I)V

iconst_0
ireturn

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 99
.limit locals 99

new FindMaximum
dup
invokespecial FindMaximum.<init>()V
astore_1

aload_1
invokevirtual FindMaximum.build_test_arr()I


return

.end method
