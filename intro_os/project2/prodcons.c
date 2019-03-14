/**
 * implementation of the producer/consumer problem using semaphores
**/

#include <stdlib.h>
#include <stdio.h>
#include <linux/unistd.h>
#include <sys/mman.h>


/* semaphore struct */
struct cs1550_sem {
	int value;
	struct Node *first;
	struct Node *last;
};

/* wrapper function up */
void up(struct cs1550_sem *sem){
	syscall(__NR_cs1550_up, sem);
}

/* wrapper function down */
void down(struct cs1550_sem *sem){
	syscall(__NR_cs1550_down, sem);
}


/* main function */
int main(int argc, char *argv[]){

	int num_prod, num_con, buf_size;


	// must have 4 arguments for number of producers, number of consumers, 
	// and size of the buffer
	if (argc != 4){
		printf("Invalid Argument!\n");
		exit(-1);
	}
	num_prod = atoi(argv[1]);
	num_con = atoi(argv[2]);
	buf_size = atoi(argv[3]);


	// store three semaphores in shared memory
	struct cs1550_sem *empty;
	struct cs1550_sem *full;
	struct cs1550_sem *mutex;
	
	empty = (struct cs1550_sem*) mmap(NULL, sizeof(struct cs1550_sem)*3, 
					PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	full = empty + 1;
	mutex = empty + 2;

	// initialize semaphores
	empty->value = 0;
	empty->first = NULL;
	empty->last = NULL;

	full->value = buf_size;
	full->first = NULL;
	full->last = NULL;

	mutex->value = 1;
	mutex->first = NULL;
	mutex->last = NULL;


	// store shared data (in, out, buf_size) in shared memory
	int *buf_size_ptr, *in_ptr, *out_ptr;

	buf_size_ptr = (int*)mmap(NULL, sizeof(int)*3, PROT_READ|PROT_WRITE, 
							MAP_SHARED|MAP_ANONYMOUS, 0, 0);
	in_ptr = buf_size_ptr + 1;
	out_ptr = buf_size_ptr + 2;

	//initialize shared data
	*buf_size_ptr = buf_size;
	*in_ptr = 0;
	*out_ptr = 0;	


	// store buffer in shared memory
	int *buf_ptr = (int*)mmap(NULL, sizeof(int)*buf_size, 
					PROT_READ|PROT_WRITE, MAP_SHARED|MAP_ANONYMOUS, 0, 0);


	// zero out buffer
	int i;
	for (i = 0; i < *buf_size_ptr; i++){
		buf_ptr[i] = 0;
	}


	// producers
	for (i = 0; i < num_prod; i++){

		// if child
		if(fork() == 0){
			
			int pitem;

			while(1) {
				// lock
				down(empty);
				down(mutex);

				// produce a pancake!
				pitem = *in_ptr;
				buf_ptr[*in_ptr] = pitem;
				printf("Chef %c Produced: Pancake%d\n", i+65, pitem);
				*in_ptr = (*in_ptr + 1) % *buf_size_ptr;

				// unlock
				up(mutex);
				up(full);
			}

		}
	}

	// consumers
	int j;
	for (j = 0; j < num_con; j++){

		// if child
		if (fork() == 0){
			int citem;

			while(1){

				// lock
				down(full);
				down(mutex);

				citem = buf_ptr[*out_ptr];
				printf("Customer %c Consumed: Pancake%d\n", j+65, citem);
				*out_ptr = (*out_ptr + 1) % *buf_size_ptr;

				// unlock
				up(mutex);
				up(empty);
			}
		}
	}

	wait(NULL);

	return 0;
}