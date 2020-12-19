package implementation;

import api.node_data;
import implementation.MyHeap.IHeapNode;

/**
 * Path node, implements IHeapNode, worth with my heap,
 * Hold's reference to the "parent" node, and the node we came from, for easy path retrieval
 */
public class WPathNode extends IHeapNode {
    /**
     *  this is a node object, it implements IHeapNode with is an interface
     *  that lets me set the "head heap" set priority, and Compare 2 node objects.
     *  the priority would be used as the Distance.
     */
    private node_data node;
    private WPathNode parent;
    private double distance;

    WPathNode(node_data node){
        this.node = node;
    }

    WPathNode(node_data node, WPathNode root){
        this.node = node;
        this.parent = root;
    }

    public WPathNode getParent(){
        // get the head, a.k.a the node we came.
        return parent;
    }

    public node_data getNode(){
        return node;
    }

    public int getKey(){
        return node.getKey();
    }

    public void setParent(WPathNode root){
        // set the head of this Node, for traversal.
        this.parent = root;
    }

    public double getDistance(){
        return this.distance;
    }

    @Override
    protected void onUpdatePriority(double priority) {
        this.distance = priority;
    }

    @Override
    protected void onUpdateIndex(int index) {
        node.setTag(index);
    }
}
