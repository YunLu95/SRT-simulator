/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package srt.simulator;

/**
 *
 * @author Thejanie
 */
public class Srt {

    int n; //size of divisor
    StringBuilder divisor;
    StringBuilder dividend;
    StringBuilder normB; //Normalized divisor
    StringBuilder negNormB; //2s compliment of normalized divisor
    int AQshift = 0; //No. of shifts needed to adjust AQ
    int maxLeftShifts;
    int numLeftShifts; //No. of left shifts
    int exTime; //execution time


    StringBuilder AQ;

    public Srt(String dividend, String divisor) {
        exTime = 0;
        StringBuilder sdividend = new StringBuilder(dividend);
        StringBuilder sdivisor = new StringBuilder(divisor);

        //call hextobin
        System.out.println("You entered dividend string: " + sdividend + ", divisor string:" + sdivisor);
        StringBuilder binDividend = HexToBin(sdividend);
        StringBuilder binDivisor = HexToBin(sdivisor);

        System.out.print("Dividend's Equivalent Binary value is : " + binDividend + "\n");
        System.out.print("Divisor's Equivalent Binary value is : " + binDivisor + "\n");

        normB = normalize(binDivisor); //normalizing divisor
        System.out.print("Normalized Divisor: " + normB + "\n");

        negNormB = negativeOfB(normB); //calculate 2s compliment of divisor
        n = normB.length() - 2;
        maxLeftShifts = n + 1;
        numLeftShifts = 0;
        this.dividend = binDividend;
        this.divisor = binDivisor;
        
        adjustAQ();
        System.out.println("adjust AQ " + AQ.toString());
        shiftOverZeros();

        //loop
        while (numLeftShifts < maxLeftShifts) {
            addB(negNormB);
            System.out.println("Subtract B " + negNormB.toString());
            if (AQ.charAt(0) == '0') {
                System.out.println("Pos result " + AQ.toString());
                positive();
            } else {
                System.out.println("Neg result " + AQ.toString());
                negative();
            }
        }
        String q = getQuotient();
        String r = getRemainder();
        
        System.out.println("Q = " +q+ "\nR = " +r+ "\n");
        System.out.println( "n = " +n+ "\tExecution time = " +exTime+ "\n");
        System.out.println("\n");
        
    }

    StringBuilder binInput(StringBuilder stringBuilder) { //format input
        int i = stringBuilder.indexOf("(");
        String binString = stringBuilder.substring(0, i);
        StringBuilder binSB = new StringBuilder(binString);
        binSB.insert(0, "0");
        return binSB;
    }

    boolean contains(StringBuilder sb, String findString) {

        /*
         * if the substring is found, position of the match is
         * returned which is >=0, if not -1 is returned
         */
        return sb.indexOf(findString) > -1;
    }

    StringBuilder HexToBin(StringBuilder hexSB) { //convert Hexadecimal values to binary
        if (contains(hexSB, "(binary)")) {
            return binInput(hexSB);
        } else {
            int i = 0;
            StringBuilder binSB = new StringBuilder();
            for (i = 0; i < hexSB.length(); i++) {
                switch (hexSB.charAt(i)) {
                    case '.':
                        binSB.append(".");
                        break;
                    case '0':
                        binSB.append("0000");
                        break;
                    case '1':
                        binSB.append("0001");
                        break;
                    case '2':
                        binSB.append("0010");
                        break;
                    case '3':
                        binSB.append("0011");
                        break;
                    case '4':
                        binSB.append("0100");
                        break;
                    case '5':
                        binSB.append("0101");
                        break;
                    case '6':
                        binSB.append("0110");
                        break;
                    case '7':
                        binSB.append("0111");
                        break;
                    case '8':
                        binSB.append("1000");
                        break;
                    case '9':
                        binSB.append("1001");
                        break;
                    case 'A':
                    case 'a':
                        binSB.append("1010");
                        break;
                    case 'B':
                    case 'b':
                        binSB.append("1011");
                        break;
                    case 'C':
                    case 'c':
                        binSB.append("1100");
                        break;
                    case 'D':
                    case 'd':
                        binSB.append("1101");
                        break;
                    case 'E':
                    case 'e':
                        binSB.append("1110");
                        break;
                    case 'F':
                    case 'f':
                        binSB.append("1111");
                        break;
                    default:
                        System.out.print("\nInvalid hexadecimal digit " + hexSB.charAt(i));

                }
            }
            binSB.insert(0, "0");
            return binSB;
        }
    }

    StringBuilder normalize(StringBuilder norm) { //Normalize a binary value
        while (norm.length() >= 2) {
            if (norm.charAt(2) == '0') {
                norm.deleteCharAt(2);
                norm.append('0');
                AQshift++;
                exTime += 3; //3 dt for shifting
            } else {
                return norm;
            }
        }
        return norm;
    }

    StringBuilder negativeOfB(StringBuilder num) { //Calculate the 2s compliment
        StringBuilder neg = new StringBuilder(num);
        //Calculate 1s compliment
        for (int i = 0; i < num.length(); i++) {
            if (neg.charAt(i) == '0') {
                neg.setCharAt(i, '1');
            } else if (neg.charAt(i) == '1') {
                neg.setCharAt(i, '0');
            }
        }
        //Calculate 2s compliment
        for (int i = num.length() - 1; i >= 0; i--) {
            if (neg.charAt(i) == '0') {
                neg.setCharAt(i, '1');
                System.out.println("2s complement " + neg.toString());
                return neg;
            } else if (neg.charAt(i) == '1') {
                neg.setCharAt(i, '0');
            }
        }
        exTime += n; //n*dt for 2s compliment operation
        return neg;
    }

