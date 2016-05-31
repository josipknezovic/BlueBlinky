#include <lpc214x.h>

#define CR     0x0D

void init_serial(void);
int sendchar(int ch);
int getkey(void);

void delay(void) {
	int i = 0;
	while(i < 8000000) i++;
}

int main(void) {
	int c;
	IO0DIR |= (1<<16);
	init_serial();
	
	while(1) {
		c = getkey();
		if(c == '2') {
			IO0SET |= (1 << 16);
			delay();
			IO0CLR |= (1 << 16);
		}
	}
}


void init_serial (void)  {               /* Initialize Serial Interface       */
  PINSEL0 = 0x00000005;                  /* Enable RxD0 and TxD0              */
  U0LCR = 0x83;                          /* 8 bits, no Parity, 1 Stop bit     */
  U0DLL = 97;                            /* 9600 Baud Rate @ 15MHz VPB Clock  */
  U0LCR = 0x03;                          /* DLAB = 0                          */
}


/* implementation of putchar (also used by printf function to output data)    */
int sendchar (int ch)  {                 /* Write character to Serial Port    */

  if (ch == '\n')  {
    while (!(U0LSR & 0x20));
    U0THR = CR;                          /* output CR */
  }
  while (!(U0LSR & 0x20));
  return (U0THR = ch);
}


int getkey (void)  {                     /* Read character from Serial Port   */

  while (!(U0LSR & 0x01));

  return (U0RBR);
}
