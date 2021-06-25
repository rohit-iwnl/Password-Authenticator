package passwordauthen;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class OpenWeb {

    public void open(String service)
    {
        try {
            Desktop.getDesktop().browse(URI.create("https://www."+service+".com"));
        } catch (IOException e)
        {
            System.out.println(e);
        }
    }

}
