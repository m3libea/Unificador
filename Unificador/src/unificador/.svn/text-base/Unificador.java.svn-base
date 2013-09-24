package unificador;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import estructurasDeDatos.ConjuntoRasgos;
import estructurasDeDatos.ECR;
import estructurasDeDatos.Rasgo;

public class Unificador {

	// Método que dados dos estructuras complejas de rasgos, las unifica
	// y devuelve una cadena formateada con las entradas y el resultado de hacer
	// la unificación.
	public static String resultadoUnificarECR(ECR a, ECR b) {
		// Cadena que servirá de separador entre diferentes elementos.
		String separador = "##########################";
		// Esta será la cadena que devolvamos al final del método. Escribimo
		// suna pequeña cabecera.
		String s = separador + separador + "\n######  UNIFICA\n";

		// Escribimos el nombre de una de las ECR.
		s += separador + "\n";
		s += "## Entrada 1" + " : " + a.getEtiqueta() + "\n";
		s += separador + "\n##\n";
		// Por cada conjunto de rasgos que tengan la misma etiqueta
		for (ConjuntoRasgos cr : a.getConjuntosRasgos()) {
			// escribirmos el conjunto formateado.
			s += writeFormatedCR(cr, 0, 10) + "\n";
		}

		// Escribimos el nombre de la otra ECR
		s += separador + "\n";
		s += "## Entrada 2" + " : " + b.getEtiqueta() + "\n";
		s += separador + "\n##\n";
		// Por cada conjunto de rasgos que tengan la misma etiqueta
		for (ConjuntoRasgos cr : b.getConjuntosRasgos()) {
			// escribimos el conjunto formateado.
			s += writeFormatedCR(cr, 0, 10) + "\n";
		}

		// Ahora escribimos los resultados de la unificación. Primero una
		// cabecera.
		s += separador + "\n" + "## RESULTADO:\n" + separador + "\n";
		// Debemos tener en cuenta que cuando una estructura compleja de rasgos
		// tiene más de un conjunto de rasgos, solamente aparecerán en la cadena
		// de salida las unificaciones que hayan sido correctas, ignorando las
		// demás, si ninguna de las unificaciones pudiera realizarse, entonces
		// sería fallo de unificación.
		boolean encontrado = false;
		// Conjunto donde vamos guardando los conjunto de rasgos unificados
		Set<ConjuntoRasgos> unificados = new HashSet<ConjuntoRasgos>();
		// Por cada conjunto de rasgos en el primer ECR
		for (ConjuntoRasgos crA : a.getConjuntosRasgos()) {
			// y por cada conjunto de rasgos en el segundo ECR
			for (ConjuntoRasgos crB : b.getConjuntosRasgos()) {
				// realizamos la unificación
				ConjuntoRasgos unificado = unifica(crA, crB);
				// si es válidad y no devuelve nulo y no está contenido entre los ya unificados:
				if (unificado != null && !unificados.contains(unificado)) {
					// lo agregamos a los unificados y
					unificados.add(unificado);
					// si es el primer resultado válido que nos encontramos
					if (!encontrado) {
						// escribimos que la unificación ha sido correcta,
						s += "##\n##   UNIFICACION OK\n##\n";
					}
					// decimos que ya hemos encontrado al menos una unificación
					// correcta
					encontrado = true;
					// y escribimos el resultado de la unificación.
					s += writeFormatedCR(unificado, 0, 10) + "\n";
				}
			}
		}
		s += "##\n";
		// Si no hemos encontrado ninguna unificación correcta
		if (!encontrado) {
			// escribimos que se ha producido un fallo.
			s += "##   UNIFICACION FALLO\n##\n";
		}
		// Por último, devolvemos la cadena
		return s;
	}

	// Método que hace la unificación, dado dos conjuntos de rasgos a y b,
	// devuelve el conjunto de rasgos resultado de la unificación de ambos o
	// nulo, en caso de que la unifación no haya podido realizarse.

	// Para realizar la unificación nos basaremos en una doble recursión con
	// eliminación, lo que mejora el tiempo del algoritmo.

