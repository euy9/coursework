# Eunsol Yun	euy9@pitt.edu

# Part A: prompts to get 5 opcodes 
li	$v0, 4			# print A1
la	$a0, A1
syscall
la	$a0, A2			# print A2
syscall

addi	$sp, $sp, -20		# store inputs in stack frame
addi	$t0, $0, 4		# loop = 4
LoopA:				# ask user for 5 inputs
	li	$v0, 5		# input nth opcode	
	syscall
	sb	$v0, 0($sp)	# store nth opcode
	beq	$t0, $0, BreakA	# if loop = 0, break
	addi	$sp, $sp, 4
	li	$v0, 4		# print string A#
	addi	$a0, $a0, 0x1E	# next string A#
	syscall
	addi	$t0, $t0, -1	# loop--
	j	LoopA
BreakA:	addi	$sp, $sp, -16	# point to the 1st opcode

###################################################################
# Part B: completing assembly instruction
li	$v0, 4			# print B1
la	$a0, B1
syscall
la	$s0, B2			# load address of B2 = $s0
add	$s2, $0, $0		# set $s2 = 0 (# of loops made)
				# set up arguments
add	$a1, $0, $0		# $a1 = rs = 0
add	$s1, $0, $0		# $s1 = imm = 0
LoopB:
	beq	$s2, 5, BreakB		# went through all five inputs
	li	$v0, 4			# print the next string B#
	move	$a0, $s0			# $a0 = address of 
	syscall
	li	$v0, 11			# print tab
	li	$a0, 9
	syscall
	lb	$a0, 0($sp)		# $a0 = nth opcode
	addi	$a2, $a1, 1		# $a2 = rt = rs + 1
	addi	$a3, $a2, 1		# $a3 = rd = rt + 1
	# opcode==1 || opcode==3 (add / or)
	beq	$a0, 1, Nextadd		# if opcode == 1
	bne	$a0, 3, Next1		# if opcode == 3
Nextadd:	jal	PrintFunction
	lb	$a0, 0($sp)		# restore $a0 = opcode
	jal	ConvertFunction
	sw	$v1, 0($sp)		# store machinecode onto stack
	lw	$t5, 4($sp)		# peek at the next opcode
	bne	$t5, 7, NEXT1
	addi	$a1, $a1, 1		# rs += 1
NEXT1:	addi	$a1, $a1, 2		# rs += 2
	j	Next5
	# opcode==2 || opcode==4 (addi / ori)
Next1:	beq	$a0, 2, Nextadi		# if opcode == 2
	bne	$a0, 4, Next2		# if opcode == 4
Nextadi:	jal	PrintFunction
	lb	$a0, 0($sp)		# restore $a0 = opcode
	jal	ConvertFunction
	sw	$v1, 0($sp)		# store machinecode onto stack
	lw	$t5, 4($sp)		# peek at the next opcode
	bne	$t5, 7, NEXT2
	addi	$a1, $a1, 1		# rs += 1
NEXT2:	addi	$a1, $a1, 1		# rs += 1
	addi	$s1, $s1, 1		# imm++
	j	Next5
	# opcode==5 || opcode==6 (lw / sw)
Next2:	beq	$a0, 5, Nextlw		# if opcode == 5
	bne	$a0, 6, Next3		# if opcode == 6
	move	$t6, $a1			# rs($a1) <-> rt($a2)
	move	$a1, $a2
	move	$a2, $t6
Nextlw:	jal	PrintFunction
	lb	$a0, 0($sp)		# restore $a0 = opcode
	jal	ConvertFunction
	sw	$v1, 0($sp)		# store machinecode onto stack
	beq	$a0, 5, NEXT3		# swap back rs($a1) <-> rt($a2)
	move	$t6, $a1			# rs($a1) <-> rt($a2)
	move	$a1, $a2
	move	$a2, $t6
NEXT3:	lw	$t5, 4($sp)		# peek at the next opcode
	bne	$t5, 7, NEXT4
	addi	$a1, $a1, 1		# rs += 1
NEXT4:	add	$a1, $a1, 1		# rs += 1
	addi	$s1, $s1, 1		# imm++
	j	Next5
	# opcode==7 (j)
