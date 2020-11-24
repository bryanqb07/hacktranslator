// PUSH push constant 111
@111
D=A
@SP
A=M
M=D
@SP
M=M+1
// PUSH push constant 333
@333
D=A
@SP
A=M
M=D
@SP
M=M+1
// PUSH push constant 888
@888
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop static 8
@SP
M=M-1
@SP
A=M
D=M
@StaticTest.8
M=D
// POP pop static 3
@SP
M=M-1
@SP
A=M
D=M
@StaticTest.3
M=D
// POP pop static 1
@SP
M=M-1
@SP
A=M
D=M
@StaticTest.1
M=D
// PUSH push static 3
@StaticTest.3
D=M
@SP
A=M
M=D
@SP
M=M+1
// PUSH push static 1
@StaticTest.1
D=M
@SP
A=M
M=D
@SP
M=M+1
// ARITHMETIC sub
@SP
M=M-1
@SP
A=M
D=M
@SP
M=M-1
A=M
D=M-D
M=D
@SP
M=M+1
// PUSH push static 8
@StaticTest.8
D=M
@SP
A=M
M=D
@SP
M=M+1
// ARITHMETIC add
@SP
M=M-1
@SP
A=M
D=M
@SP
M=M-1
A=M
D=D+M
M=D
@SP
M=M+1