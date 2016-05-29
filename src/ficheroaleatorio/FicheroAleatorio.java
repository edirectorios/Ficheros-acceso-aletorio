/*
@author Antonio Florido
Programa para guardar en fichero aleatorio los valores leidos de base de datos
 
 */
package ficheroaleatorio;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Antonio Florido
 */
public class FicheroAleatorio {

    /**
     * @param args the command line arguments
     */
    
    protected Statement sentencia;
    protected Connection conexion;
    
    // Para unificar ambas subtareas creamos el menu que engloba todas las opciones
    public static void main(String[] args) {        
       
        // instanciamos la clase y llamamos al metodo menu de la misma
        // creacion del objeto menu
        FicheroAleatorio obj = new FicheroAleatorio();
        obj.menu();
            
    }    
    
    
    /**
     * Creamos la conexion con la base de datos y extraemos los registros y 
     * mostramos por pantalla el contneido del fichero que se trae de BBDD
     */
    private void CreaConexion(){
        // establecemos la conexion con la BBDD
        try {
            // conexion a mysql
            Class.forName("com.mysql.jdbc.Driver");
            FicheroAleatorio obj = new FicheroAleatorio();
            
            obj.conexion = DriverManager.getConnection("jdbc:mysql://localhost/equipo","root","33657433");
            
            //Preparamos el query
            obj.sentencia = obj.conexion.createStatement();
            ResultSet result    = obj.sentencia.executeQuery("select * from jugadores");
            
            
            obj.EscribirResultados(result);            
            
            System.out.println("<<< ---------Contenido fichero ------ >>>>");
                        
            obj.LeerFcihero();
            
            
        }catch(ClassNotFoundException cn) { cn.printStackTrace();}
        catch(SQLException e) { e.printStackTrace();}
        catch(FileNotFoundException e ){ e.printStackTrace();}     
        catch(IOException e ){ e.printStackTrace();}     
        
    }
    
