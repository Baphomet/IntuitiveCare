package src;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Etapa 2 – Transformação e Validação dos dados.
 *
 * Responsável por:
 * - Ler o consolidado de despesas gerado na etapa 1
 * - Relacionar despesas com o cadastro oficial da ANS
 * - Agregar valores por operadora (Razão Social + UF)
 * - Calcular total, média trimestral e desvio padrão
 * - Gerar arquivo CSV final ordenado por maior despesa
 */
public class MainTransformacaoValidacao {

    /**
     * Estrutura de agregação das despesas por operadora.
     */
    static class Registro {
        String razaoSocial;
        String uf;
        List<Double> valores = new ArrayList<>();
    }

    /**
     * Estrutura de dados do cadastro de operadoras da ANS.
     */
    static class CadastroANS {
        String registroANS;
        String razaoSocial;
        String uf;
    }

    public static void main(String[] args) throws Exception {

        // Entrada: consolidado de despesas (Etapa 1)
        String consolidadoPath =
                "1_integracao_api/data/output/consolidado_despesas.csv";

        // Entrada: cadastro oficial da ANS
        String cadastroPath =
                "2_transformacao_validacao/data/raw/Relatorio_cadop.csv";

        // Saída: despesas agregadas por operadora
        String outputPath =
                "2_transformacao_validacao/data/output/despesas_agregadas.csv";

        new File("2_transformacao_validacao/data/output").mkdirs();

        Map<String, CadastroANS> cadastroMap = carregarCadastro(cadastroPath);
        Map<String, Registro> agregados = new HashMap<>();

        // Leitura do consolidado de despesas
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(consolidadoPath), StandardCharsets.UTF_8))) {

            reader.readLine(); // ignora header

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

        // Cálculo dos indicadores estatísticos
        for (Registro r : agregados.values()) {

            double total = r.valores.stream()
                    .mapToDouble(Double::doubleValue)
                    .sum();

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

        // Ordenação por maior total de despesas
        saida.sort((a, b) ->
                Double.compare(
                        Double.parseDouble(b[2]),
                        Double.parseDouble(a[2])
                )
        );

        // Escrita do CSV final
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

    /**
     * Carrega o cadastro oficial da ANS e indexa pelo Registro ANS.
     *
     * @param path caminho do arquivo Relatorio_cadop.csv
     * @return mapa de Registro ANS para dados cadastrais
     */
    private static Map<String, CadastroANS> carregarCadastro(String path) throws Exception {

        Map<String, CadastroANS> map = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8))) {

            String[] header = reader.readLine()
                    .replace("\"", "")
                    .split(";");

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
