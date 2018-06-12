package kendoUI;



import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static final String LINE = System.getProperty("line.separator");
    public static void main( String[] args )
    {
        String rootDir = "G:\\workspace\\Projects\\mykendo-ui";
        File dir = new File(rootDir);
        List<File> files = new ArrayList<>();
        listFiles( files,dir);

        for (File  file : files){
            if(file.getName().endsWith(".htm")){
                //readWantedText(file);
                try {
                    autoReplace(file,rootDir);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        String t =  "112312   https://demos.telerik.com/kendo-ui/source/index?path=~%2FViews%2Fdemos%2Farea-charts%2Fangular.cshtml";
//        String t2 =  "https://demos.telerik.com/kendo-ui/source/index?path=~%2FViews%2Fdemos%2Farea-charts%2Fangular.cshtml";
//        String newFile = t.replaceAll(t2.replaceAll("\\." ,"\\\\.").replaceAll("\\?" ,"\\\\?"),"../source");
//        System.out.println( newFile);
    }

    private static void autoReplace(File file,String rootDir) throws IOException{
        String resourcePath = rootDir + "/demos.telerik.com/kendo-ui/source";
        Long fileLength=file.length();
        byte[] fileContext=new byte[fileLength.intValue()];
        FileInputStream in=new FileInputStream(file);
        in.read(fileContext);
        in.close();
        String context = new String(fileContext);
        //https://demos.telerik.com/kendo-ui/source/index?path=~%2FViews%2Fdemos%2Farea-charts%2Ferror-bars.cshtml
        String utilRegex = "(https://demos\\.telerik\\.com/kendo-ui/source/index\\?path=.+\\.cshtml)";



        Pattern pattern = Pattern.compile(utilRegex);
        Matcher matcher = pattern.matcher(context);

        String newFile = context;
        boolean flag = false;
        while(matcher.find())
        {
            String url  = matcher.group(1);
            String filePath = url.replaceAll("https://demos\\.telerik\\.com/kendo-ui/source/index\\?path=~","").replaceAll("%2F", "/");
            File resorceFile = new File(resourcePath + filePath);
            System.out.println("URL: " + url + "  filePath = "+ resourcePath + filePath + "    "  + resorceFile.getName() );
            flag = true;
            newFile = newFile.replaceAll(url.replaceAll("\\." ,"\\\\.").replaceAll("\\?" ,"\\\\?"),"../source" + filePath);
//            String context = HttpHelper.httpGet(url);
//            writeFile(context, resourcePath + filePath);
        }

        String regex1 = "(https://demos\\.telerik\\.com/kendo-ui/source/index\\?path=.+\\.cshtml)";
        context = patternFile(context, resourcePath, regex1);
        String regex2 = "(https://demos\\.telerik\\.com/kendo-ui/source/index\\?path=.+\\.html)";
        context = patternFile(context, resourcePath, regex2);
        String regex3 = "(https://demos\\.telerik\\.com/kendo-ui/source/index\\?path=.+\\.cs)";
        context = patternFile(context, resourcePath, regex3);
        writeFile(context, file.getPath());



        //str=str.replace("","");

//        PrintWriter out=new PrintWriter(outPath);
//        out.write(str.toCharArray());
//        out.flush();
//        out.close();
    }

    private static String  patternFile(String content, String resourcePath, String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        while(matcher.find())
        {
            String url  = matcher.group(1);
            String filePath = url.replaceAll("https://demos\\.telerik\\.com/kendo-ui/source/index\\?path=~","").replaceAll("%2F", "/");
            File resorceFile = new File(resourcePath + filePath);
            System.out.println("URL: " + url + "  filePath = "+ resourcePath + filePath + "    "  + resorceFile.getName() );
            content = content.replaceAll(url.replaceAll("\\." ,"\\\\.").replaceAll("\\?" ,"\\\\?"),"../source" + filePath);
            String context = HttpHelper.httpGet(url);
            writeFile(context, resourcePath + filePath);
        }
        return content;
    }



    public static void writeFile(String context, String fileFullName) {

        File downloadFile = new File(fileFullName);
        if(!(downloadFile.exists())){
            downloadFile.getParentFile().mkdirs();
        }
        System.out.println("fileFullName = " + fileFullName);
        //downloadFile.delete();
        try {
           Files.write(Paths.get(fileFullName), context.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }



    }














    static Collection<File> listFiles(File root){
        List<File> files = new ArrayList<File>();
        listFiles(files, root);
        return files;
    }

    static void listFiles(List<File> files, File dir){
        File[] listFiles = dir.listFiles();
        for(File f: listFiles){
            if(f.isFile()){
                files.add(f);
            }else if(f.isDirectory()){
                listFiles(files, f);
            }
        }
    }



}
