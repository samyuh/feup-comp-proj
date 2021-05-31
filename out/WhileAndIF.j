.class WhileAndIF
.super java/lang/Object

.method public <init>()V
aload_0
invokespecial java/lang/Object.<init>()V

return
.end method

.method public static main([Ljava/lang/String;)V
.limit stack 21
.limit locals 11

bipush 20
istore 6

bipush 10
istore 5

bipush 10
istore_2

iload_2
newarray int
astore_3

bipush 20
bipush 10
if_icmplt then1


else1:
iload 5
iconst_1
isub 
istore 4

goto endif1

then1:
iload 6
iconst_1
isub 
istore 4

Loop1:
endif1:
iconst_0
iconst_1
isub 
istore_2

iload_2
iload 4
if_icmpge EndLoop1


aload_3
iload 4
iload 6
iload 5
isub 
iastore

iload 4
iconst_1
isub 
istore 4

iload 6
iconst_1
isub 
istore 6

iload 5
iconst_1
isub 
istore 5

goto Loop1

EndLoop1:
iconst_0
istore 4

Loop2:
aload_3
arraylength 
istore_2

iload 4
iload_2
if_icmpge EndLoop2


aload_3
iload 4
iaload
istore_2

iload_2
invokestatic io.println(I)V


iload 4
iconst_1
iadd 
istore 4

goto Loop2

EndLoop2:
return

.end method

