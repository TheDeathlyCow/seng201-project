package nz.ac.gitlab.mwa172.seng201.group8.file_manager;

import javax.swing.*;
import java.net.URL;

/**
 * Manages any assets of this application.
 * All assets should be in resources/assets
 */
public class AssetManager {

    /**
     * The icon of the application.
     */
    public static final ImageIcon ICON;

    // loads the icon when this class is loaded into memory
    static {
        ICON = loadIcon();
    }

    /**
     * Loads the file at resources/assets/icon.png, and returns the image icon.
     *
     * @return Returns the icon of this application.
     */
    private static ImageIcon loadIcon() {
        URL image = AssetManager.class.getResource("/assets/icon.png");
        if (image != null)
            return new ImageIcon(image);
        return null;
    }
}
