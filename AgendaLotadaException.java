// Tipo de exceção declarado: classe que herda de Exception
public class AgendaLotadaException extends Exception {

    public AgendaLotadaException() {
        super("A agenda está lotada: os 10 horários já estão ocupados.");
    }
}
