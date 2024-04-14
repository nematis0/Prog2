import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class P2Project
{
//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%argumentumos kezeles es tarsai%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//

    public static void main(String[] args) 
    {
        if (args.length < 1) 
        {
            System.out.println("Hasznald mar jol a kodot pls: java ImageHtmlGenerator <mappa elerese>");
            return;
        }

        String rootDirectoryPath = args[0];
        File rootDirectory = new File(rootDirectoryPath);

        if (!rootDirectory.exists()) 
        {
            System.out.println("Figyelj mar ez nem is eleresi utvonal");
            return;
        }

        if (!rootDirectory.isDirectory()) 
        {
            System.out.println("Remelem csak viccelsz eleresi utat tudod, de a konyvtarat nem?");
            return;
        }

        processDirectory(rootDirectory, rootDirectoryPath);
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%itt dolgozza fel a fileokat%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//
//leiras, ha tovabb adodna a kodom, ez a konyvtarban talalhato fileok feldolgozasaert felel amely rekurzivan jarja be a konyvtarak+almappait
//directory ez lesz nekem a feldolgozando konyvtaram
//rootDirectoryPath hat errol beszelnem se kell beszel a kod hozzad
//file(aktualis kep), files(feldolgozott file(kepek))
//index(aktualis file indexe) feldolgozott file(kepek) kozott
    private static void processDirectory(File directory, String rootDirectoryPath) 
    {
        File[] files = directory.listFiles();
        if (files != null) 
        {
            for (int i = 0; i < files.length; i++) 
            {
                File file = files[i];
                if (file.isDirectory()) 
                {
                    processDirectory(file, rootDirectoryPath);
                } 
                else if (isImageFile(file)) 
                {
                    createHtmlFile(file, rootDirectoryPath, i, files);
                }
            }

            if (directory.getParentFile() != null) 
            {
                createIndexHtml(directory, files, rootDirectoryPath);
            }
        }
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%itt vizsgalom a formatumot%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//

    private static boolean isImageFile(File file) 
    {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png");
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%gyokerkonyvtar+alkonyvtar dolgai%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//

    private static void createHtmlFile(File imageFile, String rootDirectoryPath, int currentIndex, File[] files) 
    {
        try 
        {
            String htmlFileName = imageFile.getName().replaceFirst("[.][^.]+$", ".html");
            File htmlFile = new File(imageFile.getParentFile(), htmlFileName);

            FileWriter writer = new FileWriter(htmlFile);
            writeHeader(writer, rootDirectoryPath);

            writer.write("<hr>\n");

            writer.write("<p><a href=\"index.html\">&#9650</a></p>\n");

            String forwardLink = ""; //elsonek nem igy csinaltam hanem ez 2. if-ben irtam String forwardLink-et de amikor a 112. sort irtam alahuzta a forwardLink-et

            //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*nyilak amivel lehet kozlekedni a kepek kozott-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-//
            if (currentIndex > 0) 
            {
                String backwardLink = files[currentIndex - 1].getName().replaceFirst("[.][^.]+$", ".html");
                writer.write("<p><a href=\"" + backwardLink + "\">&#9665;</a>");
            }

            if (currentIndex < files.length - 1) 
            {
                forwardLink = files[currentIndex + 1].getName().replaceFirst("[.][^.]+$", ".html");
                writer.write("<b>" + imageFile.getName() + "</b>\n");
                writer.write("<a href=\"" + forwardLink + "\" onclick=\"window.location.href='" + forwardLink + "';\">&#9655;</a></p>\n");
            } else 
            {
                writer.write("</p>\n");
            }
            
            //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-//
            
            
            writer.write("<img src=\"" + imageFile.getName() + "\" alt=\"Image\" width=\"300\" height=\"300\" onclick=\"window.location.href='" + forwardLink + "';\">\n");
            writer.write("</body>\n</html>\n");
            writer.close();

        } 
        catch (IOException e) 
        {
            System.out.println("Nem sikerult html file-t letrehozni a kovi kephez hekas" + imageFile.getName());
        }
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%alkönyvtár+images-en beluli dolgok%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//

    private static void createIndexHtml(File directory, File[] files, String rootDirectoryPath) 
    {
        try 
        {
            File indexHtmlFile = new File(directory, "index.html");

            FileWriter writer = new FileWriter(indexHtmlFile);
            writeHeader(writer, rootDirectoryPath);

            //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*bentebb kezdes a soroknak-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-//

            writer.write("<style>\n");
            writer.write(".indented-list { margin-left: 20px; }\n");
            writer.write("</style>\n");

            //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-//

            writer.write("<hr>\n");

            writer.write("<h2>Directories:</h2>\n");

            if (!directory.equals(new File(rootDirectoryPath))) 
            {
                String parentIndexLink = "../index.html";
                writer.write("<p><a href=\"" + parentIndexLink + "\">&#9665;</a></p>");
            }

            //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-egesz egy ul class-al lett megcsinalva hogy a felsorolas diszitve legyen kis egyszer ul/li felsorolas*-*-//

            writer.write("<ul class=\"indented-list\">\n");
            for (File file : files) 
            {
                if (file.isDirectory()) 
                {
                    String subdirectoryIndex = file.getName() + "/index.html";
                    writer.write("<li><a href=\"" + subdirectoryIndex + "\">" + file.getName() + "/</a></li>\n");
                }
            }
            writer.write("</ul>\n");

            writer.write("<hr>\n");

            writer.write("<h2>Images:</h2>\n");

            writer.write("<ul class=\"indented-list\">\n");
            for (File file : files) 
            {
                if (isImageFile(file)) 
                {
                    String imageHtmlLink = file.getName().replaceFirst("[.][^.]+$", ".html");
                    writer.write("<li><a href=\"" + imageHtmlLink + "\">" + file.getName() + "</a></li>\n");
                }
            }
            writer.write("</ul>\n");

            //-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-//

            writer.write("</body>\n</html>\n");
            writer.close();

        } 
        catch (IOException e) 
        {
            System.out.println("Ha azt adom meg neked, hogy hiba akkor szerinted sikerult .html file-t csinalnod a konyvtarnak? " + directory.getName());
        }
    }

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%Start Page header, barmikor nyomunk ra frissul az eredeti .html-re%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%//

    private static void writeHeader(FileWriter writer, String rootDirectoryPath) throws IOException 
    {
        writer.write("<!DOCTYPE html>\n<html>\n<body>\n");
        //writer.write("<h1><a href=\"file:///" + rootDirectoryPath.replace("\\", "/") + "/index.html\">Start Page</a></h1>\n");
        writer.write("<h1><a href=\"file:///" + rootDirectoryPath.replace("\\", "/") + "/index.html\" style=\"text-decoration: none;\">Start Page</a></h1>\n");
    }
}