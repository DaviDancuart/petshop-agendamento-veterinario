public class Consulta {

    private Animal paciente;   // referencia um Cachorro ou um Gato em tempo de execução
    private int horarioIndex;
    private String sintomas;

    public Consulta() {
    }

    public Consulta(Animal paciente, int horarioIndex, String sintomas) {
        this.paciente = paciente;
        this.horarioIndex = horarioIndex;
        this.sintomas = sintomas;
    }

    public Animal getPaciente() {
        return paciente;
    }

    public void setPaciente(Animal paciente) {
        this.paciente = paciente;
    }

    public int getHorarioIndex() {
        return horarioIndex;
    }

    public void setHorarioIndex(int horarioIndex) {
        this.horarioIndex = horarioIndex;
    }

    public String getSintomas() {
        return sintomas;
    }

    public void setSintomas(String sintomas) {
        this.sintomas = sintomas;
    }
}
