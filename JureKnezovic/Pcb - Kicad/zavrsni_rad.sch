EESchema Schematic File Version 2
LIBS:zavrsni_rad-rescue
LIBS:power
LIBS:device
LIBS:transistors
LIBS:conn
LIBS:linear
LIBS:regul
LIBS:74xx
LIBS:cmos4000
LIBS:adc-dac
LIBS:memory
LIBS:xilinx
LIBS:microcontrollers
LIBS:dsp
LIBS:microchip
LIBS:analog_switches
LIBS:motorola
LIBS:texas
LIBS:intel
LIBS:audio
LIBS:interface
LIBS:digital-audio
LIBS:philips
LIBS:display
LIBS:cypress
LIBS:siliconi
LIBS:opto
LIBS:atmel
LIBS:contrib
LIBS:valves
LIBS:pirsensor
LIBS:poe
LIBS:bluetoothhc06
LIBS:relay
LIBS:zp2148
LIBS:zavrsni_rad-cache
EELAYER 25 0
EELAYER END
$Descr A4 8268 11693 portrait
encoding utf-8
Sheet 1 1
Title "Elektronička shema brave"
Date "2017-04-08"
Rev "1.0"
Comp "Fakultet Elektrotehnike i računarstva"
Comment1 ""
Comment2 "email: jure.knezovic@fer.hr"
Comment3 "Autor: Jure Knezović"
Comment4 "Shema je izrađena za potrebe završnog rada te se može slobodno kopirati i koristiti."
$EndDescr
$Comp
L ZP2148 LPC1
U 1 1 58E854DE
P 2300 5700
F 0 "LPC1" H 2750 7100 50  0000 L CNN
F 1 "ZP2148" H 2550 4300 50  0000 L CNN
F 2 "zp2148:ZP2148" H 6050 6400 50  0001 C CIN
F 3 "" H 6050 6400 50  0001 C CNN
	1    2300 5700
	1    0    0    -1  
$EndComp
$Comp
L +3.3V #PWR01
U 1 1 58E86520
P 3200 4300
F 0 "#PWR01" H 3200 4150 50  0001 C CNN
F 1 "+3.3V" H 3200 4440 50  0000 C CNN
F 2 "" H 3200 4300 50  0001 C CNN
F 3 "" H 3200 4300 50  0001 C CNN
	1    3200 4300
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR02
U 1 1 58E86592
P 6000 6250
F 0 "#PWR02" H 6000 6000 50  0001 C CNN
F 1 "GND" H 6000 6100 50  0000 C CNN
F 2 "" H 6000 6250 50  0001 C CNN
F 3 "" H 6000 6250 50  0001 C CNN
	1    6000 6250
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR03
U 1 1 58E865A8
P 3150 2750
F 0 "#PWR03" H 3150 2500 50  0001 C CNN
F 1 "GND" H 3150 2600 50  0000 C CNN
F 2 "" H 3150 2750 50  0001 C CNN
F 3 "" H 3150 2750 50  0001 C CNN
	1    3150 2750
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR04
U 1 1 58E865BE
P 5300 1500
F 0 "#PWR04" H 5300 1250 50  0001 C CNN
F 1 "GND" H 5300 1350 50  0000 C CNN
F 2 "" H 5300 1500 50  0001 C CNN
F 3 "" H 5300 1500 50  0001 C CNN
	1    5300 1500
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR05
U 1 1 58E865D4
P 1700 7100
F 0 "#PWR05" H 1700 6850 50  0001 C CNN
F 1 "GND" H 1700 6950 50  0000 C CNN
F 2 "" H 1700 7100 50  0001 C CNN
F 3 "" H 1700 7100 50  0001 C CNN
	1    1700 7100
	1    0    0    -1  
$EndComp
Text GLabel 1850 5550 0    60   Input ~ 0
ZelenaLed
Text GLabel 1850 5750 0    60   Input ~ 0
CrvenaLed
Text GLabel 1850 6150 0    60   Input ~ 0
Relej
Text GLabel 1850 5350 0    60   Input ~ 0
SenzorPokreta
Text GLabel 1850 4550 0    56   Input ~ 0
TxD
Text GLabel 1850 4650 0    56   Input ~ 0
RxD
$Comp
L PIRsensor S1
U 1 1 58E86870
P 6450 1350
F 0 "S1" H 6550 1650 60  0000 C CNN
F 1 "PIRsensor" H 6650 1050 60  0000 C CNN
F 2 "three_pins:ThreePinConnector" H 6650 1050 60  0001 C CNN
F 3 "" H 6650 1050 60  0001 C CNN
	1    6450 1350
	1    0    0    -1  
