package lazyTrees;

import java.util.NoSuchElementException;

/**
 * Represents an ADT of a search tree that implements lazy deletion.
 * Inserts and removes generic objects to change data.
 *
 * @param <E> Generic parameter for the search tree object
 * @author srichandramouli
 */
public class LazySearchTree<E extends Comparable<? super E>> implements Cloneable {
   /**
    * Number of nodes that are not marked as deleted in search tree.
    */
   protected int mSize;

   /**
    * Main root node of search tree.
    */
   protected LazySTNode mRoot;

   /**
    * Total number of nodes in search tree. Regardless of deleted status.
    */
   protected int mSizeHard;


   /**
    * Construct for LazySearchTree.
    * Clears existing nodes.
    */
   public LazySearchTree() {
      clear();
   }

   /**
    * Returns mSizeHard instance variable
    *
    * @return the total number of nodes that have been added to the tree
    */
   public int sizeHard() {
      return mSizeHard;
   }

   /**
    * Returns mSize variable
    *
    * @return the total of number nodes that are not marked as deleted in the tree
    */
   public int size() {
      return mSize;
   }

   /**
    * Removes pointers by setting mRoot to null.
    * Sets mSize and mSizeHard to 0 to represent that there are no nodes in the tree.
    */
   public void clear() {
      mSize = 0;
      mSizeHard = 0;
      mRoot = null;
   }


   /**
    * Public version of findMin() method for client to call.
    *
    * @return data of minimum node in search tree
    */
   public E findMin() {
      if (mRoot == null)
         throw new NoSuchElementException();
      return findMin(mRoot).data;
   }

   /**
    * Public version of findMax() method for client to call.
    *
    * @return data of maximum node in search tree
    */
   public E findMax() {
      if (mRoot == null)
         throw new NoSuchElementException();
      return findMax(mRoot).data;
   }

   /**
    * Public version of find() method for client to call.
    *
    * @param x Data that is being searched for.
    * @return If matching node for data is found, that node's data will be returned. Else, null is returned.
    */
   public E find(E x) {
      LazySTNode resultNode;
      resultNode = find(mRoot, x);
      if (resultNode == null)
         throw new NoSuchElementException();
      return resultNode.data;
   }

   /**
    * Checks if a passed parameter exists as some undeleted node's data in search tree.
    *
    * @param x Data that is being searched for.
    * @return Whether data exists in an undeleted node in search tree.
    */
   public boolean contains(E x) {
      return find(mRoot, x) != null;
   }

   /**
    * Public insert() method for client to call.
    *
    * @param x Data to be inserted.
    * @return Whether data was successfully inserted from search tree.
    */
   public boolean insert(E x) {
      int oldSize = mSize;
      mRoot = insert(mRoot, x);
      return (mSize != oldSize);
   }

   /**
    * Public remove() method for client to call.
    *
    * @param x Data to be removed.
    * @return Whether data was successfully removed from search tree.
    */
   public boolean remove(E x) {
      int oldSize = mSize;
      remove(mRoot, x);
      return (mSize != oldSize);
   }

   /**
    * Public traverseHard() method for client to call.
    *
    * @param func Generic function object used to print node data.
    * @param <F>  Generic parameter for function object.
    */
   public <F extends Traverser<? super E>> void traverseHard(F func) {
      traverseHard(func, mRoot);
   }

   /**
    * Public traverseSoft() method for client to call.
    *
    * @param func Generic function object used to print node data.
    * @param <F>  Generic parameter for function object.
    */
   public <F extends Traverser<? super E>> void traverseSoft(F func) {
      traverseSoft(func, mRoot);
   }

   /**
    * Public clone() method for client to call.
    *
    * @return Cloned search tree object.
    * @throws CloneNotSupportedException Indicates object's class does not implement cloneable interface.
    */
   public Object clone() throws CloneNotSupportedException {
      LazySearchTree<E> newObject = (LazySearchTree<E>) super.clone();
      newObject.clear();

      newObject.mRoot = cloneSubtree(mRoot);
      newObject.mSize = mSize;

      return newObject;
   }


   /**
    * Finds minimum node in search tree that has not been marked as deleted.
    *
    * @param root Starting root for search.
    * @return Minimum undeleted node in search tree.
    */
   protected LazySTNode findMin(LazySTNode root) {
      if (root == null)
         return null;
      if (findMin(root.lftChild) != null)
         return findMin(root.lftChild);

      if (!root.deleted)
         return root;
      else {
         if (root.rtChild == null)
            return null;
         else
            return findMin(root.rtChild);
      }

   }

   /**
    * Finds maximum node in search tree that has not been marked as deleted.
    *
    * @param root Starting node for search.
    * @return Maximum undeleted node in search tree.
    */
   protected LazySTNode findMax(LazySTNode root) {
      if (root == null)
         return null;
      if (findMax(root.rtChild) != null)
         return findMax(root.rtChild);

      if (!root.deleted)
         return root;
      else {
         if (root.lftChild == null)
            return null;
         else
            return findMax(root.lftChild);
      }

   }

   /**
    * Inserts data as new node, or changes deleted attribute to false if data already exists in a node but is marked as deleted.
    *
    * @param root Starting root to go through search tree when checking if node already exists.
    * @param x    Data being searched for.
    * @return The inserted node.
    */
   protected LazySTNode insert(LazySTNode root, E x) {
      int compareResult;
      if (root == null) {
         mSize++;
         mSizeHard++;
         return new LazySTNode(x, null, null);
      }
      compareResult = x.compareTo(root.data);
      if (compareResult < 0)
         root.lftChild = insert(root.lftChild, x);
      else if (compareResult > 0)
         root.rtChild = insert(root.rtChild, x);
      if (root.deleted && compareResult == 0) {
         mSize++;
         root.deleted = false;
      }
      return root;
   }

