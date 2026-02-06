package Pi;


import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class main {

    public void main(String[] args) throws Exception {
        // long total = 0;

        // 10 workers avec 50000 itérations par worker
        // total = new Master().doRun(50000, 10);

        // total = new Pi.Master().doRun(50000, 10);
        // System.out.println("total from Master = " + total);

        this.getExperience(120000000,  new ArrayList<>(Arrays.asList(1,2,3, 4, 8, 16)), "./Fichier/fichier.csv");




    }


    public ArrayList calcul(Integer nTotal, Integer nbProcessus) throws Exception {

        ArrayList resultat = new Master().doRunTimeErreur(nTotal, nbProcessus);
        return resultat;
    }


    public void getExperience(Integer nTotal, ArrayList<Integer> nbProcessus, String EmplacementFichier) throws Exception {

        try (PrintWriter writer = new PrintWriter(EmplacementFichier)) {
            // entêtes
            StringBuilder header = new StringBuilder();
            header.append("Notal;TotalCount;P;TPS;Erreur");

            writer.println(header.toString());

            for (int i = 0; i < nbProcessus.size(); i++) {

                for (int j = 0; j < 10; j++) {
                    StringBuilder line = new StringBuilder();
                    ArrayList liste = calcul(nTotal / nbProcessus.get(i), nbProcessus.get(i));
                    line.append(nTotal+ ";");
                    line.append((nTotal/nbProcessus.get(i))+ ";");
                    line.append(nbProcessus.get(i) + ";");
                    line.append(liste.get(1)+ ";");
                    line.append(liste.get(0));
                    writer.println(line.toString());
                }
            }
        }


    }

}



