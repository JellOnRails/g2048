import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.Random;


public class Bot {

    private static String url = "http://gabrielecirulli.github.io/2048/";
    private static String game_field = ".game-container";
    private static String game_over = ".game-message.game-over";

    public static void main(String []args) {

        WebDriver browser = new FirefoxDriver();

        open( browser );

        WebElement game = browser.findElement(By.cssSelector( game_field ));


        char[] direction = { 'U', 'D', 'L', 'R' };
        Random rand = new Random();

        do {
            int index = rand.nextInt( 4 );
            action( game, direction[ index ] );
        } while ( isElementPresent( browser, game_over ) );


        try {
            Thread.sleep( 7000 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        close(browser);
    }

    public static void open( WebDriver browser ) {
        browser.get( url );
    }

    public static void close( WebDriver browser ) {
        browser.quit();
    }

    public static void action( WebElement game, Character direction ) {
        switch ( direction ) {
            case 'U':      game.sendKeys( Keys.ARROW_UP );
                break;
            case 'D':    game.sendKeys( Keys.ARROW_DOWN );
                break;
            case 'L':    game.sendKeys( Keys.ARROW_LEFT );
                break;
            case 'R':   game.sendKeys( Keys.ARROW_RIGHT );
                break;
        }
    }

    public static boolean isElementPresent( WebDriver browser, String element ) {

        Boolean result = false;
        try {
            browser.findElement(By.cssSelector(element));
        } catch ( org.openqa.selenium.NoSuchElementException exception ) {
            result = true;
        }
        return result;
    }

}
