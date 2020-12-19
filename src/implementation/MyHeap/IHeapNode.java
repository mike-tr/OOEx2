package implementation.MyHeap;

/**
 * abstract IHeapNode, has the basic functions to work with the heap
 */
public abstract class IHeapNode {
    private int index = -1;
    private Heap heap;
    private double priority;

    // return head heap, null if none.
    public Heap getHeap() {
        return heap;
    }

    // set the head heap, honestly this should be an inner class but whatever.
    public final void setHeap(Heap.HeapLock permission, Heap heap) {
        this.heap = heap;
    }

    // return the index of the node in the heap arr.
    public final int getIndex() {
        return index;
    }

    // set the index of the node in the arr, again this should be only settable via the HEAP.
    public final void setIndex(Heap.HeapLock permission, int index){
        this.index = index;
        onUpdateIndex(index);
    }

    // return if node priority is bigger then mine.
    public boolean compareTo(IHeapNode node){
        return getPriority() < node.getPriority();
    }

    @Override
    public String toString() {
        return "" + getPriority();
    }

    // return/set priority, this variable can be whatever.
    public final double getPriority(){
        return priority;
    }

    public final void setPriority(Heap.HeapLock permission, double priority){
        this.priority = priority;
        onUpdatePriority(priority);
    }

    public final int increasePriority(double priority){
        if(heap != null){
            return heap.increasePriority(this, priority);
        }
        return Heap.NOT_IN_HEAP;
    }

    public final int updatePriority(double priority){
        if(heap != null){
            return heap.updatePriority(this, priority);
        }
        return Heap.NOT_IN_HEAP;
    }

    protected abstract void onUpdatePriority(double priority);
    protected abstract void onUpdateIndex(int index);
}
