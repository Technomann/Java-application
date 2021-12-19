package test;

import character.Inky;
import character.PacMan;
import engine.Save;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class PacManTest {

    private final Inky inky;
    private final PacMan pacMan;
    private final short[] n = {0,0,1,0,2,0,3,0,4,0,5,0,6,0,7,0,8,0,9,0,10,0,11,0,12,0,13,0,14,0,15,0,16,0,
            0,1,8,1,16,1,
            0,2,2,2,3,2,5,2,6,2,8,2,10,2,11,2,13,2,14,2,16,2,
            0,3,2,3,3,3,5,3,6,3,8,3,10,3,11,3,13,3,14,3,16,3,
            0,4,16,4,
            0,5,2,5,3,5,5,5,7,5,8,5,9,5,11,5,13,5,14,5,16,5,
            0,6,5,6,8,6,11,6,16,6,
            0,7,1,7,2,7,3,7,5,7,6,7,8,7,10,7,11,7,13,7,14,7,15,7,16,7,
            3,8,5,8,11,8,13,8,
            0,9,1,9,2,9,3,9,5,9,7,9,8,9,9,9,11,9,13,9,14,9,15,9,16,9,
            0,10,7,10,9,10,16,10,
            0,11,1,11,2,11,3,11,5,11,7,11,8,11,9,11,11,11,13,11,14,11,15,11,16,11,
            3,12,5,12,11,12,13,12,
            0,13,1,13,2,13,3,13,5,13,7,13,8,13,9,13,11,13,13,13,14,13,15,13,16,13,
            0,14,7,14,9,14,16,14,
            0,15,1,15,3,15,4,15,5,15,11,15,12,15,13,15,15,15,16,15,
            0,16,1,16,3,16,7,16,8,16,9,16,13,16,15,16,16,16,
            0,17,5,17,8,17,11,17,16,17,
            0,18,2,18,3,18,4,18,5,18,6,18,8,18,10,18,11,18,12,18,13,18,14,18,16,18,
            0,19,16,19,
            0,20,1,20,2,20,3,20,4,20,5,20,6,20,7,20,8,20,9,20,10,20,11,20,12,20,13,20,14,20,15,20,16,20
    };

    private final int BLOCK_SIZE = 20;

    public PacManTest(){

        pacMan = new PacMan("hello", BLOCK_SIZE, n);

        inky = new Inky(BLOCK_SIZE);

        ///teszt metódusok
        testSetCentre();
        testPointIncrease();
        testLivesMaxThree();
        testMove();
        testDirectionMove();
        testDirection();
        testOppositeWall();
        testGhostMove();
        testAgeIncrease();
        testLivesMinNull();
    }

    @Test
    private void testSetCentre(){
        inky.move(3);
        inky.setCentre();
        Assert.assertEquals(inky.getX(),8*BLOCK_SIZE,0);
        Assert.assertEquals(inky.getY(), 10*BLOCK_SIZE,0);
    }

    @Test
    private void testPointIncrease(){
        pacMan.increasePoints(true);
        Assert.assertEquals(500, pacMan.getPoints(),0);
    }

    @Test
    private void testLivesMaxThree(){
        for(int i = 0; i<10; ++i)
            pacMan.changeLivesNumber(true);
        Assert.assertEquals(pacMan.getLives(), 3, 0);
    }

    @Test
    private void testMove(){
        pacMan.setCentre();
        for( int  i = 0; i< 10; ++i)
            pacMan.move();
            Assert.assertEquals(pacMan.getX(), 8*BLOCK_SIZE+10,0);
    }

    @Test
    private void testDirectionMove(){
        pacMan.setDirection(1);
        for(int i = 0; i<10; ++i)
            pacMan.move();
        Assert.assertEquals(pacMan.getX(), 8*BLOCK_SIZE, 0);
    }

    @Test
    private void testDirection(){
        for(int  i = 0; i<12; ++i)
            pacMan.setDirection(i%4 + 1);
        for(int i = 0; i<10; ++i)
            pacMan.move();

        Assert.assertEquals(pacMan.getX(), 8*BLOCK_SIZE+10,0);
    }

    @Test
    private void testOppositeWall(){
        pacMan.setCentre();
        for(int i = 0; i<6000;++i)
            pacMan.move();
        Assert.assertEquals(pacMan.getX(),10*BLOCK_SIZE,0);
    }

    @Test
    private void testGhostMove(){
        for(int i = 0; i<BLOCK_SIZE; ++i)
            inky.move(1);
        Assert.assertEquals(inky.getY(), 9*BLOCK_SIZE,0);
    }

    @Test
    private void testAgeIncrease(){
        Save s = new Save("hello");

        for(int i = 0; i<31; ++i)
            s.incrementAge();
        Assert.assertEquals(s.getAge(), 1, 0);
    }

    @Test
    private void testLivesMinNull(){
        for(int i = 0; i< 10; ++i)
            pacMan.changeLivesNumber(false);
        Assert.assertEquals(pacMan.getLives(),0,0);
    }
}
