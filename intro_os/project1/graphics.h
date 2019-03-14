/**
 * header file that defines prototypes for all the "public" functions,
 * color_t typedef, and RGB conversion macro.
**/

typedef unsigned short color_t;		// color type unsigned 16-bit


// Prototypes
void init_graphics();
void exit_graphics();
char getkey();
void sleep_ms(long ms);
void clear_screen(void *img);
void draw_pixel(void *img, int x, int y, color_t color);
void draw_line(void *img, int x1, int y1, int width, int height, color_t c);
void *new_offscreen_buffer();
void blit(void *scr);


// take 3 RGB values & make a single 16-bit
#define RGB(r, g, b) ((r & 0x1F) << 11) | ((g & 0x3F) << 5) | (b & 0x1F)