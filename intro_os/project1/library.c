/**
 * contain implementations of the functions using pure Linux
 * system calls & no C Standard Library Calls
**/

#include <unistd.h>
#include <time.h>
#include <termios.h>
#include <sys/mman.h>
#include <sys/ioctl.h>
#include <linux/fb.h>
#include <fcntl.h>
#include <sys/select.h>

#include "graphics.h"


/* global variables */
int fd;					// file descriptor
unsigned short *fb;		// address that represents file
unsigned long size;						
unsigned int y_virtual;
unsigned int x_virtual;



/* initialize the graphics library */
void init_graphics(){
	struct fb_var_screeninfo vinfo;
	struct fb_fix_screeninfo finfo;
	struct termios term;
	unsigned int line_len;


	// open the graphics device
	fd = open("/dev/fb0", O_RDWR);

	// find resolution & bit-depth
	ioctl(fd, FBIOGET_VSCREENINFO, &vinfo);	// virtual resolution
	ioctl(fd, FBIOGET_FSCREENINFO, &finfo);	// bit depth

	x_virtual = vinfo.xres_virtual;
	y_virtual = vinfo.yres_virtual;
	line_len = finfo.line_length;
	size = y_virtual * line_len;

	// memory mapping - map a file into our address space
	fb = (unsigned short*)mmap(NULL, size, PROT_WRITE, MAP_SHARED, fd, 0);

	// print sting "\033[2J" to clear the scream
	write(STDOUT_FILENO, "\033[2J", 8);

	// disable keypress echo & buffer the keypresses
	ioctl(STDIN_FILENO, TCGETS, &term);	// get current mode
	term.c_lflag &= ~ICANON;	// disable canonical mode
	term.c_lflag &= ~ECHO;		// disable echo
	ioctl(STDIN_FILENO, TCSETS, &term);	// set new terminal mode
}


/* clean up before exiting program */
void exit_graphics(){
	// unmap
	munmap(fb, size);

	// re-enable keypress echo
	struct termios term;
	ioctl(STDIN_FILENO, TCGETS, &term);
	term.c_lflag |= ICANON;
	term.c_lflag |= ECHO;
	ioctl(STDIN_FILENO, TCSETS, &term);

	// close file
	close(fd);	
}


/* read a single character */
char getkey(){
	char key;
	fd_set s_rd;
	struct timeval tv;

	// watch stdin to see if there is a keypress
	FD_ZERO(&s_rd);
	FD_SET(STDIN_FILENO, &s_rd);

	// wait up to 5 seconds
	tv.tv_sec = 5;
	tv.tv_usec = 0;

	// watch stdin (fd 0)
	int ready = select(STDIN_FILENO + 1, &s_rd, NULL, NULL, &tv);

	if (ready){
		read(STDIN_FILENO, &key, sizeof(key));
	}

	return key;
}


/* program sleeps between frames of graphics being drawn */
void sleep_ms(long ms){

   if(ms < 0){		// no need to sleep
		return;
	}

   struct timespec ts;		// hold the nanoseconds we're sleeping for
   ts.tv_sec = 0;
   ts.tv_nsec = ms * 1000000;
   nanosleep(&ts, NULL);

}


/* blank out screen */
void clear_screen(void *img){
	// loop over image buffer parameter & set each byte to zero
	unsigned short *new_img = (unsigned short*)img;
	int i;
	for (i = 0; i < (x_virtual * y_virtual); i++){
		*(new_img + i) = 0;
	}
}


/* draw to "offscreen buffer" using double buffering */
void *new_offscreen_buffer(){

	// allocate a screen-sized region of our address space
	return mmap(NULL, size, PROT_WRITE, MAP_PRIVATE | MAP_ANONYMOUS, -1, 0);
}


/* a memory copy from our offscreen buffer to the framebuffer */
void blit(void *scr){
	// loop & copy every byte from scr to fb
	int i;
	unsigned short *new_scr = (unsigned short*)scr;

	for (i = 0; i < (x_virtual * y_virtual); i++){
		*(fb + i) = *(new_scr + i);
	}
}


/* main drawing */
void draw_pixel(void *img, int x, int y, color_t color){
	if (x < 0 || x > x_virtual || y < 0 || y > y_virtual){
		return;		// Out of bound
	}

	// set the pixel at coordinate (x,y)
	unsigned short *new_img = (unsigned short*)img;
	int offset = (y * x_virtual) + x;
	color_t* pixel = new_img + offset;

	// set the pixel to the specified color
	*pixel = color;
}

/* make a line from (x1, y1) to (x2,y2) using draw_pixel() 
 * source: [Bresenham's Algorithm using Integer Arithmetic] */
void draw_line(void *img, int x1, int y1, int x2, int y2, color_t color) {

    // if x1 == x2, then it does not matter what we set here
	int dx = x2 - x1;
    signed char const ix = (dx > 0) - (dx < 0);
    if (dx < 0)
    	dx = -dx;
    dx = dx << 1;
 
    // if y1 == y2, then it does not matter what we set here
    int dy = y2 - y1;
    signed char const iy = (dy > 0) - (dy < 0);
    if (dy < 0)
    	dy = -dy;
    dy = dy << 1;
 
    draw_pixel(img, x1, y1, color);
 
    if (dx >= dy) {
        // error may go below zero
        int error = dy - (dx >> 1);
 
        while (x1 != x2) {
            // reduce error, while taking into account the corner case of error == 0
            if ((error > 0) || (!error && (ix > 0))) {
                error -= dx;
                y1 += iy;
            }
            // else do nothing
            
            error += dy;
            x1 += ix;
 
            draw_pixel(img, x1, y1, color);
        }
    }
    else {
        // error may go below zero
        int error = dx - (dy >> 1);
 
        while (y1 != y2) {
            // reduce error, while taking into account the corner case of error == 0
            if ((error > 0) || (!error && (iy > 0))) {
                error -= dy;
                x1 += ix;
            }
            // else do nothing
 
            error += dx;
            y1 += iy;
 
            draw_pixel(img, x1, y1, color);
        }
    }
}

