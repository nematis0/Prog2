import java.io.File;

public class Makefile {
    public static void main(String[] args) {
        if (args.length != 2 || !args[1].equals("--reset")) {
            System.out.println("Hasznald mar jol a kodot pls: java Makefile.java <mappa elerese> --reset");
            return;
        }

        String directoryPath = args[0];
        File directory = new File(directoryPath);

        if (!directory.exists() || !directory.isDirectory()) {
            System.out.println("Az adott eleresi utad nem egy mappa.");
            return;
        }

        deleteHTMLFiles(directory);
        System.out.println("Sikeresen torolted HTML file-kat " + directoryPath + " mappabol.");
    }

    private static void deleteHTMLFiles(File directory) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteHTMLFiles(file);
                } else {
                    if (file.getName().toLowerCase().endsWith(".html") || file.getName().equals("index.html")) {
                        if (file.delete()) {
                            System.out.println("Torolve" + file.getAbsolutePath());
                        } else {
                            System.out.println("Nem sikerult he" + file.getAbsolutePath());
                        }
                    }
                }
            }
        }
    }
}
