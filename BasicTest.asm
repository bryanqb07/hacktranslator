// PUSH push constant 10
@10
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop local 0
@SP
M=M-1
@SP
A=M
D=M
@LCL
A=M
M=D
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
@SP
M=M-1
@SP
A=M
D=M
@ARG
A=M
A=A+1
A=A+1
M=D
// POP pop argument 1
@SP
M=M-1
@SP
A=M
D=M
@ARG
A=M
A=A+1
M=D
// PUSH push constant 36
@36
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop this 6
@SP
M=M-1
@SP
A=M
D=M
@THIS
A=M
A=A+1
A=A+1
A=A+1
A=A+1
A=A+1
A=A+1
M=D
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
M=D
// POP pop that 2
@SP
M=M-1
@SP
A=M
D=M
@THAT
A=M
A=A+1
A=A+1
M=D
// PUSH push constant 510
@510
D=A
@SP
A=M
M=D
@SP
M=M+1
// POP pop temp 6
@SP
M=M-1
@SP
A=M
D=M
@5
A=A+1
A=A+1
A=A+1
A=A+1
A=A+1
A=A+1
M=D
// PUSH push local 0
@LCL
A=M
D=A
@0
D=D+A
A=D
D=M
@SP
A=M
M=D
@SP
M=M+1
// PUSH push that 5
@THAT
A=M
D=A
@5
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
// PUSH push argument 1
@ARG
A=M
D=A
@1
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
// PUSH push this 6
@THIS
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
// PUSH push this 6
@THIS
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
// PUSH push temp 6
@5
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