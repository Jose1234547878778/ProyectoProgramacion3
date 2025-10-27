import javax.swing.*;
import java.awt.*;

public class InterfazHash extends JFrame {
    private HashTable tabla;

    private JTextField campoClave, campoClaveBuscar, campoIndice;
    private JTextArea areaResultado;
    private JComboBox<String> comboMetodoHash;

    public InterfazHash() {
        setTitle("Tabla Hash con GUI");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabla = new HashTable(10, 1);  // Tamaño 10 y método hash inicial 1 (cuadrados medios)

        JPanel panelPrincipal = new JPanel(new BorderLayout());

        // Parte inferior: botones y campos
        JPanel panelIngreso = new JPanel();

        campoClave = new JTextField(15);
        campoClaveBuscar = new JTextField(15);
        campoIndice = new JTextField(3);

        JButton btnInsertar = new JButton("Insertar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar");
        JButton btnBuscarIndice = new JButton("Buscar por índice");
        JButton btnCargar = new JButton("Cargar desde archivo");

        comboMetodoHash = new JComboBox<>(new String[]{"Cuadrados Medios", "Pliegues"});
        comboMetodoHash.setSelectedIndex(0);

        panelIngreso.add(new JLabel("Clave:"));
        panelIngreso.add(campoClave);
        panelIngreso.add(btnInsertar);
        panelIngreso.add(btnEliminar);

        panelIngreso.add(new JLabel("Buscar Clave:"));
        panelIngreso.add(campoClaveBuscar);
        panelIngreso.add(btnBuscar);

        panelIngreso.add(new JLabel("Índice:"));
        panelIngreso.add(campoIndice);
        panelIngreso.add(btnBuscarIndice);

        panelIngreso.add(btnCargar);

        panelIngreso.add(new JLabel("Método Hash:"));
        panelIngreso.add(comboMetodoHash);

        // Centro: área para mostrar tabla
        areaResultado = new JTextArea();
        areaResultado.setEditable(false);
        areaResultado.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(areaResultado);

        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panelPrincipal.add(panelIngreso, BorderLayout.SOUTH);

        add(panelPrincipal);

        // Acciones botones
        btnInsertar.addActionListener(e -> {
            String clave = campoClave.getText().trim();
            if (clave.isEmpty()) {
                mostrarMensaje("Ingrese una clave.");
                return;
            }
            if (tabla.insertar(clave))
                mostrarMensaje("Clave insertada correctamente.");
            else
                mostrarMensaje("Error: clave duplicada o tabla llena.");
            mostrarReporte();
        });

        btnEliminar.addActionListener(e -> {
            String clave = campoClave.getText().trim();
            if (clave.isEmpty()) {
                mostrarMensaje("Ingrese una clave.");
                return;
            }
            if (tabla.eliminar(clave))
                mostrarMensaje("Clave eliminada correctamente.");
            else
                mostrarMensaje("Clave no encontrada.");
            mostrarReporte();
        });

        btnBuscar.addActionListener(e -> {
            String clave = campoClaveBuscar.getText().trim();
            if (clave.isEmpty()) {
                mostrarMensaje("Ingrese una clave para buscar.");
                return;
            }
            String resultado = tabla.buscar(clave);
            mostrarMensaje(resultado != null ? resultado : "Clave no encontrada.");
        });

        btnBuscarIndice.addActionListener(e -> {
            try {
                int idx = Integer.parseInt(campoIndice.getText().trim());
                String val = tabla.buscarIndice(idx);
                mostrarMensaje(val != null ? "En índice " + idx + ": " + val : "Índice vacío.");
            } catch (NumberFormatException ex) {
                mostrarMensaje("Índice inválido.");
            }
        });

        btnCargar.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                String ruta = fileChooser.getSelectedFile().getAbsolutePath();
                tabla.cargarDesdeArchivo(ruta);
                mostrarMensaje("Archivo cargado correctamente.");
                mostrarReporte();
            }
        });

        // Listener para cambio de método hash con confirmación
        comboMetodoHash.addActionListener(e -> {
            int opcion = comboMetodoHash.getSelectedIndex() + 1;
            int opcionActual = tabla != null ? tabla.getMetodoHash() : -1;

            if (opcion != opcionActual) {
                int resp = JOptionPane.showConfirmDialog(this,
                        "¿Desea cambiar el método hash? Esto reiniciará la tabla.",
                        "Confirmar cambio de método hash",
                        JOptionPane.YES_NO_OPTION);

                if (resp == JOptionPane.YES_OPTION) {
                    actualizarMetodoHash();
                    mostrarReporte();
                } else {
                    comboMetodoHash.setSelectedIndex(opcionActual - 1);
                }
            }
        });

        mostrarReporte();
    }

    private void actualizarMetodoHash() {
        int opcion = comboMetodoHash.getSelectedIndex() + 1; // 1 o 2
        tabla = new HashTable(10, opcion);
    }

    private void mostrarReporte() {
        StringBuilder sb = new StringBuilder("Estado de la Tabla Hash:\n");
        for (int i = 0; i < 10; i++) {
            String val = tabla.buscarIndice(i);
            sb.append(String.format("%02d -> %s\n", i, val != null ? val : "VACÍO"));
        }
        areaResultado.setText(sb.toString());
    }

    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InterfazHash().setVisible(true));
    }
}
