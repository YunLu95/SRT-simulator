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

    int n;
    StringBuilder divisor;
    StringBuilder dividend;
    StringBuilder normB, negNormB;
    int shift = 0;
    int maxLeftShifts;
    int numLeftShifts;

    StringBuilder AQ;


    public Srt(String dividend, String divisor) {
        StringBuilder sdividend = new StringBuilder(dividend);
        StringBuilder sdivisor = new StringBuilder(divisor);

        //call hextobin
        System.out.println("You entered dividend string: "+sdividend+", divisor string:"+sdivisor);
        System.out.print("Dividend's Equivalent Binary value is : "+HexToBin(sdividend)+"\n");
        System.out.print("Divisor's Equivalent Binary value is : "+HexToBin(sdivisor)+"\n");

//        normB = normalize(divisor);
//        System.out.println("Normalized B " + normB.toString());
//        negNormB = negativeOfB(normB);
//        n = normB.length() - 2;
//        maxLeftShifts = n+1;
//        numLeftShifts = 0;
//        adjustAQ();
//        System.out.println("adjust AQ " + AQ.toString());
//        shiftOverZeros();
//        System.out.println("shiftOverZeros AQ " + AQ.toString());
//        addB(negNormB);
//        System.out.println("subtractB AQ " + AQ.toString());
        //loop
    }


    StringBuilder HexToBin(StringBuilder hexSB)
    {
        int i = 0;
        StringBuilder binSB = new StringBuilder();
        for(i = 0;i < hexSB.length(); i ++) {
            switch (hexSB.charAt(i)) {
                case '.':
//                    System.out.print(".");
                    binSB.append(".");
                    break;
                case '0':
//                    System.out.print("0000");
                    binSB.append("0000");
                    break;
                case '1':
//                    System.out.print("0001");
                    binSB.append("0001");
                    break;
                case '2':
//                    System.out.print("0010");
                    binSB.append("0010");
                    break;
                case '3':
//                    System.out.print("0011");
                    binSB.append("0011");
                    break;
                case '4':
//                    System.out.print("0100");
                    binSB.append("0100");
                    break;
                case '5':
//                    System.out.print("0101");
                    binSB.append("0101");
                    break;
                case '6':
//                    System.out.print("0110");
                    binSB.append("0110");
                    break;
                case '7':
//                    System.out.print("0111");
                    binSB.append("0111");
                    break;
                case '8':
//                    System.out.print("1000");
                    binSB.append("1000");
                    break;
                case '9':
//                    System.out.print("1001");
                    binSB.append("1001");
                    break;
                case 'A':
                case 'a':
//                    System.out.print("1010");
                    binSB.append("1010");
                    break;
                case 'B':
                case 'b':
//                    System.out.print("1011");
                    binSB.append("1011");
                    break;
                case 'C':
                case 'c':
//                    System.out.print("1100");
                    binSB.append("1100");
                    break;
                case 'D':
                case 'd':
//                    System.out.print("1101");
                    binSB.append("1101");
                    break;
                case 'E':
                case 'e':
//                    System.out.print("1110");
                    binSB.append("1110");
                    break;
                case 'F':
                case 'f':
//                    System.out.print("1111");
                    binSB.append("1111");
                    break;
                default:
                    System.out.print("\nInvalid hexadecimal digit " + hexSB.charAt(i));
            }
        }
        return binSB;
    }

    StringBuilder normalize(String num) {
        StringBuilder norm = new StringBuilder(divisor);
        while (norm.length() >= 2) {
            if (norm.charAt(2) == '0') {
                norm.deleteCharAt(2);
                norm.append('0');
                shift++;
            } else {
                return norm;
            }
        }
        return norm;
    }

    StringBuilder negativeOfB(StringBuilder num) {
        StringBuilder neg = new StringBuilder(num);
        for (int i = 0; i < num.length(); i++) {
            if (neg.charAt(i) == '0') {
                neg.setCharAt(i, '1');
            } else if (neg.charAt(i) == '1') {
                neg.setCharAt(i, '0');
            }
        }
        //System.out.println("1s complement "+ neg.toString());

        for (int i = num.length() - 1; i >= 0; i--) {
            if (neg.charAt(i) == '0') {
                neg.setCharAt(i, '1');
                System.out.println("2s complement " + neg.toString());
                return neg;
            } else if (neg.charAt(i) == '1') {
                neg.setCharAt(i, '0');
            }
        }
        //System.out.println("2s complement"+ neg.toString());
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
        for (int i = 0; i < shift; i++) {
            AQ.deleteCharAt(2);
            AQ.append('*');
        }
    }

    void shiftOverZeros() {
        while (AQ.charAt(2) == '0' && (maxLeftShifts - numLeftShifts)>0) {
            AQ.deleteCharAt(2);
            AQ.append('0');
            numLeftShifts++;
        }
    }

    void shiftOverOnes() {
        while (AQ.charAt(2) == '1'&& (maxLeftShifts - numLeftShifts)>0) {
            AQ.deleteCharAt(2);
            AQ.append('1');
            numLeftShifts++;
        }
    }

    void negative() {
        AQ.deleteCharAt(2);
        AQ.append('0');
        numLeftShifts++;
        shiftOverOnes();
        addB(normB);
        
        //if(AQ.charAt(0) == '0')
    }

    void positive() {
        AQ.deleteCharAt(2);
        AQ.append('1');
        numLeftShifts++;
        shiftOverZeros();
    }

    void addB(StringBuilder b) {
        int carry = 0;
        for (int i = n + 1; i >= 0; i--) {
            int temp = 0;
            if (AQ.charAt(i) != '.') {
                if (AQ.charAt(i) == '1') {
                    temp++;
                }
                if (b.charAt(i) == '1') {
                    temp++;
                }
                temp += carry;

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
                }
            }
        }
    }
}