Next3:	bne	$a0, 7, Next4		# if opcode == 7
	jal	PrintFunction
	lb	$a0, 0($sp)		# restore $a0 = opcode
	jal	ConvertFunction
	sw	$v1, 0($sp)		# store machine code onto stack	
NEXT:	addi	$s1, $s1, 1		# imm++
	j	Next5	
	# opcode==8 || opcode==9 (bne / beq)
Next4:	jal	PrintFunction
	lb	$a0, 0($sp)		# restore $a0 = opcode
	move	$t6, $a1			# rs($a1) <-> rt($a2)
	move	$a1, $a2
	move	$a2, $t6
	jal	ConvertFunction
	sw	$v1, 0($sp)		# store machinecode onto stack
	addi	$a1, $a1, 1		# rs += 1  (+2, but b/c of the swap)
	addi	$s1, $s1, 1		# imm++
Next5:					# next Loop
	addi	$s0, $s0, 6		# +6; to next string (B#)
	addi	$s2, $s2, 1		# loop++
	addi	$sp, $sp, 4		# to access next opcode
	li	$v0, 11			# print \n
	li	$a0, 10
	syscall
	j	LoopB
BreakB:	

###################################################################				
# Part E: displaying the converted machine instruction
li	$v0, 4				# print E1
la	$a0, E1
syscall
li	$v0, 11				# print \n
li	$a0, 10
syscall

	addi	$sp, $sp, -20		# point to 1st opcode
	add	$t0, $0, $0		# index = $t0 = 0
ELoop:	beq	$t0, 5, Exit
	add	$t8, $0, $0		# index (i) for printing hexadecimal
	li	$v0, 11			# print \t
	li	$a0, 9
	syscall	
	li	$a0, 48			# print 0x
	syscall
	li	$a0, 120
	syscall
	
	### extra-credit print machine code without using syscall 34
		lw	$t6, 0($sp)		# $t6 = machine code to be printed
		li	$t1, 58          		# $t1 = 58
		la	$t2, 0xf0000000  		# $t2 = 0xf000 0000

	ABC:	beq	$t6, $0, exit		# if $t6 = 0, done
		and	$t3, $t2, $t6		# $t3 = mask the last 28 bit of $t6
		sll	$t6, $t6, 4		# $t6<<4, to access the next 4 bits
		srl	$t3, $t3, 28		# move $t3 to be in the LSB
		addi	$t3, $t3, 48		# convert to character
		slt	$t5, $t3, $t1
		bne	$t5, $0, print
		addi	$t3, $t3, 39
	print:	move	$a0, $t3			# print character
		li	$v0, 11		
		syscall
		addi	$t8, $t8, 1		# i++
		j	ABC
	exit:	beq	$t8, 8, realExit		# fill the rest with "0"
		li	$v0, 11			# print "0"
		li	$a0, 48
		syscall
		addi	$t8, $t8, 1		# i++	
		j	exit
	realExit:
	###
	li	$v0, 11			# \n
	li	$a0, 10
	syscall
	addi	$sp, $sp, 4
	addi	$t0, $t0, 1
	j	ELoop
Exit:	li	$v0, 10
	syscall
	
	

###################################################################
# Part C: displaying assembly instruction
PrintFunction:
	# print "opcode"
	move	$t0, $a0			# $t0 = $a0 = opcode
	la	$t2, opcode		# $t2 = address of "opcode" table
	add	$t3, $0, 1		# $t3 = index = 1
	LookOpcode:
		lbu	$t4, 0($t2)		# $t4 = "opcode"
		beq	$t3, $t0, Done		# if index($t3) == opcode($t0), print
		addi	$t2, $t2, 1		# next char on the table
		bne	$t4, $0, LookOpcode
		addi	$t3, $t3, 1		# index++
		j	LookOpcode	
	Done:	li	$v0, 4
		move	$a0, $t2
		syscall
	
	# print the rest
	li	$v0, 11				# print space
	add	$a0, $0, 32
	syscall
	
	# if add(1) / or(3)
	add	$t1, $0, $0 			# index  = $t1 = 0
	beq	$t0, 1, NextA
	bne	$t0, 3, NextC
