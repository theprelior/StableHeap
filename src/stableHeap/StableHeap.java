package stableHeap;

public class StableHeap<Type extends Comparable<? super Type>> implements Heap<Type> {
    private static final int DEFAULT_CAPACITY = 10;
    private Object[][] array;
    private int size;
    private int maxsize;
    private static final int FRONT = 0;


    public StableHeap() {
        array = new Object[DEFAULT_CAPACITY][];
        size = 0;
        this.maxsize = DEFAULT_CAPACITY;
    }
    
    /**
     * We have 2d array we are holding the array size in first index.
     * @param i
     * @return (int)array[i][0];
     */
    private int getSize(int i) {
        return (int) array[i][0];
    }
   /**
    * 2d array holds begin index in second index.
    * @param i
    * @return (int) array[i][1]
    */
    private int getBegin(int i) {
        return (int) array[i][1];
    }
    /**
     * If the array length is full it will increase the size of array[i].
     * @param i
     * @return array[i].length == getSize(i) + 2
     */
    private boolean isFull(int i) {
        return array[i].length == getSize(i) + 2;
    }

 
    /***
     * This method removes index that is coming to method.
     * <variable>el</variable> is getting begin value, at index i.
     * Array is holding begin value at array[i][1]. 
     * @param i
     * @return el(element)
     */
    private Type remove(int i) {
        Type el = (Type) array[i][getBegin(i)];
        array[i][1] = ((int) array[i][1]) + 1;
        if ((int) array[i][1] == array[i].length)
            array[i][1] = 2;

        array[i][0] = ((int) array[i][0]) - 1;
        if ((int) array[i][0] == 0)
            array[i] = null;
        return el;
    }
    /***
     * Comparing two array value.
     * @param i
     * @param b
     * @return <0 || 0> || =0
     */
    private int compare(int i, int b) {
        return ((Type) array[i][getBegin(i)]).compareTo((Type) array[b][getBegin(b)]);
    }
    
    private int compare(Type a, int b) {
        return a.compareTo((Type) array[b][getBegin(b)]);
    }

    /***
     * Adding datas in the array.It will be first value of the array if the array has no element.
     * It will throw an error if the array reached to maximum capacity.
     * Example    array[0][[size=1],[begin=2],["ID:d,priority:3"]]
     * After adding one more element =>array[0][[size=2],[begin=2],["ID:d,priority:3"],["ID:c,priority:3"]]
     * So,It gains all of elements which has same priority.
     * @param i
     * @param a
     */
    private void add(int i, Type a) {
        if (array[i] == null) {
            array[i] = new Object[DEFAULT_CAPACITY];
            array[i][0] = 1;
            array[i][1] = 2;
            array[i][2] = a;
            return;
        }
        if (isFull(i))
            throw new RuntimeException("Reached maximum number of elements inside InnerArray");
        int toAdd = getSize(i) + getBegin(i);
        if(toAdd > array[i].length)
            toAdd-=array[i].length;
        array[i][toAdd] = a;
        array[i][0] = ((int) array[i][0]) + 1;
    }


   


    
  /***
   * We use here binary search to decrease cost.We can also use other algorithms.
   * It compare line by line the elements.
   * If it's alrady order by priority it will return -1.
   * Otherwise,return index that should change with each other.
   * @param t
   * @return
   */

   
    private int binarySearch(Type t) {
        int current = 0;
        int size = this.size;
        while (current < size) {
            int mid = (current + size) / 2;
            int comp = compare(t, mid);
            if (comp < 0) {
                size = mid;
            } else if (comp > 0) {
                current = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
    /***
     * The method is looking in array if priority is already in or not.
     * If it is already in the array so,the method add the data(element) in same array.
     * Otherwise, it will add new array in the array line.
     * example  the array was {"ID:a,priority:3"}
     * after added same priority  {"ID:a,priority:3","ID:b,priority:3"}
     * If the priority isn't same that element it will be like this;
     * {
     * 		{"ID:a,priority:3","ID:b,priority:3"},
     * 		{"ID:b,priority:2"}
     * }
     * after added the new data in the array,it will compare with each other and heapify.
     * {
     * 		{"ID:b,priority:2"},
     * 		{"ID:a,priority:3","ID:b,priority:3"}
     * }
     * So the main idea here use Fifo.When it delete the min value in array it will delete first array element and then shift left.
     * That's why we use 2d array.
     * We are holding the datas in same tree element which has same priority.
     * It might be leaf,root or parent doesn't matter.
     */
    @Override
    public void insert(Type element) {
        if (size >= maxsize) {
            return;
        }
        int arrayToInsert = binarySearch(element);
        if (arrayToInsert != -1) {
            add(arrayToInsert, element);
            return;
        }
        add(size++, element);

        int N = size;
        int i, j;

        for (i = 1; i < N; i++) {
            j = i;
            while (j > 0 && compare(j, j - 1) < 0) {
                Object[] temp = array[j];
                array[j] = array[j - 1];
                array[j - 1] = temp;
                j -= 1;
            }
        }
    }
    /***
     * When the method called, it will delete first element which is minumum element in 2d heap array.
     * If there is another element, it will push it to the left in the order.
     * Otherwise, the one with different priority will go up.
     * Example:
     * {	
     * 		{"ID:b,priority:2"},
     * 		{"ID:a,priority:3","ID:b,priority:3"}		
     * }
     * after delete:
     * {	
     * 		{"ID:a,priority:3","ID:b,priority:3"}		
     * }
     * after one more delete:
     * 
     * {			
     * 		{"ID:b,priority:3"}		
     * }
     */
    @Override
    public Type deleteMin() {
        Type popped = remove(FRONT);
        if (array[FRONT] != null)
            return popped;
        for (int i = FRONT; i < size; i++) {
            array[i] = array[i + 1];
        }
        size--;
        return popped;
    }


			
	

    /**
     * The method find the minumum value in the Heap.
     *
     * @return minVal - minumum value
     */
    @Override
    public Type findMin() {

        if (size > 0) {
            return (Type) array[1][2];
        }
        return null;
    }

	
	
	/**
	 * Returns the heap is empty or not.
	 * 
	 * */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Makes empty the heap.
     */
    @Override
    public void makeEmpty() {
        array = new Object[DEFAULT_CAPACITY][];
        size = 0;
    }


}
