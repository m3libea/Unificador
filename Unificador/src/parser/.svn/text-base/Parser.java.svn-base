package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import estructurasDeDatos.ConjuntoRasgos;
import estructurasDeDatos.Par;
import estructurasDeDatos.Rasgo;

public class Parser {

	// Método que lee el archivo UNF y devuelve una lista ordenada de pares de
	// etiquetas, aquellas para las cuales hay que realizar la unificación; así
	// como un conjunto de etiquetas, las mismas que están en los pares. Se
	// usará para buscar estas etiquetas en el diccionario de estructuras.
	public static Par<List<Par<String, String>>, Set<String>> parseaArchivoUNF(
			String unf) {
		// Las dos estructuras de datos que se van a devolver.
		List<Par<String, String>> lunf = new LinkedList<Par<String, String>>();
		Set<String> sunf = new HashSet<String>();
		try {
			// Abrimos el fichero.
			File archivo = new File(unf);
			FileReader fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			// Compilamos la expresión regular que verifica la correción de las
			// instrucciones, que tienen la forma $Unifica(etiqueta1 &
			// etiqueta2).
			Pattern p = Pattern
					.compile("^\\s*[$]Unifica\\s*[(]\\s*([a-zA-Z_0-9]+)\\s*&\\s*([a-zA-Z_0-9]+)\\s*[)]\\s*$");

			String linea;
			while ((linea = br.readLine()) != null) {
				// Para cada línea del fichero comprobamos que la línea cumpla
				// con la expresión regular,
				Matcher m = p.matcher(linea);
				// si es así
				if (m.matches()) {
					// añadimos las dos etiquetas al conjunto y
					sunf.add(m.group(1));
					sunf.add(m.group(2));
					// creamos un par que añadimos a la lista de pares;
					lunf.add(new Par<String, String>(m.group(1), m.group(2)));
					// si no cumple la expresión regular
				} else {
					// y es una línea no vacía
					if (!linea.isEmpty()) {
						// informamos del error e ignoramos la línea.
						System.out
								.println("Ignorando instrucción con errores.");
					}
				}
			}
		} catch (IOException e) {
			// Si hay cualquier problema abriendo el archivo informamos.
			System.out.println("Fallo al parsear el archivo UNF");
		}
		// Devolvemos sendas estructuras de datos.
		return new Par<List<Par<String, String>>, Set<String>>(lunf, sunf);
	}

