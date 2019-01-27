#
	.data
		x: .word 2
		y: .word 0
		msg: .asciiz "Hello"
	.text
	.globl main
main:
	lw $t0, x
	addu $t1, $t0, 1
	addu $t0, $t0, $t1
	bgt $t0, $t1, cond
	j other
	cond:
		move $a0, $t1
		li $v0, 1
			syscall
	other:
		blt $t0, 10, go
		j end
	go:
		move $a0, $t0
		li $v0, 1
			syscall
		addu $t0, $t0, 1
		blt $t0, 10 go
	end:
		sw $t0, x
		sw $t1, y
		li $v0, 10
			syscall