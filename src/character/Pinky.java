package character;

import graphics.Effects;

import java.awt.*;
import java.awt.image.ImageObserver;

/**
 * \class Pinky
 * \brief Pinky szellemet megvalósító osztály
 */
public class Pinky extends Ghost {

    private final short[] route = {
            8,9,8,8,9,8,10,8,10,9,10,10,10,11,10,12,10,13,10,14,10,15,10,16,11,16,12,16,12,17,13,17,14,17,15,17,
            15,18,15,19,14,19,13,19,12,19,11,19,10,19,9,19,
            9,18,9,17,10,17,10,16,11,16,12,16,12,17,13,17,14,17,
            14,16,14,15,14,14,13,14,12,14,12,13,12,12,12,11,12,10,11,10,10,10,
            10,11,10,12,10,13,10,14,10,15,10,16,11,16,12,16,
            12,17,13,17,14,17,15,17
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
    public Pinky(int BLOCK_SIZE){
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
        frame_g.drawImage(Effects.Statics.PINKY.getStatPic(), X,Y,BLOCK_SIZE-1,BLOCK_SIZE-1,(ImageObserver) c);
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
     * a Maze objektum el tudja végezni. A 36 azért kell, mert a központi helyről 18 blokkot megy a körkörös mozgás kezdőpontjáig,
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
            if(iter == route.length)
                iter = 36;
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
