package ch.so.agi.stac.model.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Utils {
    
    /**
     * Determines if an HREF is absolute or not. May be used on either local file paths or URLs.
     * 
     * @param href
     * @return true if href is absolute, false if href is relative
     */
    public static boolean isAbsoluteHref(String href) {
        if (href.startsWith("http")) {
            return true;
        } else {
            return Paths.get(href).isAbsolute();
        }
    }
    
    public static String makeAbsoluteHref(String sourceHref) {
        return makeAbsoluteHref(sourceHref, null, null);
    }
    
    public static String makeAbsoluteHref(String sourceHref, String startHref, Boolean startIsDir) throws RuntimeException {
        if (startHref == null) {
            startHref = Paths.get(".").toFile().getAbsolutePath();
            startIsDir = true;
        }
        
        if (sourceHref.startsWith("http") || startHref.startsWith("http")) {
            try {
                return makeAbsoluteHrefUrl(sourceHref, startHref, startIsDir);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            return makeAbsoluteHrefPath(sourceHref, startHref, startIsDir);
        }
    }
    
    // TODO Untestet, da ich momentan absolute Href verwende.
    private static String makeAbsoluteHrefPath(String sourceHref, String startHref, Boolean startIsDir) {
        Path sourcePath = Paths.get(sourceHref);
        if (sourcePath.isAbsolute()) {
            return sourceHref;
        }
        
        System.out.println("TODO: Untested makeAbsoluteHrefPath.");
        
        Path startPath = Paths.get(startHref);
        Path startDir;
        if (startPath.toFile().isDirectory()) {
            startDir = startPath;
        } else {
            startDir = startPath.getParent();
        }

        Path absPath = Paths.get(startPath.toFile().getAbsolutePath(), sourcePath.toString());
        return absPath.toFile().getAbsolutePath();   
    }
    
    // TODO Untestet, da ich momentan absolute Href verwende.
    private static String makeAbsoluteHrefUrl(String sourceHref, String startHref, Boolean startIsDir) throws URISyntaxException {
        URI sourceUri = new URI(sourceHref);
        if (sourceUri.isAbsolute()) {
            return sourceHref;
        }
        
        System.out.println("TODO: Untested makeAbsoluteHrefUrl.");
        
        URI startUri = new URI(startHref);
        String startDir;
        if (startIsDir) {
            startDir = startUri.getPath();
        } else {
            startDir = startUri.getPath().substring(0, startUri.getPath().lastIndexOf("/")-1);  
        }
        
        String absPath = Paths.get(startDir, sourceHref).toString();

        String absoluteUrlString = startUri.getScheme() + "://" + absPath;
        return absoluteUrlString;
    }
}