$EndComp
Text GLabel 6150 1450 0    60   Input ~ 0
SenzorPokreta
$Comp
L VCC #PWR06
U 1 1 58E86AD6
P 5300 1050
F 0 "#PWR06" H 5300 900 50  0001 C CNN
F 1 "VCC" H 5300 1200 50  0000 C CNN
F 2 "" H 5300 1050 50  0001 C CNN
F 3 "" H 5300 1050 50  0001 C CNN
	1    5300 1050
	1    0    0    -1  
$EndComp
$Comp
L VCC #PWR07
U 1 1 58E8700E
P 2250 1650
F 0 "#PWR07" H 2250 1500 50  0001 C CNN
F 1 "VCC" H 2250 1800 50  0000 C CNN
F 2 "" H 2250 1650 50  0001 C CNN
F 3 "" H 2250 1650 50  0001 C CNN
	1    2250 1650
	1    0    0    -1  
$EndComp
$Comp
L LM1117-3.3 U2
U 1 1 58E87028
P 3150 1950
F 0 "U2" H 3250 1700 50  0000 C CNN
F 1 "LM1117-3.3V" H 3150 2200 50  0000 C CNN
F 2 "three_pins:ThreePinConnector" H 3150 1950 50  0001 C CNN
F 3 "" H 3150 1950 50  0001 C CNN
	1    3150 1950
	1    0    0    -1  
$EndComp
$Comp
L CP C1
U 1 1 58E8708B
P 2700 2200
F 0 "C1" H 2725 2300 50  0000 L CNN
F 1 "100uF/16V" H 2725 2100 50  0000 L CNN
F 2 "Capacitors_THT:CP_Radial_D6.3mm_P2.50mm" H 2738 2050 50  0001 C CNN
F 3 "" H 2700 2200 50  0001 C CNN
	1    2700 2200
	1    0    0    -1  
$EndComp
$Comp
L CP C2
U 1 1 58E870C2
P 3550 2200
F 0 "C2" H 3575 2300 50  0000 L CNN
F 1 "10uF/16V" H 3575 2100 50  0000 L CNN
F 2 "Capacitors_THT:CP_Radial_D5.0mm_P2.50mm" H 3588 2050 50  0001 C CNN
F 3 "" H 3550 2200 50  0001 C CNN
	1    3550 2200
	1    0    0    -1  
$EndComp
Connection ~ 10500 550 
Wire Wire Line
	1700 7100 1700 6950
Wire Wire Line
	1700 6950 1850 6950
Wire Wire Line
	3200 4300 3200 4450
Wire Wire Line
	3200 4450 3050 4450
Wire Wire Line
	6150 1350 5300 1350
Wire Wire Line
	5300 1350 5300 1500
Wire Wire Line
	6150 1250 5300 1250
Wire Wire Line
	5300 1250 5300 1050
Wire Wire Line
	2250 1950 2250 1650
Connection ~ 2250 1950
Wire Wire Line
	2700 1950 2700 2050
Connection ~ 2700 1950
Wire Wire Line
	2700 2350 2700 2600
Wire Wire Line
	2250 2600 3550 2600
Wire Wire Line
	3550 2600 3550 2350
Wire Wire Line
	3150 2250 3150 2750
Connection ~ 3150 2600
Wire Wire Line
	2250 2600 2250 2100
Wire Wire Line
	2250 2100 1550 2100
Connection ~ 2700 2600
Wire Wire Line
	3550 2050 3550 1950
Wire Wire Line
	3450 1950 3850 1950
$Comp
L +3V3 #PWR08
U 1 1 58E87368
P 3850 1650
F 0 "#PWR08" H 3850 1500 50  0001 C CNN
F 1 "+3V3" H 3850 1790 50  0000 C CNN
F 2 "" H 3850 1650 50  0001 C CNN
F 3 "" H 3850 1650 50  0001 C CNN
	1    3850 1650
	1    0    0    -1  
$EndComp
Wire Wire Line
	3850 1950 3850 1650
