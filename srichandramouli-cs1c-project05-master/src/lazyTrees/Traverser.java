package lazyTrees;

/**
 * Interface used for traversal with each search tree.
 * @param <E> Generic parameter for interface.
 */
public interface Traverser<E>
{
   /**
    * Declares visit() method so some method visit() must be overridden when it extends Traverser.
    * @param x Generic parameter that whole class is subjec to.
    */
   void visit(E x);
}