    /**
     * Escribe resultados a partir de un resultSet de BBDD
     * @param res
     * @throws FileNotFoundException
     * @throws SQLException
     * @throws IOException 
     */
    private void EscribirResultados (ResultSet res) throws FileNotFoundException,SQLException,IOException {
        
        File fichero = new File("listado_jugadores.dat");
        RandomAccessFile randFile = new RandomAccessFile(fichero,"rw");
        StringBuffer buffer = null;
        
        while(res.next()){
                System.out.println("Numero Jugador: "+res.getInt(1)+" Nombre: "+res.getString(2)+" Num.Partidos: "+res.getInt(3)+" Edad: "+res.getInt(4)+" Equipo: "+res.getString(5));
                
                randFile.writeInt(res.getInt(1));                
                
                buffer = new StringBuffer(res.getString(2));
                buffer.setLength(6);
                randFile.writeChars(buffer.toString());
                
                randFile.writeInt(res.getInt(3));
                randFile.writeInt(res.getInt(4));
                
                
                buffer = new StringBuffer(res.getString(5));
                buffer.setLength(20);
                randFile.writeChars(buffer.toString());
                
                randFile.writeChars("S");
                
        }
            
            randFile.close();
            res.close();
            sentencia.close();
            conexion.close();
    }
    
    
    /**
     * Lee los registro del fichero aleatorio existente
     * @throws FileNotFoundException 
     */
    private void LeerFcihero() throws FileNotFoundException{
        File fichero = new File("listado_jugadores.dat");
        RandomAccessFile randFile = new RandomAccessFile(fichero,"r");
        
        try {
            
            int pos = 0;
            char nombre[] = new char[20];
            char equipo[] = new char[20];
            while(true){
               randFile.seek(pos);
               int id = randFile.readInt();
               
               
               for(int i = 0; i  < 6; i++){ 
                nombre[i] = randFile.readChar(); 
               } 
            
              
               int partidos = randFile.readInt();
               int edad = randFile.readInt();
               
               for(int i = 0; i  < 20; i++){ 
                equipo[i] = randFile.readChar(); 
               }
               
               char activo[] = new char[1];
               for(int i = 0; i  < 1; i++){ 
                activo[i] = randFile.readChar(); 
               }
               
               pos = pos + 66;
               
               System.out.println("Numero Jugador: "+id+" Nombre: "+new String(nombre)+" Num.Partidos: "+partidos+" Edad: "+edad+" Equipo: "+new String(equipo)+" activo: "+new String(activo));
               if(randFile.getFilePointer() == randFile.length()) { break;}
               
            }
            
            randFile.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    private void menu(){
         int opcion = 0;
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        
        
        // mostramos el menu de acciones
        do{
            System.out.println("1. Generar Fichero desde BBDD");            
            System.out.println("2. Alta jugador");
            System.out.println("3. Baja jugador");            
            System.out.println("4. Leer Fichero");
            System.out.println("5. Consulta jugador");
            System.out.println("0. Salir");
            
            
            try {
                System.out.println("Escoge opcion : ");
                opcion = Integer.parseInt(buff.readLine());
                
                switch(opcion){
                  case 1 : (new FicheroAleatorio()).CreaConexion();break;
                  case 2 :  // creamos la clase jugador para guardar los datos el mismo                            
                            Jugador jugador = new Jugador(); 
                            
                            // pedimos y guardamos los datos del mismo
                            System.out.println("Introduce numero jugador");
                            BufferedReader cons = new BufferedReader(new InputStreamReader(System.in));
                            jugador.Numero(Integer.parseInt(cons.readLine()));
                            
                           (new FicheroAleatorio()).alta(jugador); 
                           break;
                      
                  case 3 : System.out.println("Introduce numero Jugador");
                           BufferedReader cons2 = new BufferedReader(new InputStreamReader(System.in));
                           (new FicheroAleatorio()).baja(Integer.parseInt(cons2.readLine())); break;
                      
                  case 4 : (new FicheroAleatorio()).LeerFcihero();break;
                      
                  case 5 : System.out.println("Introduce numero Jugador");
                           BufferedReader cons5 = new BufferedReader(new InputStreamReader(System.in));
                           (new FicheroAleatorio()).consulta(Integer.parseInt(cons5.readLine()));break;    
                  
                }
                
            }catch (Exception e){
                System.out.println("Error : "+e.getMessage());
            }
        }while (opcion != 0);
    }
    
    
    /**
     * 
     * @param numeroLocalizador
     * @return true o false dependiendo de si existe o no el numero de jugador
     * @throws FileNotFoundException 
     */
    private boolean LocalizarJugador(int numeroLocalizador) throws FileNotFoundException{
        File fichero = new File("listado_jugadores.dat");
        RandomAccessFile randFile = new RandomAccessFile(fichero,"r");
        
        try {
            
            int pos = 0;
            char nombre[] = new char[20];
            char equipo[] = new char[20];
            while(true){
               randFile.seek(pos);
               int id = randFile.readInt();
               
               
               for(int i = 0; i  < 6; i++){ 
                nombre[i] = randFile.readChar(); 
               } 
            
              
               int partidos = randFile.readInt();
               int edad = randFile.readInt();
               
               for(int i = 0; i  < 20; i++){ 
                equipo[i] = randFile.readChar(); 
               }
               
               char activo[] = new char[1];
               for(int i = 0; i  < 1; i++){ 
                activo[i] = randFile.readChar(); 
               }
               
               //4+12+4+4+40 = 92
               pos = pos + 66;
               
               if(numeroLocalizador == id) { return true; }
               
               if(randFile.getFilePointer() == randFile.length()) { break;}
            
            }
            
            randFile.close();            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return false;
    }
    
    
    private int getUltimoID() throws FileNotFoundException{
        File fichero = new File("listado_jugadores.dat");
        RandomAccessFile randFile = new RandomAccessFile(fichero,"r");
        int id = 0;
        try {
            
            int pos = 0;
            char nombre[] = new char[20];
            char equipo[] = new char[20];
            while(true){
               randFile.seek(pos);
               id = randFile.readInt();
               
               for(int i = 0; i  < 6; i++){ 
                nombre[i] = randFile.readChar(); 
               } 
            
               int partidos = randFile.readInt();
               int edad = randFile.readInt();
               
               for(int i = 0; i  < 20; i++){ 
                equipo[i] = randFile.readChar(); 
               }
               
               char activo[] = new char[1];
               for(int i = 0; i  < 1; i++){ 
                activo[i] = randFile.readChar(); 
               }
               
               //4+12+4+4+40 = 92
               pos = pos + 66;
               
               if(randFile.getFilePointer() == randFile.length()) { break;}            
            }
            
            randFile.close();            
            
        }catch(Exception e){
            e.printStackTrace();
        }   
        
        return id;
    }
    
    
    /**
     * Procesa el alta de un jugador verificando que no existe e insertando 
     * n registros segun el identificador que se le ponga al jugador
     * 
     * @param jugador
     * @throws IOException 
     */
    private void alta(Jugador jugador) throws IOException,SQLException{
        int lastID = 0;
        // si no localizamos el jugador seguimos con el alta
        if(!LocalizarJugador(jugador.getNumero())){
            
            lastID = getUltimoID();
            
            if(lastID < jugador.getNumero()){
                System.out.println("lastID: "+lastID);

                int diff = jugador.getNumero() - lastID;
                System.out.println("Registros a insertar: "+diff);

                // insertamos 'n' registros para seguir la secuencia
                int actualID = lastID +1;
                int jugadorActual = jugador.getNumero();
                while(jugadorActual != actualID){
                    //System.out.println(jugador.getNumero()+" != "+actualID);
                    if(jugadorActual != actualID){
                        jugador.Numero(actualID);
                        jugador.Nombre("Vacio");
                        jugador.Partidos(0);
                        jugador.Edad(0);
                        jugador.Equipo("Vacio");                        
                        jugador.Activo("N");
                        this.GuardarJugador(jugador);
                        actualID++;
                    }                      
                }
                    
                jugador.Numero(actualID);
                BufferedReader cons = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Introduce nombre jugador");
                jugador.Nombre(cons.readLine());

                System.out.println("Introduce numero partidos");
                jugador.Partidos(Integer.parseInt(cons.readLine()));

                System.out.println("Introduce edad ");
                jugador.Edad(Integer.parseInt(cons.readLine()));

                System.out.println("Introduce equipo jugador");
                jugador.Equipo(cons.readLine());

                System.out.println("Introduce activo [s/n]");
                jugador.Activo(cons.readLine());                        
                this.GuardarJugador(jugador);
                
            }              
            else {
                System.out.println("El numero de jugador debe ser mayor que :"+lastID);
            }
            
        }else { 
            System.out.println("El numero de jugador ya existe. Debe introducir uno mayor que :"+lastID);
        }
        
        
    }
    
    /**
     * Guarda los datos unitarios para un jugador
     * 
     * @param jugador
     * @throws FileNotFoundException
     * @throws SQLException
     * @throws IOException 
     */
    private void GuardarJugador (Jugador jugador) throws FileNotFoundException,SQLException,IOException {
        
        File fichero = new File("listado_jugadores.dat");
        RandomAccessFile randFile = new RandomAccessFile(fichero,"rw");
        StringBuffer buffer = null;
              
        // calculamos la posicion final del fichero
        int pos = this.getUltimoID() * 66;
        // posicionamos el puntero al final del fichero
        randFile.seek(pos);
        
        // comenzamos con la escritura
        randFile.writeInt(jugador.getNumero());                

        buffer = new StringBuffer(jugador.getNombre());
        buffer.setLength(6);
        randFile.writeChars(buffer.toString());

        randFile.writeInt(jugador.getPartido());
        randFile.writeInt(jugador.getEdad());


        buffer = new StringBuffer(jugador.getEquipo());
        buffer.setLength(20);
        randFile.writeChars(buffer.toString());

        randFile.writeChars(jugador.getActivo());
                
        randFile.close();
    }
    
    private void baja(int numero) throws IOException{
        File fichero = new File("listado_jugadores.dat");
        RandomAccessFile randFile = new RandomAccessFile(fichero,"rw");
        StringBuffer buffer = null;
              
        // calculamos la posicion del registro
        int pos = 0;
        if(numero == 1){
         pos = 1;
        }else {
         pos = (numero -1) * 66;
        }
        
        char nombre[] = new char[6];
        char equipo[] = new char[20];
        
        // posicionamos el puntero al final del fichero
        randFile.seek(pos);
        
        try {            
            // leemos el numero del jugador
            randFile.readInt();                
            
            // leemos su nombre
            for(int i = 0; i  < 6; i++){ 
                nombre[i] = randFile.readChar(); 
            } 
            
            // leemos los partidos jugados
            randFile.readInt();
            
            // leemos la edad del jugador
            randFile.readInt();
            
            // leemos el nombre del equipo
            for(int i = 0; i  < 20; i++){ 
                equipo[i] = randFile.readChar(); 
            }
            
            // leemos su estado
            char activo = randFile.readChar();
            
            // si el estado activo actual no es 'S' y se ha solicitado una baja el jugador ya estaba de baja
            if (!String.valueOf(activo).equals("S")) { System.out.println("El jugador ya esta dado de baja"); }
            else {
              
              // si el estado activo es 'S' posicionamos de nuevo al comienzo del registro para cambiar la letra
              randFile.seek(pos-1);
              
              // leemos el id del jugador
              randFile.readInt();                
            
              // leemos el nombre
              for(int i = 0; i  < 6; i++){ 
                nombre[i] = randFile.readChar(); 
              } 

              // leemos partidos jugados
              randFile.readInt();
              
              // leemos la edad
              randFile.readInt();

              // leemos el equipo
              for(int i = 0; i  < 20; i++){ 
                equipo[i] = randFile.readChar(); 
              }
              
              // y guardamos el estado activo a N
              randFile.writeChars("N");
            }

            randFile.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    private void bajaNew(int numero) throws IOException{
        File fichero = new File("listado_jugadores.dat");
        RandomAccessFile randFile = new RandomAccessFile(fichero,"rw");
        StringBuffer buffer = null;
              
        // calculamos la posicion del registro
        int pos = 0;
        if(numero == 1){
         pos = 1;
        }else {
         pos = (numero -1) * 66;
        }
        
        char nombre[] = new char[6];
        char equipo[] = new char[20];
        
        // posicionamos el puntero al final del fichero
        randFile.seek(pos);
        
        try {            
            // leemos el numero del jugador
            randFile.readInt();                
            
            // leemos su nombre
            for(int i = 0; i  < 6; i++){ 
                nombre[i] = randFile.readChar(); 
            } 
            
            // leemos los partidos jugados
            randFile.readInt();
            
            // leemos la edad del jugador
            randFile.readInt();
            
            // leemos el nombre del equipo
            for(int i = 0; i  < 20; i++){ 
                equipo[i] = randFile.readChar(); 
            }
            
            // leemos su estado
            char activo = randFile.readChar();
            
            // si el estado activo actual no es 'S' y se ha solicitado una baja el jugador ya estaba de baja
            if (!String.valueOf(activo).equals("S")) { System.out.println("El jugador ya esta dado de baja"); }
            else {
              
              // si el estado activo es 'S' posicionamos de nuevo al comienzo del registro para cambiar la letra
              randFile.seek(pos-1);
              
              // leemos el id del jugador
              randFile.readInt();                
            
              // leemos el nombre
              for(int i = 0; i  < 6; i++){ 
                nombre[i] = randFile.readChar(); 
              } 

              // leemos partidos jugados
              randFile.readInt();
              
              // leemos la edad
              randFile.readInt();

              // leemos el equipo
              for(int i = 0; i  < 20; i++){ 
                equipo[i] = randFile.readChar(); 
              }
              
              // y guardamos el estado activo a N
              randFile.writeChars("N");
            }

            randFile.close();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    private void consulta(int numeroJugador) throws FileNotFoundException,IOException{
        
        if(!this.LocalizarJugador(numeroJugador)) { System.out.println("El jugador no existe");}
        else { 
            File fichero = new File("listado_jugadores.dat");
            RandomAccessFile randFile = new RandomAccessFile(fichero,"rw");
            StringBuffer buffer = null;

            // calculamos la posicion del registro
            int pos = 0;
            if(numeroJugador == 1){
             pos = 1;
            }else {
             pos = (numeroJugador -1) * 66;
            }

            char nombre[] = new char[6];
            char equipo[] = new char[20];

            // posicionamos el puntero al final del fichero
            randFile.seek(pos);

            // leemos el numero del jugador
            int numero = randFile.readInt();                

            // leemos su nombre
            for(int i = 0; i  < 6; i++){ 
                nombre[i] = randFile.readChar(); 
            } 

            
            
            // leemos los partidos jugados
            int partidos = randFile.readInt();

            // leemos la edad del jugador
            int edad = randFile.readInt();

            // leemos el nombre del equipo
            for(int i = 0; i  < 20; i++){ 
                equipo[i] = randFile.readChar(); 
            }
            
            

            // leemos su estado
            char activo = randFile.readChar();

            // si el estado activo actual no es 'S' y se ha solicitado una baja el jugador ya estaba de baja
            if (!String.valueOf(activo).equals("S")) { System.out.println("El jugador ya esta dado de baja"); }
            else {
               System.out.println("Numero Jugador: "+numero+" Nombre: "+new String(nombre)+" Num.Partidos: "+partidos+" Edad: "+edad+" Equipo: "+new String(equipo)+" activo: "+activo);  
            }

        }
        
    }
    
}
