public abstract class Animal {

    private String nome;
    private int idade;
    private String nomeDono;
    private String telefoneDono;

    public Animal() {
    }

    public Animal(String nome, int idade, String nomeDono, String telefoneDono) {
        this.nome = nome;
        this.idade = idade;
        this.nomeDono = nomeDono;
        this.telefoneDono = telefoneDono;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getNomeDono() {
        return nomeDono;
    }

    public void setNomeDono(String nomeDono) {
        this.nomeDono = nomeDono;
    }

    public String getTelefoneDono() {
        return telefoneDono;
    }

    public void setTelefoneDono(String telefoneDono) {
        this.telefoneDono = telefoneDono;
    }

    // Método abstrato pedido no enunciado
    public abstract String emitirSom();

    // Métodos abstratos auxiliares (sobrescritos nas subclasses, mesma ideia do emitirSom)
    public abstract String getEspecie();

    public abstract String getDetalhe();
}
