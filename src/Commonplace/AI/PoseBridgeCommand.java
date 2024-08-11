package Commonplace.AI;

import Commonplace.Tools.Interfaces.PoseBridge;
import mindustry.ai.types.CommandAI;

public class PoseBridgeCommand extends CommandAI {
    @Override
    public void updateUnit(){
        if(command == null && unit.type.commands.length > 0){
            command = unit.type.defaultCommand == null ? unit.type.commands[0] : unit.type.defaultCommand;
        }
        var curCommand = command;
        if(lastCommand != curCommand){
            lastCommand = curCommand;
            commandController = (curCommand == null ? null : curCommand.controller.get(unit));
        }
        if(commandController != null){
            if(commandController.unit() != unit) commandController.unit(unit);
            if(commandController instanceof PoseBridge npb) {
                if(!unit.isPlayer() && targetPos != null){
                    npb.setPose(targetPos);
                }
            }
            commandController.updateUnit();
        }else{
            defaultBehavior();
            unit.updateBoosting(false);
        }
    }
}
