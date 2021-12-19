package character;

import java.awt.*;


/**
 * \class Ghost
 * \brief A szellemek közös absztrakt őse. Fontos, mivl ennek köszönhetően lehet heterogén kollekciót használni a szellemekhez.
 * Minden szellem a program indítása után, a jéték elkezdésekor inicializálódik. Sajnos egyik szellemet sem vezérli
 * AI, előre meghatározott útvonalon közlekednek, de remélem, hogy ettől még kellően nehéz lesz vele játszani!:)
 * Mindegyik szellem ütközéskor egyel csökenti a PacMan életeinek számát.
 */
public abstract class Ghost {

    protected int X; /**<Adott szellem objektum X koordinátája pixelben */
    protected int Y; /**<Adott szellem objektum Y koordinátája pixelben  */
    protected int BLOCK_SIZE; /**< A szellemek létrehozásakor érvényben lévő pályablokk méret*/

    /**
     * Az osztály konstruktora.
     * @param x az adott objektum X koordinátája
     * @param y az adott objektum Y koordinátája
     * @param block az adott objektum létrehozásakor érvényben lévő pályablokk méret
     */
    public Ghost(int x, int y, int block){
        X = x;
        Y = y;
        BLOCK_SIZE = block;
    }

    /**
     * Absztrakt metódus az osztály leszármazottjainak kirajzolásához.
     * @param frame_g Graphics2D objektum, amire rajzolunk
     * @param c ImageObserver objektum, rendszerint a hívó objektum
     */
    public abstract void draw(Graphics2D frame_g, Object c);

    /**
     * Absztrakt metódus az osztály leszármazottjainak mozgatásához.
     * @param level hányas szinten tart éppen a pacman
     */
    public abstract void move(int level);

    /**
     * Ez a metódus a kiindulópontjába helyez minden egyes leszármazottat.
     */
    public abstract void setCentre();

    /**
     * Getter az X koordinátához.
     * @return az X koordináta
     */
    public int getX(){
        return X;
    }

    /**
     * Getter az Y koordinátához.
     * @return az Y koordináta
     */
    public int getY(){
        return Y;
    }
}