    void adjustAQ() {
        AQ = dividend;
        if (AQ.length() > n * 2 + 2) {
            System.out.println("Dividend too long");
        } else if (AQ.length() < n * 2 + 2) {
            while (AQ.length() < n * 2 + 2) {
                AQ.append('0');
            }
        }
        for (int i = 0; i < AQshift; i++) {
            AQ.deleteCharAt(2);
            AQ.append('*');
            exTime += 3; //3 dt for shifting
        }
    }

    void shiftOverZeros() {
        while (AQ.charAt(2) == '0' && (maxLeftShifts - numLeftShifts) > 0) {
            AQ.deleteCharAt(2);
            AQ.append('0');
            numLeftShifts++;
            exTime += 3; //3 dt for shifting
            
        }
        System.out.println("Shift Over Zeros " + AQ.toString());
    }

    void shiftOverOnes() {
        while (AQ.charAt(2) == '1' && (maxLeftShifts - numLeftShifts) > 0) {
            AQ.deleteCharAt(2);
            AQ.append('1');
            numLeftShifts++;
            exTime += 3; //3 dt for shifting
        }
        System.out.println("Shift Over Ones " + AQ.toString());

    }

    void negative() { //Function called when result is negative
        if (numLeftShifts >= maxLeftShifts) {
            return;
        }
        AQ.deleteCharAt(2);
        AQ.append('0');
        System.out.println("ShL, q0=0 " + AQ.toString());
        numLeftShifts++;
        exTime += 3; //3 dt for shifting
        shiftOverOnes();
        if (numLeftShifts >= maxLeftShifts) {
            return;
        }
        addB(normB);
        System.out.println("Add B" + normB);

        if (AQ.charAt(0) == '0') { //positive
            System.out.println("Pos result " + AQ.toString());
            AQ.deleteCharAt(2);
            AQ.append('1');
            System.out.println("ShL, q0=1 " + AQ.toString());
            numLeftShifts++;
            exTime += 3; //3 dt for shifting
            return;
        } else { //negative
            System.out.println("Neg result " + AQ.toString());
            negative();
        }
//        if(numLeftShifts >= maxLeftShifts){
//            return;
//        }
//        AQ.deleteCharAt(2);
//        AQ.append('0');
//        System.out.println("ShL, q0=0 " + AQ.toString());
//        numLeftShifts++;

        //if(AQ.charAt(0) == '0')
    }

    void positive() { //Function called when result is positive
        if (numLeftShifts >= maxLeftShifts) {
            return;
        }
        AQ.deleteCharAt(2);
        AQ.append('1');
        numLeftShifts++;
        exTime += 3; //3 dt for shifting
        System.out.println("ShL, q0=1 " + AQ.toString());
        shiftOverZeros();
    }

    String getQuotient(){
        StringBuilder quotient = new StringBuilder();
        quotient.append("0.");
        String q = AQ.substring(n+2);
        quotient.append(q);
        return quotient.toString();
    }
    
    String getRemainder() { //Calculate the remainder
        int remPosition;
        StringBuilder remainder = new StringBuilder();
        if (AQ.charAt(0) == '1' && AQ.charAt(2) == '1') { //check if AQ is negative
            AQ.insert(2, '1'); // Shift right
            System.out.println("ShR " + AQ.substring(0, n+2));
            addB(normB);
            System.out.println("Add B" + normB);
            System.out.println("A " + AQ.substring(0, n+2));
            remPosition = AQshift+numLeftShifts-1; //num +1 (-1 for right shift)
        }
        else{
            remPosition = AQshift+numLeftShifts ; //num +1 (-1 for right shift)
        }
        String rem = AQ.substring(2, 2*n-remPosition +2);
        
        remainder.append("0.");
        for (int i=0; i < remPosition; i++) {
            remainder.append('0');
        }
        remainder.append(rem);
        return remainder.toString();
    }

    void addB(StringBuilder b) { //add or subtract B to A

        int carry = 0;
        for (int i = n + 1; i >= 0; i--) {
            if((n-i)%4 == 0){
                if (i == n){
                    exTime += 10; //1st FA
                }
                else{
                    exTime += 2; //Time for MUX
                }
            }
            int temp = 0;
            if (AQ.charAt(i) != '.') {
                if (AQ.charAt(i) == '1') {
                    temp++;
                }
                if (b.charAt(i) == '1') {
                    temp++;
                }
                temp += carry;
                if (AQ.charAt(i) == '*') {
                    temp = -1;
                }
                switch (temp) {
                    case (0):
                        AQ.setCharAt(i, '0');
                        break;
                    case (1):
                        AQ.setCharAt(i, '1');
                        carry = 0;
                        break;
                    case (2):
                        AQ.setCharAt(i, '0');
                        carry = 1;
                        break;
                    case (3):
                        AQ.setCharAt(i, '1');
                        carry = 1;
                        break;
                    case (-1):
                        AQ.setCharAt(i, '*');
                        carry = 0;
                        break;
                }
            }
        }
    }
}
