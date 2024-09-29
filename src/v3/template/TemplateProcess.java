package v3.template;

import v2.generator.DBConnector;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

public class TemplateProcess {

    private DBConnector dBConnector;
    private TemplateProcessor templateProcessor;

    public TemplateProcess() {
        this.templateProcessor = new TemplateProcessor();
    }

    public void processTemplate() throws IOException {
        String dbUrl = "jdbc:postgresql://localhost:5432/final_p";
        String dbUser = "postgres";
        String dbPassword = "$@postgres$@";

        dBConnector = DBConnector.getInstance(dbUrl, dbUser, dbPassword);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Generator V3 - šabloni za generisanje koda na osnovu relacione baze");
        System.out.println("Odaberite šemu tako što ćete uneti njen naziv:");
        System.out.println("Dostupne šeme: " + dBConnector.readSchemas());
        System.out.println("Unesite naziv šeme:");
        String schema = br.readLine().trim();

        System.out.println("Unesite naziv tabela za koje želite da se generiše kod koji odgovara domenskim klasama :");
        System.out.println("Dostupne tabela: " + dBConnector.readTables(schema));
        System.out.println("Unesite nazive tabela:");
        String tables = br.readLine().trim();
        List<String> tableNames = List.of(tables.split(", "));

        System.out.println("Unesite naziv paketa u kome želite da se generiše kod:");
        String packageName = br.readLine();

        System.out.println("Unesite putanju do foldera u koji želite da se generiše kod:");
        String path = br.readLine().trim();

        List<File> jsonTemplateConfigs = templateProcessor.chooseJsonFiles();

        templateProcessor.processTemplates(jsonTemplateConfigs, path, packageName, schema, tableNames);

    }

    public static void main(String[] args) {

        TemplateProcess templateProcess = new TemplateProcess();

        try {
            templateProcess.processTemplate();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
