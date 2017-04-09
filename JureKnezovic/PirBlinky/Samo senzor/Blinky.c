/******************************************************************************/
/* BLINKY.C: LED Flasher                                                      */
/******************************************************************************/
/* This file is part of the uVision/ARM development tools.                    */
/* Copyright (c) 2005-2006 Keil Software. All rights reserved.                */
/* This software may only be used under the terms of a valid, current,        */
/* end user licence from KEIL for a compatible version of KEIL software       */
/* development tools. Nothing else gives you the right to use this software.  */
/******************************************************************************/
                  
#include <stdio.h>                         /* standard I/O .h-file            */
#include <LPC2103.H>                       /* LPC2103 definitions             */
#define SensorPin 10
#define CR     0x0D
#define PRESCALE 12000
#define DELAY 2500


void initSerial(void);
void initTimer(void);
__irq void prekidna(void);
int sendchar(int ch);
int getkey(void);
void initPins(void);
void delay(void);

int main(void) {
	int c;	
	unsigned int i=0;
	initSerial();
	initTimer();
	initPins();
                     
	while (i<5){
		IOCLR=0x00F00000 ;	// set all outputs in mask to 0 -> LEDs OFF
		delay();
		IOSET=0x00F00000 ;	// set all outputs in mask to 1 -> LEDs ON
		delay();
		i++;
	}
	
	while (1){
		if (IOPIN & (1<<SensorPin))	{
			IOSET=0x00F00000;
		} else {
		  IOCLR=0x00F00000 ;
		}
	}
}

__irq void prekidna(void) {
	long int regValue;
	regValue = T0IR;
	
	IOCLR |= (1 << 16);
	IOCLR |= (1 << 19);
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

void delay(void){
	volatile int i,j;
	for (i=0;i<1000;i++)
		for (j=0;j<1000;j++);
}
void initPins(void){
	IOCLR=0xFFFFFFFF ;
	IODIR   = 0x00F00000; // define LED-Pin as output
	IODIR &= ~(1<<SensorPin);	// define Sensor as input
}



