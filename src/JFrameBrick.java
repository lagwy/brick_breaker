/**
 * JFrame Brick Breaker
 *
 * @author Luis Angel, Jorge Marquez, Valeria Marroquin
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.LinkedList;
import javax.swing.JFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Font;

public final class JFrameBrick extends JFrame implements Runnable, KeyListener {

    public JFrameBrick() {
        init();
        start();
    }
    //Declaración de variables
    private SoundClip aucSonidoMusica;
    private SoundClip aucSonidoAnfetaminas;
    private SoundClip aucSonidoInicio;
    private Graphics graGraficaFrame;
    private Image imaImagenFrame;
    private int iVelocidad; //Velocidad
    private Base basBarra; //Se crea la base llamada Barra
    private Base basPelota; //Se crea la pelota
    private Base basAnfetamina; //Se crea la base anfetamina
    private Base basSirena; //PowerUp
    private int iNivel; //Nivel actual
    private boolean bCargoNivel; //Para saber si ya se cargo el nivel
    //Se crean dos bases para los marcos
    private Base basMarcoDer; //Marcos derecha
    private Base basMarcoIzq; //Marco izquierda
    private Base basMarcoArr; //Marcos arriba
    private Base basMarcoAbj; //Marcos abajo
    private int iDirBarra;
    private int iCantidadBricks; //Cantidad de bricks 
    private int iVidas;
    private int iDirPelota; //Direccion de la pelota
    private boolean bPegado; //Cuando la pelota debe ir pegada a la barra
    private boolean bEmpieza;
    private boolean bPausado; //Variable que indica cuando el juego esta pausado
    private boolean bGameOver; //Se termina el juego y se puede reiniciar
    private boolean bComprueba; //Comprueba que no se re-cambie la dirección
    private boolean bSirena;
    private LinkedList lnkAnfetaminas; //Lista encadenada con todos los bricks
    private int iScore;

    /*
     * Método init
     * 
     * Método en el que se inicializan las variables
     */
    public void init() {
        //Se le da al JFrame el siguiente tamaño
        setSize(525, 700);
        iDirBarra = 0; //La direccion es 0 para que no se mueva la barra
        iNivel = 0; //primer nivel
        iScore = 0; //inicializa en cero 
        bCargoNivel = false;
        bEmpieza = false;
        bGameOver = false;
        bComprueba = false;
        bSirena = true;
        iCantidadBricks = 0;
        iVidas = 3;
        iVelocidad = 3;
        iDirPelota = 0; //La pelota no se mueve
        bPegado = true; //Inicia la barra con la pelota pegada
        bPausado = false; //Al comenzar el juego no esta pausado
        //Se crea y posiciona la barra
        URL urlImagenBarra = this.getClass().getResource("barrita.png");
        basBarra = new Base(0, 0,
                Toolkit.getDefaultToolkit().getImage(urlImagenBarra));
        basBarra.setX(getWidth() / 2 - basBarra.getAncho() / 2);
        basBarra.setY(getHeight() - 100);

        URL urlImagenPelota = this.getClass().getResource("bolita.png");
        basPelota = new Base(0, 0,
                Toolkit.getDefaultToolkit().getImage(urlImagenPelota));
        basPelota.setX(basBarra.getX() + basBarra.getAncho() / 2
                - (basPelota.getAncho() / 2));
        basPelota.setY(basBarra.getY() - basPelota.getAlto());

        //Se inicializa la lista con las anfetaminas
        lnkAnfetaminas = new LinkedList();
        for (int iI = 0; iI < 300; iI += 100) {
            URL urlImagenAnfetamina = this.getClass().
                    getResource("anfetamina.jpg");
            basAnfetamina = new Base(0, 0,
                    Toolkit.getDefaultToolkit().getImage(urlImagenAnfetamina));
            basAnfetamina.setX(getWidth() / 4 + iI);
            basAnfetamina.setY(getHeight() / 2);
            //Se crea solo uno para casos de prueba
            lnkAnfetaminas.add(basAnfetamina);
        }
        
        //Se inicializan los marcos del nivel 1
        URL urlImagenLados = this.getClass().getResource("barra_lados_ver.png");
        basMarcoDer = new Base(0, 0, Toolkit.getDefaultToolkit().
                getImage(urlImagenLados));
        basMarcoDer.setX(getWidth() - (basMarcoDer.getAncho() + 46));
        basMarcoDer.setY(basMarcoDer.getY() + 78);
        basMarcoIzq = new Base(0, 0,
                Toolkit.getDefaultToolkit().getImage(urlImagenLados));
        basMarcoIzq.setX(basMarcoIzq.getAncho() + 20);
        basMarcoIzq.setY(basMarcoIzq.getY() + 78);
        URL urlImagenArribaAbajo = this.getClass().getResource("barra_arriabajo_ver.png");

        //Marco de arriba
        basMarcoArr = new Base(0, 0, Toolkit.getDefaultToolkit().
                getImage(urlImagenArribaAbajo));
        basMarcoArr.setX(getWidth() - (basMarcoArr.getAncho() + 68));
        basMarcoArr.setY(basMarcoArr.getY() + 78);
        //Marco de abajo
        basMarcoAbj = new Base(0, 0,
                Toolkit.getDefaultToolkit().getImage(urlImagenArribaAbajo));
        basMarcoAbj.setX(getWidth() - (basMarcoArr.getAncho() + 68));
        basMarcoAbj.setY(getHeight() - 72);
               
        
        URL urlImagenSirena = this.getClass().getResource("sirena.gif");
        basSirena = new Base(0, 0, Toolkit.getDefaultToolkit().
                getImage(urlImagenSirena));
        basSirena.setY(-250);
        basSirena.setX(getWidth() / 2);
        
        //musica de fondo
        aucSonidoMusica = new SoundClip("musica.wav");
        
        
        //efecto cuando chocas anfetaminas
        aucSonidoAnfetaminas = new SoundClip ("avast.wav");
        
        //sonido inicio
        aucSonidoInicio = new SoundClip ("intro.wav");
        aucSonidoInicio.play();

        setBackground(Color.yellow);
        addKeyListener(this);
    }

    public void start() {
        // Declaras un hilo
        Thread th = new Thread(this);
        // Empieza el hilo
        th.start();
    }

    public void checaColision() {
        if(basBarra.colisiona(basSirena)){
            bSirena = false;
            basSirena.setY(-500);
            basSirena.setX((int) Math.random() * (388) + 65);
        }
        
        if(basSirena.colisiona(basMarcoAbj)){
            basSirena.setY(-500);
            basSirena.setX(((int) Math.random()) * (388) + 65);
        }
        
        if (basBarra.colisiona(basMarcoIzq)) {
            basBarra.setX(basMarcoIzq.getAncho() + 46);
        }
        if (basBarra.colisiona(basMarcoDer)) {
            basBarra.setX(getWidth() - (basMarcoDer.getAncho() + 145));
        }       
        if (basPelota.colisiona(basMarcoIzq)) {
            if (iDirPelota == 3) {
                iDirPelota = 4;
            }
            if (iDirPelota == 2) {
                iDirPelota = 1;
            }
        }
        if (basPelota.colisiona(basMarcoDer)) {
            if (iDirPelota == 4) {
                iDirPelota = 3;
            }
            if (iDirPelota == 1) {
                iDirPelota = 2;
            }
        }
        if (basPelota.colisiona(basMarcoArr)) {
            if (iDirPelota == 1) {
                iDirPelota = 4;
            }
            if (iDirPelota == 2) {
                iDirPelota = 3;
            }
        }
        if (basPelota.colisiona(basMarcoAbj)) {
            bPegado = true;
            iVidas--;
            iVelocidad++;
            bSirena = true;
            basSirena.setY(-500);
            basSirena.setX(((int) Math.random()) * (388) + 65);
        }
        
        if (basPelota.colisiona(basBarra)) {
            if (basPelota.getX() < basBarra.getX() + basBarra.getAncho() / 2) {
                iDirPelota = 2;
            }
            if (basPelota.getX() >= basBarra.getX() + basBarra.getAncho() / 2) {
                iDirPelota = 1;
            }
        }

        //Revisar si la pelota colisiona con alguna anfetamina
        for (Object objAnfetamina : lnkAnfetaminas) {
            Base basAnfetamina = (Base) objAnfetamina;
                
                if(basAnfetamina.colisionaIzquierda(basPelota) && 
                        bComprueba == false){
                    if(iDirPelota == 1){
                        iDirPelota = 2;
                    }
                    if (iDirPelota == 4){
                        iDirPelota = 3;
                    }
                    colision(basAnfetamina);
                    bComprueba = true;
                }
                if (basAnfetamina.colisionaDerecha(basPelota) && 
                        bComprueba == false){
                    if(iDirPelota == 2){
                        iDirPelota = 1;
                    }
                    if(iDirPelota == 3){
                        iDirPelota = 4;
                    }
                    colision(basAnfetamina);
                    bComprueba = true;
                }
                if(basAnfetamina.colisionaArriba(basPelota) && 
                        bComprueba == false){
                    if(iDirPelota == 3){
                        iDirPelota = 2;
                    }
                    if (iDirPelota == 4){
                        iDirPelota = 1;
                    }
                    colision(basAnfetamina);
                    bComprueba = true;
                }
                if(basAnfetamina.colisionaAbajo(basPelota) && 
                        bComprueba == false){
                    if (iDirPelota == 1){
                        iDirPelota = 4;
                    }
                    if (iDirPelota == 2){
                        iDirPelota = 3;
                    }
                    colision(basAnfetamina);
                    bComprueba = true;
                }
        }
        bComprueba = false;
    }
    
    public void colision(Base basAnfetamina){
        aucSonidoAnfetaminas.play();
                reposicionaBricks(basAnfetamina); //Lo posiciona en Oblivion
                if(!bSirena){
                    iScore += 200;
                } else {
                    iScore += 100;
                }
                iCantidadBricks--;
                if (iCantidadBricks < 1) {
                    reposicionaBarra(basBarra);
                    reposicionaPelota(basPelota);
                    if(iNivel !=2){
                    iNivel = 2;
                    try {
                    cargaNivel2();
                    } catch (IOException ex) {
                      Logger.getLogger(JFrameBrick.class.getName()).
                        log(Level.SEVERE, null, ex);
                    }    
                    } else {
                        bGameOver = true;
                        iVidas = 0;
                    }
                                       
                }
    }
    
    public void reposicionaBricks(Base basAnfetamina) {
        basAnfetamina.setX(-100);
        basAnfetamina.setY(-100);

    }
    
    public void reposicionaBarra(Base basBarra) {
        basBarra.setX(getWidth() / 2 - basBarra.getAncho() / 2);
        basBarra.setY(getHeight() - 100);
    }
    
    public void reposicionaPelota(Base basPelota) {
        basPelota.setX(basBarra.getX() + basBarra.getAncho() / 2
                - (basPelota.getAncho() / 2));
        basPelota.setY(basBarra.getY() - basPelota.getAlto());
        bPegado = !bPegado;
    }

    public void cargaNivel() throws IOException {
        if (iNivel == 1 && !bCargoNivel) {
            BufferedReader brwEntrada;
            try {
                brwEntrada = new BufferedReader(new FileReader("nivel1.txt"));
                aucSonidoMusica.setRepeat(15);
                aucSonidoMusica.play();
            } catch (FileNotFoundException e) {
                File filPredeterminado = new File("nivel0.txt");
                PrintWriter prwSalida = new PrintWriter(filPredeterminado);
                prwSalida.println("1");
                prwSalida.println("100 100");
                brwEntrada = new BufferedReader(new FileReader("nivel1.txt"));
            }
            String sAux = ""; //Se declara variable auxiliar vacia
            int iCantidad; //Cantidad de bricks
            iCantidad = Integer.parseInt(brwEntrada.readLine());
            iCantidadBricks = iCantidad;
            lnkAnfetaminas.clear();
            lnkAnfetaminas = new LinkedList();
            for (int iI = 0; iI < iCantidad; iI++) {
                sAux = brwEntrada.readLine();
                URL urlImagenAnfetamina = this.getClass().
                        getResource("anfetamina.jpg");
                Base basAnfetamina = new Base(Integer.parseInt(sAux.substring(0,
                        sAux.indexOf(" "))), Integer.parseInt(sAux.substring
                    (sAux.indexOf(" ") + 1)), Toolkit.getDefaultToolkit().
                        getImage(urlImagenAnfetamina));
                lnkAnfetaminas.add(basAnfetamina);
            }
        }     
    }
    
    public void cargaNivel2() throws IOException {
        bSirena = true;
        if (iNivel == 2) {
            BufferedReader brwEntrada;
            try {
                brwEntrada = new BufferedReader(new FileReader("nivel2.txt"));
            } catch (FileNotFoundException e) {
                File filPredeterminado = new File("nivel0.txt");
                PrintWriter prwSalida = new PrintWriter(filPredeterminado);
                prwSalida.println("1");
                prwSalida.println("100 100");
                brwEntrada = new BufferedReader(new FileReader("nivel2.txt"));
            }
            String sAux = ""; //Se declara variable auxiliar vacia
            int iCantidad; //Cantidad de bricks
            iCantidad = Integer.parseInt(brwEntrada.readLine());
            iCantidadBricks = iCantidad;
            lnkAnfetaminas.clear();
            lnkAnfetaminas = new LinkedList();
            for (int iI = 0; iI < iCantidad; iI++) {
                sAux = brwEntrada.readLine();
                URL urlImagenAnfetamina = this.getClass().
                        getResource("anfetamina.jpg");
                Base basAnfetamina = new Base(Integer.parseInt(sAux.substring(0, 
                        sAux.indexOf(" "))), Integer.parseInt(sAux.substring
                        (sAux.indexOf(" ") + 1)), Toolkit.getDefaultToolkit().
                        getImage(urlImagenAnfetamina));
                lnkAnfetaminas.add(basAnfetamina);
            }
        }        
    }
    
    public void run() {
        while (iVidas > 0) {
            if (!bPausado) {
                actualiza();
                checaColision();
            }
            repaint();
            try {
                // El thread se duerme.
                Thread.sleep(20);
            } catch (InterruptedException iexError) {
                System.out.println("Hubo un error en el juego "
                        + iexError.toString());
            }
        }
        if(iVidas < 1 || iCantidadBricks < 1){
            bGameOver = true;
        }
    }

    public void actualiza() {
        if(bSirena && !bPausado && !bPegado){
            basSirena.setY(basSirena.getY() + 2);
        }
        
        if (iDirBarra == 1) { //La direccion es la derecha
            basBarra.setX(basBarra.getX() + 4);
        }
        if (iDirBarra == 2) { //La direccion es a la izquierda
            basBarra.setX(basBarra.getX() - 4);
        }
        if (bPegado) {
            basPelota.setX(basBarra.getX() + basBarra.getAncho() / 2
                    - (basPelota.getAncho() / 2));
            basPelota.setY(basBarra.getY() - basPelota.getAlto());
        } else {
            if (iDirPelota == 1) {
                basPelota.setY(basPelota.getY() - iVelocidad);
                basPelota.setX(basPelota.getX() + iVelocidad);
            }
            if (iDirPelota == 2) {
                basPelota.setY(basPelota.getY() - iVelocidad);
                basPelota.setX(basPelota.getX() - iVelocidad);
            }
            if (iDirPelota == 3) {
                basPelota.setY(basPelota.getY() + iVelocidad);
                basPelota.setX(basPelota.getX() - iVelocidad);
            }
            if (iDirPelota == 4) {
                basPelota.setY(basPelota.getY() + iVelocidad);
                basPelota.setX(basPelota.getX() + iVelocidad);
            }
        }
    }

    public void paint(Graphics graGrafico) {
        // Inicializan el DoubleBuffer
        if (imaImagenFrame == null) {
            imaImagenFrame = createImage(this.getSize().width,
                    this.getSize().height);
            graGraficaFrame = imaImagenFrame.getGraphics();
        }

        //Se crea la imagen de fondo del nivel 1
        URL urlImagenFondo = this.getClass().getResource("background_1.png");
        Image imaImagenEspacio = Toolkit.getDefaultToolkit().
                getImage(urlImagenFondo);
        
        //Se crea la imagen de fondo del nivel 2
        URL urlImagenFondo1 = this.getClass().getResource
        ("brickbreaker_verde.png");
        Image imaImagenEspacioVerde = Toolkit.getDefaultToolkit().
                getImage(urlImagenFondo1);

        //Se crea la imagen para cuando el juego este pausado
        URL urlImagenPausa = this.getClass().getResource("pausa.gif");
        Image imaImagenPausa = Toolkit.getDefaultToolkit().
                getImage(urlImagenPausa);
        if (iNivel == 1 && !bPausado) {
            //Se despliega la imagen de fondo del nivel 1
            graGraficaFrame.drawImage(imaImagenEspacio, 0, 10,
                    getWidth(), getHeight(), this);
        }
        if(iNivel == 2 && !bPausado) {
            //Se despliega la imagen de fondo del nivel 2
            graGraficaFrame.drawImage(imaImagenEspacioVerde, 0, 10,
                    getWidth(), getHeight(), this);
        }
        if (!bEmpieza) {
            URL urlImagenInicio = this.getClass().
                    getResource("inicio.jpg");
            Image imaImagenInicio = Toolkit.getDefaultToolkit().
                    getImage(urlImagenInicio);
            graGraficaFrame.drawImage(imaImagenInicio, 0, 10, this);
            
            URL urlImagenStart = this.getClass().
                    getResource("start.gif");
            Image imaImagenStart = Toolkit.getDefaultToolkit().
                    getImage(urlImagenStart);
            graGraficaFrame.drawImage(imaImagenStart, getWidth() / 6, 610, this);
        }
        if (bGameOver) {          
            URL urlImagenGameOver = this.getClass().
                    getResource("game_over.jpg");
            Image imaImagenGameOver = Toolkit.getDefaultToolkit().
                    getImage(urlImagenGameOver);
            graGraficaFrame.drawImage(imaImagenGameOver, 0, 10, this);
            
            URL urlImagenRestart = this.getClass().getResource("restart.gif");
            Image imaImagenRestart = Toolkit.getDefaultToolkit().
                    getImage(urlImagenRestart);
            graGraficaFrame.drawImage(imaImagenRestart, getWidth() / 6, 
                     
                    100, this);
        }
        if (bPausado) {
            graGraficaFrame.drawImage(imaImagenPausa, 0, 10, this);  
        } 

        // Actualiza el Foreground.
        graGraficaFrame.setColor(getForeground());
        paint1(graGraficaFrame);

        // Dibuja la imagen actualizada
        graGrafico.drawImage(imaImagenFrame, 0, 0, this);

    }

    public void paint1(Graphics g) {
        if (!bPausado && bEmpieza && iVidas > 0) {

            //Se pinta la barra
            if (bSirena){
                URL urlImagenBarraSirena = this.getClass().
                        getResource("barrita.png");
                Image imaImagenBarraSirena = Toolkit.getDefaultToolkit().
                    getImage(urlImagenBarraSirena);
                g.drawImage(basBarra.getImagen(), basBarra.getX(),
                    basBarra.getY(), this);
            } else {
                URL urlImagenBarraSirena = this.getClass().
                        getResource("barra_sirena.gif");
                Image imaImagenBarraSirena = Toolkit.getDefaultToolkit().
                    getImage(urlImagenBarraSirena);
                basBarra.setImagen(imaImagenBarraSirena);
                g.drawImage(basBarra.getImagen(), basBarra.getX(),
                    basBarra.getY(), this);
            }
            

            //Se pinta la pelota
            g.drawImage(basPelota.getImagen(), basPelota.getX(),
                    basPelota.getY(), this);

            //Se pinta el power up
            if(basSirena.getY() + basSirena.getAlto() > 0 && !bPegado){
                g.drawImage(basSirena.getImagen(), basSirena.getX(),
                    basSirena.getY(), this);
            }
            
            
            //Se pintan las anfetaminas
            for (Object objAnfetamina : lnkAnfetaminas) {
                basAnfetamina = (Base) objAnfetamina;
                g.drawImage(basAnfetamina.getImagen(), basAnfetamina.getX(),
                        basAnfetamina.getY(), this);
            }

            //se pone el score
            g.setColor(Color.white);
            g.setFont(new Font("Serif", Font.BOLD, 22));
            g.drawString("" + iScore, 155, 60);

            //se ponen las vidas
            g.setColor(Color.white);
            g.setFont(new Font("Serif", Font.BOLD, 22));
            g.drawString("" + iVidas, 360, 60);

            //Se pintan los marcos del nivel 1
  
            if (iNivel == 1) {
            g.drawImage(basMarcoDer.getImagen(), getWidth()
                        - (basMarcoDer.getAncho() + 46), 
                    basMarcoDer.getY(), this);
            g.drawImage(basMarcoIzq.getImagen(), basMarcoIzq.getAncho() + 23,
                        basMarcoIzq.getY(), this);
            g.drawImage(basMarcoArr.getImagen(), getWidth()
                        - (basMarcoArr.getAncho() + 68), 
                    basMarcoArr.getY(), this);
            g.drawImage(basMarcoAbj.getImagen(), getWidth()
                        - (basMarcoArr.getAncho() + 68), 
                    getHeight() - 72, this); 
            }
            if (iNivel == 2) {
                URL urlImagenMarcoLadoIzqAm = this.getClass().
                    getResource("barra_lados_am.png");
            Image imaImagenMarcoAmIzq = Toolkit.getDefaultToolkit().
                    getImage(urlImagenMarcoLadoIzqAm);
            graGraficaFrame.drawImage(imaImagenMarcoAmIzq, 
                    basMarcoIzq.getAncho() + 23,
                        basMarcoIzq.getY(), this);
            URL urlImagenMarcoLadoDerAm = this.getClass().
                    getResource("barra_lados_am.png");
            Image imaImagenMarcoAmDer = Toolkit.getDefaultToolkit().
                    getImage(urlImagenMarcoLadoDerAm);
            graGraficaFrame.drawImage(imaImagenMarcoAmDer, getWidth()
                        - (basMarcoDer.getAncho() + 46), 
                    basMarcoDer.getY(), this);
            URL urlImagenMarcoArrAm = this.getClass().
                    getResource("barra_arriabajo_am.png");
            Image imaImagenMarcoArr = Toolkit.getDefaultToolkit().
                    getImage(urlImagenMarcoArrAm);
            graGraficaFrame.drawImage(imaImagenMarcoArr, getWidth()
                        - (basMarcoArr.getAncho() + 68), basMarcoArr.getY(), 
                        this);
            URL urlImagenMarcoAbjAm = this.getClass().
                    getResource("barra_arriabajo_am.png");
            Image imaImagenMarcoAbj = Toolkit.getDefaultToolkit().
                    getImage(urlImagenMarcoAbjAm);
            graGraficaFrame.drawImage(imaImagenMarcoAbj, getWidth()
                        - (basMarcoArr.getAncho() + 68), getHeight() - 72, 
                        this);
            }
        }
    }

    public void keyTyped(KeyEvent ke) {
    }

    public void keyPressed(KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_RIGHT) {
            iDirBarra = 1; //Direccion a la derecha
        }
        if (ke.getKeyCode() == KeyEvent.VK_SPACE && bPegado && iNivel > 0) {
            bPegado = false;
            if (basBarra.getX() + basBarra.getAncho() / 2 < getWidth() / 2) {
                iDirPelota = 2;
            } else {
                iDirPelota = 1;
            }
        }
        if (ke.getKeyCode() == KeyEvent.VK_P) {
            bPausado = !bPausado;
        }
        if (ke.getKeyCode() == KeyEvent.VK_LEFT) {
            iDirBarra = 2; //Direccion a la izquierda
        }
        if (ke.getKeyCode() == KeyEvent.VK_ENTER && !bEmpieza) {
            iNivel = 1;
            aucSonidoInicio.stop();
            try {
                cargaNivel();
            } catch (IOException ex) {
                Logger.getLogger(JFrameBrick.class.getName()).
                        log(Level.SEVERE, null, ex);
            }
            bEmpieza = true;
        }
        if (ke.getKeyCode() == KeyEvent.VK_R && bGameOver == true) {
            
            init();
            start();
        }
        if (ke.getKeyCode() == KeyEvent.VK_L){ //Para avanzar de nivel, cheat
            bGameOver = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        iDirBarra = 0; //Se vuelve a detener
    }

}