NextA:	li	$v0, 4				# print "$t"
	la	$a0, $t
	syscall 
	li	$v0, 1				# print int
	beq	$t1, $0, prtRd1			# if index = 0, print rd
	beq	$t1, 1, prtRs1			# if index = 1, print rs
	beq	$t1, 2, prtRt1			# if index = 2, print rt
prtRd1:	add	$a0, $0, $a3			# print rd
	syscall
	j	NextB
prtRs1:	add	$a0, $0, $a1			# print rs
	syscall
	j	NextB
prtRt1:	add	$a0, $0, $a2			# print rt
	syscall
	jr	$ra
NextB:	li	$v0, 11				# print ","
	addi	$a0, $0, 44
	syscall
	addi	$a0, $0, 32			# print space
	syscall
	addi	$t1, $t1, 1			# index++
	j	NextA
		
	# if addi(2) / ori(4) / bne(8) / beq(9)
NextC:	add	$t1, $0, $0			# $t1 = index = 0
	beq	$t0, 2, NextD			# if $t0 = opcode = 2
	beq	$t0, 4, NextD			# if $t0 = opcode = 4
	beq	$t0, 8, NextD			# if $t0 = opcode = 8
	beq	$t0, 9, NextD			# if $t0 = opcode = 9
	j	NextF
NextD:	beq	$t1, 2, prtImm2			# if index = 2, print imm
	li	$v0, 4				# print "$t"
	la	$a0, $t
	syscall 
	li	$v0, 1				# print int
	beq	$t1, $0, prtRt2			# if index = 0, print rt
	beq	$t1, 1, prtRs2			# if index = 1, print rs
prtRt2:	add	$a0, $0, $a2			# print rt
	syscall
	j	NextE
prtRs2:	add	$a0, $0, $a1			# print rs
	syscall
	j	NextE
prtImm2:	li	$v0, 1				# print imm
	beq	$t0, 2, noL			# skip "L" for opcode 2 and 4
	beq	$t0, 4, noL
	li	$v0, 11				# print "L"
	addi	$a0, $0, 76
	syscall
	li	$v0, 1
noL:	addi	$a0, $0, 10			# print "10"
	syscall
	add	$a0, $0, $s1			# print imm val
	syscall
	jr	$ra
NextE:	li	$v0, 11				# print ","
	addi	$a0, $0, 44
	syscall
	addi	$a0, $0, 32			# print space
	syscall
	addi	$t1, $t1, 1			# index++
	j	NextD		
				
	# if lw(5) / sw(6)
NextF:	beq	$t0, 5, NextG			# if $t0 = opcode = 5
	beq	$t0, 6, NextG			# if $t0 = opcode = 6
	j	NextH
NextG:	li	$v0, 4				# print "$t"
	la	$a0, $t
	syscall 
	li	$v0, 1				# print rt
	add	$a0, $0, $a2			
	syscall
	li	$v0, 11				# print ","
	addi	$a0, $0, 44
	syscall
	addi	$a0, $0, 32			# print space
	syscall
	li	$v0, 1				# print imm
	addi	$a0, $0, 10			
	syscall
	add	$a0, $0, $s1
	syscall
	li	$v0, 11				# pring "("
	addi	$a0, $0, 40
	syscall
	li	$v0, 4				# print "$t"
	la	$a0, $t
	syscall 
	li	$v0, 1				# print rs
	add	$a0, $0, $a1
	syscall
	li	$v0, 11				# print ")"
	addi	$a0, $0, 41
	syscall
	jr	$ra
	
	# if j(7)
NextH:	li	$v0, 11				# print "L"
	addi	$a0, $0, 76
	syscall
	li	$v0, 1				# print imm
	addi	$a0, $0, 10
	syscall
	add	$a0, $0, $s1
	syscall
	jr	$ra
	
	
