// PUSH push constant 3030
@3030
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop pointer 0
@SP
M=M-1
@SP
A=M
D=M
@THIS
M=D
// PUSH push constant 3040
@3040
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop pointer 1
@SP
M=M-1
@SP
A=M
D=M
@THAT
M=D
// PUSH push constant 32
@32
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop this 2
@SP
M=M-1
@SP
A=M
D=M
@THIS
A=M
A=A+1
A=A+1
M=D
// PUSH push constant 46
@46
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop that 6
@SP
M=M-1
@SP
A=M
D=M
@THAT
A=M
A=A+1
A=A+1
A=A+1
A=A+1
A=A+1
A=A+1
M=D
// PUSH push pointer 0
@THIS
D=M
@SP
A=M
M=D
@SP
M=M+1
// PUSH push pointer 1
@THAT
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
// PUSH push this 2
@THIS
A=M
D=A
@2
D=D+A
A=D
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
// PUSH push that 6
@THAT
A=M
D=A
@6
D=D+A
A=D
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