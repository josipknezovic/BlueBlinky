/******************************************************************************/
/* TIME.C: Time Functions for 100Hz Clock Tick                                */
/******************************************************************************/
/* This file is part of the uVision/ARM development tools.                    */
/* Copyright (c) 2005-2006 Keil Software. All rights reserved.                */
/* This software may only be used under the terms of a valid, current,        */
/* end user licence from KEIL for a compatible version of KEIL software       */
/* development tools. Nothing else gives you the right to use this software.  */
/******************************************************************************/

#include <LPC21XX.H>                           // LPC21XX Peripheral Registers
#include "Timer.h"

long volatile hoursNumber=0;
long volatile minutesNumber=0;
/* Timer Counter 0 Interrupt executes each 10ms @ 60 MHz CPU Clock */
__irq void tc0 (void) {
	if(minutesNumber<60){															/*We have to count minutes,since number of ticks in hour would overflow long value of T0MR0*/
		minutesNumber++;
	}else{
		minutesNumber=0;
		if(hoursNumber>23){ 														/*Since we count from zero,it is 23hours,not 24*/
			hoursNumber=0;}
		else{
			hoursNumber++;
		}  
	}
	
	T0IR = 1;                                    // Clear interrupt flag
  VICVectAddr = 0;                             // Acknowledge Interrupt
}

/* Setup the Timer Counter 0 Interrupt */
void init_timer (void) {				
  T0MR0 = 149999;                           	 // 149999 counts(for testing purposes); 1min=899994000 counts(for real use)
  T0MCR = 3;                                   // Interrupt and Reset on MR0
  T0TCR = 1;                                   // Timer0 Enable
  VICVectAddr0 = (unsigned long)tc0;           // set interrupt vector in 0
  VICVectCntl0 = 0x20 | 4;                     // use it for Timer 0 Interrupt
  VICIntEnable = 0x00000010;                   // Enable Timer0 Interrupt
}
