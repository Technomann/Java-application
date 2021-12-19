package character;

import java.awt.*;

/**
 * \class Elements
 * \brief Ez az osztály a közös őse a PacMan-nek, és a Pellet-nek
 *
 * Azért van szükség erre a közös absztrakt ős osztályra, mert így a grafika megvalósítása sokkal könnyebb a heterogén kollekció révén.
 */
public abstract class Elements {

     /**
      * Absztrakt metódus az osztály leszármazottjainak kirajzolásához.
      * @param frame_g Graphics2D objektum, amire rajzolunk
      * @param c ImageObserver objektum, rendszerint a hívó objektum
      */
     public abstract void draw(Graphics2D frame_g, Object c);

     /**
      * Getter metódus, ami Pellet esetén null, PacMan esetén pedig a játékos által begépelt nevet adja vissza.
      * @return a játékos által begépelt név
      */
     public abstract String getPlayerName();

     /**
      * Ez a függvény szolgál arra, hogyha netán két programindítás között megváltozna a pályablokk mérete, akkor minden
      * leszármazott megkapja az új méretet, és azzal tudjon tovább dolgozni.
      * @param b az új pályablokk méret
      */
     public abstract void changeBlockSize(int b);

     /**
      * Getter az adott objektum X koordinátájára.
      * @return az X koordináta
      */
     public abstract int getX();

     /**
      * Getter az adott objektum Y koordinátájára.
      * @return az Y koordináta
      */
     public abstract int getY();
}
