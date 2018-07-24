package project5;

/**
 * The class provides a recursive implementation of an AVL tree.
 *
 * @author Sarah Wardles and code adapted from Joanna Klukowska
 *
 * @param <E> generic type of data that is stored in nodes of the tree; needs to
 *            implement Comparable<E> interface
 */
public class AVLTree<E extends Comparable<E>> {

	/**
	 * Node class is used to represent nodes in an AVL tree.
	 * It contains a data item that has to implement Comparable interface
	 * and references to left and right subtrees as well as the height of the node.
	 *
	 * @author Sarah Wardles
	 *
	 * @param <E> a reference type that implements Comparable<E> interface
	 */
	static class Node<E extends Comparable<E>> implements Comparable<Node <E>> {
		
		protected Node <E> left;  // reference to the left subtree
		protected Node <E> right; // reference to the right subtree
		protected E data;         // data item stored in the node
		protected int height;	  //reference to the height of the node
		// No reference to preceding element
		
		/**
		 * Constructs a AVLNode initializing the data part
		 * according to the parameter and setting both
		 * references to subtrees to null.
		 * @param data (data to be stored in the node)
		 */
		protected Node(E data) {
			this.data = data;
			left = null;
			right = null;
			height = 1;
		}
		
		/** 
		 * Helper method for returning the data of a specific node in the AVLTree.
		 * @return this.data
		 */
		public E getData() {
			return this.data;
		}
		
		/** 
		 *This method compares the data inside of two nodes.
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 * @return an integer representation of the comparision (1,0,-1)
		 */
		@Override
		public int compareTo(Node<E> other) {
			return this.data.compareTo(other.data);
		}
		
		/** 
		 * This method returns a string representation of the data inside a node.
		 * @see java.lang.Object#toString()
		 * @return a string representation of the this.data
		 */
		@Override
		public String toString() {
			return data.toString();
		}
	}
	
	//DATA FIELDS OF AVLTREE CLASS
	// root of the tree
	protected Node<E> root;
	// current number of nodes in the tree
	protected int numOfElements;
	//helper variable used by the remove methods
	private boolean found;

	/**
	 * Default constructor that creates an empty tree.
	 */
	public AVLTree() {
		this.root = null;
		numOfElements = 0;
	}

	/**
	 * Add the given data item to the tree. If item is null, the tree does not
	 * change. If item already exists, the tree does not change.
		 *
		 * @param item the new element to be added to the tree
		 */
		public void add(E item) {
			if (item == null)
				return;
			root = add (root, item);
		}

		/**
		 * Actual recursive implementation of add.
		 *
		 * @param item the new element to be added to the tree
		 */
		private Node<E> add(Node<E> node, E item) {
			//if root is null
			if (node == null) {
				numOfElements++;
				return new Node<E>(item); // instantiates a new node and adds it
			}
			
			if (node.data.compareTo(item) > 0) {
				node.left = add(node.left, item);
			} else if (node.data.compareTo(item) < 0) {
				node.right = add(node.right, item);
			} else { // no duplicates allowed
				return node; 
			}
			
			//add new node
			//update all the heights from the newly inserted node to the root
			//go up towards the root from the newly inserted node
			//stop when you find the balancing factor to be 2/-2
			//from the unbalanced node go down two nodes towards the newly inserted node to find the necessary rotation
			//rotate

			//update the height of all the nodes in the path from the added node to the root
			node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
			//check the balancing factor of the node
			int balanceFactor = balanceFactor(node);
			
			//if the node is unbalanced then call specific rotations to balance the tree

			//LL
			if (balanceFactor >= 2 && item.compareTo(node.left.data) < 0) { 
				return rightRotate(node);
			}
			//RR
			if (balanceFactor <= -2 && item.compareTo(node.right.data) > 0) { 
				return leftRotate(node);
			}
			//LR
			if (balanceFactor >= 2 && item.compareTo(node.left.data) > 0) { 
				node.left = leftRotate(node.left);
				return rightRotate(node);
			}
			//RL
			if (balanceFactor <= -2 && item.compareTo(node.right.data) < 0) { 
				node.right = rightRotate(node.right);
				return leftRotate(node);
			}
			return node;
		}
		
		/**
		* Helper method to perform a right rotation on a node in the AVLTree.
		* @return the new node
		*/
		protected Node<E> rightRotate(Node<E> current) {
			Node<E> child = current.left;
			Node<E> temp = child.right;
			
			child.right = current;
			current.left = temp;
			
			// update the height of the nodes
			current.height = 1 + Math.max(getHeight(current.left), getHeight(current.right));
			child.height = 1 + Math.max(getHeight(child.left), getHeight(child.right));
						
			return child;
		}

		/**
		* Helper method to perform a left rotation on a node in the AVLTree.
		* @return the new node
		*/
		protected Node<E> leftRotate(Node<E> current) {
			Node<E> child = current.right;
			Node<E> temp = child.left;
			
			child.left = current;
			current.right = temp;
			
			// update the height of the nodes
			current.height = 1 + Math.max(getHeight(current.left), getHeight(current.right));
			child.height = 1 + Math.max(getHeight(child.left), getHeight(child.right));
						
			return child;
		}

		/**
		* Helper method to return the height of a node in the AVLTree.
		* @return this.height
		*/
		protected int getHeight(Node<E> node) {
			if (node == null) {
				return 0;
			}
			return node.height;
		}
		
