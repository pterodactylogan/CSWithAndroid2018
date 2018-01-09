/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();

    private ArrayList<String> wordList = new ArrayList<String>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordSet.add(word);
            if(lettersToWord.containsKey(sortWord(word))){
                ArrayList<String> list = lettersToWord.get(sortWord(word));
                list.add(word);
                lettersToWord.put(sortWord(word),list);
            }else{
                ArrayList<String> list = new ArrayList<String>();
                list.add(word);
                lettersToWord.put(sortWord(word),list);
            }
        }
    }

    //word entered, current word
    public boolean isGoodWord(String word, String base) {
        if(!wordSet.contains(word)) return false;
        for(int i=0; i<=word.length()-base.length();i++){
            if(word.substring(i,base.length()+i).equals(base)) return false;
        }
        return true;
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = lettersToWord.get(sortWord(targetWord));
        return result;
    }

    static boolean isAnagram(String first, String second) {
        if(first.length()!=second.length()) return false;
        //Log.d("sorted", sortWord(first));
        if(sortWord(first).equals(sortWord(second))) {
            //Log.d("equal", first);
            return true;
        }
        return false;
    }
    //puts the characters in a word in alphabetical order
    static String sortWord(String word){
        char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return String.valueOf(chars);
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for(int i=0; i<26; i++){
            String letter = Character.toString((char)(i+97));
            if(lettersToWord.containsKey(sortWord(word+letter))){
                List<String> anagrams = getAnagrams(word+letter);
                if(anagrams.size()>0) for(String s: anagrams) result.add(s);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        return "stop";
    }
}
