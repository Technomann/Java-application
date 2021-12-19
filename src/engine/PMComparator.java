package engine;

import character.PacMan;

import java.util.Comparator;

/**
 * \class PMComparator
 * \brief PacMan kompatárot a ranglistáhopz
 *
 * Ezen osztályon keresztül tudunk összehasonlítani két PacMan-t, ehhez a Comparator interfészt valósítja meg.
 */
public class PMComparator implements Comparator<PacMan> {
    /**
     * A metódus összehasonlít két PacMan-t azok pontszáma alapján. A csökkenő sorrendnek megfelelően hasonlít össze.
     * @param o1 a bal oldali PacMan
     * @param o2 a jobb oldali PacMan
     * @return negatív, ha o2 kisebb, mint o1; 0, ha egyenlőek; pozitív, ha 02 nagyobb, mint o1
     */
    @Override
    public int compare(PacMan o1, PacMan o2) {
        return Long.compare(o2.getPoints(), o1.getPoints());
    }
}