	public static ConjuntoRasgos unifica(ConjuntoRasgos a, ConjuntoRasgos b) {
		// Creamos el conjunto de rasgos que vamos a devolver.
		ConjuntoRasgos res = new ConjuntoRasgos();

		// Debemos copiar los conjuntos de entrada, ya que Java pasa los objetos
		// por referencia no podemos tocar los originales, si no estariamos
		// cambiando la estructura de los ECR al eliminar de los parámetros los
		// rasgos que hemos analizado. Eliminamos los warning de comprobación de
		// tipo, aquí vamos a trabajar sin importarnos de qué tipo son los
		// rasgos
		@SuppressWarnings("unchecked")
		HashSet<Rasgo<?>> sa = (HashSet<Rasgo<?>>) a.getRasgos().clone();
		@SuppressWarnings("unchecked")
		HashSet<Rasgo<?>> sb = (HashSet<Rasgo<?>>) b.getRasgos().clone();

		// Creamos el set de rasgos que luego asignaremos al ConjuntoRasgos res.
		HashSet<Rasgo<?>> hsres = new HashSet<Rasgo<?>>();

		// Vamos a hacer una doble iteración, primero iteramos sobre el conjunto
		// de rasgos a, su copia, en realidad.
		Iterator<Rasgo<?>> ita = sa.iterator();
		// Mientras el iterador tenga siguiente elemento,
		while (ita.hasNext()) {
			// cogemos el rasgo
			Rasgo<?> ra = ita.next();
			// e iteramos sobre el conjunto b
			Iterator<Rasgo<?>> itb = sb.iterator();
			boolean encontrado = false;
			// Mientras en b no hayamos encontrado el rasgo con el mismo nombre
			// y siga habiendo más rasgos en b
			while (!encontrado && itb.hasNext()) {
				// cogemos el rasgo de b
				Rasgo<?> rb = itb.next();
				// y si los nombres coinciden:
				if (ra.getKey().equals(rb.getKey())) {
					// en caso de que sean rasgos de cadena
					if (ra.getType() == String.class
							&& rb.getType() == String.class) {
						// comparamos los valores y si coinciden
						if (ra.getValue().equals(rb.getValue())) {
							// hemos encontrado el valor que queríamos
							encontrado = true;
							// así que lo añadimos a la lista que vamos a
							// devolver
							hsres.add(ra);
							// y eliminamos de b dicho rasgo.
							itb.remove();
							// En caso de que los valores no coincidan
						} else {
							// la unificación falla y devolvemos nulo.
							return null;
						}
						// Si son rasgos con conjunto de rasgos
					} else if (ra.getType() == ConjuntoRasgos.class
							&& rb.getType() == ConjuntoRasgos.class) {
						// intentamos unificar.
						ConjuntoRasgos crin = unifica(
								(ConjuntoRasgos) ra.getValue(),
								(ConjuntoRasgos) rb.getValue());
						// Si la unificación ha salido bien
						if (crin != null) {
							// creamos un nuevo rasgo
							Rasgo<ConjuntoRasgos> nra = new Rasgo<ConjuntoRasgos>();
							// que tiene por nombre el nombre del rasgo que
							// estabamos recorriendo
							nra.setKey(ra.getKey());
							// y por valor el resultado de la unificación.
							nra.setValue(crin);
							// hemos encontrado el valor que queríamos
							encontrado = true;
							// así que lo aadimos a la lista que vamos a
							// devolver
							hsres.add(nra);
							// y lo eliminamos de b.
							itb.remove();
							// Si no se pueden unificar
						} else {
							// devolvemos nulo
							return null;
						}
					}
				}
			}
			// En caso de que entre los rasgos de b no hayamos encontrado
			// ninguno con el mismo nombre que el de a,
			if (!encontrado) {
				// añadimos el rasgo de a a los rasgos que vamos a devolver.
				hsres.add(ra);
			}
		}
		// Por último agregamos a los rasgos que vamos a devolver, todos los
		// rasgos que queden en b
		hsres.addAll(sb);
		// asociamos el conjunto de rasgo que creamos al principio con el set de
		// rasgos
		res.setRasgos(hsres);
		// y devolvemos el conjunto de rasgos.
		return res;
	}

