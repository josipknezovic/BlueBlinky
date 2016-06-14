#include <lpc214x.h>

#define CR     0x0D
#define PRESCALE 12000
#define DELAY 2500

void initSerial(void);
void initTimer(void);
__irq void prekidna(void);
int sendchar(int ch);
int getkey(void);


int main(void) {
	int c;
	IO0DIR = 0x000F0000;
	IO0CLR = 0x000F0000;
	initSerial();
	initTimer();
	
	IO0SET = (1 << 18);
	
	while(1) {
		c = getkey();
		if(c == '2') {
			IO0SET |= (1 << 16);
			IO0SET |= (1 << 19);
			T0TCR = 0x01;
		}
	}
}

__irq void prekidna(void) {
	long int regValue;
	regValue = T0IR;
	
	IO0CLR |= (1 << 16);
	IO0CLR |= (1 << 19);
	T0TCR = 0x00;
	
	T0IR = regValue;
	VICVectAddr = 0x00;
}

void initSerial (void)  {               /* Initialize Serial Interface       */
  PINSEL0 = 0x00000005;                  /* Enable RxD0 and TxD0              */
  U0LCR = 0x83;                          /* 8 bits, no Parity, 1 Stop bit     */
  U0DLL = 97;                            /* 9600 Baud Rate @ 15MHz VPB Clock  */
  U0LCR = 0x03;                          /* DLAB = 0                          */
}

void initTimer(void) {
	T0CTCR = 0x00;
	T0PR = PRESCALE - 1;
	T0MR0 = DELAY - 1;
	T0MCR = 0x03;
	
	VICVectAddr4 = (unsigned) prekidna;
	VICVectCntl4 = 0x20 | 4;
	VICIntEnable = 0x10;
	
	T0TCR = 0x02; //Reset timer
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