Connection ~ 3550 1950
$Comp
L BluetoothHC06 BT1
U 1 1 58E87D81
P 6600 2800
F 0 "BT1" H 6750 3150 60  0000 C CNN
F 1 "BluetoothHC06" H 6600 2500 60  0000 C CNN
F 2 "three_pins:FourPinConnector" H 6750 3150 60  0001 C CNN
F 3 "" H 6750 3150 60  0001 C CNN
	1    6600 2800
	1    0    0    -1  
$EndComp
$Comp
L +3V3 #PWR09
U 1 1 58E880D6
P 5400 2450
F 0 "#PWR09" H 5400 2300 50  0001 C CNN
F 1 "+3V3" H 5400 2590 50  0000 C CNN
F 2 "" H 5400 2450 50  0001 C CNN
F 3 "" H 5400 2450 50  0001 C CNN
	1    5400 2450
	1    0    0    -1  
$EndComp
Wire Wire Line
	5400 2450 5400 2600
Wire Wire Line
	5400 2600 5750 2600
$Comp
L GND #PWR010
U 1 1 58E88492
P 5400 2900
F 0 "#PWR010" H 5400 2650 50  0001 C CNN
F 1 "GND" H 5400 2750 50  0000 C CNN
F 2 "" H 5400 2900 50  0001 C CNN
F 3 "" H 5400 2900 50  0001 C CNN
	1    5400 2900
	1    0    0    -1  
$EndComp
Text GLabel 5750 2900 0    56   Input ~ 0
TxD
Text GLabel 5750 2800 0    56   Input ~ 0
RxD
Wire Wire Line
	5400 2900 5400 2700
Wire Wire Line
	5400 2700 5750 2700
$Comp
L LED_ALT D2
U 1 1 58E88C4B
P 5950 7600
F 0 "D2" H 5950 7700 50  0000 C CNN
F 1 "Crvena_led" H 5950 7450 50  0000 C CNN
F 2 "LEDs:LED_D5.0mm" H 5950 7600 50  0001 C CNN
F 3 "" H 5950 7600 50  0001 C CNN
	1    5950 7600
	-1   0    0    -1  
$EndComp
$Comp
L LED_ALT D3
U 1 1 58E88F17
P 5950 8150
F 0 "D3" H 5950 8250 50  0000 C CNN
F 1 "Zelena_LED" H 5950 8000 50  0000 C CNN
F 2 "LEDs:LED_D5.0mm" H 5950 8150 50  0001 C CNN
F 3 "" H 5950 8150 50  0001 C CNN
	1    5950 8150
	-1   0    0    -1  
$EndComp
$Comp
L R R4
U 1 1 58E89315
P 6550 7600
F 0 "R4" V 6630 7600 50  0000 C CNN
F 1 "1K" V 6550 7600 50  0000 C CNN
F 2 "Resistors_SMD:R_1206_HandSoldering" V 6480 7600 50  0001 C CNN
F 3 "" H 6550 7600 50  0001 C CNN
	1    6550 7600
	0    1    1    0   
$EndComp
$Comp
L R R3
U 1 1 58E893A9
P 6500 8150
F 0 "R3" V 6580 8150 50  0000 C CNN
F 1 "1K" V 6500 8150 50  0000 C CNN
F 2 "Resistors_SMD:R_1206_HandSoldering" V 6430 8150 50  0001 C CNN
F 3 "" H 6500 8150 50  0001 C CNN
	1    6500 8150
	0    1    1    0   
$EndComp
Text GLabel 5800 8150 0    60   Input ~ 0
ZelenaLed
Text GLabel 5800 7600 0    60   Input ~ 0
CrvenaLed
Wire Wire Line
	6100 7600 6400 7600
Wire Wire Line
	6350 8150 6100 8150
$Comp
L GND #PWR011
U 1 1 58E89FEA
P 6850 7700
F 0 "#PWR011" H 6850 7450 50  0001 C CNN
F 1 "GND" H 6850 7550 50  0000 C CNN
F 2 "" H 6850 7700 50  0001 C CNN
F 3 "" H 6850 7700 50  0001 C CNN
	1    6850 7700
	1    0    0    -1  
$EndComp
$Comp
L GND #PWR012
U 1 1 58E8A01F
P 6850 8250
F 0 "#PWR012" H 6850 8000 50  0001 C CNN
F 1 "GND" H 6850 8100 50  0000 C CNN
F 2 "" H 6850 8250 50  0001 C CNN
F 3 "" H 6850 8250 50  0001 C CNN
	1    6850 8250
	1    0    0    -1  