   /**
    * Removes the node corresponding to the data parameter.
    *
    * @param root Starting node to search for the corresponding node.
    * @param x    Data to be removed from search tree.
    */
   protected void remove(LazySTNode root, E x) {
      LazySTNode test;
      test = find(root, x);
      test.deleted = true;
      mSize--;

   }

   /**
    * Traverses and prints data of every node in the search tree. Does not consider deleted attribute.
    *
    * @param func     Function object used to print data.
    * @param treeNode Root of search tree.
    * @param <F>      Generic parameter for function object.
    */
   protected <F extends Traverser<? super E>> void traverseHard(F func, LazySTNode treeNode) {
      if (treeNode == null)
         return;

      traverseHard(func, treeNode.lftChild);
      func.visit(treeNode.data);
      traverseHard(func, treeNode.rtChild);
   }

   /**
    * Traverses and prints data of every undeleted node in search tree. Does consider deleted attribute.
    *
    * @param func     Function object used to print tree.
    * @param treeNode Root of search tree.
    * @param <F>      Generic parameter for function object.
    */
   protected <F extends Traverser<? super E>> void traverseSoft(F func, LazySTNode treeNode) {
      if (treeNode != null) {
         if (treeNode.deleted) {
            traverseSoft(func, treeNode.lftChild);
            traverseSoft(func, treeNode.rtChild);
         } else {
            traverseSoft(func, treeNode.lftChild);
            func.visit(treeNode.data);
            traverseSoft(func, treeNode.rtChild);
         }
      }

   }

   /**
    * Finds node containing data from parameter.
    *
    * @param root Starting root for search.
    * @param x    Data to be found.
    * @return If node is undeleted and not null, returns the node with matching data as x parameter. Else, found node does not exist and is null, or is deleted, returns null.
    */
   protected LazySTNode find(LazySTNode root, E x) {
      int compareResult;

      if (root == null)
         return null;
      else {
         compareResult = x.compareTo(root.data);
         if (compareResult < 0)
            return find(root.lftChild, x);
         if (compareResult > 0)
            return find(root.rtChild, x);

         if (root.deleted)
            return null;
         return root;
      }
   }

   /**
    * Clones search tree from root parameter.
    *
    * @param root Starting root.
    * @return Cloned search tree object.
    */
   protected LazySTNode cloneSubtree(LazySTNode root) {
      LazySTNode newNode;
      if (root == null)
         return null;

      newNode = new LazySTNode(root.data, cloneSubtree(root.lftChild), cloneSubtree(root.rtChild));
      return newNode;
   }

   /**
    * Finds minimum node in search tree. Ignores if node has been marked deleted.
    *
    * @param root Starting root for search
    * @return Absolute minimum node in search tree
    */
   protected LazySTNode findMinHard(LazySTNode root) {

      if (root == null)
         return null;
      if (root.lftChild == null)
         return root;
      return findMinHard(root.lftChild);

   }

   /**
    * Finds maximum node in search tree. Ignores if node has been marked as deleted.
    *
    * @param root Starting root for search
    * @return Absolute maximum node in search tree
    */
   protected LazySTNode findMaxHard(LazySTNode root) {
      if (root == null)
         return null;
      if (findMaxHard(root.rtChild) != null)
         return findMaxHard(root.rtChild);
      else {
         if (root.lftChild == null)
            return null;
         else
            return findMaxHard(root.lftChild);
      }

   }

   /**
    * Permanently removes node by changing reference and potentially replacing data.
    *
    * @param root Node to be removed
    * @return Returns altered or removed root
    */
   protected LazySTNode removeHard(LazySTNode root) {

      if (root == null)
         return null;

      // 2 children
      if (root.lftChild != null && root.rtChild != null) {
         root.data = findMinHard(root.rtChild).data;
         root.deleted = false;
         root.rtChild = removeHard(root.rtChild);

         // 1 or 0 children
      } else {
         // Left child
         if (root.lftChild != null)
            root = root.lftChild;
         else
            // Right or no child
            root = root.rtChild;

         mSizeHard--;
      }
      return root;
   }

   /**
    * Public version. Calls private version.
    *
    * @return Whether garbage collection has removed elements that have been marked as deleted.
    */
   public boolean collectGarbage() {
      mRoot = collectGarbage(mRoot);
      return true;
   }

   /**
    * Private version. Recursively calls in post-order to check if root has been marked as deleted.
    *
    * @param root Starting root for iteration through search tree.
    * @return Root that has been searched from or on.
    */
   private LazySTNode collectGarbage(LazySTNode root) {

      if (root == null)
         return root;

      root.lftChild = collectGarbage(root.lftChild);
      root.rtChild = collectGarbage(root.rtChild);

      if (root.deleted) {
         root = removeHard(root);
      }

      return root;
   }

   /**
    * Node class for search tree. Represents each item in search tree.
    *
    * @author srichandramouli
    */
   private class LazySTNode {
      /**
       * Left child of node object.
       * Right child of node object.
       */
      public LazySTNode lftChild, rtChild;
      /**
       * Data stored in node.
       */
      public E data;
      /**
       * Represents whether node has been lazy deleted.
       */
      public boolean deleted;


      /**
       * Constructor for LazySTNode object.
       *
       * @param d   Data for node.
       * @param lft Left child for node.
       * @param rt  Right child for node.
       */
      public LazySTNode(E d, LazySTNode lft, LazySTNode rt) {
         lftChild = lft;
         rtChild = rt;
         data = d;
         deleted = false;
      }

   }
}