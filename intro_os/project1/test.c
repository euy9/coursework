/* A test program that writes "PITT" 
   if 'q' is pressed, quit; 
   if 'w' is pressed, the program writes "PITT"
   if 'c' is pressed, it clears the screen
*/

#include "graphics.h"

int main (int argc, char **argv){

	// to initialize
	init_graphics();
	void *buf = new_offscreen_buffer();
	clear_screen(buf);

	char key;

	// write
	do {

		key = getkey();
		if (key == 'q')
			break;
		else if (key == 'w'){

			// write P
			draw_line(buf, 10, 50, 10, 430, RGB(255, 255, 0));
			draw_line(buf, 10, 50, 150, 50, RGB(255, 255, 0));
			draw_line(buf, 150, 50, 150, 200, RGB(255, 255, 0));
			draw_line(buf, 150, 200, 10, 200, RGB(255, 255, 0));
			blit(buf);
			sleep_ms(1000000);

			// write I
			draw_line(buf, 170, 50, 310, 50, RGB(0, 0, 255));
			draw_line(buf, 240, 50, 240, 430, RGB(0, 0, 255));
			draw_line(buf, 170, 430, 310, 430, RGB(0, 0, 255));
			blit(buf);
			sleep_ms(1000000);

			// write T
			draw_line(buf, 330, 50, 470, 50, RGB(255, 255, 0));
			draw_line(buf, 400, 50, 400, 430, RGB(255, 255, 0));
			blit(buf);
			sleep_ms(1000000);

			// write T
			draw_line(buf, 490, 50, 630, 50, RGB(0, 0, 255));
			draw_line(buf, 560, 50, 560, 430, RGB(0, 0, 255));
			blit(buf);
			sleep_ms(1000000);
		
		} else if (key == 'c'){
			clear_screen(buf);
			blit(buf);
		}
	} while (1);


	// to exit
	clear_screen(buf);
	blit(buf);
	exit_graphics();
	return 0;

}