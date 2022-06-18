package Utils;

import java.io.*;
import java.util.ArrayList;

public class FilesHandler {

    public static String[] readFile(String fileName) {
        String path = "src/main/java/Files/InFiles/";
        FileReader fr;
        ArrayList<String> listaLineasArchivo = new ArrayList<String>();
        try {
            fr = new FileReader(path + fileName);
            BufferedReader br = new BufferedReader(fr);
            String lineaActual = br.readLine();
            while (lineaActual != null) {
                listaLineasArchivo.add(lineaActual);
                lineaActual = br.readLine();

            }
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo "
                    + fileName);
            e.printStackTrace();
        }

        return listaLineasArchivo.toArray(new String[0]);
    }

    public static void writeFile(String fileName, String[] fileLines) throws InterruptedException {

        String path = "src/main/java/Files/OutFiles/";
        File archivo = new File(path + fileName + ".txt");
        FileWriter fw;
        try {
            fw = new FileWriter(archivo, true);
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < fileLines.length; i++) {
                String lineaActual = fileLines[i];
                bw.write(lineaActual);
                bw.newLine();
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo "
                    + fileName);
            e.printStackTrace();
        }

    }

}