		/**
		* Helper method to return the balance factor of a node in the AVLTree.
		* @return the difference between the height of the two children of the node
		*/
		protected int balanceFactor(Node<E> node) {
			if (node == null) {
				return 0;
			}
			
			return getHeight(node.left) - getHeight(node.right);
		}

		/**
		 * Remove the item from the tree. If item is null the tree remains unchanged. If
		 * item is not found in the tree, the tree remains unchanged.
		 *
		 * @param target the item to be removed from this tree
		 */
		public boolean remove(E target)
		{
			root = recRemove(target, root);
			if (found) numOfElements--; 
			return found;
		}


		/**
		 * Actual recursive implementation of remove method: find the node to remove.
		 *
		 * @param target the item to be removed from this tree
		 */
		private Node<E> recRemove(E target, Node<E> node)
		{
			if (node == null)
				found = false;
			else if (target.compareTo(node.data) < 0)
				node.left = recRemove(target, node.left);
			else if (target.compareTo(node.data) > 0)
				node.right = recRemove(target, node.right );
			else {
				//if weve found the node to remove
				node = removeNode(node);
				found = true;
			}
			
			//if the node is null dont update height or balance
			if (node == null) {
				return node;
			}
			
			//update the height of the ndoe after removing
			node.height = 1 + Math.max(getHeight(node.left), getHeight(node.right));
			//check the balancin factor of the nodes
			int balanceFactor = balanceFactor(node);
			
			//if unbalanced then determine which case it is and then perform appropriate rotations
			//LL
			if (balanceFactor >= 2 && target.compareTo(node.left.data) < 0) { 
				return rightRotate(node);
			}
			//RR
			if (balanceFactor <= -2 && target.compareTo(node.right.data) > 0) { 
				return leftRotate(node);
			}
			//LR
			if (balanceFactor >= 2 && target.compareTo(node.left.data) > 0) { 
				node.left = leftRotate(node.left);
				return rightRotate(node);
			}
			//RL
			if (balanceFactor <= -2 && target.compareTo(node.right.data) < 0) { 
				node.right = rightRotate(node.right);
				return leftRotate(node);
			}
			return node;
		}
		
		/*
		 * Actual recursive implementation of remove method: perform the removal.
		 *
		 * @param target the item to be removed from this tree
		 * @return a reference to the node itself, or to the modified subtree
		 */
		private Node<E> removeNode(Node<E> node)
		{
			E data;
			if (node.left == null)
				return node.right ;
			else if (node.right  == null)
				return node.left;
			else {
				data = getPredecessor(node.left);
				node.data = data;
				node.left = recRemove(data, node.left);
				return node;
			}
		}

		/*
		 * Returns the information held in the rightmost node of subtree
		 *
		 * @param subtree root of the subtree within which to search for the rightmost node
		 * @return returns data stored in the rightmost node of subtree
		 */
		private E getPredecessor(Node<E> subtree)
		{
			if (subtree==null) throw new NullPointerException("getPredecessor called with an empty subtree");
			Node<E> temp = subtree;
			while (temp.right  != null)
				temp = temp.right ;
			return temp.data;
		}

		/**
		 * Determines the number of elements stored in this AVL tree.
		 *
		 * @return number of elements in this AVL tree
		 */
		public int size() {
			return numOfElements;
		}

		/**
		 * Returns a string representation of this tree using an inorder traversal .
		 * @see java.lang.Object#toString()
		 * @return string representation of this tree
		 */
		public String toString() {
			StringBuilder s = new StringBuilder();
			inOrderPrint(root, s);
			return s.toString();
		}

		/*
		 * Actual recursive implementation of inorder traversal to produce string
		 * representation of this tree.
		 *
		 * @param tree the root of the current subtree
		 * @param s the string that accumulated the string representation of this BST
		 */
		protected void inOrderPrint(Node<E> tree, StringBuilder s) {
			if (tree != null) {
				inOrderPrint(tree.left, s);
				s.append(tree.data.toString() + "  ");
				inOrderPrint(tree.right , s);
			}
		}

		/**
		 * DO NOT MOFIFY THIS METHOD.
		 * INCLUDE IT AS-IS IN YOUR CODE.
		 *
		 * Produces tree like string representation of this BST.
		 * @return string containing tree-like representation of this BST.
		 */
		public String toStringTreeFormat() {

			StringBuilder s = new StringBuilder();

			preOrderPrint(root, 0, s);
			return s.toString();
		}

		/*
		 * DO NOT MOFIFY THIS METHOD.
		 * INCLUDE IT AS-IS IN YOUR CODE.
		 *
		 * Actual recursive implementation of preorder traversal to produce tree-like string
		 * representation of this tree.
		 *
		 * @param tree the root of the current subtree
		 * @param level level (depth) of the current recursive call in the tree to
		 *   determine the indentation of each item
		 * @param output the string that accumulated the string representation of this
		 *   BST
		 */
		private void preOrderPrint(Node<E> tree, int level, StringBuilder output) {
			if (tree != null) {
				String spaces = "\n";
				if (level > 0) {
					for (int i = 0; i < level - 1; i++)
						spaces += "   ";
					spaces += "|--";
				}
				output.append(spaces);
				output.append(tree.data);
				preOrderPrint(tree.left, level + 1, output);
				preOrderPrint(tree.right , level + 1, output);
			}
			// uncomment the part below to show "null children" in the output
			else {
				String spaces = "\n";
				if (level > 0) {
					for (int i = 0; i < level - 1; i++)
						spaces += "   ";
					spaces += "|--";
				}
				output.append(spaces);
				output.append("null");
			}
		}
}