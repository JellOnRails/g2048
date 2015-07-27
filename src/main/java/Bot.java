import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class Bot {

    private static String url = "http://gabrielecirulli.github.io/2048/";
    private static String game_field = ".game-container";
    private static String game_over = ".game-message.game-over";
    private static String tile = ".tile";
    private static String score = ".score-container";

    public static void main( String []args ) {

        PrintWriter log_writer = logFileOpen();
        WebDriver browser = new FirefoxDriver();
        open( browser );
        WebElement game = browser.findElement(By.cssSelector( game_field ));

        char[] direction = { 'U', 'D', 'L', 'R' };
        Random rand = new Random();

        do {
            int index = rand.nextInt( 4 );
            action( game, direction[ index ], log_writer );
            log( browser, log_writer );
        } while ( isElementPresent( browser, game_over ) );

        getScore( browser, score, log_writer );

        logFileClose( log_writer );
        close(browser);
    }

    public static void open( WebDriver browser ) {
        browser.get( url );
    }

    public static void close( WebDriver browser ) {
        browser.quit();
    }

    public static void action( WebElement game, char direction, PrintWriter log_writer ) {
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
        log_writer.print( direction );
    }

    public static boolean isElementPresent( WebDriver browser, String element ) {
        boolean result = false;
        try {
            browser.findElement(By.cssSelector(element));
        } catch ( org.openqa.selenium.NoSuchElementException exception ) {
            result = true;
        }
        return result;
    }

    public static int countElements( WebDriver browser, String selector ) {
        boolean boo;
        int res = 1;

        do {
            try {
                res = browser.findElements(By.cssSelector(selector)).size();
                boo = false;
            } catch (StaleElementReferenceException ex) {
                boo = true;
            }
        } while (boo);
        return res;
    }

    public static String getAttr( WebDriver browser, String Attr, String selector, int index ) {
        Boolean boo;
        String res = "";
        do {
            try {
                res = browser.findElements(By.cssSelector( selector )).get( index ).getAttribute( Attr );
                boo = false;
            } catch ( StaleElementReferenceException ex ) {
                boo = true;
            }
        } while ( boo );
        return res;
    }

    public static PrintWriter logFileOpen() {
        String file_name = "log.txt";
        PrintWriter log_writer = null;
        try {
            File log_file = new File( file_name );
            if (!log_file.exists()) {
                log_file.createNewFile();
            }
            log_writer = new PrintWriter( log_file );
        } catch ( IOException  e ) {
            System.out.println( "IOException > " + e );
        }
        return log_writer;
    }

    public static void logFileClose( PrintWriter log_writer ) {
        log_writer.close();
    }

    public static void log( WebDriver browser, PrintWriter log_writer ) {

        int[][] tiles_value = {
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 },
                { 0, 0, 0, 0 }
        };
        int el_num = countElements(browser, tile);

        for( int k = 0; k < el_num; k++ ) {
            String tile_class = getAttr(browser, "class", tile, k);
            String digits = tile_class.replaceAll("\\D+", " ").trim();
            String[] dig_list = digits.split("\\s+");

            tiles_value[Integer.parseInt(dig_list[2]) - 1][Integer.parseInt(dig_list[1]) - 1] = Integer.parseInt(dig_list[0]);

            el_num = countElements(browser, tile);
        }

        for( int[] x : tiles_value ) {
            log_writer.println();
            for ( int y : x ) {
                log_writer.print( " | " + y );
            }
            log_writer.print(" | ");
        }
        log_writer.println();
    }

    public static void getScore( WebDriver browser, String selector, PrintWriter log_writer ) {
        String script = "return document.querySelector('"+selector+"').innerHTML";
        String value = ((JavascriptExecutor)browser).executeScript(script).toString();
        String score = value.replaceAll("<.*>$", "");
        log_writer.println( "\nfinal score = "+score );
    }

}
