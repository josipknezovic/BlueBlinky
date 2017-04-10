#include <lpc214x.h>
#include <ctype.h>
#include <string.h>
#define MR0I (1 << 0) //Interrupt When TC matches MR0
#define MR0R (1 << 1) //Reset TC when TC matches MR0
#define DELAY_1M 60000 //1 Minute Delay
#define PRESCALE 15000 //15000 PCLK clock cycles to increment TC by 1ms
#define sensorPin 8 //Pin number on which we will connect our proximity sensor
#define greenLed 10 //Pin number on which we will connect our green led
#define redLed 12 //Pin number on which we will connect our red led
#define relayPin 16 //Pin number on which we will connect our relay
//All pins are actually +2 on our board, chinese business...
void initPins(void);
void initCounterValues(void);
void prohibitEntry(void);
void allowEntry(void);
void deactivateRelay(void);
void disableLedsAndRelay(void);
void activateGreenLed(void);
void disableGreenLed(void);
void activateRedLed(void);
void disableRedLed(void);
void blinkRedLeds(void);
void blinkGreenLeds(void);
void initTimers(void);
void delayMS(unsigned int);
void initSerial(void);
__irq void T0INT(void);
int getchar(void);

int currentCounterHours;
int counterMinutes;
int workingHours;
int previousChar = 0;
int counterDelay = 5000;
int ledStatus = 1;
int sensorStatus = 1;
char password[9] = "ABCD";
char inputPassword[9] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
char newPassword[9] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
char oldPassword[9] = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };

int main(void){
    int c;
    int b;
    int counterNewPassword = 0;
    int counterInputPassword = 0;
    int counterOldPassword = 0;
    initPins();
    initCounterValues();
    initTimers();
    initSerial();

    while (1) {
        c = getchar();
        if (isdigit(c)) {
            previousChar = c;
            if (c == '6') {
                if (strcmp(password, inputPassword) == 0) {
                    allowEntry();
                }
                else {
                    prohibitEntry();
                }
                delayMS(counterDelay);
                disableLedsAndRelay();
                memset(inputPassword, 0, 9);
                memset(oldPassword, 0, 9);
                previousChar = '~';
                counterInputPassword = 0;
                counterOldPassword = 0;
            }
            else if (c == '7') {
                if (strlen(newPassword) > 0 & strcmp(oldPassword, password) == 0) {
                    memset(password, 0, 9);
                    strcpy(password, newPassword);
                    memset(newPassword, 0, 9);
                    previousChar = '~';
                    counterNewPassword = 0;
                    blinkGreenLeds();
                }
                else {
                    blinkRedLeds();
                }
                memset(oldPassword, 0, 9);
                counterOldPassword = 0;
            }
            else {
                memset(inputPassword, 0, 9);
                memset(newPassword, 0, 9);
                counterNewPassword = 0;
                counterInputPassword = 0;
            }
        }
        else if (c >= 'A' & c <= '|') {
            switch (previousChar) {
            case '1':
                if (c >= 'A' & c <= 'J' & counterInputPassword < 8) {
                    //unosimo password u string inputPassword
                    inputPassword[counterInputPassword] = c;
                    counterInputPassword++;
                }
                if (c >= 'a' & c <= 'j' & counterInputPassword < 8) {
                    newPassword[counterNewPassword] = c - 32;
                    counterNewPassword++;
                }
                break;
            case '2':
                if (islower(c)) {
                    if (c <= 'm') {
                        //dozvoli paljenje ledica
                        ledStatus = 1;
                    }
                    else {
                        //blokiraj paljene ledica
                        ledStatus = 0;
                    }
                }
                else {
                    if (c <= 'M') {
                        //dozvoli koristenje senzora
                        sensorStatus = 1;
												blinkGreenLeds();
                    }
                    else {
                        //blokiraj koristenje senzora
                        sensorStatus = 0;
												blinkGreenLeds();
                    }
                }
                break;
            case '3':
                if (islower(c)) {
                    if (c <= 'x') {
                        //radno vrijeme u satima, npr. 1,2,3...
                        workingHours = c - 'a';
                    }
                }
                else {
                    if (c <= 'X') {
                        //sati koji ovise o trenutnoj vrijednosti brojaca; droid aplikacija ih racuna te salje ovdje
                        currentCounterHours = c - 'A';
                    }
                }
                break;
            case '4':
                //trenutne minute
                counterMinutes = c - 'A';
                break;
            case '5':
                if (islower(c)) {
                    if (c <= 'm') {
                        for (b = 0; b < 3; b++) {
                            blinkGreenLeds();
                        }
                    }
                    else {
                        for (b = 0; b < 3; b++) {
                            blinkRedLeds();
                        }
                    }
                }
                else {
                    if (c >= 'D' & c <= 'K') {
                        //kasnjenje ulaza u milisekundama, npr.1000,2000....10000
                        counterDelay = 1000 * (c - 'A');
                    }
                }
                break;
            case '8':
                if (c >= 'A' & c <= 'J' & counterOldPassword < 8) {
                    //unosimo password u string oldPassword
                    oldPassword[counterOldPassword] = c;
                    counterOldPassword++;
                }
                break;
                ;
            default:
                disableLedsAndRelay();
                break;
            }
        }
        if (sensorStatus) {
            if (IO0PIN & (1 << sensorPin)) {
                if (currentCounterHours < workingHours) {
                    allowEntry();
                }
                else {
                    prohibitEntry();
                }
            }
            else {
                disableLedsAndRelay();
            }
        }
        else {
            disableLedsAndRelay();
        }
    }
}
/*Initialize part - Pins*/
void initPins(void)
{
    IO0CLR = 0xFFFFFFFF;
    IO0DIR |= (1 << greenLed); // define Green LED as output
    IO0DIR |= (1 << redLed); // define Red LED as output
    IO0DIR |= (1 << relayPin); // define Relay as output
    IO0DIR &= ~(1 << sensorPin); // define Sensor as input
}

