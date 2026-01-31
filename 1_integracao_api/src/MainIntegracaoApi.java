package src;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class MainIntegracaoApi {

    public static void main(String[] args) throws Exception {

        // Lista fixa de arquivos CSV a serem consolidados.
        // Os nomes seguem o padrão de trimestre/ano conforme disponibilizado no FTP.
        String[] arquivos = {
                "1_integracao_api/data/raw/1T2025.csv",
                "1_integracao_api/data/raw/2T2025.csv",
                "1_integracao_api/data/raw/3T2025.csv"
        };

        // Garante que o diretório de saída exista antes da escrita do arquivo final
        File outputDir = new File("1_integracao_api/data/output");
        outputDir.mkdirs();

        File output = new File(outputDir, "consolidado_despesas.csv");

        // Escrita do arquivo consolidado em UTF-8 para evitar problemas de encoding
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8))) {

            // Cabeçalho padronizado do CSV final
            writer.write("CNPJ;RazaoSocial;Trimestre;Ano;ValorDespesas");
            writer.newLine();

            // Processa cada arquivo individualmente
            for (String caminho : arquivos) {
                processarArquivo(caminho, writer);
            }
        }

        System.out.println("Consolidação finalizada com sucesso.");
    }

    /**
     * Processa um arquivo CSV individual e escreve os registros válidos
     * diretamente no arquivo consolidado.
     */
    private static void processarArquivo(String caminho, BufferedWriter writer) {

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(caminho), StandardCharsets.UTF_8))) {

            // Leitura do cabeçalho para mapear dinamicamente as colunas
            String header = reader.readLine();
            if (header == null) return;

            String[] cols = header.replace("\"", "").split(";");

            // Mapa coluna -> índice para lidar com variações de layout entre arquivos
            Map<String, Integer> idx = new HashMap<>();
            for (int i = 0; i < cols.length; i++) {
                idx.put(cols[i].trim().toUpperCase(), i);
            }

            // Algumas colunas possuem nomes diferentes dependendo do arquivo,
            // por isso são tratadas com fallback
            Integer idxData = idx.containsKey("DATA") ? idx.get("DATA") : idx.get("DT_REFERENCIA");
            Integer idxCnpj = idx.containsKey("CNPJ") ? idx.get("CNPJ") : idx.get("REG_ANS");
            Integer idxDescricao = idx.containsKey("DESCRICAO") ? idx.get("DESCRICAO") : idx.get("DS_CONTA");
            Integer idxValor = idx.get("VL_SALDO_FINAL");

            // Caso alguma coluna essencial não exista,
            // o arquivo é ignorado para evitar inconsistência nos dados consolidados
            if (idxData == null || idxCnpj == null || idxDescricao == null || idxValor == null) {
                System.err.println("Estrutura incompatível: " + caminho);
                return;
            }

            String linha;
            while ((linha = reader.readLine()) != null) {

                String[] c = linha.replace("\"", "").split(";");

                // Proteção contra linhas quebradas ou com colunas faltantes
                if (c.length <= idxValor) continue;

                // Apenas registros relacionados a eventos ou sinistros são considerados
                String descricao = c[idxDescricao].toUpperCase();
                if (!descricao.contains("EVENTO") && !descricao.contains("SINISTRO")) continue;

                // Normalização do valor monetário para padrão numérico
                String valorStr = c[idxValor]
                        .replace(".", "")
                        .replace(",", ".");

                double valor;
                try {
                    valor = Double.parseDouble(valorStr);
                } catch (Exception e) {
                    // Valores inválidos são ignorados
                    continue;
                }

                // Valores zerados não agregam informação ao consolidado
                if (valor == 0) continue;

                // Garante valor positivo para consolidação
                valor = Math.abs(valor);

                // Extração de ano e trimestre a partir da data (YYYY-MM-DD)
                String data = c[idxData];
                int ano = Integer.parseInt(data.substring(0, 4));
                int mes = Integer.parseInt(data.substring(5, 7));
                int trimestre = (mes - 1) / 3 + 1;

                String cnpj = c[idxCnpj];

                // Razão social não está disponível nos arquivos utilizados
                // e é mantida como "N/A" por padronização
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
            // Erros de leitura ou parsing são logados sem interromper o processamento geral
            System.err.println("Erro ao processar: " + caminho);
            e.printStackTrace();
        }
    }
}
