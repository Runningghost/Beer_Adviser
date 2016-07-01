package com.ghost.dmac.beeradviser;

public class parse {
    StringBuilder beerList = new StringBuilder();
    int brew_plate;

    public String printNames(String string) {

        int pi = 0;
        int pcount = 1;
        while (true) {
            int found = string.indexOf("\"brew_plate\":", pi);
            if (found == -1) break;
            int pstart = found + 14;
            int pend = string.indexOf("\",\"", pstart);
            brew_plate = Integer.parseInt(string.substring(pstart, pend));

            if (brew_plate > pcount) {
                pcount = brew_plate;
            }
            pi = pend + 1;
        }

        int i = 0;
        int count = 0;
        while (true) {
            int found = string.indexOf("\"name\":", i);
            if (found == -1) break;
            int start = found + 8;
            int end = string.indexOf("\",\"", start);
            String beerName = string.substring(start, end);

            int plate_found = string.indexOf("\"brew_plate\":", end);
            int plate_start = plate_found + 14;
            int plate_end = string.indexOf("\",\"", plate_start);
            int plateNum = Integer.parseInt(string.substring(plate_start, plate_end));


            if (pcount == plateNum) {
                beerList.append(beerName).append('\n');

                count++;
            }
            i = end + 1;
        }

        String amount = Integer.toString(count) + '\n';
        beerList.insert(0, amount);
        String strReplace =  String.valueOf(beerList);
        String first = strReplace.replace("\\/", "/");
        return first.replace("\\u0027", "'");

    }

    public String userNum(String num) {
        //String str1 = new String(num);
        //int j = 75;//length of class
        return num.substring(82);



    }

}
