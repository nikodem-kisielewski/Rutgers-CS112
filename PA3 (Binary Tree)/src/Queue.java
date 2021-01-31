public class Queue<T> {
	private DLL<T> myList;
	
	public Queue() {
		myList=new DLL<T>();
	}
	
	public void enqueue(T element) {
		myList.addFirst(element);
	}
	
	public T dequeue() {
		T element=null;
		if (myList.size()>0) {
			element = myList.getLast();
			myList.deleteLast();
		}
		return element;
	}
	
	public int size() {
		return myList.size();
	}
	
	public boolean isEmpty() {
		return myList.size()==0;
	}
	
	public void traverse() {
		myList.traverse();
	}

	public static void main(String[] args) {
		Queue<String> myQueue=new Queue<String>();
		
		myQueue.enqueue("the");
		myQueue.enqueue("quick");
		myQueue.enqueue("brown");
		myQueue.enqueue("fox");
		myQueue.enqueue("jumps");
		myQueue.enqueue("over");
		
		myQueue.traverse();
		
		System.out.println("dequeue->"+myQueue.dequeue());
		
		myQueue.traverse();
		myQueue.enqueue("the");
		myQueue.enqueue("lazy");
		myQueue.traverse();		
		
		System.out.println("dequeue->"+myQueue.dequeue());
		
		myQueue.traverse();		
	}
	
}
