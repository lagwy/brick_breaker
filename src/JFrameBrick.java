/**
 * JFrame Brick Breaker
 * 
 * @author Luis Angel, Jorge Marquez, Valeria Marroquin
 */
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;


public final class JFrameBrick extends JFrame implements Runnable, KeyListener {
    public JFrameBrick(){
        init();
        start();
    }
    //Declaración de variables
    private Graphics graGraficaFrame;
    private Image imaImagenFrame;
    private Base basBarra; //Se crea la base llamada Barra
    private Base basPelota; //Se crea la pelota
    private Base basAnfetamina; //Se crea la base anfetamina
    private int iDirBarra;
    private int iDirPelota; //Direccion de la pelota
    private boolean bPegado; //Cuando la pelota debe ir pegada a la barra
    private boolean bPausado; //Variable que indica cuando el juego esta pausado
    private LinkedList lnkAnfetaminas; //Lista encadenada con todos los bricks
    
    
    /*
     * Método init
     * 
     * Método en el que se inicializan las variables
     */
    
    public void init(){
        //Se le da al JFrame el siguiente tamaño
        setSize(525,700);
        iDirBarra = 0; //La direccion es 0 para que no se mueva la barra
        iDirPelota = 0; //La pelota no se mueve
        bPegado = true; //Inicia la barra con la pelota pegada
        bPausado = false; //Al comenzar el juego no esta pausado
        //Se crea y posiciona la barra
        URL urlImagenBarra = this.getClass().getResource("barrita.jpg");
        basBarra = new Base (0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenBarra));
        basBarra.setX(getWidth() / 2 - basBarra.getAncho() / 2);
        basBarra.setY(getHeight() - 100);
        
