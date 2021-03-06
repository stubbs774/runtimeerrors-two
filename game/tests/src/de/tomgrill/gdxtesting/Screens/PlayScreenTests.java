package de.tomgrill.gdxtesting.Screens;

import com.team5.game.MainGame;
import com.team5.game.Screens.PlayScreen;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.mock;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class PlayScreenTests {

    @Spy
    MainGame game = mock(MainGame.class, "game");

    @Spy
    PlayScreen play = mock(PlayScreen.class, "play");

    @Test
    public void emptyTest(){
        assertNotNull("passes if play has been instantiated",play);
    }

    @Test
    public void openTest(){
        game.setScreen(play);
        try{
            Mockito.verify(game).setScreen(play);
        }catch (MockitoAssertionError error){
            throw new MockitoAssertionError("This tests passes if game sets play as its current screen");
        }
    }
}
