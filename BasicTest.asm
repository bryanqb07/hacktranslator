// PUSH push constant 10
@10
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop local 0
local
0
// PUSH push constant 21
@21
D=A
@SP
A=M
M=D
@SP
M=M+1
// PUSH push constant 22
@22
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop argument 2
argument
2
// POP pop argument 1
argument
1
// PUSH push constant 36
@36
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop this 6
this
6
// PUSH push constant 42
@42
D=A
@SP
A=M
M=D
@SP
M=M+1
// PUSH push constant 45
@45
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop that 5
that
5
// POP pop that 2
that
2
// PUSH push constant 510
@510
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop temp 6
temp
6
// PUSH push local 0
@LCL
A=M+0
D=M
@SP
A=M
M=D
@SP
M=M+1
// PUSH push that 5
@THAT
A=M+5
D=M
@SP
A=M
M=D
@SP
M=M+1
// ARITHMETIC add
// PUSH push argument 1
@ARG
A=M+1
D=M
@SP
A=M
M=D
@SP
M=M+1
// ARITHMETIC sub
// PUSH push this 6
@THIS
A=M+6
D=M
@SP
A=M
M=D
@SP
M=M+1
// PUSH push this 6
@THIS
A=M+6
D=M
@SP
A=M
M=D
@SP
M=M+1
// ARITHMETIC add
// ARITHMETIC sub
// PUSH push temp 6
@TEMP
A=M+6
D=M
@SP
A=M
M=D
@SP
M=M+1
// ARITHMETIC add