package cs445.a5;

import java.util.ArrayList;
import java.util.Iterator;

public class TernaryTreeTest {
    public static void main(String[] args){
        
        TernaryTreeInterface<String> testA = new TernaryTree();
        TernaryTreeInterface<String> testB = new TernaryTree();
        testB.setTree("B");
        TernaryTreeInterface<String> testC = new TernaryTree();
        testC.setTree("C");
        TernaryTreeInterface<String> testD = new TernaryTree("D");
        //testD.setTree("D");
        
        TernaryTreeInterface<String> testE = new TernaryTree();
        testE.setTree("E");
        TernaryTreeInterface<String> testF = new TernaryTree();
        testF.setTree("F");
        TernaryTreeInterface<String> testG = new TernaryTree();
        testG.setTree("G");
        TernaryTreeInterface<String> testH = new TernaryTree();
        //testH.setTree("H");
        TernaryTreeInterface<String> testI = new TernaryTree();
        testI.setTree("I");
        TernaryTreeInterface<String> testJ = new TernaryTree();
        testJ.setTree("J");
        TernaryTreeInterface<String> testK = new TernaryTree();
        testK.setTree("K");
        TernaryTreeInterface<String> testL = new TernaryTree("L");
        TernaryTreeInterface<String> testM = new TernaryTree();
        testM.setTree("M");
        TernaryTreeInterface<String> testN = new TernaryTree();
        testN.setTree("N");
        TernaryTreeInterface<String> testO = new TernaryTree("O");

        testO.setTree(testO.getRootData(), null, null, testN);
        testJ.setTree(testJ.getRootData(), null, testL, testL);
        System.out.println("testJ's height is " + testJ.getHeight());
        System.out.println("testJ's number of nodes is " + testJ.getNumberOfNodes());
        
        testH.setTree("H", testM, testK, testO);
        testC.setTree(testC.getRootData(), testH, testI, testJ);
        testB.setTree(testB.getRootData(), testE, testF, null);
        testA.setTree("A", testB, testC, testD);     

        
        System.out.println("testA's root data is "+ testA.getRootData());
        System.out.println("testA is empty: " + testA.isEmpty());
        System.out.println("testA's height is " + testA.getHeight());
        System.out.println("testA's number of nodes is " + testA.getNumberOfNodes());
        System.out.println("testB is empty: " + testB.isEmpty());
        
        
        System.out.println("testA pre-order traversal:");
        Iterator<String> preOrder = testA.getPreorderIterator();
        while (preOrder.hasNext()){  
            System.out.println(preOrder.next());
        }
        
        System.out.println("testA post-order traversal:");
        Iterator<String> postOrder = testA.getPostorderIterator();
        while (postOrder.hasNext()){
            System.out.println(postOrder.next());
        }
        
        System.out.println("testA level-order traversal:");
        Iterator<String> levelOrder = testA.getLevelOrderIterator();
        while (levelOrder.hasNext()){
            System.out.println(levelOrder.next());
        }
    }
}
