import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLOutput;
import java.util.Scanner;

public class ASD4 {

    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner = new Scanner(new File(args[0]));

        HeapTree heapTree = new HeapTree(26);


        while (scanner.hasNext()) {
            String[] line = scanner.nextLine().split(" ");
            heapTree.insert(new Element(line[0], Integer.parseInt(line[1])));
        }


        Element root = null;



        if(heapTree.size<=1){
            System.out.println(heapTree.getAndDeleteMin().getLetterComb() + " 1");
        }else {
            while (heapTree.size > 1) {
                Element first = heapTree.getAndDeleteMin();
                Element second = heapTree.getAndDeleteMin();
                Element combo = new Element(first.getLetterComb() + second.getLetterComb(),
                        first.getValue() + second.getValue());
                combo.setLeft(first);
                combo.setRight(second);
                heapTree.insert(combo);
                root = combo;
            }

            encode(root, "");
        }

    }

    public static void encode(Element root, String code){


        if(root.getLeft() != null){
            encode(root.getLeft(), code+"0");
        }

        if(root.getRight() != null) {
            encode(root.getRight(), code + "1");
        }

        if(root.getRight() == null && root.getLeft() == null)
            System.out.println(root.getLetterComb() + " " + code);

    }
}

class Element{

    private String letterComb;
    private int value;

    private Element left, right;


    public Element getLeft() {
        return left;
    }

    public Element getRight() {
        return right;
    }

    public void setLeft(Element left) {
        this.left = left;
    }

    public void setRight(Element right) {
        this.right = right;
    }

    public Element(String letterComb, int value) {
        this.letterComb = letterComb;
        this.value = value;
    }

    public String getLetterComb() {
        return letterComb;
    }

    public int getValue() {
        return value;
    }

    public void join(Element toJoin){
        letterComb += toJoin.letterComb;
        value += toJoin.value;
    }
}


class HeapTree{

    Element[] heap;
    int size, maxSize;

    public HeapTree(int maxSize) {
        this.maxSize = maxSize;
        heap = new Element[maxSize];
        size = 0;
    }

    public int parent(int key){
        return (key-1)/2;
    }

    public int left(int key){
        return 2 * key +1;
    }

    public int right(int key){
        return 2 * key + 2;
    }

    public void insert(Element element){

        heap[size++] = element;
        int currentPosition = size-1;

        while(heap[currentPosition].getValue()<heap[parent(currentPosition)].getValue()){
            swap(currentPosition, parent(currentPosition));
            currentPosition = parent(currentPosition);
        }
    }

    public void heapify(int key){

        int left = left(key);
        int right = right(key);

        int smallest = key;

        if(left < size && heap[left].getValue()<heap[smallest].getValue())
            smallest = left;

        if(right < size && heap[right].getValue()<heap[smallest].getValue())
            smallest = right;

        if(smallest != key){
            swap(key, smallest);
            heapify(smallest);
        }

    }

    public void swap(int prevKey, int newKey){

        Element temp = heap[prevKey];
        heap[prevKey] = heap[newKey];
        heap[newKey] = temp;

    }

    public Element getAndDeleteMin(){
        Element minElem = heap[0];
        swap(0, --size);
        heapify(0);
        return minElem;
    }

    public int getSize() {
        return size;
    }
}


