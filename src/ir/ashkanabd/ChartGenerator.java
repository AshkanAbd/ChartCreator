package ir.ashkanabd;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class ChartGenerator {
    private Chart chart;
    private LinkedHashMap<Set<String>, Chart> result;
    private int min;
    private int max;
    private String[] includes;

    public ChartGenerator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public void includes(String... includes) {
        this.includes = includes;
    }

    public void generate() throws Exception {
        List<Vahed> vaheds = parseJson();
        this.result = new LinkedHashMap<>();
        Vahed[] vahedList = new Vahed[vaheds.size()];
        for (int i = 0; i < vahedList.length; i++) {
            vahedList[i] = vaheds.get(i);
        }
        for (int i = 0; i < vahedList.length; i++) {
            List<Vahed> vList = Arrays.asList(vahedList);
            createChart(vList);
            vahedList = rollArray(vahedList);
        }
    }

    public void sortResult() {
        result = result.entrySet().stream().sorted(Map.Entry.comparingByValue(this::compareChart))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (o1, o2) -> o1, LinkedHashMap::new));
    }

    private int compareChart(Chart o1, Chart o2) {
        return Integer.compare(o1.sum, o2.sum);
    }

    public void showResult() {
        int counter = 0;
        for (Map.Entry<Set<String>, Chart> e : result.entrySet()) {
            boolean check = true;
            if (includes != null && includes.length != 0) {
                for (String s : includes) {
                    check = check && e.getKey().contains(s);
                }
            }
            if (!check) continue;
            System.out.println(e.getValue());
            System.out.println();
            System.out.println("####################");
            System.out.println();
            counter++;
        }
        System.out.println(counter + " charts generated.");
    }

    private Vahed[] rollArray(Vahed[] vaheds) {
        Vahed[] vaheds1 = new Vahed[vaheds.length];
        for (int i = 0; i < vaheds.length; i++) {
            if (i + 1 < vaheds.length) {
                vaheds1[i + 1] = vaheds[i];
            } else {
                vaheds1[0] = vaheds[i];
            }
        }
        return vaheds1;
    }

    private void createChart(List<Vahed> vahedsList) throws Exception {
        for (int i = 0; i < vahedsList.size(); i++) {
            chart = new Chart();
            add(vahedsList, i);
            if (chart.sum >= min && chart.sum <= max) {
                if (valid(chart.vahedList))
                    result.put(new HashSet<>(chart.vahedList), ((Chart) chart.clone()));
            }
        }
    }

    private void addToChart(List<Vahed> vahedsList) throws Exception {
        if (vahedsList.size() == 0) return;
        for (int i = 0; i < vahedsList.size(); i++) {
            if (chart.sum >= min && chart.sum <= max) {
                if (valid(chart.vahedList)) {
                    result.put((Set<String>) chart.vahedList.clone(), ((Chart) chart.clone()));
                }
            }
            add(vahedsList, i);
        }
    }

    private void add(List<Vahed> vahedsList, int i) throws Exception {
        Vahed v = vahedsList.get(i);
        boolean b = true;
        for (Vahed.Info.Time t : v.info.times) {
            b = b && chart.canAdd(t, v.info.exam, v.name);
        }
        if (b) {
            for (Vahed.Info.Time t : v.info.times) {
                chart.addToDay(t, v.info.exam, v.name);
            }
            chart.sum += v.vahed;
        }
        List<Vahed> vList = new ArrayList<>(vahedsList);
        vList.remove(i);
        addToChart(vList);
    }

    private boolean valid(Set<String> chartVehed) {
        Set<Set<String>> vahedSets = result.keySet();
        for (Set<String> stringSet : vahedSets) {
            if (stringSet.equals(chartVehed)) {
                return false;
            }
        }
        return true;
    }

    private List<Vahed> parseJson() throws Exception {
        JsonReader reader = Json.createReader(new FileReader(new File("info.json")));
        JsonObject rootObj = reader.readObject();
        JsonArray listObj = rootObj.getJsonArray("list");
        List<Vahed> vahedList = new ArrayList<>();
        for (int i = 0; i < listObj.size(); i++) {
            JsonObject obj = listObj.getJsonObject(i);
            Vahed v = new Vahed();
            v.name = obj.getString("name");
            v.vahed = obj.getInt("vahed");
            v.info = new Vahed.Info();
            JsonObject obj1 = obj.getJsonObject("info");
            v.info.exam = obj1.getString("exam");
            v.info.times = new ArrayList<>();
            JsonArray arr = obj1.getJsonArray("time");
            for (int j = 0; j < arr.size(); j++) {
                JsonObject obj2 = arr.getJsonObject(j);
                Vahed.Info.Time t = new Vahed.Info.Time();
                t.day = obj2.getString("day");
                t.time = obj2.getString("time");
                t.odd = obj2.getBoolean("odd");
                t.even = obj2.getBoolean("even");
                v.info.times.add(t);
            }
            vahedList.add(v);
        }
        return vahedList;
    }
}
