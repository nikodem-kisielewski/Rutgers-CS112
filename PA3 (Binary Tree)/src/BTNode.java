public class BTNode<T extends Comparable<T>> {
	private T data;
	private BTNode<T> left,right,parent;
	
	public BTNode(T data,BTNode<T> left,BTNode<T> right,BTNode<T> parent) {
		this.data=data;
		this.left=left;
		this.right=right;
		this.parent=parent;
	}
	
	public BTNode<T> getLeft(){
		return this.left;
	}
	
	public BTNode<T> getRight(){
		return this.right;
	}

	public BTNode<T> getParent(){
		return this.parent;
	}
	
	public T getData(){
		return this.data;
	}
	
	public void setLeft(BTNode<T> left) {
		this.left=left;
	}

	public void setRight(BTNode<T> right) {
		this.right=right;
	}

	public void setParent(BTNode<T> parent) {
		this.parent=parent;
	}
	
	public void setData(T data) {
		this.data=data;
	}

	public String toString() {
		return this.data.toString();
	}
}