###################################################################
# Part D: convert to machine instruction and return it
ConvertFunction:
	# converts opcode (first 6 bit)
	la	$t2, OPcode			# $t2 = address of OPcode table
	addi	$t2, $t2, -1
	add	$t2, $t2, $a0			# lookup opcode machine code
	lb	$v1, 0($t2)
	sll	$v1, $v1, 26			# $v1 = opcode | 00000000...

	# if j(7)
	bne	$a0, 7, Move1
	addi	$t1, $0, 0x00100000		# load target
	or	$t2, $t1, $s1			
	or	$v1, $v1, $t2			# $v1 = opcode | target
	jr	$ra
	
	# if opcode != 7
Move1:	move	$t3, $a1				# tempVal = $t3 = rs ($a1)
	slti	$t5, $t3, 8			# if rs < 8
	bne	$t5, $0, Move4
	addi	$t3, $t3, 8
Move4:	addi	$t3, $t3, 8
	sll	$t3, $t3, 21
	move	$t4, $a2				# tempVal = $t4 = rt ($a2)
	slti	$t5, $t4, 8			# if rt < 8
	bne	$t5, $0, Move5
	addi	$t4, $t4, 8
Move5:	addi	$t4, $t4, 8
	sll	$t4, $t4, 16
	or	$t3, $t3, $t4			
	or	$v1, $v1, $t3			# $v1 = opcode | rs | rt | 00000 | 00000 | 000000  [first half of code]
	
	##### the second half
	# if add(1) / or(3)
	add	$t7, $0, $0			# reset $t7 = 0
	beq	$a0, 1, Move2
	bne	$a0, 3, Move7
	addi	$t7, $0, 0x05			# $t7 saves the funct
Move2:	addi	$t7, $t7, 0x20			# if OP==1, $t7 = 0x20;  if OP==3, $t7 = 0x25
	move	$t3, $a3				# tempVal = #t3 = rd ($a3)
	slti	$t5, $t3, 8			# if rd < 8
	bne	$t5, $0, Move6
	addi	$t3, $t3, 8
Move6:	addi	$t3, $t3, 8
	sll	$t3, $t3, 11
	or	$t3, $t3, $t7
	or	$v1, $v1, $t3			# $v1 = opcode | rs | rt | rd | 00000 | funct
	jr	$ra
	
	# if beq(8) / bne(9)
Move7:	slti	$t0, $a0, 8
	bne	$t0, $0, Move8
	sll	$t2, $s2, 2			# multiply by 4 = pc
	sll	$t1, $s1, 2			# multiply by 4 = npc
	sub	$t1, $t1, $t2			# $t1 = $t1 - $t2
	subi	$t1, $t1, 4			# $t1 = $t1 - 4
	srl	$t1, $t1, 2
	andi	$t1, $t1, 0x0000ffff		# hide the first 16 bits
	or	$v1, $v1, $t1			# $v1 = opcode | rs | rt | address
	jr	$ra
	
	
	# if addi(2) / ori(4) / lw(5) / sw(6)
Move8: 	addi	$t1, $s1, 100			# $t1 = 100 + imm
	or	$v1, $v1, $t1			# $v1 = opcode | rs | rt | imm
	jr	$ra


###################################################################
.data
	A1:	.asciiz	"Welcome to Auto Coder! \nThe opcode (1-9 : 1=add, 2=addi, 3=or, 4=ori, 5=lw, 6=sw, 7=j, 8=beq, 9=bne)\n"
	A2:	.asciiz	"please enter the 1st opcode: "
	A3:	.asciiz	"please enter the 2nd opcode: "
	A4:	.asciiz	"please enter the 3rd opcode: "
	A5:	.asciiz	"please enter the 4th opcode: "
	A6:	.asciiz	"please enter the 5th opcode: "
	
	B1:	.asciiz	"The completed code is\n"
	B2:	.asciiz 	"L100:", "L101:", "L102:", "L103:", "L104:"
	
	E1:	.asciiz	"\nThe machine code is "
	
	opcode:	.asciiz	"add", "addi", "or", "ori", "lw", "sw", "j", "beq", "bne"
	OPcode:	.byte	0x00, 0x08, 0x00, 0x0d, 0x23, 0x2b, 0x02, 0x04, 0x05
	$t:	.asciiz	"$t"
	
