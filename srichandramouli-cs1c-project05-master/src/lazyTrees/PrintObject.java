package lazyTrees;

/**
 * Allows for use of function object.
 * Implements Traverser interface and allows for printing.
 * @param <E> Generic parameter from Traverser interface.
 */
class PrintObject<E> implements Traverser<E>
{
   /**
    * Prints parameter x and space after.
    * @param x Generic parameter that whole class is subject to.
    */
   public void visit(E x)
   {
      System.out.print( x + " ");
   }
}