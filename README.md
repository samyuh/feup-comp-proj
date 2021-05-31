## GROUP: T3G1

- Diana Freitas, NUMBER: up201806230, GRADE: 19, CONTRIBUTION: 25%
- Diogo Fernandes, NUMBER: up201806250, GRADE: 19, CONTRIBUTION: 25%
- Hugo Guimarães, NUMBER: up201806490, GRADE: 19, CONTRIBUTION: 25%
- Juliane Marubayashi, NUMBER: up201800175, GRADE: 19, CONTRIBUTION: 25%

GLOBAL Grade of the project: 19

### Summary:
This project consists in a compiler tool to parse the J-- language for java bytecode, which can be executed using the [jasmin library](http://jasmin.sourceforge.net/).   
This tool also has options for code optimization, which can be applied making use of the `-o` and `-r=x` options.


### Dealing with syntatic errors:
The syntatic error recovering was applied for while statments. The following code shows the recovery is done:

```java 
<WHILE> <OPENCURVEBRACKET>
    try{
         AndExpression() <CLOSECURVEBRACKET>
    } catch(ParseException e){
        hasError = true;
        int lineError = e.getStackTrace()[0].getLineNumber();
        String message = e.getMessage();
        reportList.add(new Report(ReportType.ERROR, Stage.SYNTATIC, lineError, message));

        Token t;
        int counter = 0;
        while(true) {
           t = getToken(1);

           if (t.kind != OPENBRACKET) getNextToken();
           else break;  // found a {

           // Consecutives ). Init new while.
           if (t.kind == CLOSECURVEBRACKET) break;
        }

        // Consume all consecutives ) if exists.
        while(t.kind == CLOSECURVEBRACKET){
            t = getToken(1);
            if (t.kind == CLOSECURVEBRACKET) t = getNextToken();
        }

    }
     Statement() #WhileBody
```
Putting it into words, once there's an error in the while loop statment the catch block will be executed. Inside it, a report will be added to the reports list indicating the it has occurred an error. Besides, the parser will start re-executing the from the first token that is not part of the while statment, so as to try to find more errors.
This process is done to maximize the number of errors displayed for the programmer in the terminal.



### Semantic Analysis: (Refer the semantic rules implemented by your tool.)
The goals of the Semantic Analysis are to verify if the program is according to the definitions of the programming language, by reporting the semantic errors with useful messages to the user.

Our compiler implments the following semantic rules:
1. __Type Verification__:
- The boolean operations && and ! must be between boolean operands;
- The boolean operation < must be between integers;
- Arithmetic operations +, -, *, / must be between integers;
- The type of the assignee must be equal to the type of the assigned (a_int = b_boolean not allowed);
- Conditional expressions (if e while) result in a boolean value.
2. __Array Access Verification__:
- It is not possible to use array access directly to arithmetic operations;
- Ensure that an array access is done to an array (e.g. true[1] is not allowed);
- Verify if the index of an array access is an integer (e.g. a[true])
3. __Method Verification__
- Verifing if the object of a certain class contains the method invoked;
- Case the method invoked is from the current class (call the method using this `this`) and there's no extends, then the code returns an error. Otherwise it's assumed that it's from the super class.
- Case the method is not from a declared class (a importeded class), it's assumed as existent and the exptected types are assumed. (i.e. a = Foo.b(), if a is an integer and Foo is a imported class, it's assumed that the method b is static, since we are accessing a method directly from teh class that doesn't have arguments and return an integer);
- Verifying if the number of arguments is equals the number of parameters in the declaration.
- Verifying if the type of parameters are equals the type of the arguments.
4. __Array Initialization__:
- The size of array should be and integer when creating a new one (e.g. a = new int[0];)
5. __Class declaration__:
- The class extended needs to be imported.
6. __Length__
Builtin "length" only exists over arrays.


### Code Generation: (describe how the code generation of your tool works and identify the possible problems your tool has regarding code generation.)

#### OLLIR Generation
The AST is recursively converted to OLLIR (Optimized Low-Level Intermediate Representation).
We created the class OllirEmitter, in which there is a function for converting each type of expression and statement. Auxiliar variables are created when necessary.

The optimizations we did to the code were the following:
##### Constant Propagation:
Before generating the Ollir code, a ConstantPropagationVisitor, which extends the AJmmVisitor, was used to replace the values of constants in expressions.
This visitor is applied until there are no changes in the AST.

##### While loops

Example of an optimized While Loop:

```java
if(!condition) goto endLoop;
Loop:
    codeBlock
if(condition) goto Loop;
endLoop:
    codeBlock
```

#### Jasmin Generation

All the jasmin basic structure is being generate including the constructor, fields and methods.

More than that, assignments and arithmetic operations are converted from the OLLIR code.

Also the limit_stack and limit_locals are being calculated dinamically.
The approachs for defining each one of them is well defined:
- The limit_locals is calculated according to this formula: 1 + number of parameters + number of local variables. The number 1 stands for the register 0 which is allocated by the instance of the class: `this`.
- To calculating the limit_stack we have developed an simple algorithm to calculate this. The pseudocode for it can be checked bellow:

  ```java
      int currentSize = 0; 
      int maxUntilNow = 0; 
      for (var instruction: instructions){
          int numberOfPops = getNumberOfPops(instruction); 
          int numberOfPushes = getNumberOfPush(instruction); 
          int stackVariation = numberOfPushes - numberOfPops; 
          maxUntilNow = max(currentSize, maxUntilNow); 
          currentSize += stackVariation; 
      }
  ```

In other words, each instruction contributes for the variation of the stack size. This variation is computed in the `currentSize` variable, while the jasmin is being produced. However, in the end the max size for the variable is saved in the `maxUntilNow` variable.
By using this algorithm, it's possible to calculate the limit stack in an efficient way.

### Optimization -r
The optimization -r is a process that contains three types of algorithms:
- The dataflow analysis
- Calculating the liverange and the interferences between variables
- Apply the coloring graph

About the -r optimization it's important to discuss some aspects that might defeer from other works of this subject.
#### Live range
The concept of live ranging and how to calculate is one of the aspects that we think that might be controversial. In our project we consider that the live range is the all the instructions between the first `definition` of the variable and the last `in` of it.
The `in` was calculated in the process of dataflow.

#### Coloring heuristic
The coloring heuristics follows the algorithm apresented in the theorical classes: Kempe’s graph-coloring algorithm. The algorithm can be checked in this [link](https://www.cs.princeton.edu/~appel/Color.pdf).


### Task Distribution:
- __Checkpoint 1__:
  The first checkpoint was made in group by pair programming and sharing between the members using tools such as `code with me` from IntelliJ.
- __Checkpoint 2__:
  In the second checkpoint the group divided the tasks equally. Just the symbol table creation wasn't divided, but made by the whole group together.
- __Checkpoint 3__:
  In this checkpoint the tasks were divided in the following way:
    - __OLLIR__: Diana Freitas and Diogo Samuel
    - __JASMIN__: Hugo Guimarães and Juliane Marubayashi
- __Checkpoint 4__:
  In this checkpoint the tasks were divided in the following way:
    - __-o optimization__: Diana Freitas and Diogo Samuel
    - __-r optimization__: Juliane Marubayashi


### PROS:
We did everything that was suppose to do, trying to always use the best design patterns to make the code modular and easy to understand. We also did the optimizations -o and -r.


### CONS:
We don't sanitize the variables. For example, 'array' and '$' are reserved in OLLIR and if the code has these variables it will break and OLLIR will not run.  
The -r optimization could be better when showing error messages. Once it's not possible to make the graph coloring for certain number of registers the code generation is interrupted by an `OPTIMIZER` exception. However in some cases that the number of register is too low, another error might appear, which does not describe correctly the nature of the exception.  