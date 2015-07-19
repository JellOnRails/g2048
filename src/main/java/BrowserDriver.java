import org.openqa.selenium.WebDriver;


public class BrowserDriver {

    private static String url = "http://gabrielecirulli.github.io/2048/";


    public static void init( WebDriver browser ) {

        browser.get( url );



        browser.quit();
    }



}
