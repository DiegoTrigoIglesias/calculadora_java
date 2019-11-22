 //hacemos las importaciones necesarias 
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

//creamos la clase principal de Calculadora 
public class Calculadora extends JFrame {
    private JLabel Resultado = new JLabel(" ");
    private JPanel panel_botones = new JPanel(new GridLayout(4, 4));
    private JPanel resultado_final = new JPanel(new GridLayout(1, 1));
    private JButton[] botones = { //creamos los botones de todas las operaciones deseadas
        new JButton("1"), new JButton("2"), new JButton("3"), new JButton("+"), new JButton("4"), new JButton("5"),
        new JButton("6"), new JButton("-"), new JButton("7"), new JButton("8"), new JButton("9"), new JButton("*"),
        new JButton("AC"), new JButton("0"), new JButton(","), new JButton("/"), new JButton("=")
    };
    private Dimension dimension_ventana = new Dimension(400, 440); //tamaño de mi ventana 
    private double resultado = 0; //valor del resultado inicial 
    private double numero;
    private static final int SUMA = 1; //operaacion de la suma 
    private static final int RESTA = 2; //operaacion de la resta 
    private static final int MULTIPLICACION = 3; //operacion de la multiplicacion
    private static final int DIVISION = 4;  //operacion de la division
    private static final int NINGUNO = 0; //resultado final comenzamos a cero 
    private int operador = Calculadora.NINGUNO;
    private boolean decinamales = false; //contador de decimales 
    private boolean nuevoNumero = true;
    private NumberFormat nf = NumberFormat.getInstance();

    public Calculadora() {
        Dimension Pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (Pantalla.width - dimension_ventana.width) / 2;
        int y = (Pantalla.height - dimension_ventana.height) / 2;
        this.setLocation(x, y);
        this.setSize(dimension_ventana);
        this.setTitle("Calculadora_Diego");//nombre de la ventana 

        Resultado.setBackground(Color.white);
        Resultado.setOpaque(true);
        PulsaRaton pr = new PulsaRaton();
        for (int i = 0; i < botones.length - 1; i++) {
            panel_botones.add(botones[i]);
            botones[i].addActionListener(pr);
        }
        resultado_final.add(botones[botones.length - 1]);
        botones[botones.length - 1].addActionListener(pr);

        resultado_final.setPreferredSize(new Dimension(0, 50));
        this.add(Resultado, BorderLayout.NORTH);
        this.add(panel_botones);
        this.add(resultado_final, BorderLayout.SOUTH);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        Resultado.setText("0,0");//rexto inicial con comas 
        Resultado.setHorizontalAlignment(JLabel.RIGHT);
    }
    public static void main(String[] args) {
        new Calculadora();
    }
    //detectamos la tecla pulsada por el usuario
    class PulsaRaton implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JButton origen = (JButton) e.getSource();
            String texto = origen.getText();
            switch (texto) {
                case "+"://cuando se pulse el simbolo + realiz la opcion de suma 
                    operar(Calculadora.SUMA);
                    break;
                case "-"://cuando se pulse el simbolo - realiz la opcion de resta 
                    operar(Calculadora.RESTA);
                    break;
                case "*"://cuando se pulse el simbolo * realiz la opcion de multiplicacion  
                    operar(Calculadora.MULTIPLICACION);
                    break;
                case "/"://cuando se pulse el simbolo / realiz la opcion de division  
                    operar(Calculadora.DIVISION);
                    break;
                case ",": //utilizado para poner decimales 
                    if (!nuevoNumero) {
                        if (!decinamales) { //een caso de que anteriormente no haya 
                            String result = Resultado.getText();
                            Resultado.setText(result + ",");// se introduce la coma de los decimales 
                        }
                    } else {//si anteriormente hay decimales se descarta la coma 
                        Resultado.setText("0,"); //se ignora 
                        nuevoNumero = false;
                    }
                    decinamales = true;//pasamos los decimales a cierto 
                    break;
                case "AC":  //reseteo 
                    Resultado.setText("0,0");//valor inicial 
                    nuevoNumero = true;
                    decinamales = false;//resultado inicial sin decimales 
                    break;
                case "=": //cuando se pulse el igual requerimos el resultdo de las operaciones 
                    if (operador != Calculadora.NINGUNO) {
                        String result = Resultado.getText();
                        if (!result.isEmpty()) {
                            Number n = null;
                            try {
                                n = (Number) nf.parse(result);
                                numero = n.doubleValue();
                            } catch (ParseException ex) {
                                numero = 0;
                            }
                            switch (operador) {
                                case Calculadora.SUMA: //cuaando se requiere la suma 
                                    resultado += numero;
                                    break;
                                case Calculadora.RESTA://cuaando se requiere la resta 
                                    resultado -= numero;
                                    break;
                                case Calculadora.MULTIPLICACION://cuaando se requiere la multiplicacion 
                                    resultado *= numero;
                                    break;
                                case Calculadora.DIVISION://cuaando se requiere la division
                                    resultado /= numero;
                                    break;
                                default://opcion por defecto 
                                    resultado = numero;
                                    break;
                            }
                            operador = Calculadora.NINGUNO;
                            Resultado.setText(nf.format(resultado));
                        }
                    }
                    break;
                default:
                    String result = Resultado.getText();
                    if (nuevoNumero) {
                        Resultado.setText(texto);
                    } else {
                        Resultado.setText(result + texto);
                    }
                    nuevoNumero = false;
                    break;
            }
        }
    }
    public void operar(int operacion) {
        if (!nuevoNumero) {
            String result = Resultado.getText();
            if (!result.isEmpty()) {
                Number n = null;
                try {
                    n = (Number) nf.parse(result);
                    numero = n.doubleValue();
                } catch (ParseException ex) {
                }
                switch (operador) {
                    case Calculadora.SUMA:
                        resultado += numero;//utilizamos el numero anterior sumado al nuevo 
                        break;
                    case Calculadora.RESTA://utilizamos el numero anterior restado al nuevo 
                        resultado -= numero;
                        break;
                    case Calculadora.MULTIPLICACION://utilizamos el numero anterior multiplicado al nuevo 
                        resultado *= numero;
                        break;
                    case Calculadora.DIVISION://utilizamos el numero anterior dividido al nuevo 
                        resultado /= numero;
                        break;
                    default:
                        resultado = numero; //opcion del resltado final 
                }
                operador = operacion;
                Resultado.setText(nf.format(resultado));
                nuevoNumero = true;
                decinamales = false;
            }
        }
    }
}
