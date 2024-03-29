package estructurasDeDatos;


public class Rasgo<T> {

	String key;
	T value;

	public void setKey(String key) {
		this.key = key;
	}

	public void setValue(T value) {
		this.value = value;
	}


	public String getKey(){
		return key;
	}
	
	public T getValue() {
		return value;
	}

	
	public Class<?> getType(){
		return value.getClass();
	}

	public String toString() {
		return getKey() + ":" + getValue();
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Rasgo<?> other = (Rasgo<?>) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

}
