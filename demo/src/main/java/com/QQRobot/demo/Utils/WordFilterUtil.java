package com.QQRobot.demo.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordFilterUtil {
    private TrieNode rootNode = new TrieNode();


    public List<String> checkKeyWord(String message) {
        TrieNode tempNode = rootNode;
        List<String> res = new ArrayList<>();
        int position = 0;
        for(int begin=0;begin<message.length();++begin) {
            tempNode = rootNode;
            position = begin;
            while (position < message.length()) {
                tempNode = tempNode.nextNode(message.charAt(position));
                if(tempNode == null) break;
                ++position;
                if(tempNode.isWord()) {
                    res.add(message.substring(begin,position));
                }
            }
        }
        return res;
    }

    public void addWord(String s) {
        TrieNode tempNode = rootNode;
        for (int i = 0; i < s.length(); i++) {
            if(tempNode.nextNode(s.charAt(i))!=null) {
                tempNode = tempNode.nextNode(s.charAt(i));
            } else {
                tempNode = tempNode.addNode(s.charAt(i));
            }
        }
        tempNode.setWord(true);
    }

    private class TrieNode {
        private boolean isWord = false;
        private Map<Character,TrieNode> children;

        public TrieNode() {
            this.children = new HashMap<>();
        }
        public boolean isWord() {
            return this.isWord;
        }
        public void setWord(boolean isWord) {
            this.isWord = isWord;
        }
        public TrieNode nextNode(Character c) {
            return children.get(c);
        }
        public TrieNode addNode(Character c) {
            children.put(c,new TrieNode());
            return nextNode(c);
        }
    }
}