	// Método que lee el archivo ECR y devuelve un mapa con las estructuras
	// complejas de rasgos. Como porámetros tiene la ruta al arhivo .ecr y un
	// conjunto de etiquetas, que son las que se buscarán dentro del diccionario
	// de ecr. Es posible que no se encuentren en el diccionario todas las
	// estructuras que queremos buscar, esas se ignoran y se devuelven
	// únicamente las que sí se han encontrado. Que se pretenda unificar
	// estructuras que no están en el diccionario provocará un fallo en la
	// unificación.
	public static Map<String, Set<String>> parseaArchivoECR(String ecr,
			Set<String> ecrsABuscar) {
		// La estructura que vamos a devolver.
		Map<String, Set<String>> mecr = new HashMap<String, Set<String>>();
		try {
			// Abrimos el fichero.
			File archivo = new File(ecr);
			FileReader fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			// Compilamos la expresión regular que servirá para verificar la
			// sintaxis correcta (a priori) de los conjuntos de rasgos. Tienen
			// la estructura: [ etiqueta = (definicion de rasgos) ]. Por
			// cuestiones de eficiencia aquí no se crean las estructuras
			// complejas de rasgos, si no que se almacena como un String,
			// posteriormente se creará la estructura cuando sea necesario a
			// partir de la cadena que aquí se guarda, hacerlo de otro modo
			// provocaría desbordamiento de la memoria para diccionarios muy
			// grandes. Este método no es infalible y también puede desbordar la
			// memoria, aunque para diccionarios mucho más grande que los
			// necesarios para desbordar la memoria si creasemos las ecr en este
			// momento. Dado que no creamos la estructura en este momento,
			// tampoco se verifica que su sintasix sea correcta, deja este
			// trabajo para cuando se cree la estructura.
			Pattern p = Pattern
					.compile("\\s*\\[\\s*([a-zA-Z_0-9]+)\\s*=\\s*([a-zA-Z_0-9() :,]+)\\s*\\]\\s*");

			String linea;
			while ((linea = br.readLine()) != null) {
				// Para cada línea del fichero comprobamos que se cumpla la
				// expresión regular,
				Matcher m = p.matcher(linea);
				// si es así
				if (m.matches()) {
					String etiqueta = m.group(1);
					String conjunto = m.group(2);
					// eliminamos todos los espacios del valor del rango
					conjunto = conjunto.replaceAll(" ", "");
					// buscamos si la etiqueta de esa línea se encuentra entre
					// los ecr que estamos buscando
					if (ecrsABuscar.contains(etiqueta)) {
						// si es así, comprobamos si ya habíamos encontrado un
						// conjunto de rasgos para esa etiqueta, en cuyo caso
						if (mecr.containsKey(etiqueta)) {
							// añadimos el nuevo conjunto de rasgos a los que ya
							// teníamos definidos para esa etiqueta
							mecr.get(etiqueta).add(conjunto);
							// en caso contrario
						} else {
							// creamos un nuevo set de strings,
							Set<String> s = new HashSet<String>();
							// añadimos el conjunto de rasgos recien leído al
							// set
							s.add(conjunto);
							// y lo colocamos en el mapa junto con la etiqueta.
							mecr.put(etiqueta, s);
						}
						// Si la línea leída no la estamos buscando, no hacemos
						// nada.
					}
					// Si no se cumple la expresión regular
				} else {
					// y es una línea no vacía
					if (!linea.isEmpty()) {
						// informamos del error e ignoramos la línea
						System.out
								.println("Ignorando definición de ecr con errores.");
					}
				}
			}
			// Si hay cualquier problema abriendo el archivo informamos.
		} catch (IOException e) {
			System.out.println("Fallo al parsear el archivo ERC");
		}
		// Devolvemos la estructura de datos.
		return mecr;
	}

	// Método que dado una cadena de tipo (nombreRasgo:valorRasgo,
	// nombreRasgo:valorRasgo...) devuelve el conjunto de rasgos formado por los
	// rasgos de dicha cadena.
	public static ConjuntoRasgos parseaConjuntoRasgos(String conjunto) {
		ConjuntoRasgos resultado = new ConjuntoRasgos();
		// Primero elminamos de la cadena los paréntesis exteriores,
		String listaRasgos = conjunto.substring(1, conjunto.length() - 1);
		// creamos una cadena que será el par "nombreRasgo:valorRasgo" en el que
		// nos encontramos.
		String rasgoString = "";
		// Es necesario saber si nos encontramos en el nivel 0 o en otro nivel.
		// El nivel cero es el más exterior, cuando nos encontremos con un
		// paréntesis abierto entraremos en un nivel inferior ya que lo que esté
		// dentro del paréntesis será el conjunto de rasgos correspondiente al
		// valor del rasgo que estemos analizando.
		int nivel = 0;
		// Recorremos la cadena caracter a carácter:
		for (char c : listaRasgos.toCharArray()) {
			// si nos encontramos con una coma y estamos en el nivel 0 significa
			// que acabamos de pasar a otro rasgo
			if (c == ',' && nivel == 0) {
				// entonces parseamos el par "nombreRasgo:valorRasgo", creamos
				// el rasgo y
				Rasgo<?> rasgo = parseaRasgo(rasgoString);
				// si no es nulo (los rasgos cuyo valor sean () se consideran
				// inexistentes)
				if (rasgo != null) {
					// añadimos el rasgo al conjunto de rasgos que vamos a
					// devolver
					resultado.getRasgos().add(rasgo);
				}
				// y ponemos a vacío la cadena.
				rasgoString = "";
				// Si nos encontramos con un paréntesis abierto
			} else if (c == '(') {
				// aumentamos el nivel
				nivel++;
				// añadimos el caracter a la cadena que representa el rasgo
				// actual.
				rasgoString += c;
				// Si nos encontramos con un paréntesis cerrado
			} else if (c == ')') {
				// disminuimos el nivel
				nivel--;
				// añadimos el caracter a la cadena que representa el rasgo
				// actual.
				rasgoString += c;
				// Si es cualquier otro carácter
			} else {
				// lo añadimos a la cadena y listo.
				rasgoString += c;
			}
		}
		// Después de recorrer toda la cadena no nos encontramos con ninguna
		// coma ni ningún paréntesis así que tenemos que parsear el rasgo del
		// final "manualmente". Si la lista de rasgos no es vacía
		if (!listaRasgos.equals("")) {
			// creamos el rasgo parseando lo que tengamos en rasgoString
			Rasgo<?> rasgo = parseaRasgo(rasgoString);
			// y si no es nulo
			if (rasgo != null) {
				// lo añadimos al conjunto de rasgos de salida.
				resultado.getRasgos().add(rasgo);
			}
		}
		// Devolvemos el conjunto de rasgos
		return resultado;
	}

