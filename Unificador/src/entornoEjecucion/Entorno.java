package entornoEjecucion;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parser.Parser;
import unificador.Unificador;
import estructurasDeDatos.ConjuntoRasgos;
import estructurasDeDatos.ECR;
import estructurasDeDatos.Par;

public class Entorno {

	private static boolean permitirComandos = false;
	private static String direccionECR;
	private static String direccionUNF;
	private static String direccionO;
	private static Map<String, Set<String>> diccionarioECR;

	// private static List<Par<String, String>> listaParesUnificar;

	public static void main(String[] args) {
		// Hay dos fomas de usar el programa:
		// 1) Pasándole como argumento únciamente el diccionario
		// de ECR, con lo cual se introducirán las instrucciones
		// de unificación por consola y se mostrará el resultado
		// por la salida estándar, o
		// 2) Pasándole el diccionario, el archivo con las
		// instrucciones de unficación y el archivo de salida.
		// Si se especifica la opcion -c entonces se permitirá la
		// entrada de instrucciones por consola al finalizar las
		// instrucciones del fichero.

		// Lectura de los parámetros
		try {
			// Recorremos la lista de parámetros
			for (int i = 0; i < args.length; i++) {
				// Si nos encontramos con un parámetros "-ecr" significa que
				// detrás viene la dirección del archivo del diccionario
				if ("-ecr".equals(args[i])) {
					// guardamos la dirección del diccionario
					direccionECR = args[i + 1];
					// e incrementamos el índice para pasar al siguiente
					// comando.
					i++;
					// Actuamos de forma similar para el archivo unf
				} else if ("-unf".equals(args[i])) {
					direccionUNF = args[i + 1];
					i++;
					// y para el archivo de salida.
				} else if ("-o".equals(args[i])) {
					direccionO = args[i + 1];
					i++;
					// En caso de que encontremos un "-c" significa que queremos
					// introducir comandos
				} else if ("-c".equals(args[i])) {
					// activamos esa opción.
					permitirComandos = true;
					// si nos encontramos con cualquier otra situación,
				} else {
					// informámos del uso del programa
					System.out
							.println("Uso: -ecr <diccionario de ecr> [-unf <archivo de instrucciones> -o <archivo de salida> [-c]]");
					return;
				}
			}
			// Igualmente si ocurre una excepción no capturada durante la
			// lectura de parámetros
		} catch (RuntimeException e) {
			// informámos del uso del programa.
			System.out
					.println("Uso: -ecr <diccionario de ecr> [-unf <archivo de instrucciones> -o <archivo de salida> [-c]]");
			return;
		}

		// Comprobamos que todos los parámetros no sean incorrectos, lo que
		// puede suceder si no se especifica archivo ecr, o si no se especifican
		// alguna de las otras dos direcciones y la otra sí.
		if (direccionECR == null
				|| (direccionUNF != null && direccionO == null)
				|| (direccionUNF == null && direccionO != null)) {
			// En caso de que ocurra, informámos del error
			System.out
					.println("Uso: -ecr <diccionario de ecr> [-unf <archivo de instrucciones> -o <archivo de salida> [-c]]");
			return;
		}
		// La ejecución del programa se divide en dos. Si hemos especificado un
		// archivo UNF
		if (direccionUNF != null) {
			// nos vamos por aquí
			trataArchivoUNF();
			// y si no
		} else {
			// creamos el diccionario
			diccionarioECR = new HashMap<String, Set<String>>();
			// y esperamos a que nos llegue un comando
			esperarComando();
		}
	}

