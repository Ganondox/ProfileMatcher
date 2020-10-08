package com.company;

import javax.crypto.Cipher;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.PrivateKey;
import java.util.LinkedList;
import java.util.List;

public class HandlerSupporter {


    public static char intToChar(int integer){
        switch (integer){
            case 10:
                return '\n';
            case 33:
                return '!';
            case 39:
                return '\'';
            case 35:
                return '#';
            case 38:
                return '&';
            case 45:
                return '-';
            case 46:
                return '.';
            case 48:
                return '0';
            case 49:
                return '1';
            case 50:
                return '2';
            case 51:
                return '3';
            case 52:
                return '4';
            case 53:
                return '5';
            case 54:
                return '6';
            case 55:
                return '7';
            case 56:
                return '8';
            case 57:
                return '9';
            case 58:
                return ':';
            case 64:
                return '@';
            case 65:
                return 'A';
            case 66:
                return 'B';
            case 67:
                return 'C';
            case 68:
                return 'D';
            case 69:
                return 'E';
            case 70:
                return 'F';
            case 71:
                return 'G';
            case 72:
                return 'H';
            case 73:
                return 'I';
            case 74:
                return 'J';
            case 75:
                return 'K';
            case 76:
                return 'L';
            case 77:
                return 'M';
            case 78:
                return 'N';
            case 79:
                return 'O';
            case 80:
                return 'P';
            case 81:
                return 'Q';
            case 82:
                return 'R';
            case 83:
                return 'S';
            case 84:
                return 'T';
            case 85:
                return 'U';
            case 86:
                return 'V';
            case 87:
                return 'W';
            case 88:
                return 'X';
            case 89:
                return 'Y';
            case 90:
                return 'Z';
            case 97:
                return 'a';
            case 98:
                return 'b';
            case 99:
                return 'c';
            case 100:
                return 'd';
            case 101:
                return 'e';
            case 102:
                return 'f';
            case 103:
                return 'g';
            case 104:
                return 'h';
            case 105:
                return 'i';
            case 106:
                return 'j';
            case 107:
                return 'k';
            case 108:
                return 'l';
            case 109:
                return 'm';
            case 110:
                return 'n';
            case 111:
                return 'o';
            case 112:
                return 'p';
            case 113:
                return 'q';
            case 114:
                return 'r';
            case 115:
                return 's';
            case 116:
                return 't';
            case 117:
                return 'u';
            case 118:
                return 'v';
            case 119:
                return 'w';
            case 120:
                return 'x';
            case 121:
                return 'y';
            case 122:
                return 'z';
            default:
                return ' ';

        }
    }

    public static String decryptStream(InputStream stream, PrivateKey key){
        try {
            List<Byte> bytereader = new LinkedList<>();
            int letter = stream.read();
            while(letter != -1){
                Byte bite = (byte)letter;
                //System.out.println(bite + "p");
                bytereader.add(bite);
                letter = stream.read();
            }

            byte[] bytes = new byte[bytereader.size()];
            for(int i = 0; i < bytes.length; i++){
                bytes[i] = bytereader.get(i);
            }

            byte[] out = cipherTrans(false, key, bytes);


            String message = "";
            for (int i = 0; i < out.length; i++) {
                message += HandlerSupporter.intToChar(out[i]);
            }

            return message;
        } catch (Exception e){
            return null;
        }
    }

    public static byte[] cipherTrans(boolean encrypt, Key key, byte[] bytes) throws Exception{


        int insize;
        int outsize;
        int mode;

        if(encrypt) {

            insize = 245;
            outsize = 256;
            mode = Cipher.ENCRYPT_MODE;
        } else {
            insize = 256;
            outsize = 245;
            mode = Cipher.DECRYPT_MODE;
        }


            //byte[] out = new byte[bytes.length];

            /*int fullblocks = bytes.length / 245;
            int totalblocks;
            if(bytes.length % 245 == 0){
                totalblocks = fullblocks;
            } else {
                totalblocks = fullblocks + 1;
            }

            byte[] out = new byte[totalblocks * 256];




             */

            //split into blocks
            int blocks = bytes.length / insize;
            byte[] out = new byte[blocks * outsize];
            for (int i = 0; i < blocks; i++) {
                byte[] block = new byte[insize];
                for (int j = 0; j < insize; j++) {

                    block[j] = bytes[i * insize + j];

                }

                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(mode, key);
                //System.out.println("OHHHH");
                byte[] o = cipher.doFinal(block);
                //System.out.println(o.length + "b");

                for (int j = 0; j < outsize; j++) {

                    out[i * outsize + j] = o[j];

                }
            }

            //encrypt tail
            /*int end = blocks * 245;
            byte[] tail = new byte[bytes.length - end];
            for (int i = end; i < bytes.length; i++) {
                tail[i - end] = bytes[i];
            }
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //System.out.println("OHHHH");
            byte[] o = cipher.doFinal(tail);
            System.out.println(o.length + "b");
            for (int i = end; i < bytes.length; i++) {
                out[i] = o[i - end];
            }*/

            return out;

    }


}
