package character;

import graphics.Effects;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * \class Blinky
 * \brief Blinky szellemet megvalósító osztály
 */
public class Blinky extends Ghost {

    private final short[] route = {
        8,9,8,8,7,8,7,7,7,6,6,6,6,5,6,4,5,4,4,4,3,4,2,4,1,4,
            1,3,1,2,1,1,2,1,3,1,4,1,4,2,4,3,4,4,5,4,6,4,7,4,
            7,3,7,2,7,1,6,1,5,1,4,1,4,2,4,3,4,4,5,4,6,4,
            6,5,6,6,7,6,7,7,7,8,6,8,6,9,6,10,5,10,4,10,
            4,9,4,8,4,7,4,6,3,6,2,6,1,6,1,5,1,4
    };/**< A szellem útvonalát tároló objektum. X,Y koordináta párokat tárol, amik az oszlopszám, sorszám mintát követik.
     Fontos: ezek nem pixel koordináták, hanem blokk koordináták.*/

    private int direction; /**< Az irány, amerre a szellem éppen halad*/

    private int callNum; /** Ennek a változónak a feladata, hogy a szellem tudja, hogy hányadik lépését hajtja végre egy adott blokkon belül.*/
    private int iter; /**< Ezen változó segítségével iterál végig az útvonalon a szellem.*/

    /**
     * Az osztály konstruktora.
     * @param BLOCK_SIZE az objektum létrehozásakor érvényben lévő pályablokk méret
     *
     */
    public Blinky(int BLOCK_SIZE){
        super(8*BLOCK_SIZE, 10*BLOCK_SIZE, BLOCK_SIZE);

        iter = 0;
        callNum = 0;
        direction = 2;
    }

    /**
     * Ez a metúdus a kiindulási pontjába helyezi a szellemet, visszaállítja a kezdőirányt, és nullázza a hívásszámot,
     * és az iterátor értékét.
     */
    @Override
    public void setCentre(){
        X = 8*BLOCK_SIZE;
        Y = 10*BLOCK_SIZE;
        iter = 0;
        callNum = 0;
        direction = 2;
    }

    /**
     * Ez a metódus rajzolja ki Pinky szellem képét a megfelelő koordínátákra, felüldefiniálva az ős ugyanilyen absztrakt
     * metódusát.
     * @param frame_g Graphics2D objektum, amire rajzolunk
     * @param c ImageObserver objektum, rendszerint a hívó objektum
     */
    @Override
    public void draw(Graphics2D frame_g, Object c) {
        frame_g.drawImage(Effects.Statics.BLINKY.getStatPic(), X,Y,BLOCK_SIZE-1,BLOCK_SIZE-1,(ImageObserver) c);
    }

    /**
     * Ez a metódus minden hívsára annyival növeli a direction attribútum szerinti irányba a szellem sebességét, amekkora
     * szinten tart a PacMan.
     * @param level a PacMan szintje
     */
    private void step(int level){
        switch (direction) {
            case 1 -> X -= level;
            case 2 -> Y -= level;
            case 3 -> Y += level;
            default -> X += level;
        }
    }

    /**
     * Ez a metúdus felel annak eldöntéséért, hogy a szellem elért-e már egy adott pályablokk végére, és ha igen, akkor merre folytassa útját,
     * a route attribútum szerint. Itt hívódik a step metódus is. Ezt a heterogén kollekciónak köszönhetően egy for ciklussal
     * a Maze objektum el tudja végezni. A 26 azért kell, mert a központi helyről 13 blokkot megy a körkörös mozgás kezdőpontjáig,
     * viszont nem akarjuk, hogy visszamenjen a középső "zárkába".
     * @param level hányas szinten tart éppen a pacman
     */
    @Override
    public void move(int level) {
        callNum++;
        if(callNum == BLOCK_SIZE/level){
            short tempX = route[iter];
            short tempY = route[iter+1];
            iter+=2;
            callNum = 0;
            if(iter == route.length){
                iter = 26;
            }
            if(tempX > route[iter])
                direction = 1;
            else if(tempX < route[iter])
                direction = 4;
            else if(tempY > route[iter+1])
                direction = 2;
            else
                direction = 3;
        }
        step(level);
    }
}
