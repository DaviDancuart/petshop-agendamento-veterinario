public class Gato extends Animal {

    private String temperamento;

    public Gato() {
        super();
    }

    public Gato(String nome, int idade, String nomeDono, String telefoneDono, String temperamento) {
        super(nome, idade, nomeDono, telefoneDono);
        this.temperamento = temperamento;
    }

    public String getTemperamento() {
        return temperamento;
    }

    public void setTemperamento(String temperamento) {
        this.temperamento = temperamento;
    }

    @Override
    public String emitirSom() {
        return "Miau!";
    }

    @Override
    public String getEspecie() {
        return "Gato";
    }

    @Override
    public String getDetalhe() {
        return temperamento;
    }
}
