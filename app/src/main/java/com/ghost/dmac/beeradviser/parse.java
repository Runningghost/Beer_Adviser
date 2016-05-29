package com.ghost.dmac.beeradviser;

/**
 * Created by Dmac on 5/28/2016.
 */
public class parse {
    StringBuilder beerList = new StringBuilder();

    public String printNames(String string) {
        int i = 0;
        int count = 0;
        while (true) {
            int found = string.indexOf("\"name\":", i);
            if (found == -1) break;
            int start = found + 8;
            int end = string.indexOf("\",\"", start);
            String beerName = string.substring(start, end);
            beerList.append(beerName).append('\n');
            i = end + 1;
            count++;

        }
        String amount = Integer.toString(count) + '\n';
        beerList.insert(0, amount);
        return String.valueOf(beerList);

    }
}
