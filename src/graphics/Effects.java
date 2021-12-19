package graphics;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * \class Pictures
 * \brief A játékban felhasznált képeket tároló osztály.
 *
 * Ez az osztály felel a játékban használt képek és hangok tárolásáért. Három enumerációt használ, melyek segítségével könnyen lehet a PacMan,
 * illetve az egyéb pályaelemek, szellemek, falak képeit, illetve hangjait tárolni, majd elérni. Minden kép a picture_context nevű mappában
 * foglal helyet, minden hang, pedig az audio_context mappában. A képek 90%-át magamnak készítettem, a maradékot (WALL, GATE, HEART) pedig a Google-el találtam.
 * A hangokat online kerestem.
 */
public class Effects {
    /**
     * Az osztály konstruktora. Inicializálja a Statikus, illetve Dinamikus (felhasználás szerint) képeket.
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    public Effects() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        initStaticsPictures();
        initDynamicsPictures();
        initSounds();
    }

    /**
     * A statikus elemeket tartalmazó enumeráció. Minden elem statikus, ami nem a PacMan megjelenítéséhez tartozó kép.
     * Ezek képtípus szerint érhetőek el az enumerációban.
     */
    public enum Statics{
        WALL, /**< A pályán megjelenő falak képe*/
        HEART, /**< Az eredményjelző tálbán megjelenő szívek képe*/
        GATE, /**< A pályán megjelenő kapu képe*/
        DOT, /**< A pályán megjelenő pontok képe*/
        INKY, /**< A pályán megjelenő Inky szellem képe*/
        PINKY, /**< A pályán megjelenő Pinky szellem képe*/
        BLINKY, /**< A pályán megjelenő Blinky szellem képe*/
        CLYDE, /**< A pályán megjelenő Clyde szellem képe*/
        POWER; /**< A pályán megjelenő extra pontok képe*/

        private Image kep = null; /**< A típusonkénti kép attribútum*/

        /**
         * Getter a típusonkétni képre - elérés pl.: Picture.Statics.INKY.getStatPic();
         * @return a típusnak megfelelő kép
         */
        public Image getStatPic(){
            return kep;
        }
    }

    /**
     * A dinamikusabb, a PacMan-hez tartalmazó képek enumerációja. Irányonként tartozik hozzá egy tároló, aminek a segítségével
     * a Timer minden tick-jére lépteti a tároló elemeit, így meganimálva a PacMan szájmozgását.
     */
    public enum Dynamics{
        PAC_UP, /**< A PacMan felfele irányának leképezése*/
        PAC_DOWN, /**< A PacMan lefele irányának leképezése*/
        PAC_RIGHT, /**< A PacMan jobbra irányának leképezése*/
        PAC_LEFT; /**< A PacMan balra irányának leképezése*/


        private final ArrayList<BufferedImage> kep = new ArrayList<>(); /**< Az iránytípusonkétni tároló*/

        /**
         * Getter a tároló típusonként tárolt elemeire, indexelhetően.  Elérés pl.: Pictures.Dynamics.PAC_UP.getDynPic(0);
         * @param i az indexe az irány szerint elérni kívánt képnek
         * @return az adott indexhez, és irányhoz tartozó kép
         */
        public BufferedImage getDynPic(int i){
            return kep.get(i);
        }
    }

    /**
     * A PacMan hangjait tartalmazó enumeráció.
     */
    public enum Sounds{
        DEATH,
        BEGINNING,
        COIN,
        EXTRA;

        private AudioInputStream audioIS;
        private Clip clip;

        public Clip getClip(){
            return clip;
        }
    }