        URL urlImagenPelota = this.getClass().getResource("bolita.jpg");
        basPelota = new Base (0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenPelota));
        basPelota.setX(basBarra.getX() + basBarra.getAncho() / 2 - (basPelota.getAncho() / 2));
        basPelota.setY(basBarra.getY() - basPelota.getAlto());
        
        //Se inicializa la lista con las anfetaminas
        lnkAnfetaminas = new LinkedList();
        URL urlImagenAnfetamina = this.getClass().getResource("anfetamina.jpg");
        basAnfetamina = new Base(0, 0, 
                Toolkit.getDefaultToolkit().getImage(urlImagenAnfetamina));
        basAnfetamina.setX(getWidth() / 2);
        basAnfetamina.setY(getHeight() / 2);
        //Se crea solo uno para casos de prueba
        lnkAnfetaminas.add(basAnfetamina);
        
        setBackground (Color.yellow);
        addKeyListener(this);
    }
    
    public void start (){
         // Declaras un hilo
        Thread th = new Thread (this);
        // Empieza el hilo
        th.start ();
    }
    
    public void checaColision(){
        if (basBarra.getX() <= 0){
            basBarra.setX(0);
        }
        if (basBarra.getX() + basBarra.getAncho() > getWidth()){
            basBarra.setX(getWidth() - basBarra.getAncho());
        }
        
        if (basPelota.getX() <= 0){
            basPelota.setX(0);
            if (iDirPelota == 3){
                iDirPelota = 4;
            }
            if (iDirPelota == 2){
                iDirPelota = 1;
            }
        }
        if (basPelota.getX() + basPelota.getAncho() > getWidth()){
            basPelota.setX(getWidth() - basPelota.getAncho());
            if (iDirPelota == 4){
                iDirPelota = 3;
            } 
            if (iDirPelota == 1){
                iDirPelota = 2;
            }
        }
        if (basPelota.getY() <= 0){
            basPelota.setY(0);
            if (iDirPelota == 1){
                iDirPelota = 4;
            }
            if (iDirPelota == 2){
                iDirPelota = 3;
            }
        }
        if (basPelota.getY() - basPelota.getAlto() > getHeight()){
            bPegado = true;
            //Agregar el código que quita las vidas, etc
        }
        //Copiar esta parte del código hacia donde vayan a estar los bricks
        //Aquí debe estar un error
        if (basPelota.colisiona(basBarra)){
            if (basPelota.getX()  < basBarra.getX() + basBarra.getAncho() / 2){
                iDirPelota = 2;
            } 
            if (basPelota.getX() >= basBarra.getX() + basBarra.getAncho() / 2){
                iDirPelota = 1;
            }
        }
    }
    
    public void run(){
        while (true){
            if (!bPausado){
                actualiza();
                checaColision();   
            }
            repaint();
            try	{
                // El thread se duerme.
                Thread.sleep (20);
            }
            catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego " + 
                        iexError.toString());
            }
        }
    }
    
    public void actualiza(){
        if (iDirBarra == 1){ //La direccion es la derecha
            basBarra.setX(basBarra.getX() + 3);
        }
        if (iDirBarra == 2){ //La direccion es a la izquierda
            basBarra.setX(basBarra.getX() - 3);
        }
        if (bPegado){
            basPelota.setX(basBarra.getX() + basBarra.getAncho() / 2 - 
                    (basPelota.getAncho() / 2));
            basPelota.setY(basBarra.getY() - basPelota.getAlto());
        } else {
            if (iDirPelota == 1){
                basPelota.setY(basPelota.getY() - 2);
                basPelota.setX(basPelota.getX() + 2);
            }
            if (iDirPelota == 2){
                basPelota.setY(basPelota.getY() - 2);
                basPelota.setX(basPelota.getX() - 2);
            }
            if (iDirPelota == 3){
                basPelota.setY(basPelota.getY() + 2);
                basPelota.setX(basPelota.getX() - 2);
            }
            if (iDirPelota == 4){
                basPelota.setY(basPelota.getY() + 2);
                basPelota.setX(basPelota.getX() + 2);
            }
        }
    }
    
    public void paint (Graphics graGrafico){
        // Inicializan el DoubleBuffer
        if (imaImagenFrame == null){
                imaImagenFrame = createImage (this.getSize().width, 
                        this.getSize().height);
                graGraficaFrame = imaImagenFrame.getGraphics ();
        }
        
        
            //Se crea la imagen de fondo
            URL urlImagenFondo = this.getClass().getResource("espacio.png");
            Image imaImagenEspacio = Toolkit.getDefaultToolkit().
                getImage(urlImagenFondo);
        
            //Se despliega la imagen de fondo
            graGraficaFrame.drawImage(imaImagenEspacio, 0, 0,
                getWidth(), getHeight(), this);
        
        

        // Actualiza el Foreground.
        graGraficaFrame.setColor (getForeground());
        paint1(graGraficaFrame);

        // Dibuja la imagen actualizada
        graGrafico.drawImage (imaImagenFrame, 0, 0, this);
    }
    
    public void paint1(Graphics g) {
        g.drawImage(basBarra.getImagen(), basBarra.getX(), 
                basBarra.getY(), this);
        
        g.drawImage (basPelota.getImagen(), basPelota.getX(),
                basPelota.getY(), this);
        for (Object basBrick : lnkAnfetaminas){
            basAnfetamina = (Base) basBrick;
            g.drawImage(basAnfetamina.getImagen(), basAnfetamina.getX(), 
                    basAnfetamina.getY(), this);
        }
    }

    @Override
    public void keyTyped(KeyEvent ke) {
    }

    @Override
    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT){
            iDirBarra = 1; //Direccion a la derecha
        }  
        if (ke.getKeyCode() == KeyEvent.VK_SPACE && bPegado){
            bPegado = false;
            if (basBarra.getX() + basBarra.getAncho() / 2 < getWidth() / 2){
                iDirPelota = 2;
            } else {
                iDirPelota = 1;
            }
        }
        if (ke.getKeyCode() == KeyEvent.VK_P){
            bPausado = !bPausado;
        }
        if (ke.getKeyCode() == KeyEvent.VK_LEFT){
            iDirBarra = 2; //Direccion a la izquierda
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        iDirBarra = 0; //Se vuelve a detener
    }

}

