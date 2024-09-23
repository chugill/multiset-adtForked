import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree {

    private Object root;
    private List<Tree> subtrees;
    private static final Random random = new Random();

    private void CheckInvariant(){
        // If root is None, then subtrees is an empty list.
        assert root != null || subtrees.isEmpty();
    }

    // Constructor
    private Tree(Object root, List<Tree> subtrees){
        this.root = root;
        this.subtrees = subtrees;
    }

    public boolean is_empty(){
        /**
         * Returns whether the tree is empty
         */
        return root == null;
    }

    public int size() {
        if (is_empty()) {
            return 0;
        } else {
            int size = 1;  // count the root
            for (Tree subtree : subtrees) {
                size += subtree.size();
            }
            return size;
        }
    }

    public int count(int item) {
        /**
         * Return the number of occurrences of <item> in this tree.
         */
        if (is_empty()) {
            return 0;
        } else {
            int num = 0;
            if (root.equals(item)) {
                num += 1;
            }
            for (Tree subtree : subtrees) {
                num += subtree.count(item);
            }
            return num;
        }
    }

    @Override
    public String toString() {
        /**
         * Return a string representation of this tree.
         */
        return strIndented(0);
    }

    private String strIndented(int depth) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            sb.append("  ");
        }
        if (is_empty()) {
            sb.append("null\n");
        } else {
            sb.append(root.toString()).append("\n");
            for (Tree subtree : subtrees) {
                sb.append(subtree.strIndented(depth + 1));
            }
        }
        return sb.toString();
    }

    public double average() {
        /**
         * Return the average of all the values in this tree.
         * Return 0.0 if this tree is empty.
         * Precondition: this is a tree of numbers.
         */
        if (is_empty()) {
            return 0.0;
        } else {
            double[] result = averageHelper();
            return result[0] / result[1];
        }
    }

    private double[] averageHelper() {
        double total = ((Number) root).doubleValue();
        int count = 1;
        for (Tree subtree : subtrees) {
            double[] subtreeResult = subtree.averageHelper();
            total += subtreeResult[0];
            count += subtreeResult[1];
        }
        return new double[]{total, count};
    }

    @Override
    public boolean equals(Object obj) {
        /**
         * Return whether <self> and <other> are equal.
         */
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Tree other = (Tree) obj;
        if (is_empty() && other.is_empty()) {
            return true;
        } else if (is_empty() || other.is_empty()) {
            return false;
        } else {
            if (!root.equals(other.root)) {
                return false;
            }
            if (subtrees.size() != other.subtrees.size()) {
                return false;
            }
            return subtrees.equals(other.subtrees);
        }
    }

    public boolean contains(int item) {
        /**
         * Return whether <item> is in this tree.
         */
        if (is_empty()) {
            return false;
        }
        if (root.equals(item)) {
            return true;
        } else {
            for (Tree subtree : subtrees) {
                if (subtree.contains(item)) {
                    return true;
                }
            }
            return false;
        }
    }

    public List<Integer> leaves() {
        /**
         * Return a list of all the leaf items in the tree.
         */
        List<Integer> leaves = new ArrayList<>();
        if (is_empty()) {
            return leaves;
        } else if (subtrees.isEmpty()) {
            leaves.add((Integer) root);
        } else {
            for (Tree subtree : subtrees) {
                leaves.addAll(subtree.leaves());
            }
        }
        return leaves;
    }

    public boolean delete_item(int item) {
        /**
         * Delete one occurrence of the given item from this tree.
         * Return true if <item> was deleted, and false otherwise.
         * Do not modify this tree if it does not contain <item>.
         */
        if (is_empty()) {
            return false;
        } else if (root.equals(item)) {
            delete_root();
            return true;
        } else {
            for (Tree subtree : subtrees) {
                boolean deleted = subtree.delete_item(item);
                if (deleted && subtree.is_empty()) {
                    subtrees.remove(subtree);
                    return true;
                } else if (deleted) {
                    return true;
                }
            }
            return false;
        }
    }

    private void delete_root() {
        if (!subtrees.isEmpty()) {
            Tree firstSubtree = subtrees.remove(0);
            root = firstSubtree.root;
            subtrees.addAll(firstSubtree.subtrees);
        } else {
            root = null;
        }
    }

    public int extract_leaf() {
        /**
         * Remove and return the leftmost leaf in a tree.
         * Precondition: this tree is non-empty.
         */
        if (subtrees.isEmpty()) {
            int oldRoot = (Integer) root;
            root = null;
            return oldRoot;
        } else {
            int leaf = subtrees.get(0).extract_leaf();
            if (subtrees.get(0).is_empty()) {
                subtrees.remove(0);
            }
            return leaf;
        }
    }

    public void insert(int item) {
        /**
         * Insert <item> into this tree using the specified algorithm.
         */
        if (is_empty()) {
            root = item;
        } else if (subtrees.isEmpty()) {
            subtrees.add(new Tree(item, new ArrayList<>()));
        } else {
            int rand = random.nextInt(3) + 1;
            if (rand == 3) {
                subtrees.add(new Tree(item, new ArrayList<>()));
            } else {
                int subtreeIndex = random.nextInt(subtrees.size());
                subtrees.get(subtreeIndex).insert(item);
            }
        }
    }

    public boolean insert_child(int item, int parent) {
        /**
         * Insert <item> into this tree as a child of <parent>.
         * If successful, return true. If <parent> is not in this tree, return false.
         * If <parent> appears more than once in this tree, <item> should only be inserted once.
         */
        if (is_empty()) {
            return false;
        } else if (root.equals(parent)) {
            subtrees.add(new Tree(item, new ArrayList<>()));
            return true;
        } else {
            for (Tree subtree : subtrees) {
                if (subtree.insert_child(item, parent)) {
                    return true;
                }
            }
            return false;
        }
    }
}
