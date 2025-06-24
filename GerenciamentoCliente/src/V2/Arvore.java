package V2;

import dados.Item;

public class Arvore {
    private NoArv raiz;
    private int quantNos;

    public Arvore() {
        this.quantNos = 0;
        this.raiz = null;
    }

    public boolean eVazia() {
        return (this.raiz == null);
    }

    public NoArv getRaiz() {
        return this.raiz;
    }

    public int getQuantNos() {
        return this.quantNos;
    }

    // Inserir cliente na árvore
    public int inserir(Item elem) {
        // Verifica se o nome ou CPF já existem
        if (pesquisarPorNome(elem.getCliente().getNome()) != null) {
            return 1; // Cliente com este nome já existe
        }
        if (pesquisarPorCpf(elem.getCliente().getCpf()) != null) {
            return 2; // Cliente com este CPF já existe
        }

        this.raiz = inserir(elem, this.raiz);
        this.quantNos++;
        return 0; // Sucesso
    }

    private NoArv inserir(Item elem, NoArv no) {
        if (no == null) {
            NoArv novo = new NoArv(elem);
            return novo;
        } else {
            // Compara pelo nome do cliente (chave)
            if (elem.getChave().compareTo(no.getInfo().getChave()) < 0) {
                no.setEsq(inserir(elem, no.getEsq()));
                return no;
            } else {
                no.setDir(inserir(elem, no.getDir()));
                return no;
            }
        }
    }

    // Pesquisar cliente por nome
    public Cliente pesquisarPorNome(String nome) {
        return pesquisarPorNome(nome, this.raiz);
    }

    private Cliente pesquisarPorNome(String nome, NoArv no) {
        if (no != null) {
            if (nome.compareTo(no.getInfo().getChave()) < 0) {
                return pesquisarPorNome(nome, no.getEsq());
            } else if (nome.compareTo(no.getInfo().getChave()) > 0) {
                return pesquisarPorNome(nome, no.getDir());
            } else {
                return no.getInfo().getCliente(); // Encontrou o cliente
            }
        }
        return null; // Cliente não encontrado
    }

    // Pesquisar cliente por CPF
    public Cliente pesquisarPorCpf(String cpf) {
        return pesquisarPorCpf(cpf, this.raiz);
    }

    private Cliente pesquisarPorCpf(String cpf, NoArv no) {
        if (no == null) {
            return null;
        }
        if (no.getInfo().getCliente().getCpf().equals(cpf)) {
            return no.getInfo().getCliente();
        }
        Cliente clienteEsq = pesquisarPorCpf(cpf, no.getEsq());
        if (clienteEsq != null) {
            return clienteEsq;
        }
        return pesquisarPorCpf(cpf, no.getDir());
    }

    // Remover cliente da árvore
    public boolean remover(String chave) {
        if (pesquisarPorNome(chave, this.raiz) != null) {
            this.raiz = remover(chave, this.raiz);
            this.quantNos--;
            return true;
        } else {
            return false;
        }
    }

    private NoArv remover(String chave, NoArv arv) {
        if (arv == null) return null;

        if (chave.compareTo(arv.getInfo().getChave()) < 0) {
            arv.setEsq(remover(chave, arv.getEsq()));
        } else if (chave.compareTo(arv.getInfo().getChave()) > 0) {
            arv.setDir(remover(chave, arv.getDir()));
        } else { // Encontrou o nó a ser removido
            if (arv.getDir() == null) {
                return arv.getEsq();
            } else if (arv.getEsq() == null) {
                return arv.getDir();
            } else {
                // Nó com dois filhos: encontra o sucessor (menor na subárvore direita)
                NoArv temp = encontrarMenor(arv.getDir());
                arv.setInfo(temp.getInfo());
                arv.setDir(remover(temp.getInfo().getChave(), arv.getDir()));
            }
        }
        return arv;
    }

    private NoArv encontrarMenor(NoArv no) {
        if (no.getEsq() == null) {
            return no;
        } else {
            return encontrarMenor(no.getEsq());
        }
    }

    // Listar clientes em ordem alfabética (percurso em ordem central)
    public String listarEmOrdemAlfabetica() {
        if (eVazia()) {
            return "A árvore está vazia. Nenhum cliente para listar.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Clientes em Ordem Alfabética ---\n");
        listarEmOrdemAlfabetica(this.raiz, sb);
        sb.append("-------------------------------------\n");
        return sb.toString();
    }

    private void listarEmOrdemAlfabetica(NoArv no, StringBuilder sb) {
        if (no != null) {
            listarEmOrdemAlfabetica(no.getEsq(), sb);
            sb.append(no.getInfo().getCliente().getNome()).append("\n");
            listarEmOrdemAlfabetica(no.getDir(), sb);
        }
    }

    // Consultar a média de saldos dos clientes
    public double consultarMediaSaldos() {
        if (eVazia()) {
            return 0.0;
        }
        double totalSaldos = somarSaldos(this.raiz);
        return totalSaldos / this.quantNos;
    }

    private double somarSaldos(NoArv no) {
        if (no == null) {
            return 0.0;
        }
        return no.getInfo().getCliente().getSaldo() +
               somarSaldos(no.getEsq()) +
               somarSaldos(no.getDir());
    }

