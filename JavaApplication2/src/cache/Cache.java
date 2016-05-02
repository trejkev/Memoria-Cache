/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache;


import java.io.*;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;


public class Cache {
    private static String kine;
    private static Object line;

    
    public static void main(String[] args)  throws IOException {
        int tamcache =0;
        int asociatividad; //mapeo directo es la opción default
        int tambloq = 0; //bloque nulo es la opción default
        long hitrate = 0;
        long missrate = 0;
        
        
        Scanner teclado = new Scanner(System.in);
        
        System.out.println("Hola, bienvenido a la plataforma de implementación de memoria Cache.");
        System.out.println("A continuación se le han de solicitar ciertos datos necesarios para implementar la memoria.");
        System.out.println("Tome en cuenta que solo se solicitan números, por favor no ingrese letras.\n");
        
        System.out.println("Ingrese el tamaño de la memoria Cache.\n");
        System.out.println("NOTA: \n     Tome en cuenta que este valor debe estar en bytes.");
        System.out.println("     Tome en cuenta que un solo dato ocupa al menos 4 bytes.\n");
        tamcache = teclado.nextInt();
        System.out.println("\n \n");
        
        while(tamcache <= 3){
            System.out.println("El tamaño de la memoria no es válido.\n \n");
            System.out.println("Ingrese el tamaño de la memoria Cache.\n");
            System.out.println("NOTA: \n     Tome en cuenta que este valor debe estar en bytes.");
            System.out.println("     Tome en cuenta que un solo dato ocupa al menos 4 bytes.\n");
            tamcache = teclado.nextInt();
            System.out.println("\n \n");
        }
        
        System.out.println("Ingrese el tipo de asociatividad.\nSi desea mapeo directo digite 1.\n");
        asociatividad = teclado.nextInt();
        System.out.println("\n \n");
        
        while(asociatividad == 0){
            System.out.println("El tipo de asociatividad no es válido.\n \n");
            System.out.println("Ingrese el tipo de asociatividad.\nSi desea mapeo directo digite 1.\n");
            asociatividad = teclado.nextInt();
            System.out.println("\n \n");
        }
        
        if(asociatividad == 1){
            tambloq = 4;
            System.out.println("Al ser mapeo directo el tamaño del bloque se fija automáticamente en 4 bytes. \n \n");
        }else{
        
            System.out.println("Ingrese el tamaño del bloque.\n");
            System.out.println("NOTA: \n     Tome en cuenta que este valor debe estar en bytes.");
            System.out.println("Interprete que cada bloque es de 32 bits UNICAMENTE.");
            System.out.println("     Tome en cuenta que un solo dato ocupa al menos 4 bytes.\n");
            tambloq = teclado.nextInt();
            System.out.println("\n \n");
        
            while(tambloq <= 3){
                System.out.println("El tamaño del bloque no es válido.\n \n");
                System.out.println("Ingrese el tamaño del bloque.\n");
                System.out.println("NOTA: \n     Tome en cuenta que este valor debe estar en bytes.");
                System.out.println("     Tome en cuenta que un solo dato ocupa al menos 4 bytes.\n");
                System.out.println("Considere que la cantidad de datos que quepan en el bloque debe ser 2*par para obtener un byte offset entero.\n");
                tambloq = teclado.nextInt();
                System.out.println("\n \n");
            }
        }
        
        long byteoffset;
        long set;
        long index;
        long tag;
        double a = Math.log(tambloq/4) / Math.log(2); //byteoffset=logaritmo en base 2 de la cantidad de posiciones del bloque
        byteoffset = (long)a;
        System.out.println("El byte offset es: " + byteoffset);
        double b = Math.log(asociatividad) / Math.log(2);
        set = (long)b;
        System.out.println("La cantidad de bits para el set es: " + set);
        double c = Math.log(tamcache/tambloq) / Math.log(2);
        index = (long)c;
        System.out.println("La cantidad de bits para el index es: " + index);
        tag = 31-byteoffset-set-index;
        System.out.println("La cantidad de bits para el tag es: " + tag + "\n \n \n");
        
        //Hasta acá ya se tienen todos los datos necesarios para el diseño de la memoria cache
        
        
        //Declaración de caches según sea mapeo directo (cachemd) o N-way associative (cacheasoc)
        
        String[][] cachemd = new String[1][tamcache/tambloq+1];
        String[][] cacheasoc = new String[tambloq/4+1][(tamcache/tambloq)+1];
          
        
        //Lectura de la línea y separación de información
        
        /*BufferedReader in;
        in = new BufferedReader(new FileReader("C:/prueba.trace"));
        line = in.readLine(); //line contiene cada linea de datos*/
        try(FileReader fr = new FileReader("C:/aligned.trace")){
            BufferedReader in = new BufferedReader(fr);
        
            while((line = in.readLine()) != null) {
                //System.out.println(line);
                //System.out.println("La línea sin manipular es: " + line);
                String linea = null; //reinicio linea
                linea = in.readLine();
                StringTokenizer tokens = new StringTokenizer(linea, "    ");
                String direccion = tokens.nextToken().trim();
                Long k = Long.parseLong(direccion, 16); //Convierte el string direccion en long k
                String direcbin = Long.toBinaryString(k); //Convierte el long k en string binario
                String accion = tokens.nextToken().trim();
                //System.out.println("La dirección es: " + direccion); 
                //System.out.println("La dirección en binario es: " + direcbin);
                //System.out.println("La accion es: " + accion); 
                
            
                //Acá empiezan los cálculos lógicos para el almacenamiento de los datos
            
                //variables auxiliares para manipular el string
            
                int boint = (int)byteoffset; 
                int setint = (int)set;
                int indexint = (int)index;
                int length = direcbin.length();
            
            
                //Construcción de los datos necesarios para el correcto almacenamiento de datos
            
                String bodato = direcbin.substring(length - boint,length);
                //System.out.println("El byte offset de éste dato es: " + bodato);
                String setdato = direcbin.substring(length - boint - setint, length - boint);
                //System.out.println("El set de éste dato es: " + setdato);
                String indexdato = direcbin.substring(length - boint - setint - indexint , length - boint - setint);
                //System.out.println("El index de éste dato es: " + indexdato);
                String tagdato = direcbin.substring(0,length - boint - setint - indexint);
                //System.out.println("El tag de éste dato es: " + tagdato);
                
                    
                //inicia algoritmo para pasar index a decimal
                long indexbinario = Long.parseLong(indexdato.trim());
                long count = 1;
                long auxdecimal;
                long indexdecimal=0;
                while(indexbinario > 0) { 
                    auxdecimal = indexbinario %2;
                    indexdecimal = indexdecimal + auxdecimal*count;
                    indexbinario /= 10;
                    count = count*2;
                }
                //System.out.println("El index en decimal es: " + indexdecimal);
                int indexdecima = (int)indexdecimal;
                //Termina algoritmo para pasar index a decimal


                //Empieza algoritmo para leer y escribir MAPEO DIRECTO
                if(asociatividad == 1){
                    String F;
                    F = cachemd[0][indexdecima];
                    if(accion.equals("S")){ //escritura
                        if(tagdato.equals(F)){
                            //System.out.println("Se tiene un hit.");
                            hitrate++;
                            cachemd[0][indexdecima]=tagdato; //escribe en memoria cache
                        }else {
                            //System.out.println("Se tiene un miss.");
                            missrate++;
                            cachemd[0][indexdecima]=tagdato;//crea la posición en memoria cache y escribe
                        }
                    
                    //Termina escritura y comienza lectura en mapeo directo    
                    }else{   
                        if(tagdato.equals(F)){
                            //System.out.println("Se tiene un hit.");
                            hitrate++;
                        }else {
                            //System.out.println("Se tiene un miss.");
                            missrate++;
                            cachemd[0][indexdecima]=tagdato; //Se escribe simulando que se trae el dato de memorias posteriores
                        }
                    }
                }
                //Fin de mapeo directo e inicio de N-way Associative
            
                if(asociatividad > 1){
                        
                    //inicia algoritmo para pasar set a decimal
                    long setbinario = Long.parseLong(setdato.trim());
                    long counte = 1;
                    long auxdecimalset;
                    long setdecimal=0;
                    while(indexbinario > 0) { 
                        auxdecimalset = setbinario %2;
                        setdecimal = setdecimal + auxdecimalset*counte;
                        indexbinario /= 10;
                        count = count*2;
                    }
                    //System.out.println("El set en decimal es: " + setdecimal);
                    int setdecima = (int)setdecimal;
                    //termina algoritmo para pasar set a decimal
                        
                    //Variables auxiliares

                    int p = tambloq/4;
                    int i = 0;
                    int q = 0;
                    int aux = 0;
                    int aux1 = 0;
                        
                    //Algoritmo para escritura
                
                    if(accion.equals("S")){ 
                        while(q <= p){ //q menor o igual que máxima fila
                            String F = cacheasoc[q][setdecima];
                            if(tagdato.equals(F)){
                                //System.out.println("Se tiene un hit.");
                                hitrate++;
                                aux = q; 
                                q = p + 1;
                            }else {
                                q = q + 1 ;
                            }
                        }
                        String W = cacheasoc[aux][setdecima];
                        if(tagdato.equals(W)){
                            //System.out.println("");
                            cacheasoc[aux][setdecima] = tagdato; //escribe justo sobre la posición en que se tuvo el hit
                        }else {
                            //System.out.println("Se tiene un miss.");
                            missrate = missrate + 1;
                            Random ran = new Random();
                            int bloqaguard;
                            bloqaguard = ran.nextInt(1) + (tambloq/4);
                            cacheasoc[bloqaguard][setdecima] = tagdato; //se guarda en una posición aleatoria
                        }
                    }
                    //Termina algoritmo para escribir y empieza algoritmo para lectura
                    
                    if(accion.equals("L")){
                        while(i <= p){ //while de búsqueda
                            String Y = cacheasoc[i][setdecima];
                            if(tagdato.equals(Y)){
                                //System.out.println("Se tiene un hit.");
                                hitrate = hitrate + 1;
                                aux1 = i;
                                i = p + 1;
                            }else {
                                i = i+1;
                            }
                        }
                        String Z = cacheasoc[aux1][setdecima];
                        if(tagdato.equals(Z)){
                            //System.out.println("");
                        }else {
                            //System.out.println("Se tiene un miss.");
                            missrate = missrate + 1;   
                            Random ran = new Random();
                            int bloqaguard;
                            bloqaguard = ran.nextInt(1) + (tambloq/4);
                            cacheasoc[bloqaguard][setdecima] = tagdato; //se trae el dato y se guarda en una posición aleatoria
                        }
                    }
                }        
            }
            System.out.println("El hitrate es: " + 100*hitrate/(hitrate+missrate) + ", el missrate es: " + 100*missrate/(hitrate+missrate) + "\n\n\n");
        }
    }                   
}