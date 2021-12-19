package engine;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

/**
 * \class Game
 * \brief A játéknak összefglaló keretet adó osztály.
 *
 * Ez az osztály alkot keretet, a menü, és a játékpanel körül. Nincs sok funkciója, de itt állítható be a pályablokk
 * méret is. A játékban az X koordináta tengely balról jobbra, az Y fentről lefele növekszik.
 */
public class Game {

    private static int GAME_FRAME_COLUMN = 17; /**< NE SZERKESZD A panelek szélessége blokkszámban*/
    private static int GAME_FRAME_ROW = 24; /**< NE SZERKESZD A panelek magassága blokkszámban*/
    private static int BLOCK_SIZE = 30; /**< Ez szerkeszthető, de legyen 3-mal osztható, és páros: a pályablokk mérete, pixelben */

    /**
     * Az osztály konstruktora. Itt inicializálódik a játékkeret, és a menükeret is.
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws LineUnavailableException
     * @throws UnsupportedAudioFileException
     */
    public Game() throws IOException, ClassNotFoundException, LineUnavailableException, UnsupportedAudioFileException {

        GameFrame game = new GameFrame(GAME_FRAME_COLUMN, GAME_FRAME_ROW, BLOCK_SIZE);
        MenuFrame menu = new MenuFrame(GAME_FRAME_COLUMN, GAME_FRAME_ROW, 30, game);
        game.giveMenuFrame(menu);
    }
}
