package implementation.MyHeap;

import java.util.Arrays;

/**
 *  this is my own implementation for a heap,
 *  i can assure you i made it my self.
 *  this should be a proper heap, but mostly its fine tuned for the path finding algorithm.
 *
 *  Java does not have a heap implementation that support heapify up and down,
 *  and i also need a way to quickly "take out" items from the heap,
 *  in this case i save the heap index, as the tag inside the node_info, with lets me
 *  a very quick way of updating an existing item in the heap
 *  this is the main reason why this heap is much much better, then the PriorityQueue
 * @param <T> the object we will sort on must be IHeapNode so we can induce index from it
 */

public class Heap<T extends IHeapNode> {
    private T[] items;
    int boundUp = 0;
    int used = 0;
    int arrSize = 1000;
    public Heap() {
        items = (T[])new IHeapNode[arrSize];
    }
    
    public static final int HEAPIFIED_UP = 1;
    public static final int HEAPIFIED_DOWN = -1;
    public static final int NO_CHANGE = 0;
    public static final int OLD_MEMBER = -2;
    public static final int NOT_IN_HEAP = -3;

    public Heap(int size){
        arrSize = size;
        items = (T[])new IHeapNode[size];
    }

    /**
     * A lock object that lets some methods communicate with the heap as public but blocks for everything else.
     */
    public static final class HeapLock {
        private HeapLock() {}
    }
    private static final HeapLock permission = new HeapLock();

    public T lookFirst(){
        return items[0];
    }

    @Override
    public String toString() {
        // print heap content
        if(size() > 0) {
            String content = "[";
            for (int i = 0; i < size() - 1; i++) {
                content += items[i] + " ,";
            }
            content += items[size()-1] + "]";
            return content;
        }
        return "[]";
    }

    public int size(){
        return used;
    }

    public T getAt(int index){
        return items[index];
        //return items.get(index);
    }

    public void add(T item, double priority){
        // for now no protection at overlapping
        if(item.getHeap() != null){
            return;
        }
        // mark the item to be part of the heap.
        item.setHeap(permission,this);
        item.setPriority(permission, priority);
        item.setIndex(permission, used);

        items[size()] = item;
        used++;

        // if the heap Arr is to small we would expand it by 5.
        // this takes O(n) time.
        if(used >= arrSize){
            arrSize *= 5;
            items = Arrays.copyOf(items, arrSize);
        }

        //items.add(item);
        boundUp = (used - 1) / 2 - 1;

        heapifyUp(item);
    }

    public T poll(){
        // get the first element, this takes O(logn);
        swap(getAt(0), getAt(used - 1));
        T first = getAt(used - 1);
        used--;
        if(size() > 0) {
            heapifyDown(getAt(0));
        }
        // we mark the node to be out of heap.
        first.setHeap(permission,null);
        first.setIndex(permission, OLD_MEMBER);
        boundUp = (used - 1) / 2 - 1;
        return first;
    }

    public int increasePriority(T target, double priority){
        if(target == null || target.getHeap() != this){
            return NOT_IN_HEAP;
        }
        if(target.getPriority() > priority){
            target.setPriority(permission, priority);
            heapifyUp(target);
            return HEAPIFIED_UP;
        }
        return NO_CHANGE;
    }

    public int updatePriority(T target, double priority){
        // this is mainly for the PathFinding,
        // if the node is in the heap.
        // we try to updateFromJson the priority, if its a "better" one,
        // we updateFromJson it.
        // return 1 if successful.
        // return 0, if failed
        // return -1, if node not in heap.
        if(target.getHeap() != this){
            return NOT_IN_HEAP;
        }else if(target.getPriority() > priority){
            target.setPriority(permission, priority);
            heapifyUp(target);
            return HEAPIFIED_UP;
        }else if(target.getPriority() < priority){
            target.setPriority(permission, priority);
            heapifyDown(target);
            return HEAPIFIED_DOWN;
        }
        return NO_CHANGE;
    }

    private void swap(T node1, T node2){
        int index = node1.getIndex();
        int index2 = node2.getIndex();

        node1.setIndex(permission, index2);
        node2.setIndex(permission, index);

        items[index] = node2;
        items[index2] = node1;
    }

    private void heapifyUp(T target){
        // updateFromJson item, dest_pos upwards.
        // this takes O(logn);
        int index = target.getIndex();
        if(index == 0){
            return;
        }

        T parent = getAt((target.getIndex() - 1) / 2);
        if(target.compareTo(parent)){
            swap(target, parent);
            heapifyUp(target);
        }
    }

    private void heapifyDown(T target){
        // updateFromJson item, dest_pos downwards.
        // this takes O(logn);
        int index = target.getIndex();
        if(index > boundUp){
            return;
        }


        T left = getAt(index * 2 + 1);
        if(index *2 + 3 > used){
            if(left.compareTo(target)){
                swap(target, left);
            }
            return;
        }
        T right = getAt(index * 2 + 2);
        if(left.compareTo(target)){
            if(left.compareTo(right)){
                swap(target, left);
                heapifyDown(target);
            }else{
                swap(target, right);
                heapifyDown(target);
            }
        }else if(right.compareTo(target)){
            swap(target, right);
            heapifyDown(target);
        }
    }
}
