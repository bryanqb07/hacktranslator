// FUNCTION function SimpleFunction.test 2
(SimpleFunction.test)
@SP
A=M
M=0
@SP
M=M+1
@SP
A=M
M=0
@SP
M=M+1
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
// PUSH push local 1
@LCL
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
// ARITHMETIC not
@SP
M=M-1
A=M
M=!M
@SP
M=M+1
// PUSH push argument 0
@ARG
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
// RETURN return
@LCL
D=M
@endframe
M=D
@5
D=D-A
@retAddr
M=D
@SP
M=M-1
@SP
A=M
D=M
@ARG
A=M
M=D
D=A
D=D+1
@SP
M=D
@endframe
D=M
@1
D=D-A
A=D
D=M
@THAT
M=D
@endframe
D=M
@2
D=D-A
A=D
D=M
@THIS
M=D
@endframe
D=M
@3
D=D-A
A=D
D=M
@ARG
M=D
@endframe
D=M
@4
D=D-A
A=D
D=M
@LCL
M=D
@retAddr
D=M
A=D
0; JMP