	// Este método es el encargado de la ejecución en el caso de que se haya
	// definido un archivo de instrucciones
	public static void trataArchivoUNF() {
		// Para hacer las mediciones de tiempo, calculamos el tiempo actual en
		// milisegundos
		long tiempoInicio = System.currentTimeMillis();
		// Informámos de lo que va a realizarse
		System.out.println("Tratando archivos ECR y UNF.");
		System.out.println("Comenzando a parsear el archivo UNF.");
		// Se parsea el archivo .unf,
		Par<List<Par<String, String>>, Set<String>> parseoUNF = Parser
				.parseaArchivoUNF(direccionUNF);
		// informamos
		System.out.println("Parseado el archivo UNF.");
		System.out.println("Comenzando a parsear el archivo ECR.");
		// y se parsea el archivo ecr, se buscan únicamente los ecr que nos
		// interesan
		diccionarioECR = Parser.parseaArchivoECR(direccionECR,
				parseoUNF.getSecond());
		// y se informa.
		System.out.println("Parseado el archivo ECR.");
		// Ahora se va a proceder a realizar la unificación.
		List<Par<String, String>> paresAUnificar = parseoUNF.getFirst();
		System.out.println("Abriendo el fichero de escritura.");
		try {
			// Creamos los objetos encargados de la escritura en el fichero
			FileWriter fichero = new FileWriter(direccionO);
			PrintWriter trace = new PrintWriter(fichero);
			// e informamos.
			System.out.println("Comenzando a unificar estructuras.");
			// por cada uno de los pares que tenemos que unificar:
			for (int i = 0; i < paresAUnificar.size(); i++) {
				// obtenemos el par
				Par<String, String> par = paresAUnificar.get(i);
				// creamos dos set de conjuntos de rasgos (que asociaremos al
				// ECR posteriormente
				HashSet<ConjuntoRasgos> cra = new HashSet<ConjuntoRasgos>();
				HashSet<ConjuntoRasgos> crb = new HashSet<ConjuntoRasgos>();
				// por cada cadena que haya en el diccionario que tenga como
				// etiqueta la primera de las dos que estamos unificando
				for (String s : diccionarioECR.get(par.getFirst())) {
					// parseamos la cadena para crear el conjunto de rasgos
					ConjuntoRasgos cr = Parser.parseaConjuntoRasgos(s);
					// y añadimos dicho conjunto al set que creamos antes.
					cra.add(cr);
				}
				// Procedemos de la misma forma para el segundo ecr.
				for (String s : diccionarioECR.get(par.getSecond())) {
					ConjuntoRasgos cr = Parser.parseaConjuntoRasgos(s);
					crb.add(cr);
				}
				// Creamos el ecr
				ECR a = new ECR();
				// le colocamos la etiqueta y el set de conjunto de rasgos.
				a.setEtiqueta(par.getFirst());
				a.setConjuntosRasgos(cra);
				// Igual para el otro ecr.
				ECR b = new ECR();
				b.setEtiqueta(par.getSecond());
				b.setConjuntosRasgos(crb);
				// Por último, hacemos la unificacón y la escribimos en el
				// fichero de salida.
				String resultado = Unificador.resultadoUnificarECR(a, b);
				trace.println(resultado);
				// Informamos de que se ha terminado la unificacion y de cuantas
				// quedan por hacer
				System.out.println("Unificada estructura: " + (i + 1) + "/"
						+ paresAUnificar.size());
			}
			// Si hubiera algo en el buffer de escritura, lo escribimos.
			trace.flush();
			// Informamos del final del algoritmo
			System.out
					.println("Todas las estructuras unificadas y escritas en el fichero de salida.");
			// En caso de que haya algún problema
		} catch (IOException e) {
			// informamos.
			System.out.println("Problema abriendo el fichero de salida");
			return;
		}
		// actualizamos la medida de tiempo
		long tiempoFinal = System.currentTimeMillis() - tiempoInicio;
		// e imprimimo lo que hemos tardado
		System.out.println("Tiempo consumido: " + tiempoFinal);
		// Si se ha especificado la introducción de comandos, después de hacer
		// todo esto
		if (permitirComandos) {
			// nos vamos a esperar comandos.
			esperarComando();
		}

	}

