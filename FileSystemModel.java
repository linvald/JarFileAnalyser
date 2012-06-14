// FileSystemModel.java
// TreeModel implementation using File objects as tree nodes.

// Java core packages
import java.io.*;
import java.util.*;

// Java extension packages
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

public class FileSystemModel implements TreeModel {

   // hierarchy root
   private File root;
   
   // TreeModelListeners
   private Vector listeners = new Vector();

   // FileSystemModel constructor
   public FileSystemModel( File rootDirectory ) 
   {
     root = rootDirectory;
   }   
   
   // get hierarchy root (root directory)
   public Object getRoot() 
   {
      return root;
   }

   // get parent's child at given index
   public Object getChild( Object parent, int index ) 
   {      
      // get parent File object
     try{
     	
     	File directory = ( File ) parent;
     	 // get list of files in parent directory
        String[] children = directory.list();
        // return File at given index and override toString
        // method to return only the File's name
        return new TreeFile( directory, children[ index ] );   
      
     
     }catch(Exception e){
     	System.err.println("Error in getchild:" + e);
     	
     }
   return null;
   }

   // get parent's number of children
   public int getChildCount( Object parent ) 
   {
      // get parent File object
      File file = ( File ) parent;
      
      // get number of files in directory
      if ( file.isDirectory() ) {
         
         String[] fileList = file.list();
         
         if ( fileList != null )
            return file.list().length;
      }
            
      return 0; // childCount is 0 for files
   }

   // return true if node is a file, false if it is a directory
   public boolean isLeaf( Object node ) 
   {
      File file = ( File ) node;
      return file.isFile();
   }

   // get numeric index of given child node
   public int getIndexOfChild( Object parent, Object child ) 
   {
      // get parent File object
      File directory = ( File ) parent;
      
      // get child File object
      File file = ( File ) child;
      
      // get File list in directory
      String[] children = directory.list();
      
      // search File list for given child
      for ( int i = 0; i < children.length; i++ ) {
         
         if ( file.getName().equals( children[ i ] ) ) {
            
            // return matching File's index
            return i;
         }
      }      
      
      return -1; // indicate child index not found

   } // end method getIndexOfChild
   
   // invoked by delegate if value of Object at given
   // TreePath changes
   public void valueForPathChanged( TreePath path, 
      Object value ) 
   {
      // get File object that was changed
      File oldFile = ( File ) path.getLastPathComponent();
      
      // get parent directory of changed File
      String fileParentPath = oldFile.getParent();
      
      // get value of newFileName entered by user
      String newFileName = ( String ) value;
      
      // create File object with newFileName to rename oldFile 
      File targetFile = new File( fileParentPath, newFileName );
     
      // rename oldFile to targetFile
      oldFile.renameTo( targetFile );
      
      // get File object for parent directory
      File parent = new File( fileParentPath );
      
      // create int array for renamed File's index
      int[] changedChildrenIndices = 
         { getIndexOfChild( parent, targetFile) };
         
      // create Object array containing only renamed File
      Object[] changedChildren = { targetFile };
         
      // notify TreeModelListeners of node change
      fireTreeNodesChanged( path.getParentPath(), 
         changedChildrenIndices, changedChildren );      

   } // end method valueForPathChanged
   
   // notify TreeModelListeners that children of parent at
   // given TreePath with given indices were changed
   private void fireTreeNodesChanged( TreePath parentPath, 
      int[] indices, Object[] children )
   {
      // create TreeModelEvent to indicate node change
      TreeModelEvent event = new TreeModelEvent( this,
         parentPath, indices, children );
         
      Iterator iterator = listeners.iterator();      
      TreeModelListener listener = null;         
      
      // send TreeModelEvent to each listener 
      while ( iterator.hasNext() ) {
         listener = ( TreeModelListener ) iterator.next();
         listener.treeNodesChanged( event );
      }      
   } // end method fireTreeNodesChanged
   
   // add given TreeModelListener 
   public void addTreeModelListener( 
      TreeModelListener listener ) 
   {
      listeners.add( listener );
   }
   
   // remove given TreeModelListener
   public void removeTreeModelListener(
      TreeModelListener listener ) 
   {
      listeners.remove( listener );
   }
      
   // TreeFile is a File subclass that overrides method
   // toString to return only the File name. 
   private class TreeFile extends File {

      // TreeFile constructor
      public TreeFile( File parent, String child )
      {
         super( parent, child );
      }

      // override method toString to return only the File name
      // and not the full path
      public String toString()
      {
         return getName();
      }
   } // end inner class TreeFile
}