$EndComp
Wire Wire Line
	6700 7600 6850 7600
Wire Wire Line
	6850 7600 6850 7700
Wire Wire Line
	6650 8150 6850 8150
Wire Wire Line
	6850 8150 6850 8250
$Comp
L R R1
U 1 1 58E8A4BF
P 5350 5550
F 0 "R1" V 5430 5550 50  0000 C CNN
F 1 "4k7" V 5350 5550 50  0000 C CNN
F 2 "Resistors_SMD:R_0805_HandSoldering" V 5280 5550 50  0001 C CNN
F 3 "" H 5350 5550 50  0001 C CNN
	1    5350 5550
	0    1    1    0   
$EndComp
$Comp
L R R2
U 1 1 58E8A4F4
P 6000 4400
F 0 "R2" V 6080 4400 50  0000 C CNN
F 1 "68" V 6000 4400 50  0000 C CNN
F 2 "Resistors_SMD:R_1206_HandSoldering" V 5930 4400 50  0001 C CNN
F 3 "" H 6000 4400 50  0001 C CNN
	1    6000 4400
	1    0    0    -1  
$EndComp
$Comp
L D_ALT D1
U 1 1 58E8A563
P 5700 4950
F 0 "D1" H 5700 5050 50  0000 C CNN
F 1 "Dioda" H 5700 4850 50  0000 C CNN
F 2 "Diodes_SMD:D_0603" H 5700 4950 50  0001 C CNN
F 3 "" H 5700 4950 50  0001 C CNN
	1    5700 4950
	0    -1   1    0   
$EndComp
$Comp
L Q_NPN_BCE Q1
U 1 1 58E8A5AC
P 5900 5550
F 0 "Q1" H 6100 5600 50  0000 L CNN
F 1 "NPN" H 6100 5500 50  0000 L CNN
F 2 "three_pins:NPN" H 6100 5650 50  0001 C CNN
F 3 "" H 5900 5550 50  0001 C CNN
	1    5900 5550
	1    0    0    -1  
$EndComp
Text GLabel 4850 5550 0    60   Input ~ 0
Relej
$Comp
L Relay Rel1
U 1 1 58E8AF78
P 6600 5000
F 0 "Rel1" H 6600 5200 60  0000 C CNN
F 1 "Relay" H 6600 4900 60  0000 C CNN
F 2 "three_pins:TwoPinConnector" H 6600 5200 60  0001 C CNN
F 3 "" H 6600 5200 60  0001 C CNN
	1    6600 5000
	1    0    0    -1  
$EndComp
Wire Wire Line
	5200 5550 4850 5550
Wire Wire Line
	5500 5550 5700 5550
Wire Wire Line
	5700 5100 5700 5250
Wire Wire Line
	5700 5250 6250 5250
Wire Wire Line
	6000 5250 6000 5350
Wire Wire Line
	6250 5250 6250 5000
Connection ~ 6000 5250
Wire Wire Line
	6250 4700 6250 4900
Wire Wire Line
	5700 4700 6250 4700
Wire Wire Line
	6000 4700 6000 4550
Wire Wire Line
	5700 4800 5700 4700
Connection ~ 6000 4700
$Comp
L VCC #PWR013
U 1 1 58E8B824
P 6000 4050
F 0 "#PWR013" H 6000 3900 50  0001 C CNN
F 1 "VCC" H 6000 4200 50  0000 C CNN
F 2 "" H 6000 4050 50  0001 C CNN
F 3 "" H 6000 4050 50  0001 C CNN
	1    6000 4050
	1    0    0    -1  
$EndComp
Wire Wire Line
	6000 4250 6000 4050
Wire Wire Line
	6000 5750 6000 6250
$Comp
L PoE U1
U 1 1 58E990F9
P 1200 2050
F 0 "U1" H 1300 2350 60  0000 C CNN
F 1 "PoE" H 1300 1800 79  0000 C CNN
F 2 "three_pins:TwoPinConnector" H 1950 800 60  0001 C CNN
F 3 "" H 1950 800 60  0001 C CNN
	1    1200 2050
	1    0    0    -1  
$EndComp
Wire Wire Line
	1550 1950 2850 1950
$EndSCHEMATC