	// Este método es el encargado de leer los comando que introduzcamos por
	// pantalla y de realizar las acciones pertinentes.
	public static void esperarComando() {
		// Creamos los objetos de los que vamos a leer los comandos
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		// Creamos la expresión regular que verifica la correción de un comando.
		Pattern p = Pattern
				.compile("^\\s*[$]Unifica\\s*[(]\\s*([a-zA-Z_0-9]+)\\s*&\\s*([a-zA-Z_0-9]+)\\s*[)]\\s*$");
		// También creamos el string donde se guardará el comando actual.
		String comando = "";
		try {
			// Mientras el comando no sea 'quit'
			while (!"quit".equals(comando)) {
				// Informamos de que estamos esperando un comando y de que
				// 'quit' sirve para terminar el programa
				System.out.print("Introduzca comando ('quit' para terminar): ");
				// leemos la línea introducida,
				comando = in.readLine();
				// informamos de que hemos leído la línea
				System.out.println("Interpretando comando :" + comando);
				// Si el comando tiene la forma '$Unifica(etiqueta & etiqueta)'
				// continuamos
				Matcher m = p.matcher(comando);
				if (m.matches()) {
					// Obtenemos las dos etiquetas
					String a = m.group(1);
					String b = m.group(2);
					// intentamos obtener las etiquetas del diccionario
					Set<String> seta = diccionarioECR.get(a);
					Set<String> setb = diccionarioECR.get(b);
					// creamos las dos estructuras complejas de rasgos
					ECR ecra = new ECR();
					ECR ecrb = new ECR();
					// Informamos de que hemos reconocido el comando y cuales
					// son las estructuras que vamos a unificar
					System.out
							.println("Comando reconocido. Unificar estructuras '"
									+ a + "' y '" + b + "'");
					System.out.println("Buscando ECR con dichas etiquetas.");
					// Es muy posible que las estructuras no se encuentre en el
					// diccionario, así que tendremos que buscarlas, para ello
					// creamos un set
					Set<String> ecrBuscar = new HashSet<String>();
					// si la variable "seta" está a null significa que no se
					// encontró el ECR en el diccionario, así que
					if (seta == null) {
						// lo añadimos para buscar
						ecrBuscar.add(a);
					}
					// y hacemos lo mismo con la otra etiqueta
					if (setb == null) {
						ecrBuscar.add(b);
					}
					boolean encontrados = true;
					// en caso de que tengamos que buscar algo
					if (!ecrBuscar.isEmpty()) {
						// llamamos al parser con la dirección del diccionario y
						// el conjunto de elementos que queremos encontrar
						Map<String, Set<String>> dicc = Parser
								.parseaArchivoECR(direccionECR, ecrBuscar);
						// si el primer ecr era uno de los que teniamos que
						// buscar
						if (seta == null) {
							// vemos si está en el diccionario devuelto por el
							// parse
							seta = dicc.get(a);
							// si no es así
							if (seta == null) {
								// informamos de que ese rasgo no existe en el
								// diccionario y abortamos
								System.out
										.println("No se ha encontrado el rasgo '"
												+ a
												+ "' en el diccionario. Abortando comando.");
								encontrados = false;
								// si sí que existe
							} else {
								// lo añadimos al diccionario por si en el
								// futuro fueramos a usar esa ecr en otro
								// comando
								diccionarioECR.put(a, seta);
							}
						}
						// procedemos de forma idéntica en el caso del segundo
						// ecr y solamente si encontramos el primero.
						if (setb == null && encontrados) {
							setb = dicc.get(b);
							if (setb == null) {
								System.out
										.println("No se ha encontrado el rasgo '"
												+ b
												+ "' en el diccionario. Abortando comando.");
								encontrados = false;
							} else {
								diccionarioECR.put(b, setb);
							}
						}
					}
					// Si hemos encontrado ambos ecr
					if (encontrados) {
						// informamos.
						System.out
								.println("Estructuras encontradas. Procediendo a la unificación.");
						// Creamos los set de conjunto de rasgos que tendrán los
						// ecr
						HashSet<ConjuntoRasgos> cra = new HashSet<ConjuntoRasgos>();
						HashSet<ConjuntoRasgos> crb = new HashSet<ConjuntoRasgos>();
						// Por cada elemento en el set de cadenas
						for (String s : seta) {
							// lo parseamos y
							ConjuntoRasgos cr = Parser.parseaConjuntoRasgos(s);
							// lo añadimos al set de conjunto de rasgos
							cra.add(cr);
						}
						// Procedemos de la misma forma para el segundo elemento
						for (String s : setb) {
							ConjuntoRasgos cr = Parser.parseaConjuntoRasgos(s);
							crb.add(cr);
						}
						// asociamos las etiquetas y los set de conjuntos de
						// rasgos con los ecrs
						ecra.setEtiqueta(a);
						ecra.setConjuntosRasgos(cra);
						ecrb.setEtiqueta(b);
						ecrb.setConjuntosRasgos(crb);
						// Finalmente, imprimimos por pantalla (y NO en el
						// archivo, aunque lo hubiere) el resultado
						System.out.println(Unificador.resultadoUnificarECR(
								ecra, ecrb));
						// e informamos de que se ha completado la unificación
						System.out.println("Unificación finalizada.");
					}
					// si el comando no coincide con la expresión regular pero
					// es quit
				} else if ("quit".equals(comando)) {
					// informamos.
					System.out.println("Saliendo.");
					// si es cualquier otra cosa
				} else {
					// informamos.
					System.out
							.println("Comando no reconocido. Comandos válidos: $Unifica(etiqueta1 & etiqueta2)");
				}
			}
			// si se produjera una excepción
		} catch (IOException e) {
			// informamos del error y terminamos
			System.out.println("Error no reconocido");
		}
	}
}
