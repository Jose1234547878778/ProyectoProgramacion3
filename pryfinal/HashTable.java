import java.io.*;

public class HashTable {
    private TablaHashPersonalizada tabla;
    private int tamano;
    private int metodoHash;  // 1 = cuadrados medios, 2 = pliegues

    public HashTable(int tamano, int metodoHash) {
        this.tamano = tamano;
        this.metodoHash = metodoHash;
        this.tabla = new TablaHashPersonalizada(tamano);
    }

    public int getMetodoHash() {
        return metodoHash;
    }

    private int hashBase(String clave) {
        int valor = 0;
        try {
            valor = Integer.parseInt(clave);
        } catch (NumberFormatException e) {
            valor = clave.hashCode();
        }
        valor = Math.abs(valor);

        if (metodoHash == 1) {
            return hashCuadradosMedios(valor);
        } else if (metodoHash == 2) {
            return hashPliegues(clave);
        } else {
            return valor % tamano;
        }
    }

    private int hashCuadradosMedios(int valor) {
        long cuadrado = (long) valor * valor;
        String s = String.valueOf(cuadrado);
        int longitud = s.length();
        int start = longitud / 2 - 1;
        if (start < 0) start = 0;
        int end = start + 2;
        String medio = s.substring(start, Math.min(end, longitud));
        int medioInt = Integer.parseInt(medio);
        return medioInt % tamano;
    }

    private int hashPliegues(String clave) {
        StringBuilder numeros = new StringBuilder();
        for (char c : clave.toCharArray()) {
            if (Character.isDigit(c)) {
                numeros.append(c);
            }
        }
        String numStr = numeros.toString();
        if (numStr.isEmpty()) {
            numStr = String.valueOf(Math.abs(clave.hashCode()));
        }

        int suma = 0;
        for (int i = 0; i < numStr.length(); i += 2) {
            String bloque;
            if (i + 2 <= numStr.length())
                bloque = numStr.substring(i, i + 2);
            else
                bloque = numStr.substring(i, i + 1);

            suma += Integer.parseInt(bloque);
        }
        return suma % tamano;
    }

    public boolean insertar(String clave) {
        if (buscar(clave) != null) {
            return false;
        }

        int hash = hashBase(clave);
        int i = 0, index;

        while (i < tamano) {
            index = (hash + i * i) % tamano;
            HashNode nodo = tabla.obtener(index);
            if (nodo == null || nodo.eliminado) {
                tabla.asignar(index, new HashNode(clave));
                return true;
            }
            i++;
        }
        return false;
    }

    public boolean eliminar(String clave) {
        int hash = hashBase(clave);
        int i = 0, index;

        while (i < tamano) {
            index = (hash + i * i) % tamano;
            HashNode nodo = tabla.obtener(index);
            if (nodo == null) return false;
            if (!nodo.eliminado && nodo.clave.equals(clave)) {
                nodo.eliminado = true;
                return true;
            }
            i++;
        }
        return false;
    }

    public String buscar(String clave) {
        int hash = hashBase(clave);
        int i = 0, index;

        while (i < tamano) {
            index = (hash + i * i) % tamano;
            HashNode nodo = tabla.obtener(index);
            if (nodo == null) return null;
            if (!nodo.eliminado && nodo.clave.equals(clave)) {
                return "Clave encontrada en índice: " + index;
            }
            i++;
        }
        return null;
    }

    public String buscarIndice(int index) {
        if (index >= 0 && index < tamano) {
            HashNode nodo = tabla.obtener(index);
            if (nodo != null && !nodo.eliminado)
                return nodo.clave;
        }
        return null;
    }

    public void cargarDesdeArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String clave = linea.trim();
                if (!clave.isEmpty()) {
                    insertar(clave);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String[][] obtenerDatos() {
        String[][] datos = new String[tamano][2];
        for (int i = 0; i < tamano; i++) {
            datos[i][0] = String.valueOf(i);
            HashNode nodo = tabla.obtener(i);
            if (nodo == null) {
                datos[i][1] = "Vacío";
            } else if (nodo.eliminado) {
                datos[i][1] = "Eliminado";
            } else {
                datos[i][1] = nodo.clave;
            }
        }
        return datos;
    }
}
