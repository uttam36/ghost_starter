package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix)
    {
        if(prefix.equals(""))
        {
            Random random = new Random();
            int n = random.nextInt(words.size());
            return words.get(n);
        }
        return binarySearch(prefix,0,words.size()-1);
    }

    public String binarySearch(String prefix,int start,int end)
    {
        if(start>end)
            return null;
        int mid = (start+end)/2;

        String tmp = words.get(mid);
        if(tmp.startsWith(prefix))
            return words.get(mid);
        else if(tmp.compareTo(prefix)<0)
        {
           return binarySearch(prefix,mid+1,end);
        }
        else
            return binarySearch(prefix,start,mid-1);
    }
    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