    /**
     * Inicializáló függvény a PacMan hangjaira. Minden Timer tick-re lefut, hogy mindig le lehessen játszani a
     * hangfájlokat.
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    public void initSounds() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        ///Beginning
        Sounds.BEGINNING.audioIS = AudioSystem.getAudioInputStream(new File(
                System.getProperty("user.dir") + File.separator +"audio_context"+File.separator+"pacman_begin.wav"));
        Sounds.BEGINNING.clip = AudioSystem.getClip();
        Sounds.BEGINNING.clip.open(Sounds.BEGINNING.audioIS);

        ///DEATH
        Sounds.DEATH.audioIS = AudioSystem.getAudioInputStream(new File(
                System.getProperty("user.dir") + File.separator +"audio_context"+File.separator+"pacman_death.wav"));
        Sounds.DEATH.clip = AudioSystem.getClip();
        Sounds.DEATH.clip.open(Sounds.DEATH.audioIS);

        ///EXTRA
        Sounds.EXTRA.audioIS = AudioSystem.getAudioInputStream(new File(
                System.getProperty("user.dir") + File.separator +"audio_context"+File.separator+"pacman_extra.wav"));
        Sounds.EXTRA.clip = AudioSystem.getClip();
        Sounds.EXTRA.clip.open(Sounds.EXTRA.audioIS);

        ///COIN
        Sounds.COIN.audioIS = AudioSystem.getAudioInputStream(new File(
                System.getProperty("user.dir") + File.separator +"audio_context"+File.separator+"pacman_coin.wav"));
        Sounds.COIN.clip = AudioSystem.getClip();
        Sounds.COIN.clip.open(Sounds.COIN.audioIS);
    }

    /**
     * Ez a metódus felel a statikus képek inicializálásért. Sajnos, tekintve, hogy minden kép egyedi, és egy van belőlük, nem igazán tudtam
     * kevésbé "sormintává" tenni.
     */
    private void initStaticsPictures(){
        Statics.HEART.kep = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir")+File.separator+"picture_context"+File.separator+"heart.png");
        Statics.WALL.kep = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir")+File.separator+"picture_context"+File.separator+"block.png");
        Statics.DOT.kep = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir")+File.separator+"picture_context"+File.separator+"dotPic.png");
        Statics.GATE.kep = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir")+File.separator+"picture_context"+File.separator+"gate.png");
        Statics.INKY.kep = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir")+File.separator+"picture_context"+File.separator+"inky.png");
        Statics.PINKY.kep = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir")+File.separator+"picture_context"+File.separator+"pinky.png");
        Statics.BLINKY.kep = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir")+File.separator+"picture_context"+File.separator+"blinky.png");
        Statics.CLYDE.kep = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir")+File.separator+"picture_context"+File.separator+"clyde.png");
        Statics.POWER.kep = Toolkit.getDefaultToolkit().createImage(
                System.getProperty("user.dir")+File.separator+"picture_context"+File.separator+"powerPellet.png");
    }

    /**
     * Ez a metódus felel a dinamikus, PacMan-hez tartozó képek inicializálásáért. Itt már sokkal jobban tudtam ciklusba szervezni
     * a képek beolvasását.
     * @throws IOException
     */
    private void initDynamicsPictures() throws IOException {
        String[] s = {"PacMan1", "PacMan2down","PacMan3down", "PacMan4down", "PacMan3down", "PacMan2down",
                "PacMan1", "PacMan2left", "PacMan3left", "PacMan4left", "PacMan3left", "PacMan2left",
                "PacMan1", "PacMan2right", "PacMan3right", "PacMan4right", "PacMan3right", "PacMan2right",
                "PacMan1", "PacMan2up", "PacMan3up", "PacMan4up", "PacMan3up", "PacMan2up"
        };

        for(int i = 0; i<24; ++i){
            if(i < 6) {
                Dynamics.PAC_DOWN.kep.add(ImageIO.read(new File(
                        System.getProperty("user.dir") + File.separator + "picture_context" + File.separator + s[i] + ".png")));
            }else if(i<12){
                Dynamics.PAC_LEFT.kep.add(ImageIO.read(new File(
                        System.getProperty("user.dir")+File.separator+"picture_context"+File.separator + s[i] + ".png")));
            }else if(i<18){
                Dynamics.PAC_RIGHT.kep.add(ImageIO.read(new File(
                        System.getProperty("user.dir")+File.separator+"picture_context"+File.separator + s[i] + ".png")));
            }else{
                Dynamics.PAC_UP.kep.add(ImageIO.read(new File(
                        System.getProperty("user.dir")+File.separator+"picture_context"+File.separator + s[i] + ".png")));
            }
        }
    }
}
