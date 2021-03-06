import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.ArrayList;

/* An AmoebaFamily is a tree, where nodes are Amoebas, each of which can have
   any number of children. */
public class AmoebaFamily implements Iterable<AmoebaFamily.Amoeba> {

    /* ROOT is the root amoeba of this AmoebaFamily */
    private Amoeba root = null;

    /* Creates an AmoebaFamily, where the first Amoeba's name is NAME. */
    public AmoebaFamily(String name) {
        root = new Amoeba(name, null);
    }

    /* Adds a new Amoeba with CHILDNAME to this AmoebaFamily as the youngest
       child of the Amoeba named PARENTNAME. This AmoebaFamily must contain an
       Amoeba named PARENTNAME. */
    public void addChild(String parentName, String childName) {
        if (root != null) {
            root.addChildHelper(parentName, childName);
        }
    }

    /* Prints the name of all Amoebas in this AmoebaFamily in preorder, with
       the ROOT Amoeba printed first. Each Amoeba should be indented four spaces
       more than its parent. */
    public void print() {
        if (root != null) {
            root.print();
        }
    }

    /* Returns the length of the longest name in this AmoebaFamily. */
    public int longestNameLength() {
        if (root != null) {
            return root.longestNameLengthHelper();
        }
        return 0;
    }

    /* Returns the longest name in this AmoebaFamily. */
    public String longestName() {
        if (root != null) {
            return root.longestName();
        }
        return "";
    }

    public void removeChildren(String name){
        if (root == null){
            return;
        }
        root.removeChildren(name);
    }

    /* Returns an Iterator for this AmoebaFamily. */
    public Iterator<Amoeba> iterator() {
        return new AmoebaDFSIterator();
    }

    /* Creates a new AmoebaFamily and prints it out. */
    public static void main(String[] args) {
        AmoebaFamily family = new AmoebaFamily("Amos McCoy");
        family.addChild("Amos McCoy", "mom/dad");
        family.addChild("Amos McCoy", "auntie");
        family.addChild("mom/dad", "me");
        family.addChild("mom/dad", "Fred");
        family.addChild("mom/dad", "Wilma");
        family.addChild("me", "Mike");
        family.addChild("me", "Homer");
        family.addChild("me", "Marge");
        family.addChild("Mike", "Bart");
        family.addChild("Mike", "Lisa");
        family.addChild("Marge", "Bill");
        family.addChild("Marge", "Hilary");
//        System.out.println("Here's the family:");
//        family.print();
//        System.out.println(family.longestName() + " " +
//                family.longestNameLength());

//        family.removeChildren("Marge");
        for (Amoeba f : family) {
            System.out.println(f);
        }

//        ArrayDeque<Integer> a = new ArrayDeque<>();
//        a.add(1);
//        a.add(2);
//        a.add(3);
//        System.out.println(a);
//        a.add(4);
//        System.out.println(a);
//        a.remove();
//        System.out.println(a);
    }

    /* An Amoeba is a node of an AmoebaFamily. */
    public static class Amoeba {

        private String name;
        private Amoeba parent;
        private ArrayList<Amoeba> children;

        public Amoeba(String name, Amoeba parent) {
            this.name = name;
            this.parent = parent;
            this.children = new ArrayList<Amoeba>();
        }

        public String toString() {
            return name;
        }

        public void removeChildren(String name){
            if (this.name == name){
                for(Amoeba a : children){
                    this.children = null;
                    a.removeChildren(name);
                }

            }else {
                for(Amoeba b: children){
                    b.removeChildren(name);
                }
            }
        }

        public Amoeba getParent() {
            return parent;
        }

        public ArrayList<Amoeba> getChildren() {
            return children;
        }

        /* Adds child with name CHILDNAME to an Amoeba with name PARENTNAME. */
        public void addChildHelper(String parentName, String childName) {
            if (name.equals(parentName)) {
                Amoeba child = new Amoeba(childName, this);
                children.add(child);
            } else {
                for (Amoeba a : children) {
                    a.addChildHelper(parentName, childName);
                }
            }
        }

        public void print() {
            System.out.println(this.name);
            for (Amoeba a : children) {
                a.printHelper(1);
            }
        }

        public void printHelper(int i) {
            for (int j = 0; j < i; j++) {
                System.out.print("    ");
            }
            System.out.println(this.name);
            for (Amoeba a : children) {
                a.printHelper(i + 1);
            }
        }

        /* Returns the length of the longest name between this Amoeba and its
           children. */
        public int longestNameLengthHelper() {
            int maxLengthSeen = name.length();
            for (Amoeba a : children) {
                maxLengthSeen = Math.max(maxLengthSeen,
                        a.longestNameLengthHelper());
            }
            return maxLengthSeen;
        }

        public String longestName() {
            String longest = name;
            for (Amoeba a : children) {
                if (longest.length() < a.longestName().length()) {
                    longest = a.longestName();
                }
            }
            return longest;
        }
    }

    /* An Iterator class for the AmoebaFamily, running a DFS traversal on the
       AmoebaFamily. Complete enumeration of a family of N Amoebas should take
       O(N) operations. */
    public class AmoebaDFSIterator implements Iterator<Amoeba> {


        Stack<Amoeba> fringe;

        /* AmoebaDFSIterator constructor. Sets up all of the initial information
           for the AmoebaDFSIterator. */
        public AmoebaDFSIterator() {

            fringe = new Stack<Amoeba>();
            if (root != null) {
                fringe.push(root);
            }
        }

        /* Returns true if there is a next element to return. */
        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        /* Returns the next element. */
        public Amoeba next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Tree ran out of elements!");
            }
            Amoeba node = fringe.pop();
            for (Amoeba c : node.children) {
                fringe.push(c);
            }
            return node;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* An Iterator class for the AmoebaFamily, running a BFS traversal on the
       AmoebaFamily. Complete enumeration of a family of N Amoebas should take
       O(N) operations. */
    public class AmoebaBFSIterator implements Iterator<Amoeba> {

        ArrayDeque<Amoeba> fringe;

        /* AmoebaBFSIterator constructor. Sets up all of the initial information
           for the AmoebaBFSIterator. */


        public AmoebaBFSIterator() {
            fringe = new ArrayDeque<Amoeba>();
            if (root != null) {
                fringe.add(root);
            }
        }

        /* Returns true if there is a next element to return. */
        public boolean hasNext() {
            return !fringe.isEmpty();
        }

        /* Returns the next element. */
        public Amoeba next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Tree ran out of elements!");
            }
            Amoeba node = fringe.remove();
            for (Amoeba c : node.children) {
                fringe.add(c);
            }
            return node;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
