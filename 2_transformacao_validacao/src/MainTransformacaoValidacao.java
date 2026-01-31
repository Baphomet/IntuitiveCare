package src;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MainTransformacaoValidacao {

    static class Registro {
        String razaoSocial;
        String uf;
        List<Double> valores = new ArrayList<>();
    }

    static class CadastroANS {
        String registroANS;
        String razaoSocial;
        String uf;
    }

    public static void main(String[] args) throws Exception {

        String consolidadoPath =
                "1_integracao_api/data/output/consolidado_despesas.csv";

        String cadastroPath =
                "2_transformacao_validacao/data/raw/Relatorio_cadop.csv";

        String outputPath =
                "2_transformacao_validacao/data/output/despesas_agregadas.csv";

        new File("2_transformacao_validacao/data/output").mkdirs();

        Map<String, CadastroANS> cadastroMap = carregarCadastro(cadastroPath);
        Map<String, Registro> agregados = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(consolidadoPath), StandardCharsets.UTF_8))) {

            reader.readLine();

            String linha;
            while ((linha = reader.readLine()) != null) {

                String[] c = linha.split(";");
                if (c.length < 5) continue;


                String regANS = c[0].trim();

                double valor;
                try {
                    valor = Double.parseDouble(c[4].replace(",", "."));
                } catch (Exception e) {
                    continue;
                }

                if (valor <= 0) continue;

                CadastroANS cad = cadastroMap.get(regANS);
                if (cad == null) continue;

                String chave = cad.razaoSocial + "|" + cad.uf;
                agregados.putIfAbsent(chave, new Registro());

                Registro r = agregados.get(chave);
                r.razaoSocial = cad.razaoSocial;
                r.uf = cad.uf;
                r.valores.add(valor);
            }
        }

        List<String[]> saida = new ArrayList<>();

        for (Registro r : agregados.values()) {

            double total = r.valores.stream().mapToDouble(Double::doubleValue).sum();
            double media = total / r.valores.size();

            double variancia = 0;
            for (double v : r.valores) {
                variancia += Math.pow(v - media, 2);
            }
            variancia /= r.valores.size();
            double desvio = Math.sqrt(variancia);

            saida.add(new String[]{
                    r.razaoSocial,
                    r.uf,
                    String.format(Locale.US, "%.2f", total),
                    String.format(Locale.US, "%.2f", media),
                    String.format(Locale.US, "%.2f", desvio)
            });
        }


        saida.sort((a, b) ->
                Double.compare(
                        Double.parseDouble(b[2]),
                        Double.parseDouble(a[2])
                )
        );

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(outputPath), StandardCharsets.UTF_8))) {

            writer.write("RazaoSocial;UF;TotalDespesas;MediaTrimestral;DesvioPadrao");
            writer.newLine();

            for (String[] l : saida) {
                writer.write(String.join(";", l));
                writer.newLine();
            }
        }

        System.out.println("Etapa 2 finalizada com sucesso.");
    }

    private static Map<String, CadastroANS> carregarCadastro(String path) throws Exception {

        Map<String, CadastroANS> map = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {

            String[] header = reader.readLine().replace("\"", "").split(";");
            Map<String, Integer> idx = new HashMap<>();

            for (int i = 0; i < header.length; i++) {
                idx.put(header[i].trim().toUpperCase(), i);
            }

            String linha;
            while ((linha = reader.readLine()) != null) {

                String[] c = linha.replace("\"", "").split(";");

                CadastroANS cad = new CadastroANS();
                cad.registroANS = c[idx.get("REGISTRO_OPERADORA")];
                cad.razaoSocial = c[idx.get("RAZAO_SOCIAL")];
                cad.uf = c[idx.get("UF")];

                map.putIfAbsent(cad.registroANS, cad);
            }
        }
        return map;
    }
}
