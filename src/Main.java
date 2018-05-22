import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {

    private static final String FILENAME = "/Users/masoud/Documents/Uni/1- Data Mining/qid_tag.txt";

    private static HashMap<Long, String> tagID = new HashMap<>();
    private static HashMap<Integer, String> tagNumber = new HashMap<>();
    private static HashMap<String, Long> tagCount = new HashMap<>();
    private static HashMap<Long, ArrayList<String>> idTags = new HashMap<>();
    private static HashMap<String, Long> tagCouples = new HashMap<>();

    public void readingCreating() {
        BufferedReader br = null;
        FileReader fr = null;
        int number = 0;
        try {
//            br = new BufferedReader(new FileReader(FILENAME));
            fr = new FileReader(FILENAME);
            br = new BufferedReader(fr);

            String currentLine;

            while ((currentLine = br.readLine()) != null) {

                System.out.println(currentLine);
                String[] fields = currentLine.split("\t");

                Long id = Long.parseLong(fields[0]);
                String tag = fields[1];

                tagID.put(id, tag);
                tagNumber.put(number, tag);
                number++;
                if (tagCount.containsKey(id)) {
                    Long count = tagCount.get(id);
                    tagCount.put(tag, count + 1);
                } else {
                    tagCount.put(tag, 1L);
                }
                if (idTags.containsKey(id)) {
                    ArrayList<String> list = idTags.get(id);
                    if (!list.contains(tag)) {

                        ArrayList<String> l = idTags.get(id);
                        l.add(tag);
                        Collections.sort(l);
                        idTags.put(id, l);

//                        idTags.get(id).add(tag);
//                        Collections.sort(idTags.get(id));
                    }
                } else {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(tag);
                    idTags.put(id, list);
                }
            }

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (br != null)
                    br.close();

                if (fr != null)
                    fr.close();

            } catch (IOException ex) {

                ex.printStackTrace();
            }
        }
    }

    public static void coupling() {
        ArrayList<String> l = new ArrayList<String>();
        l.add("Parham");
        l.add("Masoud");
        idTags.put(12L, l);
        Iterator it = idTags.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            ArrayList<String> list = (ArrayList<String>) pair.getValue();
            for (int i = 0; i < list.size(); i++) {
                System.out.println(list.get(i));
                for (int j = i + 1; j < list.size(); j++) {
                    String s = list.get(i) + "-" + list.get(i);
                    if (tagCouples.containsKey(s)) {
                        tagCouples.put(s, tagCouples.get(s) + 1);
                    } else {
                        tagCouples.put(s, 1L);
                    }
                }
            }
        }
    }

    public static void createDesimilarity() {
        int size = tagCount.size();
        double[][] matrix = new double[size][size];
        Iterator it = idTags.entrySet().iterator();

        for (int i = 0; i < tagCount.size(); i++) {
            for (int j = i + 1; j < tagCount.size(); j++) {

                String tag1 = tagNumber.get(i);
                String tag2 = tagNumber.get(j);
                String coupleName = tag1 + "-" + tag2;

                Long l1 = tagCount.get(tag1);
                Long l2 = tagCount.get(tag2);
                Long lCouple;
                try {
                    lCouple = tagCouples.get(coupleName);
                } catch (Exception e) {
//                    System.out.println(e);
                    lCouple = 0L;
                }
                double dissimilarity = 1.0 - (lCouple / (l1 + l2 - lCouple));
                matrix[i][j]= dissimilarity;
            }
        }

    }

    public static void main(String[] args) {
        coupling();
    }
}
























