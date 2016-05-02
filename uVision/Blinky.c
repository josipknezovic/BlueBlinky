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

extern void init_serial (void);            /* Initialize Serial Interface     */
extern int getkey(void);

extern void init_serial (void);            /* Initialize Serial Interface     */
extern int  sendchar (int ch);             /* Write character to Serial Port  */


void sendhex (int hex) {                   /* Write Hex Digit to Serial Port  */
  if (hex > 9) sendchar('A' + (hex - 10));
  else         sendchar('0' +  hex);
}

void sendstr (char *p) {                   /* Write string */
  while (*p) {
    sendchar (*p++);
  }
}


void delay (void) {                        /* Delay function */
  unsigned int cnt;
  unsigned int val;

  ADCR |= 0x01200000;                      /* Start A/D Conversion */
  do {
    val = ADGDR;                           /* Read A/D Data Register */
  } while ((val & 0x80000000) == 0);       /* Wait for end of A/D Conversion */
  ADCR &= ~0x01000000;                     /* Stop A/D Conversion */
  val =  (val >> 6) & 0x03FF;              /* Extract AIN0 Value */

  sendstr ("\nAIN2 Result = 0x");          /* Output A/D Conversion Result */
  sendhex((val >> 8) & 0x0F);              /* Write 1. Hex Digit */
  sendhex((val >> 4) & 0x0F);              /* Write 2. Hex Digit */
  sendhex (val & 0x0F);                    /* Write 3. Hex Digit */

  val = (val >> 2) << 12;                  /* Adjust Delay Value */
  for (cnt = 0; cnt < val; cnt++);         /* Delay */
}


int main (void) {
	
	int c;
	
  IODIR   = 0x00FF0000;                    /* P0.16..23 defined as Outputs  */
	IOCLR 	= 0x00FF0000;
  PINSEL1 = 0x00030000; 
  ADCR    = 0x00200404;                    /* Setup A/D: 10-bit AIN2 @ 3MHz */
	
	init_serial();                          /* Initialize Serial Interface    */
	sendstr("Hello\n");
	
  while (1) {   		/* Loop forever */
		c = getkey();
		IOCLR = 0x00FF0000;
		switch(c) {
			case '1' :
				IOSET = 0x00010000;
				break;
			case '2' :
				IOSET = 0x00080000;
				break;
			case '3' :
				IOSET = 0x00100000;
				break;
			case '4' :
				IOSET = 0x00200000;
				break;
			case '5' :
				IOSET = 0x00400000;
				break;
			case '6' :
				IOSET = 0x00800000;
				break;
			default :
				IOSET = 0x00FF0000;
				break;
		}
  }
}
