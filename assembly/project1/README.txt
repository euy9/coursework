# Eunsol Yun	euy9@pitt.edu

Part A
	- prompts user to get 5 opcodes
	- store 1st to 5th opcode (input) in stackframe
		- subtract 20 from $sp, to create space for five words
		- Loop through to display question to user & to save each opcode
			- keep an index, which starts from 4
			- after each loop, index--
			- breaks the loop, when index==0
			- string A2 - A6 uses the same amount of memory.
				- after each loop, add 0x1E to the address to access next string
		- stack pointer points to where the 1st opcode is
 
Part B
	- completing assembly instruction
	n = 0					//number of loops
	rs = 0;			
	imm = 0;					// immediate value
	Loop 5 times to complete instruction of 1st - 5th opcode
	while (n < 5){
		print (B2)				// L10n: L100 through L105
		get nth opcode from $sp
		rt = rs + 1	
		rd = rt + 1
		if (opcode==1 || opcode==3){
			PrintFunction(opcode, rs, rt, rd, imm)
			restore nth opcode from $sp
			replace opcode with (ConvertFunction (opcode, rs, rt, rd, imm))	in stack	[machine code]
			if (peek.(n+1)th opcode == 7){
				rs+=3
			} else {
				rs+=2
			}	
		}else if (opcode==2 || opcode==4){
			PrintFunction(opcode, rs, rt, rd, imm)
			restore nth opcode from $sp
			replace opcode with (ConvertFunction (opcode, rs, rt, rd, imm))	in stack
			if (peek.(n+1)th opcode == 7){
				rs+=2
			} else {
				rs+=1
			}
			imm++
		}else if (opcode==5 || opcode==6){
			if (opcode==6){
				swap rs & rt
			}
			PrintFunction(opcode, rs, rt, rd, imm)
			restore nth opcode from $sp
			replace opcode with (ConvertFunction (opcode, rs, rt, rd, imm))	in stack
			if (opcode==6){
				swap back rs & rt
			}
			if (peek.(n+1)th opcode == 7){
				rs+=2
			} else {
				rs+=1
			}
			imm++
		}else if (opcode==7){
			PrintFunction(opcode, rs, rt, rd, imm)
			restore nth opcode from $sp
			replace opcode with (ConvertFunction (opcode, rs, rt, rd, imm))	in stack
			imm++
		}else{
			PrintFunction(opcode, rs, rt, rd, imm)
			restore nth opcode from $sp
			swap rs & rt
			replace opcode with (ConvertFunction (opcode, rs, rt, rd, imm))	in stack
			rs+=1
			imm++
		}
		to next string address in B2
		n++						// next loop
		$sp += 4				// next opcode	
	}
	
Part C
	- displaying the machine instruction
	/* use loops to repeatedly print space, comma, "$t" */
	PrintFunction (opcode, rs, rt, rd, imm){		
		LoopOpcode{				
			//find string opcode in from "opcode" table & print
		}
		index = 0;
		while (opcode==1 || opcode==3){
			if (index==0)
				print rd
			if (index==1)
				print rs
			if (index==2){}
				print rt
				return back to Part B
			}
			index++
		}
		while (opcode==2 || opcode==4 || opcode==8 || opcode==9){
			if (index==0)
				print rt
			if (index==1)
				print rs
			if (index==2){
				if (opcode==8 || opcode==9){
					print "L"
				}
				print imm
				return back to Part B
			}
			index++
		}
		while (opcode==5 || opcode==6){
			print rs + imm + "(" + rs + ")"
			return back to Part B	
		}
		while (opcode==7){
			print L + imm
			return back to Part B
		}	
	}
	
Part D
-convert to machine instruction 
	ConvertFunction(opcode, rs, rt, rd, imm){
		look up machine code of the opcode from OPcode table and save to $v1
		$v1 = [opcode | 00000 | 00000 | 00000 | 00000 | 000000]
		if (opcode==7){
			load target (0x0010 0000 + imm )
			$v1 = [opcode | target]
			return back to Part B
		}
		rs += 8							// $t0 = $8, $t1 = $9, .....
		rt += 8
		if (rs > 8)						// $t8 = $24, ....
			rs += 8
		if (rt > 8)
			rt += 8
		load $v1 = [opcode | rs | rt | 00000 | 00000 | 000000]
		
		if (opcode==1 || opcode==3){
			rd += 8
			if (rd > 8)
				rd += 8
			if (opcode==1){
				funct = 0x20
			} else {
				funct = 0x25
			}
			load rd & funct
			$v1 = [opcode | rs | rt | rd | 00000 | funct]
			return back to Part B
		}else if (opcode==8 || opcode==9){
			imm *= 4 = npc
			current *= 4 = pc
			address = (npc - pc - 4)>>2
			load to $v1 = [opcode | rs | rt | address]
			return back to Part B
		}else{
			load imm to $v1 = [opcode | rs | rt | imm]
			return back to Part B
		}
	}
	
Part E
- displaying the machine code ***extra-credit
	while (index < 5){
		print "0x"
		i = 0;
		$t6 = machine code to be printed
		while ($t6 != 0){
			$t3 = take the first 4 bit of $t6
			$t6<<4
			convert $t3 into character
			print character
			i++
		}
		while (i <= 8){
			print 0
		}
		$sp += 4		//next machine code
		index++
	}

	
	
	
	
	
	
	
	
	