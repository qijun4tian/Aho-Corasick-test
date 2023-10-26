package ac;

import lombok.Data;

import java.util.*;

/**
 * @author qijun
 * @since 2023/10/19
 */
@Data
public class Node {
    boolean isRoot = false;                    //自动机的状态，也就是节点数字
    char character = 0;           //指向当前节点的字符，也即条件
    Node failureNode;             //匹配失败时，下一个节点
    Set<Keyword> keywords;       //匹配成功时，当前节点对应的关键词
    int depth;
    Node parentNode;
    Map<Character,Node> children = null;

    public Node() {
    }


    public Node(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public Node(boolean isRoot, char character, Node failureNode) {
        this.isRoot = isRoot;
        this.character = character;
        this.failureNode = failureNode;
    }


    public void addKeyword(Keyword keyword){
        if(keywords == null || keywords.size() == 0){
            keywords = new HashSet<>();
        }
        keywords.add(keyword);
    }

    public void removeKeyword(Keyword keyword){
       if(keywords != null && keywords.size() != 0) {
           keywords.remove(keyword);
       }
    }

    public boolean containsChild(char character){
        return children != null && children.containsKey(character);
    }

    public Node getChild(char character){
        if(children == null){
            return null;
        }
        return children.get(character);

    }

    public void addChild(Node node){
        if(children == null){
            children = new HashMap<>();
        }
        children.put(node.character, node);

    }

    public void addKeywords(Collection<Keyword> addKeywords){
        if (addKeywords == null) {
            return;
        }
        if(keywords == null || keywords.size() == 0){
            keywords = new HashSet<>();
        }
        keywords.addAll(addKeywords);
    }


    @Override
    public String toString() {
        return "Node{" +
                "isRoot=" + isRoot +
                ", character=" + character +
                ", keywords=" + keywords +
                '}';
    }

    public void setCharacter(char character) {
        this.character = character;
    }

    public void setChildren(Map<Character, Node> children) {
        this.children = children;
    }
}
