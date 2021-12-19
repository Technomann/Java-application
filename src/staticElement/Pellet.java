package staticElement;

import character.Elements;
import graphics.Effects;

import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.Serializable;


/** \class Pellet
    \brief Ez az osztály valósítja meg a játékban a felvehető pontokat.

    Az Elements ősből származik le. Ez az osztály szerializálódik a mentésekben.
*/
public class Pellet extends Elements implements Serializable {

    private static final long serialVersionUID = 50L; /**< Szerializáláshoz nem kötelezően szükséges egyéni azonosító.*/

    private int X; /**< Az adot pellet X koordinátája, pixelben.*/
    private int Y;/**< Az adot pellet Y koordinátája, pixelben.*/

    private int BLOCK_SIZE; /**< Konstruktorban paraméterként átvett pályablokk méret.*/

    /**
     * Az osztály konstruktora.
     * @param x az adott objektum X koordinátája
     * @param y az adott objektum Y koordinátája
     * @param b a az objektum létrejöttekor érvényben lévő pályablokk méret
     */
    public Pellet(int x, int y,  int b){
       BLOCK_SIZE = b;
        X=x;
        Y=y;
    }

    /**
     * A függvény megváltoztatja az objektumban eltárolt blokkméretet, és ezzel együtt az X,Y koordinátákat,
     * így az következő kirajzoláskor már alkalmazkodik a megváltozott pályaméretekhez.
     * @param d az új blokkméret
     */
    public void changeBlockSize(int d){
       int tempX = X/BLOCK_SIZE;
       int tempY = Y/BLOCK_SIZE;

        BLOCK_SIZE = d;

        X = tempX*BLOCK_SIZE;
        Y = tempY*BLOCK_SIZE;
    }

    /**
     * Getter az X koordinátára.
     * @return X értéke
     */
    @Override
    public int getX() {
        return X;
    }

    /**
     * Getter az Y koordinátára.
     * @return Y értéke
     */
    @Override
    public int getY() {
        return Y;
    }

    /**
     * Getter a pellet "nevére". Erre a heterogén kollekció miatt van szükség.
     * @return minden esetben 'null'
     */
    @Override
    public String getPlayerName() {
        return null;
    }

    /**
     * Minden egyes Timer tick-re ez a metódus íhvódik meg, ami kirajzolja a pályára a pelletet.
     * @param frame_g - az a Graphics2D elem, amire rajzolunk.
     * @param c - ImageObserver a drawImage metódushoz, az az objektum, ahonnan hívták a draw-t
     */
    @Override
    public void draw(Graphics2D frame_g, Object c) {
        frame_g.drawImage(Effects.Statics.DOT.getStatPic(),X, Y, BLOCK_SIZE, BLOCK_SIZE, (ImageObserver) c);
    }
}