	// Método auxiliar que devuelve una cadena formateada dado un conjunto de
	// rasgos. Los parámetros son: el conjunto de rasgos, el nivel de
	// recursividad en el que nos encontramos y el numero de carácteres en
	// blanco que deben ir al principio de cada línea.
	public static String writeFormatedCR(ConjuntoRasgos cr, int n, int bs) {
		// Creamos la cadena vacía que vamos a devolver.
		String s = "";
		// Si estamos en el nivel 0
		if (n == 0) {
			// guardamos en la cadena "##", tantos espacios en blanco como diga
			// el parámetro de entrada y " -> ".
			s += "##" + emptyString(bs) + " -> ";
		}
		// Guardamos en la cadena un paréntesis abierto, lo que indica que
		// empieza el conjunto de rangos.
		s += "(";
		// Vamos a iterar por cada uno de los rangos que tiene el conjunto de
		// rangos
		Iterator<Rasgo<?>> it = cr.getRasgos().iterator();
		// pero el primero es un poco diferente, por eso lo sacamos del resto
		if (it.hasNext()) {
			// obtenemos el primer rango, que debe ir pegado al "(" que ya
			// tenemos en la cadena
			Rasgo<?> r = it.next();
			// si el rasgo es simple
			if (r.getType() == String.class) {
				// simplemente guardamos en s el rasgo, que se imprime de la
				// forma "nombre:valor".
				s += r;
				// En caso de que sea un rasgo con un conjunto de rasgos por
				// valor
			} else if (r.getType() == ConjuntoRasgos.class) {
				// guardamos en s el nombre y los dos puntos
				s += r.getKey() + ":";
				// y llamamos a esta misma función con el conjunto de rasgos que
				// tiene por valor el primer rasgo, aumentando en 1 el nivel y
				// sumandole a los espacios en blanco el tamaño del nombre del
				// rasgo y 2, que corresponden al parentesis abierto y a los dos
				// puntos.
				s += writeFormatedCR((ConjuntoRasgos) r.getValue(), n + 1, bs
						+ r.getKey().length() + 2);
			}
			// Si el conjunto tiene más rasgo
			if (it.hasNext()) {
				// añadimos a s un salto de linea y ## representando el
				// principio de la línea.
				s += ",\n##";
			}
		}
		// Ahora empezamos el bucle con el resto de rasgos que no son el primero
		while (it.hasNext()) {
			// cogemos el siguiente rasgo,
			Rasgo<?> r = it.next();
			// si es simple
			if (r.getType() == String.class) {
				// añadimos a s tanto espacios en blanco como sean necesario más
				// 5, que son " -> " y el paréntesis abierto
				s += emptyString(bs + 5) + r;
				// en caso de que sea un rasgo con un conjunto de rasgos
			} else if (r.getType() == ConjuntoRasgos.class) {
				// añadimos a s los espacios en blanco necesarios, el nombre del
				// rasgos y los :
				s += emptyString(bs + 5) + r.getKey() + ":";
				// y llamamos a esta misma función con el conjunto de rasgos del
				// rasgo y aumentando el nivel en 1 y el número de espacios en
				// el tamaño del nombre más dos (el '(' y los ':')
				s += writeFormatedCR((ConjuntoRasgos) r.getValue(), n + 1, bs
						+ r.getKey().length() + 2);
			}
			// Si el conjunto tiene más rasgos
			if (it.hasNext()) {
				// añadimos el salto de linea y los ##
				s += ",\n##";
			}
		}
		// por último añadimos el ')' y devolvemos la cadena
		return s + ")";

		// Al final el resultado es algo de la forma:

		/*
		 * ##           -> (FAP0018VhTHw:RASODgqOH,
		 * ##               FCM0027iZkUg:(FAP0018VhTHw:RASOmKDDz,
		 * ##                             FAI0004FamhP:1))
		 * 
		 */

	}

	// Método auxiliar que devuelve una cadena con un número de espacios igual
	// al parámetro de entrada.
	public static String emptyString(int n) {
		String s = "";
		for (int i = 0; i < n; i++) {
			s += " ";
		}
		return s;
	}

}
