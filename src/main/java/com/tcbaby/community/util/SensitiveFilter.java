package com.tcbaby.community.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tcbaby
 * @date 20/05/09 15:49
 */
@Slf4j
@Component
public class SensitiveFilter {

    /** 替换敏感字符 */
    private static final String REPLACEMENT = "***";

    /** 敏感词文件 */
    @Value("${community.sensitive-words-file}")
    private String filename = "sensitive-words.txt";

    /** 前缀树根节点 */
    private final TrieNode rootNode;

    /** 构建前缀树 */
    {
        rootNode = new TrieNode();
        try(
                InputStream in = SensitiveFilter.class.getClassLoader().getResourceAsStream(filename);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        ) {
            String keyword = null;
            while ((keyword = reader.readLine()) != null) {
                addKeyword(keyword);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("前缀树构建失败：{}", e.getMessage());
        }
    }

    /**
     * 向前缀树中添加一个敏感词
     * @param keyword
     */
    private void addKeyword(String keyword) {
        TrieNode node = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            Character c = keyword.charAt(i);
            TrieNode childNode = node.getChildNode(c);
            if (childNode == null) {
                childNode = new TrieNode();
                node.addChildNode(c, childNode);
            }
            node = childNode;
        }
        // 设置敏感词结束标记
        node.isEnd = true;
    }

    /**
     * 过滤content中存在的敏感词
     * @param content
     * @return 过滤后的结果
     */
    public String filter(String content) {
        if (StringUtils.isBlank(content)) {
            throw new IllegalArgumentException("参数content不能为空！");
        }
        StringBuffer sb = new StringBuffer();
        int start = 0, position = 0;
        TrieNode node = rootNode;
        while (position < content.length()) {
            char c = content.charAt(position);
            if (isSymbol(c)) {
                if (node == rootNode) {
                    // 在前缀树开头 碰到敏感词
                    sb.append(c);
                    start++;
                }
                // 前缀树之前 碰到敏感词
                position++;
                continue;
            }

            node = node.getChildNode(c);
            if (node == null) {
                // start-position之间的字符 不构成敏感词  记录start出字符，再从start+1检测
                sb.append(content.charAt(start));
                position = ++start;
                node = rootNode;
            } else if (!node.isEnd) {
                // start-position之间的字符 是敏感词的开头部分
                position++;
            } else {
                // 到敏感词得结尾， start-position之间的字符 构成敏感词
                sb.append(REPLACEMENT);
                position = ++start;
                node = rootNode;
            }
        }
        sb.append(content.substring(start));
        return sb.toString();
    }

    /** 判断是否是符号 */
    private boolean isSymbol(char c) {
        // 0x2E80~0x9FFF 是东亚文字范围
        return (c < 0x2E80 || c > 0x9FFF) && !CharUtils.isAsciiAlphanumeric(c);
    }

    /** 前缀树节点 */
    private class TrieNode {

        /** 是否是关键字的结尾 */
        private boolean isEnd = false;

        /**
         * 所有的孩子节点
         * key: 关键字符    value：孩子节点
         */
        private Map<Character, TrieNode> childNodes = new HashMap<>();

        public void addChildNode(Character c, TrieNode node) {
            childNodes.put(c, node);
        }

        public TrieNode getChildNode(Character c) {
            return childNodes.get(c);
        }

        public boolean containChildNode(Character c) {
            return childNodes.containsKey(c);
        }
    }
}
