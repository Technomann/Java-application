package staticElement;


import graphics.Effects;

import java.awt.*;
import java.awt.image.ImageObserver;


/** \class PowerPellet
 *  \brief Ez az osztály valósítja meg a játékban felvehető extra pontokat.
 *
 *  Noha funkcionalitásban szinte ugyanazt tudja mint a Pellet osztály, a tárolás eltérősége miatt nem lenne hasznosabb, ha abból származna le.
 *  A Pellet osztállyal ellentétben, itt nem szükséges a changeBlockSize metódus, ugyanis ezek az objektumok mindig a játék futása során jönnek létre,
 *  tehát mindig az aktuális pályablokkméretet fogják használni.
 */
public class PowerPellet {

    private final int X; /**< Az objektum képének X koordinátája pixelben.*/
    private final int Y;/**< Az objektum képének Y koordinátája pixelben.*/
    private final int BLOCK_SIZE; /**< A játékban a létrehozáskor használatos pályablokk méret.*/

    /**
     * Az osztály konstruktora.
     * @param x az X koordináta értéke
     * @param y az Y koordináta értéke
     * @param b a játékban a létrehozáskor használatos pályablokk méret
     */
    public PowerPellet(int x, int y, int b){
        X = x;
        Y = y;
        BLOCK_SIZE = b;
    }

    /**
     * A metódus segítségével jeleníti meg az objektum a képét a paraméterként átvett bufferelt képre utaló Graphics2D objektummal.
     * @param frame_g Graphics2D objektum, aminek a segítségével kirajzolható a kép
     * @param c ImageObserver objektum. Megfelel a hívó objektumnak.
     */
    public void draw(Graphics2D frame_g, Object c) {
        frame_g.drawImage(
                Effects.Statics.POWER.getStatPic(), X, Y, BLOCK_SIZE, BLOCK_SIZE, (ImageObserver)c);
    }

    /**
     * Getter az X koordinátára.
     * @return az X koordináta
     */
    public int getX() {
        return X;
    }

    /**
     * Getter az Y koordinátára.
     * @return az Y koordináta
     */
    public int getY() {
        return Y;
    }
}
