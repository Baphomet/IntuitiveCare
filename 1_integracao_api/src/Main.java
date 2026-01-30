package src;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {

    public static void main(String[] args) throws Exception {

        String[] arquivos = {
                "1_integracao_api/data/raw/1T2025.csv",
                "1_integracao_api/data/raw/2T2025.csv",
                "1_integracao_api/data/raw/3T2025.csv"
        };

        File outputDir = new File("1_integracao_api/data/output");
        outputDir.mkdirs();

        File output = new File(outputDir, "consolidado_despesas.csv");

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8))) {

            writer.write("CNPJ;RazaoSocial;Trimestre;Ano;ValorDespesas");
            writer.newLine();

            for (String caminho : arquivos) {
                processarArquivo(caminho, writer);
            }
        }

        System.out.println("Consolidação finalizada com sucesso.");
    }

    private static Integer findIndex(Map<String, Integer> idx, String... possiveis) {
        for (String p : possiveis) {
            if (idx.containsKey(p)) return idx.get(p);
        }
        return null;
    }


    private static void processarArquivo(String caminho, BufferedWriter writer) {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(caminho), StandardCharsets.UTF_8))) {

            String header = reader.readLine();
            if (header == null) return;

            String[] cols = header.replace("\"", "").split(";");
            Map<String, Integer> idx = new HashMap<>();

            for (int i = 0; i < cols.length; i++) {
                idx.put(cols[i].trim().toUpperCase(), i);
            }

            Integer idxData = idx.containsKey("DATA") ? idx.get("DATA") : idx.get("DT_REFERENCIA");
            Integer idxCnpj = idx.containsKey("CNPJ") ? idx.get("CNPJ") : idx.get("REG_ANS");
            Integer idxDescricao = idx.containsKey("DESCRICAO") ? idx.get("DESCRICAO") : idx.get("DS_CONTA");
            Integer idxValor = idx.get("VL_SALDO_FINAL");

            if (idxData == null || idxCnpj == null || idxDescricao == null || idxValor == null) {
                System.err.println("Estrutura incompatível: " + caminho);
                return;
            }

            String linha;
            while ((linha = reader.readLine()) != null) {

                String[] c = linha.replace("\"", "").split(";");
                if (c.length <= idxValor) continue;

                String descricao = c[idxDescricao].toUpperCase();
                if (!descricao.contains("EVENTO") && !descricao.contains("SINISTRO")) continue;

                String valorStr = c[idxValor]
                        .replace(".", "")
                        .replace(",", ".");

                double valor;
                try {
                    valor = Double.parseDouble(valorStr);
                } catch (Exception e) {
                    continue;
                }

                if (valor == 0) continue;
                valor = Math.abs(valor);

                String data = c[idxData];
                int ano = Integer.parseInt(data.substring(0, 4));
                int mes = Integer.parseInt(data.substring(5, 7));
                int trimestre = (mes - 1) / 3 + 1;

                String cnpj = c[idxCnpj];
                String razaoSocial = "N/A";

                writer.write(String.format(
                        "%s;%s;%d;%d;%.2f",
                        cnpj,
                        razaoSocial,
                        trimestre,
                        ano,
                        valor
                ));
                writer.newLine();
            }

        } catch (Exception e) {
            System.err.println("Erro ao processar: " + caminho);
            e.printStackTrace();
        }
    }
}