package ac;


import java.util.*;

/**
 * @author qijun
 * @since 2023/10/19
 */
public class Patterns {
    protected final Node root = new Node();

    Map<Character, List<Node>> charNodeMap = new HashMap<>();
    {
        root.isRoot = true;
        root.failureNode = root;
        root.parentNode = null;
        root.depth = 0;
    }



    public Patterns(List<Keyword> keywords) {
        for(Keyword keyword : keywords){
            addKeywordBeforeSetFailed(keyword);
        }
        buildFailNode();
    }
    public void addKeywordBeforeSetFailed(Keyword keyword) {
        char[] wordCharArr = keyword.getWord().toCharArray();
        Node current = root;
        for(char currentChar : wordCharArr){
            if(current.containsChild(currentChar)){
                current = current.getChild(currentChar);
            } else {
                Node node = new Node(false,currentChar, root);
                node.parentNode = current;
                node.depth = current.depth + 1;
                putCharNodeMap(currentChar, node);
                current.addChild(node);
                current = node;
            }
        }
        current.addKeyword(keyword);
    }


    public void removeKeyword(Keyword keyword){
        char[] wordCharArr = keyword.getWord().toCharArray();
        int length = wordCharArr.length;

        List<Node> nodes = charNodeMap.get(wordCharArr[length - 1]);
        Node lastNode = null;
        // todo 待优化
        for (Node node : nodes) {
            if (node.depth == length && node.keywords.contains(keyword)) {
                lastNode = node;
                break;
            }
        }
        Node beginNode = lastNode;

        while (lastNode != null) {
            List<Node> tempNodes = charNodeMap.get(lastNode.character);
            if (lastNode.children == null || lastNode.children.size() == 0) {
                lastNode.parentNode.children.remove(lastNode.character);
                charNodeMap.get(lastNode.character).remove(lastNode);

                for (Node tempNode : tempNodes) {
                    // 更新fail指针
                    if(tempNode.failureNode != null && tempNode.failureNode == lastNode && tempNode.parentNode != null){
                        tempNode.removeKeyword(keyword);
                        Node failNode = tempNode.parentNode.failureNode;
                        while (!failNode.containsChild(lastNode.character)) {
                            failNode = failNode.failureNode;
                            if (failNode.isRoot) break;
                        }
                        if (failNode.containsChild(lastNode.character)) {
                            tempNode.failureNode = failNode.getChild(lastNode.character);
                            tempNode.addKeywords(tempNode.failureNode.keywords);
                        }
                    }

                }
            }else {
                for (Node tempNode : tempNodes) {
                    // 更新fail指针
                    if(tempNode.failureNode != null && tempNode.failureNode == lastNode && tempNode == beginNode){
                        tempNode.removeKeyword(keyword);
                    }

                }
            }
            lastNode = lastNode.parentNode;
            if(lastNode.isRoot){
                break;
            }
        }
    }

    public void addKeyword(Keyword keyword){
        char[] wordCharArr = keyword.getWord().toCharArray();
        Node current = root;
        for (int i = 0; i < wordCharArr.length; i++) {
            char currentChar = wordCharArr[i];
            if(current.containsChild(currentChar)){
                current = current.getChild(currentChar);
            } else {
                Node node = new Node(false,currentChar, root);
                node.parentNode = current;
                node.depth = current.depth + 1;
                putCharNodeMap(currentChar, node);
                // 设置这个节点的failed node
                setFailNode(node);
                // 设置这个节点相关的节点的failedNode
                current.addChild(node);
                if(i == wordCharArr.length- 1){
                    node.addKeyword(keyword);
                }
                if(charNodeMap.get(currentChar) != null){
                    List<Node> nodes = charNodeMap.get(currentChar);
                    for (Node node1 : nodes) {
                        if(node1.depth > current.depth){
                            setFailNode(node1);
                        }
                    }

                }
                current = node;
            }
        }
       


    }


    private void putCharNodeMap(Character character, Node node){
        if(charNodeMap.containsKey(character)){
            charNodeMap.get(character).add(node);
        }else {
            List<Node> nodes = new ArrayList<>();
            nodes.add(node);
            charNodeMap.put(character, nodes);
        }
    }

    /**
     * 这边实际上没有设置父节点的子节点的failed node
     */
    public void buildFailNode(){
        Queue<Node> queue = new LinkedList<Node>();
        Node node = root;
        for (Node d1 : node.children.values())
            queue.offer(d1);
        while (!queue.isEmpty()) {
            node = queue.poll();
            if (node.children != null) {
                for (Node curNode : node.children.values()) {
                    queue.offer(curNode);
                    Node failNode = node.failureNode;
                    while (!failNode.containsChild(curNode.character)) {
                        failNode = failNode.failureNode;
                        if (failNode.isRoot) break;
                    }
                    if (failNode.containsChild(curNode.character)) {
                        curNode.failureNode = failNode.getChild(curNode.character);
                        curNode.addKeywords(curNode.failureNode.keywords);
                    }
                }
            }
        }
    }

    private void setFailNode(Node curNode){
        if(curNode.isRoot){
            return;
        }
        if(curNode.parentNode.isRoot){
            curNode.failureNode = root;
            return;
        }
        Node failNode = curNode.parentNode.failureNode;
        while (!failNode.containsChild(curNode.character)) {
            failNode = failNode.failureNode;
            if (failNode.isRoot) break;
        }
        if (failNode.containsChild(curNode.character)) {
            curNode.failureNode = failNode.getChild(curNode.character);
            curNode.addKeywords(curNode.failureNode.keywords);
        }

    }

    public Set<Keyword> searchKeyword(String data, Integer category) {
        Set<Keyword> matchResult = new HashSet<Keyword>();
        Node node = root;
        char[] chs = data.toCharArray();
        for(int i=0; i < chs.length; i++){
            while (!node.containsChild(chs[i])) {
                node = node.failureNode;
                if (node.isRoot) break;
            }
            if(node.containsChild(chs[i])){
                node = node.getChild(chs[i]);
                if(node.keywords != null){
                    for(Keyword pattern : node.keywords){
                        if (category == null) {
                            matchResult.add(pattern);
                        } else {
                            if (pattern.getCategories().contains(category)) {
                                matchResult.add(pattern);
                            }
                        }
                    }
                }
            }
        }
        return matchResult;
    }

    public static void main(String[] args) {
        List<Keyword> keywords = new ArrayList<>();
        Keyword keyword1 = new Keyword("中国",Arrays.asList(1));

        keywords.add(keyword1);
        Keyword keyword2 = new Keyword("美国",Arrays.asList(1));

        keywords.add(keyword2);

        Keyword keyword3 = new Keyword("高铁",Arrays.asList(2));
        keywords.add(keyword3);

        Keyword keyword4 = new Keyword("飞机",Arrays.asList(2));

        keywords.add(keyword4);

        Patterns patterns = new Patterns(keywords);

        // 新增关键词确认
        patterns.addKeyword(new Keyword("大飞机", Arrays.asList(2)));

        Set<Keyword> keywords1 = patterns.searchKeyword("中国有高帝俄，中国有高铁，美国有飞机，轮船,大飞机", null);

        System.out.println(keyword1);

        // 删除关键词确认
        patterns.removeKeyword(new Keyword("飞机", Arrays.asList(2)));
        keywords1 = patterns.searchKeyword("中国有高帝俄，中国有高铁，美国有飞机，轮船,大飞机", null);

        System.out.println(keyword1);





    }
}
