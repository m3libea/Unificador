package estructurasDeDatos;

import java.util.HashSet;

public class ConjuntoRasgos {
	HashSet<Rasgo<?>> rasgos;

	public ConjuntoRasgos() {
		rasgos = new HashSet<Rasgo<?>>();
	}

	public HashSet<Rasgo<?>> getRasgos() {
		return rasgos;
	}

	public void setRasgos(HashSet<Rasgo<?>> rasgos) {
		this.rasgos = rasgos;
	}

	public String toString() {
		String s = "(";
		for (Rasgo<?> r : rasgos) {
			s += r + ",\n ";
		}
		return s + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rasgos == null) ? 0 : rasgos.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConjuntoRasgos other = (ConjuntoRasgos) obj;
		if (rasgos == null) {
			if (other.rasgos != null)
				return false;
		} else if (!rasgos.equals(other.rasgos))
			return false;
		return true;
	}



}
