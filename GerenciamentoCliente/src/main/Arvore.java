package main;


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

    public boolean inserir(Item elem) {
        if (pesquisarPorNome(elem.getCliente().getNome()) != null) {
            
            return false;
        }
        if (pesquisarPorCpf(elem.getCliente().getCpf()) != null) {
            
            return false;
        }

        this.raiz = inserir(elem, this.raiz);
        this.quantNos++;
        
        return true;
    }

    private NoArv inserir(Item elem, NoArv no) {
        if (no == null) {
            NoArv novo = new NoArv(elem);
            return novo;
        } else {
    
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
        if (arv == null) 
            return null;

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
    public void listarEmOrdemAlfabetica() {
        if (eVazia()) {
            return;
        }
        listarEmOrdemAlfabetica(this.raiz);
        
    }

    private void listarEmOrdemAlfabetica(NoArv no) {
        if (no != null) {
            listarEmOrdemAlfabetica(no.getEsq());
            System.out.println(no.getInfo().getCliente().getNome());
            listarEmOrdemAlfabetica(no.getDir());
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
        
        exibirClientesComSaldo(this.raiz, maiorSaldo);
        return "\n--- Cliente(s) com Maior Saldo (" + maiorSaldo + ") ---";
    }

    private double encontrarMaiorSaldo(NoArv no) {
        if (no == null) {
            return 0.0; 
        }
        double maxEsq = encontrarMaiorSaldo(no.getEsq());
        double maxDir = encontrarMaiorSaldo(no.getDir());
        return Math.max(no.getInfo().getCliente().getSaldo(), Math.max(maxEsq, maxDir));
    }

    private void exibirClientesComSaldo(NoArv no, double saldoAlvo) {
        if (no != null) {
            exibirClientesComSaldo(no.getEsq(), saldoAlvo);
            if (no.getInfo().getCliente().getSaldo() == saldoAlvo) {
                System.out.println(no.getInfo().getCliente().getNome() + " - Saldo: " + no.getInfo().getCliente().getSaldo());
            }
            exibirClientesComSaldo(no.getDir(), saldoAlvo);
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

    
}