/*Initialize part - Serial port*/
void initSerial(void)
{ // Initialize Serial Interface
    PINSEL0 = 0x00000005; // Enable RxD0 and TxD0
    U0LCR = 0x83; // 8 bits, no Parity, 1 Stop bit
    U0DLL = 97; // 9600 Baud Rate @ 15MHz VPB Clock
    U0LCR = 0x03; // DLAB = 0
}

/*Initialize part - Default values*/
void initCounterValues(void)
{
    currentCounterHours = 0;
    counterMinutes = 0;
    workingHours = 8;
}

/*Timer functions - Delay and interrupt*/
void initTimers(void)
{
    //Init Timer0 - Interrupts every minute
    T0CTCR = 0x0;
    T0PR = PRESCALE - 1;
    //T0MR0 = 10-1; TEST VALUES 10ms
    T0MR0 = DELAY_1M - 1;
    T0MCR = 3;
    VICVectAddr4 = (unsigned)T0INT; //Point to interrupt function
    VICVectCntl4 = 0x20 | 4;
    VICIntEnable = 0x10;
    T0TCR = 0x02; //Reset Timer
    T0TCR = 0x01; //Enable Timer

    //Init Timer1
    T1CTCR = 0x0;
    T1PR = PRESCALE - 1;
    T1TCR = 0x02; //Reset Timer
}

__irq void T0INT(void)
{
    if (counterMinutes < 60) { //We have to count minutes,since number of ticks in hour would overflow value of T0MR0
        counterMinutes++;
    }
    else {
        counterMinutes = 0;
        if (currentCounterHours > 23) { //Since we count from zero,it is 23hours,not 24
            currentCounterHours = 0;
        }
        else {
            currentCounterHours++;
        }
    }
    T0IR = 1;
    VICVectAddr = 0x0;
}

void delayMS(unsigned int milliseconds)
{ //Using Timer1
    T1TCR = 0x02; //Reset Timer
    T1TCR = 0x01; //Enable timer
    while (T1TC < milliseconds)
        ; //wait until timer counter reaches the desired delay
    T1TCR = 0x00; //Disable timer
}

/*Serial port functions*/
int getchar(void)
{ /* Read character from Serial Port   */
    if (U0LSR & 0x01) {
        return (U0RBR);
    }
    else {
        return '~';
    }
}

/*Default functions - Used to control access*/
void allowEntry(void)
{
    disableRedLed();
    if (ledStatus) {
        activateGreenLed();
    }
    IO0SET = (1 << relayPin);
}

void prohibitEntry(void)
{
    disableLedsAndRelay();
    if (ledStatus) {
        activateRedLed();
    }
}

/*IO Functions*/
void deactivateRelay(void)
{
    IO0CLR = (1 << relayPin);
}

void disableLedsAndRelay(void)
{
    disableRedLed();
    IO0CLR = (1 << greenLed);
    IO0CLR = (1 << relayPin);
}
void activateGreenLed(void)
{
    IO0SET = (1 << greenLed);
}
void disableGreenLed(void)
{
    IO0CLR = (1 << greenLed);
}

void activateRedLed(void)
{
    IO0SET = (1 << redLed);
}

void disableRedLed(void)
{
    IO0CLR = (1 << redLed);
}

void blinkGreenLeds(void)
{
    activateGreenLed();
    delayMS(200);
    disableGreenLed();
    delayMS(200);
}
void blinkRedLeds(void)
{
    activateRedLed();
    delayMS(200);
    disableRedLed();
    delayMS(200);
}
