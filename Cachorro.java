public class Cachorro extends Animal {

    private String porte;

    public Cachorro() {
        super();
    }

    public Cachorro(String nome, int idade, String nomeDono, String telefoneDono, String porte) {
        super(nome, idade, nomeDono, telefoneDono);
        this.porte = porte;
    }

    public String getPorte() {
        return porte;
    }

    public void setPorte(String porte) {
        this.porte = porte;
    }

    @Override
    public String emitirSom() {
        return "Au Au!";
    }

    @Override
    public String getEspecie() {
        return "Cachorro";
    }

    @Override
    public String getDetalhe() {
        return porte;
    }
}
