package com.antoine.io;

import java.io.*;
import java.net.URL;

public class IOHelper {

    public static String readFile(File file)
    {
        StringBuffer data = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(
                new FileReader(file)))
        {
            String buff = reader.readLine();
            while (buff != null)
            {
                data.append(buff);
                buff = reader.readLine();
            }

        }catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        return data.toString();
    }

    public static String reeadResourceLines(InputStream resourceStream, String collapse)
    {
        StringBuffer data = new StringBuffer();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resourceStream)))
        {
            String buff = reader.readLine();
            while (buff != null)
            {
                data.append(buff+",");
                buff = reader.readLine();
            }

        }catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        return data.toString();
    }


    public static void writeFile(File file, String data)
    {
        try(BufferedWriter writer =  new BufferedWriter(
                new FileWriter(file)))
        {
            writer.write(data);

        }catch(Throwable throwable){
            throw new RuntimeException(throwable);
        }
    }
}
