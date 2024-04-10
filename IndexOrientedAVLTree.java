import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ASD3 {



    static int currentIndex = 0;
    static AVL avl = new AVL();
    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(args[0]));



        int k;
        String[] line;

        k = Integer.parseInt(scanner.nextLine());
        line = scanner.nextLine().split(" ");

        for(String num:line){
            avl.insert(Integer.parseInt(num));
        }




        for(int i=0; i<k; i++){
            if(avl.getRoot() == null)
                break;
            if(avl.index(currentIndex).value%2==0)
                delete();
            else
                add();
        }


        if(avl.getRoot() != null)
            for(int i=0; i<avl.getRoot().size; i++){
                System.out.print(avl.index(findIndex(i)).value);
                if(i!=avl.getRoot().size-1)
                    System.out.print(" ");

            }

    }

    static void add(){
        int currentValue = avl.index(currentIndex).value;
        avl.insert(currentValue-1, currentIndex+1);

        currentIndex = findIndex(currentValue);
    }

    static void delete(){
        int toDeleteIndex = findIndex(1);
        int value = avl.index(toDeleteIndex).value;
        avl.delete(toDeleteIndex);
        if(toDeleteIndex<currentIndex)
            currentIndex--;
        currentIndex = findIndex(value);
    }
    static int findIndex(int i){
        if(avl.getRoot()!=null)
            return (currentIndex+i)-(((currentIndex+i)/avl.getRoot().size)*avl.getRoot().size);
        else
            return 0;
    }


}

class Node {

    int height, value, size;
    Node leftKid, rightKid;

    public Node(int value) {
        this.value = value;
        height = 1;
        size = 1;
    }


}

class AVL {


    private Node root;

    public AVL() {
        this.root = null;
    }

    private int getHeight(Node node) {
        return node != null ? node.height : 0;
    }

    private int getSize(Node node) {
        return node != null ? node.size : 0;
    }


    private int getBalance(Node node) {
        return node != null ? getHeight(node.leftKid) - getHeight(node.rightKid) : 0;
    }

    public Node index(int i) {
        return index(root, i);
    }

    public void insert(int value) {
        root = insertNode(root, value);
    }

    public void insert(int value, int index) {
        root = addNodeAtIndex(root, new Node(value), index);
    }

    public void delete(int index){
        root = deleteAtIndex(root, index);
    }

    private void update(Node node) {
//        if(node.leftKid==null && node.rightKid == null)
//            node.height =0;
//        else
        node.height = 1 + Max(getHeight(node.leftKid), getHeight(node.rightKid));

        node.size = 1 + getSize(node.rightKid) + getSize(node.leftKid);
    }

    private Node insertNode(Node currentNode, int value) {

        if (currentNode == null)
            return new Node(value);
        else {
            currentNode.rightKid = insertNode(currentNode.rightKid, value);
        }

        update(currentNode);

        return balance(currentNode);

    }

    private static int Max(int i1, int i2) {

        return i1 - i2 >= 0 ? i1 : i2;
    }

    public Node addNodeAtIndex(Node currentNode, Node newNode, int i) {


        if (currentNode == null)
            currentNode = newNode;

        if (getSize(currentNode.leftKid) == i) {

            Node tmp = currentNode;
            currentNode = newNode;
            currentNode.rightKid = tmp;
            currentNode.leftKid = tmp.leftKid;
            tmp.leftKid = null;
            update(currentNode.rightKid);
            currentNode.rightKid = balance(currentNode.rightKid);

        }else {
            if (i < getSize(currentNode.leftKid))
                currentNode.leftKid = addNodeAtIndex(currentNode.leftKid, newNode, i);
            else {
                if(currentNode.rightKid == null)
                    currentNode.rightKid = insertNode(null, newNode.value);
                else
                    currentNode.rightKid = addNodeAtIndex(currentNode.rightKid, newNode, i - getSize(currentNode.leftKid) - 1);
            }
        }

        update(currentNode);
        return balance(currentNode);
    }

    public Node deleteAtIndex(Node node, int i){

//        if(node == null)
//            return node;

        if(i < getSize(node.leftKid)){
            node.leftKid = deleteAtIndex(node.leftKid, i);
        }else if(i>getSize(node.leftKid))
            node.rightKid = deleteAtIndex(node.rightKid, i-getSize(node.leftKid) -1);
        else {
            if(node.leftKid == null || node.rightKid == null){
                Node tmp;
                if(node.leftKid == null)
                    tmp = node.rightKid;
                else
                    tmp = node.leftKid;

                if(tmp == null) {
                    node = null;
                }else
                    node = tmp;
            }else{
                Node mostLeftKid = findNext(node.rightKid);
                node.value = mostLeftKid.value;
                node.rightKid = deleteAtIndex(node.rightKid, 0);
            }
        }

        if(node == null)
            return null;

        update(node);
        return balance(node);
    }

    private Node balance(Node node) {



        int balance = getBalance(node);


        if (balance > 1) {
            if (getSize(node.leftKid.leftKid) >= getSize(node.leftKid.rightKid))
                return rightRotate(node);
            else {
                node.leftKid = leftRotate(node.leftKid);
                return rightRotate(node);
            }
        }

        if (balance < -1) {
            if (getSize(node.rightKid.rightKid) >= getSize(node.rightKid.leftKid))
                return leftRotate(node);
            else {
                node.rightKid = rightRotate(node.rightKid);
                return leftRotate(node);
            }
        }

        update(node);
        return node;


    }

    private Node leftRotate(Node node) {

        Node child = node.rightKid;
        Node grandChild = child.leftKid;

        child.leftKid = node;
        node.rightKid = grandChild;

        update(node);
        update(child);

        return child;
    }

    private Node rightRotate(Node node) {
        Node child = node.leftKid;
        Node grandchild = child.rightKid;
        child.rightKid = node;
        node.leftKid = grandchild;

        update(node);
        update(child);

        return child;
    }


    public Node getRoot() {
        return root;
    }

    public Node index(Node node, int i) {

        if (node == null)
            return null;

        if (getSize(node.leftKid) == i)
            return node;
        if (i < getSize(node.leftKid))
            return index(node.leftKid, i);
        return index(node.rightKid, i - getSize(node.leftKid) - 1);
    }

    public Node findNext(Node node){
        if(node == null)
            return null;

        if(node.leftKid != null){
            if(node.leftKid.leftKid != null)
                return findNext(node.leftKid);
            else
                return node.leftKid;
        }else
            return node;
    }


}
