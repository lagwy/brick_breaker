import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;

public class Base {

    private int iX;     //posicion en x.       
    private int iY;     //posicion en y.
    private ImageIcon imiIcono;	//icono.

    public Base(int iX, int iY ,Image imaImagen) {
        this.iX = iX;
        this.iY = iY;
        imiIcono = new ImageIcon(imaImagen);
    }

    public Base(int iX, int iY ,ImageIcon icoImagen) {
        this.iX = iX;
        this.iY = iY;
        imiIcono = icoImagen;
    }

    public void setX(int iX) {
        this.iX = iX;
    }

    public int getX() {
            return iX;
    }

    public void setY(int iY) {
            this.iY = iY;
    }

    public int getY() {
        return iY;
    }
    
    public void derecha() {
        this.setX(this.getX() + 1);
    }
    
    public void izquierda() {
        this.setX(this.getX() - 1);
    }
    
    public void setImageIcon(ImageIcon imiIcono) {
        this.imiIcono = imiIcono;
    }

    public ImageIcon getImageIcon() {
        return imiIcono;
    }

    public void setImagen(Image imaImagen) {
        this.imiIcono = new ImageIcon(imaImagen);
    }

    public Image getImagen() {
        return imiIcono.getImage();
    }

    public int getAncho() {
        return imiIcono.getIconWidth();
    }
    
    public int getAlto() {
        return imiIcono.getIconHeight();
    }
    
    public boolean colisiona(Base aniParametro) {
        // creo un objeto rectangulo a partir de este objeto Animal
        Rectangle recObjeto = new Rectangle(this.getX(),this.getY(),
                this.getAncho(), this.getAlto());
        
        // creo un objeto rectangulo a partir del objeto Animal parametro
        Rectangle recParametro = new Rectangle(aniParametro.getX(),
                aniParametro.getY(), aniParametro.getAncho(),
                aniParametro.getAlto());
        
        // si se colisionan regreso verdadero, sino regreso falso
        return recObjeto.intersects(recParametro);
    }
 
    public boolean colisiona(int iX, int iY) {
        // creo un objeto rectangulo a partir de este objeto Animal
        Rectangle recObjeto = new Rectangle(this.getX(),this.getY(),
                this.getAncho(), this.getAlto());
               
        // si se colisionan regreso verdadero, sino regreso falso
        return recObjeto.contains(iX, iY);
    }    
}