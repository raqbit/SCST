package it.raqb.spongepl.scst.commands;

import com.google.common.collect.Iterables;
import it.raqb.spongepl.scst.SCST;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Raqbit on 6-29-17.
 */
public class CommandSClear extends Command {

    public CommandSClear(SCST plugin) {
        super(plugin);
    }

    @Override
    public CommandSpec getCommand() {
        return CommandSpec.builder()
                .description(Text.of("Clears everything in your inventory, except for your armor"))
                .executor(new Executor())
                .permission("scst.command.sclear")
                .build();
    }

    @Override
    public String[] getAliases() {
        return new String[]{"sclear", "scclear", "scl"};
    }

    private class Executor implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

            if (!(src instanceof Player)) {
                src.sendMessage(Text.builder("Only players can use this command").color(TextColors.RED).build());
                return CommandResult.empty();
            }

            Slot[] slotArr = Iterables
                    .toArray(((Player) src)
                            .getInventory().slots(), Slot.class);
            int itemCounter = 0;
            for(int i = 0; i < slotArr.length; i++){
                Slot slot = slotArr[i];
                if(i < 36 || i > 39){
                    if(slot.totalItems() != 0){
                        itemCounter += slot.totalItems();
                        slot.clear();
                    }
                }
            }

            if(itemCounter != 0){
                src.sendMessage(Text.builder("Cleared your inventory, removing " + itemCounter + " items").build());
                return CommandResult.success();

            } else {
                src.sendMessage(Text.builder("Could not clear your inventory, no items to remove").color(TextColors.RED).build());
                return CommandResult.empty();
            }
        }
    }
}
