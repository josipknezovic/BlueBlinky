/******************************************************************************/
/* BLINKY.C: LED Flasher                                                      */
/******************************************************************************/
/* This file is part of the uVision/ARM development tools.                    */
/* Copyright (c) 2005-2006 Keil Software. All rights reserved.                */
/* This software may only be used under the terms of a valid, current,        */
/* end user licence from KEIL for a compatible version of KEIL software       */
/* development tools. Nothing else gives you the right to use this software.  */
/******************************************************************************/

#include <LPC2103.H>                        /* LPC21xx definitions */
#include "Timer.h"
#define sensorPin 10												/*Pin number on which we will connect our proximity sensor*/
#define allowOpeningHours 8 								/*Working hours of our Office;when we turn it on it will count */
																						/*x Hours in which sensor will operate*/
#define relayPin 2													/*Pin number on which we will connect our relay for opening the doors*/							
																						
void delay(void);
void initPins(void);

int main (void) {
  unsigned int j;                          /* LED var */
	initPins();
  init_timer();

  while (1)  {                                   /* Loop forever */
		if(hoursNumber<allowOpeningHours){
			if (IOPIN & (1<<sensorPin))	{							/*IF sensor pin is active' */
				for (j = 0x100000; j < 0x800000; j <<= 1) {  /* Blink LED 1,2,3,4 */
					IOSET = j;                                /* Turn on LED */
					delay();                                   /* delay LED shutdown */
					IOCLR = j;                                /* Turn off LED */
				}
				for (j = 0x800000; j > 0x100000; j >>=1 ) {  /* Blink LED 4,3,2,1 */
					IOSET = j;                                /* Turn on LED */
					delay ();                                   /* delay LED shutdown */
					IOCLR = j;                                /* Turn off LED */
				}
			}
    }else{
			IOCLR=0x00F00000;										/*If sensor was on when time ran out;we need to cancel its request*/
		}
  }
}

void delay(void){
	volatile int i,j;
	for (i=0;i<1000;i++)
		for (j=0;j<1000;j++);
}

void initPins(void){
	IOCLR = 0xFFFFFFFF ;
	IODIR = 0x00F00000; 			// define LED-Pin as output
	IODIR |= (1<<relayPin);		// define Relay-Pin as output	
	IODIR &= ~(1<<sensorPin);	// define Sensor as input
}
