package bd.ac.buet.paxosevalutaion.shell;

import bd.ac.buet.paxosevalutaion.controller.PaxosController;
import org.springframework.shell.standard.ShellComponent;

@ShellComponent
public class PaxosEvaluationCommands {
    private final PaxosController paxosController;

    public PaxosEvaluationCommands(PaxosController paxosController) {
        this.paxosController = paxosController;
    }


}