    // Listar o cliente com maior saldo no banco
    public String listarClienteMaiorSaldo() {
        if (eVazia()) {
            return "A árvore está vazia. Nenhum cliente para listar.";
        }

        double maiorSaldo = encontrarMaiorSaldo(this.raiz);
        StringBuilder sb = new StringBuilder();
        sb.append("\n--- Cliente(s) com Maior Saldo (").append(maiorSaldo).append(") ---\n");
        exibirClientesComSaldo(this.raiz, maiorSaldo, sb);
        sb.append("---------------------------------------------------\n");
        return sb.toString();
    }

    private double encontrarMaiorSaldo(NoArv no) {
        if (no == null) {
            return 0.0; // Valor mínimo para iniciar a comparação
        }
        double maxEsq = encontrarMaiorSaldo(no.getEsq());
        double maxDir = encontrarMaiorSaldo(no.getDir());
        return Math.max(no.getInfo().getCliente().getSaldo(), Math.max(maxEsq, maxDir));
    }

    private void exibirClientesComSaldo(NoArv no, double saldoAlvo, StringBuilder sb) {
        if (no != null) {
            exibirClientesComSaldo(no.getEsq(), saldoAlvo, sb);
            if (no.getInfo().getCliente().getSaldo() == saldoAlvo) {
                sb.append(no.getInfo().getCliente().getNome()).append(" - Saldo: ").append(no.getInfo().getCliente().getSaldo()).append("\n");
            }
            exibirClientesComSaldo(no.getDir(), saldoAlvo, sb);
        }
    }

    // Atualizar dados do cliente
    public boolean atualizarDadosCliente(String nome, String novoTelefone, String novoEmail, double novoSaldo) {
        Cliente cliente = pesquisarPorNome(nome);
        if (cliente != null) {
            cliente.setTelefone(novoTelefone);
            cliente.setEmail(novoEmail);
            cliente.setSaldo(novoSaldo);
            return true;
        } else {
            return false;
        }
    }

    // Métodos de percurso (mantidos, mas o CamCentral foi adaptado para listar nomes)
    public Item[] CamCentral() {
        int[] n = new int[1];
        Item[] vet = new Item[this.quantNos];
        return FazCamCentral(this.raiz, vet, n);
    }

    private Item[] FazCamCentral(NoArv arv, Item[] vet, int[] n) {
        if (arv != null) {
            vet = FazCamCentral(arv.getEsq(), vet, n);
            vet[n[0]++] = arv.getInfo();
            vet = FazCamCentral(arv.getDir(), vet, n);
        }
        return vet;
    }

    public Item[] CamPreFixado() {
        int[] n = new int[1];
        Item[] vet = new Item[this.quantNos];
        return FazCamPreFixado(this.raiz, vet, n);
    }

    private Item[] FazCamPreFixado(NoArv arv, Item[] vet, int[] n) {
        if (arv != null) {
            vet[n[0]++] = arv.getInfo();
            vet = FazCamPreFixado(arv.getEsq(), vet, n);
            vet = FazCamPreFixado(arv.getDir(), vet, n);
        }
        return vet;
    }

    public Item[] CamPosFixado() {
        int[] n = new int[1];
        Item[] vet = new Item[this.quantNos];
        return FazCamPosFixado(this.raiz, vet, n);
    }

    private Item[] FazCamPosFixado(NoArv arv, Item[] vet, int[] n) {
        if (arv != null) {
            vet = FazCamPosFixado(arv.getEsq(), vet, n);
            vet = FazCamPosFixado(arv.getDir(), vet, n);
            vet[n[0]++] = arv.getInfo();
        }
        return vet;
    }

    // Métodos de contagem e altura (mantidos, mas não diretamente usados nas funcionalidades do briefing)
    public String mostrarFolhas() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nós folhas: ");
        mostrarFolhasRec(this.raiz, sb);
        sb.append("\n");
        return sb.toString();
    }

    private void mostrarFolhasRec(NoArv no, StringBuilder sb) {
        if (no != null) {
            if (no.getEsq() == null && no.getDir() == null) {
                sb.append(no.getInfo().getChave()).append(" ");
            }
            mostrarFolhasRec(no.getEsq(), sb);
            mostrarFolhasRec(no.getDir(), sb);
        }
    }

    public int altura() {
        return alturaRec(this.raiz);
    }

    private int alturaRec(NoArv no) {
        if (no == null) return -1;
        int esq = alturaRec(no.getEsq());
        int dir = alturaRec(no.getDir());
        return 1 + Math.max(esq, dir);
    }

    public int totalFolhas() {
        return contarFolhasRec(this.raiz);
    }

    private int contarFolhasRec(NoArv no) {
        if (no == null) return 0;
        if (no.getEsq() == null && no.getDir() == null) return 1;
        return contarFolhasRec(no.getEsq()) + contarFolhasRec(no.getDir());
    }

    public int totalNosInternos() {
        return contarNosInternosRec(this.raiz);
    }

    private int contarNosInternosRec(NoArv no) {
        if (no == null || (no.getEsq() == null && no.getDir() == null)) return 0;
        return 1 + contarNosInternosRec(no.getEsq()) + contarNosInternosRec(no.getDir());
    }

    public int totalNosNoNivel(int nivel) {
        return contarNivelRec(this.raiz, 0, nivel);
    }

    private int contarNivelRec(NoArv no, int atual, int alvo) {
        if (no == null) return 0;
        if (atual == alvo) return 1;
        return contarNivelRec(no.getEsq(), atual + 1, alvo) + contarNivelRec(no.getDir(), atual + 1, alvo);
    }
}

