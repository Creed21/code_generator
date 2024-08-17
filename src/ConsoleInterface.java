import generator.DBConnector;
import generator.GenerateEntities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ConsoleInterface {

    private DBConnector dBConnector;
    private GenerateEntities entitiesGenerator;


    public void consoleMenuTest() throws IOException {
        String dbUrl = "jdbc:postgresql://localhost:5432/final_p";
        String dbUser = "postgres";
        String dbPassword = "$@postgres$@";

        dBConnector = DBConnector.getInstance(dbUrl, dbUser, dbPassword);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Odaberite šemu tako što ćete uneti njen naziv:");
        System.out.println("Dostupne šeme: " + dBConnector.readSchemas());

        System.out.println("Unesite naziv šeme:");

        String schema = br.readLine().trim();

        System.out.println("Unesite naziv tabela za koje želite da se generiše kod koji odgovara domenskim klasama :");
        System.out.println("Dostupne tabela: " + dBConnector.readTables(schema)+"\n");
        System.out.println("Unesite nazive tabela:");
        String tables = br.readLine().trim();
        List<String> tableNames = List.of(tables.split(", "));

        System.out.println("Unesite naziv paketa u kome želite da se generiše kod:");

        String packageName = br.readLine();
        // create generate entities class
        entitiesGenerator = new GenerateEntities(dBConnector);

        System.out.println("Unesite putanju do foldera u koji želite da se generiše kod:");
        String path = br.readLine().trim();
        entitiesGenerator.generateFiles(path, packageName, schema, tableNames);
    }

    public static void main(String[] args) {
        ConsoleInterface consoleInterface = new ConsoleInterface();

        try {
            consoleInterface.consoleMenuTest();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}