package main;


import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuPrincipal {
    private static Arvore arvoreClientes = new Arvore();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        acessoAoSistema();
    }

    private static void acessoAoSistema() {
        System.out.println("\n--- Acesso ao Sistema --- ");
        System.out.print("Número: ");
        String numero = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        if (numero.equals("11111") && senha.equals("adm")) {
            System.out.println("Acesso concedido!\n");
            exibirMenuPrincipal();
        } else {
            System.out.println("Número ou senha incorretos. Acesso negado.\n");
            acessoAoSistema();
        }
    }

    private static void exibirMenuPrincipal() {
        int opcao;
        do {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Cadastrar novo Cliente");
            System.out.println("2. Consultar Dados Pessoais de um Cliente");
            System.out.println("3. Listar Clientes em Ordem Alfabética");
            System.out.println("4. Consultar a Média de Saldos dos Clientes");
            System.out.println("5. Buscar  o Cliente com Maior Saldo no Banco");
            System.out.println("6. Excluir Cliente");
            System.out.println("7. Atualizar Dados do Cliente");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); 

                switch (opcao) {
                    case 1:
                        cadastrarCliente();
                        break;
                    case 2:
                        consultarCliente();
                        break;
                    case 3:
                        System.out.println("\n--- Clientes em Ordem Alfabética ---");
                        arvoreClientes.listarEmOrdemAlfabetica();
                        System.out.println("-------------------------------------");
                        break;
                    case 4:
                        consultarMediaSaldos();
                        break;
                    case 5:
                        System.out.println(arvoreClientes.listarClienteMaiorSaldo());
                        
                        break;
                    case 6:
                        excluirCliente();
                        break;
                    case 7:
                        atualizarDadosCliente();
                        break;
                    case 0:
                        System.out.println("Saindo do sistema. Até mais!");
                        break;
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.nextLine();
                opcao = -1; 
            }
        } while (opcao != 0);
    }

    private static void cadastrarCliente() {
        System.out.println("\n--- Cadastrar Cliente ---");
        String nome, cpf, telefone, email;
        double saldo;

        System.out.print("Nome: ");
        nome = scanner.nextLine().toLowerCase().trim();

        System.out.print("CPF (apenas números): ");
        cpf = scanner.nextLine().replaceAll("[^\\d]", "");  // tira tudo que não for numero REGEX 
        if (!validarCpf(cpf)) {
            System.out.println("Erro: CPF inválido. Deve conter 11 dígitos numéricos.");
            return;
        }

        System.out.print("Telefone: ");
        telefone = scanner.nextLine().replaceAll("[^\\d]", "");
        if (!validarTelefone(telefone)) {
            System.out.println("Erro: Telefone inválido. Deve conter 11 ou 13 dígitos numéricos.");
            return;
        }

        System.out.print("E-mail: ");
        email = scanner.nextLine();

        try {
            System.out.print("Saldo: ");
            saldo = scanner.nextDouble();
            scanner.nextLine(); 
            if (saldo < 0) {
                System.out.println("Erro: Saldo não pode ser negativo.");
                return;
            }
        } catch (InputMismatchException e) {
            System.out.println("Erro: Saldo inválido. Por favor, digite um número.");
            scanner.nextLine();
            return;
        }

        Cliente novoCliente = new Cliente(nome.toLowerCase(), cpf, telefone, email.toLowerCase(), saldo);
        
        if(arvoreClientes.inserir(new Item(novoCliente))){
            arvoreClientes.inserir(new Item(novoCliente));
            System.out.println("Cliente cadastrado com sucesso!");
        }else{
            System.out.println("Erro: Cliente com este nome já existe!");
        }
    }
    
    private static void consultarCliente() {
        System.out.println("\n--- Consultar Dados Pessoais de um Cliente ---");
        System.out.print("Digite o nome do cliente para consultar: ");
        String nome = scanner.nextLine().toLowerCase();

        Cliente cliente = arvoreClientes.pesquisarPorNome(nome);
        if (cliente != null) {
            System.out.println("\nDados do Cliente:\n" + cliente);
        } else {
            System.out.println("Cliente não encontrado.");
        }
    }

    private static void consultarMediaSaldos() {
        System.out.println("\n--- Média de Saldos dos Clientes ---");
        double media = arvoreClientes.consultarMediaSaldos();
        if (arvoreClientes.eVazia()) {
            System.out.println("Não há clientes cadastrados para calcular a média.");
        } else {
            System.out.printf("A média dos saldos dos clientes é: %.2f\n", media);
        }
    }

    private static void excluirCliente() {
        System.out.println("\n--- Excluir Cliente ---");
        System.out.print("Digite o nome do cliente a ser excluído: ");
        String nome = scanner.nextLine().toLowerCase().trim();

        if (arvoreClientes.remover(nome)){
             arvoreClientes.remover(nome);
               System.out.println("Cliente removido com sucesso!");
        }else{
            System.out.println("Erro: Cliente não encontrado para remoção!");
        }
       
    }

    private static void atualizarDadosCliente() {
        System.out.println("\n--- Atualizar Dados do Cliente ---");
        System.out.print("Digite o nome do cliente para atualizar: ");
        String nome = scanner.nextLine().toLowerCase().trim();

        Cliente clienteExistente = arvoreClientes.pesquisarPorNome(nome);
        if (clienteExistente == null) {
            System.out.println("Erro: Cliente não encontrado para atualização.");
            return;
        }

        System.out.println("Cliente encontrado. Digite os novos dados (deixe em branco para manter o atual):");
        System.out.println("Telefone atual: " + clienteExistente.getTelefone());
        System.out.print("Novo Telefone: ");
        String novoTelefone = scanner.nextLine().replaceAll("[^\\d]", "");
        if (novoTelefone.isEmpty()) {
            novoTelefone = clienteExistente.getTelefone();
        }
        if (!validarTelefone(novoTelefone)) {
            System.out.println("Erro: Telefone inválido. Deve conter 11 ou 13 dígitos numéricos.");
            return;
        }

        System.out.println("E-mail atual: " + clienteExistente.getEmail());
        System.out.print("Novo E-mail: ");
        String novoEmail = scanner.nextLine().toLowerCase();
        if (novoEmail.isEmpty()) {
            novoEmail = clienteExistente.getEmail();
        }

        double novoSaldo = clienteExistente.getSaldo();
        System.out.println("Saldo atual: " + clienteExistente.getSaldo());
        System.out.print("Novo Saldo: ");
        String saldoStr = scanner.nextLine();
        if (!saldoStr.isEmpty()) {
            try {
                novoSaldo = Double.parseDouble(saldoStr);
                if (novoSaldo < 0) {
                    System.out.println("Erro: Saldo não pode ser negativo. Mantendo saldo atual.");
                    novoSaldo = clienteExistente.getSaldo();
                }
            } catch (NumberFormatException e) {
                System.out.println("Erro: Saldo inválido. Mantendo saldo atual.");
            }
        }

        if(arvoreClientes.atualizarDadosCliente(nome, novoTelefone, novoEmail, novoSaldo)){
            System.out.println("Dados do cliente atualizados com sucesso!");
        }else{
            System.out.println("Erro: Cliente não encontrado para atualização!");
        }

        
    }

    private static boolean validarCpf(String cpf) {
        return cpf.matches("\\d{11}");
    }

        private static boolean validarTelefone(String telefone) {
        return telefone.matches("\\d{11}|\\d{13}");
    }
}

