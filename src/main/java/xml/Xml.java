package xml;

import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;

/**
 * Created by Alex on 28.11.2016.
 */
 public class Xml {
    public static Object load(File file, Class cl) {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper xmlMapper = new XmlMapper(module);
        Object readed = null;
        try {
            readed = xmlMapper.readValue(file, cl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return readed;
    }
    public  static void save(File file, Object object) {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(true);

        XmlMapper xmlMapper = new XmlMapper(module);
        try {
            xmlMapper.writerWithDefaultPrettyPrinter().writeValue(file, object);
            System.out.println("Сохранено успешно!");
        } catch (IOException e) {
           e.printStackTrace();
        }
    }
}