	// Metodo que dado una cadena de la forma nombreRasgos:valorRasgos devuelve
	// el rasgo asociado a dicha cadena.
	// Debido a la estructura que hemos creado para los rasgos, con un tipo
	// genérico, para evitar todos los warning del compilador sería necesario
	// esar constantemente comprobando que la estructura es de un tipo u otro
	// concreto, para evitar estos warning, suprimimos los warning que se
	// producen por la no comprobación de tipos.
	@SuppressWarnings("unchecked")
	public static Rasgo<?> parseaRasgo(String rasgoString) {
		Rasgo<?> rasgo;
		// Primero dividimos la cadena de entrada en dos partes, diviendola por
		// el primer carácter : que encuentre
		String[] elementos = rasgoString.split(":", 2);
		// esto nos asegura que en el primer elemento estará el nombre del
		// rasgos
		String key = elementos[0];
		// y en el segundo elemento el valor del rasgo.
		String value = elementos[1];
		// Si el valor está vacío
		if (value.equals("()")) {
			// devolvemos un nulo.
			return null;
			// Si el primer carácter del valor es un paréntesis abierto,
			// significa que estamos ante un Rasgo que tiene por valor un
			// ConjuntoRasgos.
		} else if (value.charAt(0) == '(') {
			// Creamos el rasgo
			rasgo = new Rasgo<ConjuntoRasgos>();
			// y parseamos el conjunto de rasgos que tiene por valor.
			ConjuntoRasgos cr = parseaConjuntoRasgos(value);
			// si el valor devuelto está vació
			if (cr.getRasgos().isEmpty()) {
				// devolvemos un nulo,
				return null;
			}
			// si no está vacío, añadimos el conjunto de rasgo al rasgo.
			((Rasgo<ConjuntoRasgos>) rasgo).setValue(cr);
			// En caso de que el primer caracter no sea el paréntisis abierto
			// estamos ante un valor de tipo cadena
		} else {
			// creamos el rasgo
			rasgo = new Rasgo<String>();
			// y añadimos la caena al valor del rasgo.
			((Rasgo<String>) rasgo).setValue(value);
		}
		// Por último y sea cual sea el caso colocamos el nombre del rango en su
		// sitio
		rasgo.setKey(key);
		// y lo devolvemos.
		return rasgo;
	}

	// Como se puede observar, estos dos métodos forman una recursividad doble,
	// de modo que uno llama al otro y viceversa para poder analizar por
	// completo la cadena que contiene al conjunto de rasgos de un ECR, es un
	// sistema complejo y, seguramente, más optimizable, pero dada la
	// representación concreta que teniamos de los conjuntos de rasgos, no
	// pareció la mejor solución.

}
