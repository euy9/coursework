package cs445.a5;

import java.util.Iterator;
import java.util.NoSuchElementException;
import StackAndQueuePackage.*;

public class TernaryTree<T> implements TernaryTreeInterface<T>{
    private TernaryNode<T> root;
    
    public TernaryTree() {
        root = null;
    }
    
    public TernaryTree(T rootData) {
        root = new TernaryNode<>(rootData);
    }

    public TernaryTree(T rootData, TernaryTree<T> leftTree, TernaryTree<T> middleTree,
                        TernaryTree<T> rightTree){
        privateSetTree(rootData, leftTree, middleTree, rightTree);
    }
    
    @Override
    public void setTree(T rootData) {
        root = new TernaryNode<>(rootData);
    }

    @Override
    public void setTree(T rootData, TernaryTreeInterface<T> leftTree, 
                         TernaryTreeInterface<T> middleTree, 
                         TernaryTreeInterface<T> rightTree) {
        privateSetTree(rootData, (TernaryTree<T>)leftTree, (TernaryTree<T>)middleTree,
                        (TernaryTree<T>)rightTree);
    }
    
    private void privateSetTree(T rootData, TernaryTree<T> leftTree, 
                        TernaryTree<T> middleTree, TernaryTree<T> rightTree) {
        root = new TernaryNode<>(rootData);
        if ((leftTree != null) && !leftTree.isEmpty())
            root.setLeftChild(leftTree.root);
        if ((middleTree != null) && !middleTree.isEmpty()){
            if (middleTree != leftTree)
                root.setMiddleChild(middleTree.root);
            else
                root.setMiddleChild(middleTree.root.copy());
        }
        if ((rightTree != null) && !rightTree.isEmpty()){
            if ((rightTree != leftTree) && (rightTree != middleTree))
                root.setRightChild(rightTree.root);
            else
                root.setRightChild(rightTree.root.copy());
        }
        
        if ((leftTree != null) && (leftTree != this))
            leftTree.clear();
        if ((middleTree != null) && (middleTree != this))
            middleTree.clear();
        if ((rightTree != null) && (rightTree != this))
            rightTree.clear();       
    }

    @Override
    public T getRootData() {
        if(isEmpty())
            throw new EmptyTreeException();
        else
            return root.getData();
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public void clear() {
        root = null;
    }
    
    protected void setRootData(T rootData){
        root.setData(rootData);
    }
    
    protected void setRootNode(TernaryNode<T> rootNode){
        root = rootNode;
    }
        
    protected TernaryNode<T> getRootNode() {
        return root;
    }
    
    @Override
    public int getHeight() {
        return root.getHeight();
    }

    @Override
    public int getNumberOfNodes() {
        return root.getNumberOfNodes();
    }

    
    
    
    @Override
    public Iterator getPreorderIterator() {
        return new PreorderIterator();
    }

    @Override
    public Iterator getInorderIterator() {
        throw new UnsupportedOperationException();
        /*  TernaryTree does not support InorderIterator because...
        For preorder traversal, we can visit the root, then visit from leftmost
        child to rightmost child.
        And for postorder traversal, we can visit children from left to right, then
        visit the root.
        However, for inorder traversal - where root is visited after visiting
        left child and before visiting right child - in a ternary tree,
        the middle child stands awkwardly. It would be a problem whether to visit
        the root before or after vising the middle tree.
        */
    }
    
    @Override
    public Iterator getPostorderIterator() {
        return new PostorderIterator();
    }

    @Override
    public Iterator getLevelOrderIterator() {
        return new LevelOrderIterator();
    }
    
    
    
    private class PreorderIterator implements Iterator<T> {
        
        private StackInterface<TernaryNode<T>> nodeStack;
    
        public PreorderIterator() {
            nodeStack = new LinkedStack<>();
            if (root != null)
                nodeStack.push(root);
        }
        
        public boolean hasNext() {
            return !nodeStack.isEmpty();
        }
        
        public T next() {
            TernaryNode<T> nextNode;
            if (hasNext()) {
                nextNode = nodeStack.pop();
                TernaryNode<T> leftChild = nextNode.getLeftChild();
                TernaryNode<T> middleChild = nextNode.getMiddleChild();
                TernaryNode<T> rightChild = nextNode.getRightChild();
                
                if (rightChild != null) 
                    nodeStack.push(rightChild);
                if (middleChild != null)
                    nodeStack.push(middleChild);
                if (leftChild != null)
                    nodeStack.push(leftChild);
            }else 
                throw new NoSuchElementException();
            return nextNode.getData();
        }
        
        public void remove(){
            throw new UnsupportedOperationException();
        }
        
    }
    
    private class PostorderIterator implements Iterator<T> {
        private StackInterface<TernaryNode<T>> nodeStack;
        private TernaryNode<T> currentNode;
        
        public PostorderIterator() {
            nodeStack = new LinkedStack<>();
            currentNode = root;
        }
        
        @Override
        public boolean hasNext() {
            return !nodeStack.isEmpty() || currentNode != null;
        }

        @Override
        public T next() {
            TernaryNode<T> leftChild, middleChild;
            TernaryNode<T> nextNode = null;
            
            //find the leftmost leaf
            while (currentNode != null) {
                nodeStack.push(currentNode);
                leftChild = currentNode.getLeftChild();
                middleChild = currentNode.getMiddleChild();
                if (leftChild != null)
                    currentNode = leftChild;
                else if (leftChild == null && middleChild != null)
                    currentNode = middleChild;
                else if (middleChild == null)
                    currentNode = currentNode.getRightChild();
            }
            
            if (!nodeStack.isEmpty()){
                nextNode = nodeStack.pop();
                TernaryNode<T> parent = null;
                if (!nodeStack.isEmpty()){
                    parent = nodeStack.peek();
                    if (nextNode == parent.getLeftChild())
                        currentNode = parent.getMiddleChild();
                    else if (nextNode == parent.getMiddleChild())
                        currentNode = parent.getRightChild();
                    else
                        currentNode = null;   
                } else                              // stack empty; reach root
                    currentNode = null;
            } else 
                throw new NoSuchElementException();
            return nextNode.getData();
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
    
    private class LevelOrderIterator implements Iterator<T> {
        private QueueInterface<TernaryNode<T>> nodeQueue;
        
        public LevelOrderIterator(){
            nodeQueue = new LinkedQueue<>();
            if (root != null)
                nodeQueue.enqueue(root);
        }

        @Override
        public boolean hasNext() {
            return !nodeQueue.isEmpty();
        }

        @Override
        public T next() {
            TernaryNode<T> nextNode;
            if (hasNext()) {
                nextNode = nodeQueue.dequeue();
                TernaryNode<T> leftChild = nextNode.getLeftChild();
                TernaryNode<T> middleChild = nextNode.getMiddleChild();
                TernaryNode<T> rightChild = nextNode.getRightChild();
                
                //add to queue from left to right
                if (leftChild != null)
                    nodeQueue.enqueue(leftChild);
                if (middleChild != null)
                    nodeQueue.enqueue(middleChild);
                if (rightChild != null)
                    nodeQueue.enqueue(rightChild);
            } else 
                throw new NoSuchElementException();
            return nextNode.getData();
        }
        
        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
        
    }
}
