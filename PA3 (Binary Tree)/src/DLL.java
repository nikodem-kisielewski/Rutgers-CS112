public class DLL<T> {
	private Node<T> first;
	private Node<T> last;
	private int count;
	private Node<T> current;

	public DLL() {
		this.first=null;
		this.last=null;
		count=0;
	}
	
	public T getFirst() {
		current=first;
		if (first!=null)
			return first.getData();
		return null;
	}
	
	public T getNext() {
		if (current!=null) {
			current=current.getNext();
			return current.getData();
		}
		return null;
	}
	
	public T getLast() {
		if (last!=null)
			return last.getData();
		return null;
	}
	
	public void addFirst(T data) {
		Node<T> n=new Node<T>(data,null,first);
		if (this.first!=null) {
			this.first.setPrev(n);
		}
		else {
			this.last=n;
		}
		this.first=n;
		count++;
	}
	
	public void addLast(T data) {
		Node<T> n=new Node<T>(data,last,null);
		if (this.last!=null) {
			this.last.setNext(n);
		}
		else {
			this.first=n;
		}
		this.last=n;
		count++;
	}

	public void deleteFirst() {
		if (this.first!=null) {
			Node<T> newFirst=this.first.getNext();
			this.first=newFirst;
			if (newFirst!=null) {
				newFirst.setPrev(null);
			}
			else {
				this.last=null;
			}
			count--;
		}
	}

	public void deleteLast() {
		if (this.last!=null) {
			Node<T> newLast=this.last.getPrev();
			this.last=newLast;
			if (newLast!=null) {
				newLast.setNext(null);
			}
			else {
				this.first=null;
			}
			count--;
		}
	}


	public void traverse() {
		Node<T> current=this.first;
		while (current!=null) {
			System.out.print(current.getData()+" ");
			current=current.getNext();
		}
	}

	
	public int size() {
		return count;
	}
	
	public String toString() {
		String ret="";
		Node<T> current=this.first;
		while (current!=null) {
			ret=ret+"+"+current.getData();
			current=current.getNext();
		}
		return ret;
	}
	
}
