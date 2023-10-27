# ac算法(动态更新关键词)的JAVA实现
* 实现了比如短语“我有一个大飞机”，设置关键词“飞机”分类为1，使用`Patterns#searchKeyword`能够返回语句中的关键词”飞机“
* `Patterns#addKeyword`添加“大飞机”关键词返回“大飞机”、“飞机”关键词。`Patterns#removeKeyword`删除“大飞机”关键词返回“飞机”关键词
* 参考
  1. **提供了动态删除和新增关键词** 算法参考了[stackoverflow](https://stackoverflow.com/questions/53288664/updating-an-aho-corasick-trie-in-the-face-of-inserts-and-deletes)
  2. 参考了[美团](https://tech.meituan.com/2014/06/09/ac-algorithm-in-meituan-order-system-practice.html)
