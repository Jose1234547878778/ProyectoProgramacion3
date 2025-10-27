public class TablaHashPersonalizada {
    private HashNode[] arreglo;

    public TablaHashPersonalizada(int tamano) {
        arreglo = new HashNode[tamano];
    }

    public HashNode obtener(int index) {
        return arreglo[index];
    }

    public void asignar(int index, HashNode nodo) {
        arreglo[index] = nodo;
    }

    public int tamanio() {
        return arreglo.length;
    }
}
