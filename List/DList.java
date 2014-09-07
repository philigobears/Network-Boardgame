/* DList.java */

package List;
/**
 *  A DList is a mutable doubly-linked list ADT.  Its implementation is
 *  circularly-linked and employs a sentinel (dummy) node at the head
 *  of the list.
 *
 *  DO NOT CHANGE ANY METHOD PROTOTYPES IN THIS FILE.
 */

public class DList {

	/**
	 *  head references the sentinel node.
	 *  size is the number of items in the list.  (The sentinel node does not
	 *       store an item.)
	 *
	 *  DO NOT CHANGE THE FOLLOWING FIELD DECLARATIONS.
	 */

	protected DListNode head;
	protected int size;

	/* DList invariants:
	 *  1)  head != null.
	 *  2)  For any DListNode x in a DList, x.next != null.
	 *  3)  For any DListNode x in a DList, x.prev != null.
	 *  4)  For any DListNode x in a DList, if x.next == y, then y.prev == x.
	 *  5)  For any DListNode x in a DList, if x.prev == y, then y.next == x.
	 *  6)  size is the number of DListNodes, NOT COUNTING the sentinel,
	 *      that can be accessed from the sentinel (head) by a sequence of
	 *      "next" references.
	 */

	/**
	 *  newNode() calls the DListNode constructor.  Use this class to allocate
	 *  new DListNodes rather than calling the DListNode constructor directly.
	 *  That way, only this method needs to be overridden if a subclass of DList
	 *  wants to use a different kind of node.
	 *  @param item the item to store in the node.
	 *  @param prev the node previous to this node.
	 *  @param next the node following this node.
	 */
	protected DListNode newNode(Object item, DListNode prev, DListNode next) {
		return new DListNode(item, prev, next);
	}

	/**
	 *  DList() constructor for an empty DList.
	 */
	public DList() {
		size=0;
		head=newNode(null,null,null);
		head.next=head;
		head.prev=head;
	}

	/**
	 *  isEmpty() returns true if this DList is empty, false otherwise.
	 *  @return true if this DList is empty, false otherwise. 
	 *  Performance:  runs in O(1) time.
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/** 
	 *  length() returns the length of this DList. 
	 *  @return the length of this DList.
	 *  Performance:  runs in O(1) time.
	 */
	public int length() {
		return size;
	}

	/**
	 *  insertFront() inserts an item at the front of this DList.
	 *  @param item is the item to be inserted.
	 *  Performance:  runs in O(1) time.
	 */
	public void insertFront(Object item) {
		head.next = newNode(item,head,head.next);
		head.next.next.prev = head.next;
		size++;
	}

	/**
	 *  insertBack() inserts an item at the back of this DList.
	 *  @param item is the item to be inserted.
	 *  Performance:  runs in O(1) time.
	 */
	public void insertBack(Object item) {
		head.prev = newNode(item,head.prev,head);
		head.prev.prev.next = head.prev;
		size++;
	}

	/**
	 *  front() returns the node at the front of this DList.  If the DList is
	 *  empty, return null.
	 *
	 *  Do NOT return the sentinel under any circumstances!
	 *
	 *  @return the node at the front of this DList.
	 *  Performance:  runs in O(1) time.
	 */
	public DListNode front() {
		if(head.next!=head){
			return head.next;
		}else{
			return null;
		}
	}

	/**
	 *  back() returns the node at the back of this DList.  If the DList is
	 *  empty, return null.
	 *
	 *  Do NOT return the sentinel under any circumstances!
	 *
	 *  @return the node at the back of this DList.
	 *  Performance:  runs in O(1) time.
	 */
	public DListNode back() {
		if (head.prev!=head){
			return head.prev;
		}else{
			return null;
		}
	}

	/**
	 *  next() returns the node following "node" in this DList.  If "node" is
	 *  null, or "node" is the last node in this DList, return null.
	 *
	 *  Do NOT return the sentinel under any circumstances!
	 *
	 *  @param node the node whose successor is sought.
	 *  @return the node following "node".
	 *  Performance:  runs in O(1) time.
	 */
	public DListNode next(DListNode node) {
		if (node==null){
			return null;
		}else if (node.next.item==null){
			return null;
		}else{
			return node.next;
		}
	}

	/**
	 *  prev() returns the node prior to "node" in this DList.  If "node" is
	 *  null, or "node" is the first node in this DList, return null.
	 *
	 *  Do NOT return the sentinel under any circumstances!
	 *
	 *  @param node the node whose predecessor is sought.
	 *  @return the node prior to "node".
	 *  Performance:  runs in O(1) time.
	 */
	public DListNode prev(DListNode node) {
		if (node==null){
			return null;
		}else if (node.prev.item==null){
			return null;
		}else{
			return node.prev;
		}
	}

	/**
	 *  insertAfter() inserts an item in this DList immediately following "node".
	 *  If "node" is null, do nothing.
	 *  @param item the item to be inserted.
	 *  @param node the node to insert the item after.
	 *  Performance:  runs in O(1) time.
	 */
	public void insertAfter(Object item, DListNode node) {
		if (node!=null){
			node.next = newNode(item,node,node.next);
			node.next.next.prev = node.next;
			size++;
		}
	}

	/**
	 *  insertBefore() inserts an item in this DList immediately before "node".
	 *  If "node" is null, do nothing.
	 *  @param item the item to be inserted.
	 *  @param node the node to insert the item before.
	 *  Performance:  runs in O(1) time.
	 */
	public void insertBefore(Object item, DListNode node) {
		if (node!=null){		  
			node.prev = newNode(item,node.prev,node);
			node.prev.prev.next = node.prev;
			size++;
		}
	}

	/**
	 *  remove() removes "node" from this DList.  If "node" is null, do nothing.
	 *  Performance:  runs in O(1) time.
	 */
	public void remove(DListNode node) {
		if (node!=null&&node!=head&&size!=0){
			node.prev.next = node.next;
			node.next.prev = node.prev;
			size--;
		}
	}


	/**
	 * insert() will insert item at the back of the list and return true, otherwise, if 
	 * the position represented by item is already in this DList, return false and do nothing
	 * @param item the item that will be inserted
	 * @return true if the item is not already in this DList; false if it is
	 */

	public boolean insert(Object item){
		DListNode p = front();
		while (p!=null){
			if(((int[])item)[0]==((int[])p.item)[0]&&((int[])item)[1]==((int[])p.item)[1]){
				return false;
			}else{
				p = next(p);
			}
		}
		insertBack(item);
		return true;
	}

	/**
	 * copy() returns a new copy of this DList
	 * @return a new copy of this DList
	 */
	public DList copy(){
		DList newl = new DList();
		DListNode p = front();
		while(p!=null){
			newl.insertBack(new int[2]);
			((int[])newl.back().item)[0] = ((int[])p.item)[0];
			((int[])newl.back().item)[1] = ((int[])p.item)[1];
			p = next(p);
		}
		return newl;
	}

	/**
	 *  toString() returns a String representation of this DList.
	 *
	 *  @return a String representation of this DList.
	 *  Performance:  runs in O(n) time, where n is the length of the list.
	 */
	public String toString() {
		String result = "[  ";
		DListNode current = head.next;
		while (current != head) {
			result = result + current.item.toString() + "  ";
			current = current.next;
		}
		return result + "]";
	}
}

