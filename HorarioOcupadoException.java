// Tipo de exceção declarado: classe que herda de Exception
public class HorarioOcupadoException extends Exception {

    public HorarioOcupadoException() {
        super("Este horário já está ocupado. Escolha outro horário.");
    }
}
