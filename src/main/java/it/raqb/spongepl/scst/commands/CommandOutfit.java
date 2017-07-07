package it.raqb.spongepl.scst.commands;

import com.google.common.collect.Iterables;
import com.google.common.reflect.TypeToken;
import it.raqb.spongepl.scst.SCST;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.HashMap;

/**
 * Created by Raqbit on 7-1-17.
 */
public class CommandOutfit extends Command {

    private SCST pluginInstance;

    public CommandOutfit(SCST plugin) {
        super(plugin);
        pluginInstance = plugin;
    }

    @Override
    public CommandSpec getCommand() {
        CommandSpec saveCommandSpec = CommandSpec.builder()
                .description(Text.of("Save your current outfit"))
                .executor(new SaveExecutor())
                .build();

        return CommandSpec.builder()
                .description(Text.of("Allows you to save and restore your outfit"))
                .executor(new RestoreExecutor())
                .permission("scst.command.outfit")
                .child(saveCommandSpec, "save", "set", "s")
                .build();
    }

    @Override
    public String[] getAliases() {
        return new String[]{"outfit", "of"};
    }

    private class RestoreExecutor implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

            if (!(src instanceof Player)) {
                src.sendMessage(Text.builder("Only players can use this command").color(TextColors.RED).build());
                return CommandResult.empty();
            }

            Player player = (Player)src;

            CommentedConfigurationNode saveNode = pluginInstance.getConfigHelper().rootNode.getNode("outfit", "savedOutfits", player.getUniqueId().toString());

            if(saveNode.isVirtual()){
                player.sendMessage(
                        Text.builder("You don't have an outfit saved, use /outfit save to save one")
                        .color(TextColors.RED).build()
                );
                return CommandResult.empty();
            }

            Slot[] slotArr = Iterables
                    .toArray(player.getInventory().slots(), Slot.class);

            HashMap<String, Integer> slotIDs = new HashMap<String, Integer>();

            slotIDs.put("head", 39);
            slotIDs.put("chest",38);
            slotIDs.put("legs",37);
            slotIDs.put("feet",36);

            for(String key : slotIDs.keySet()) {
                ConfigurationNode node = saveNode.getNode(key);
                Slot slot = slotArr[slotIDs.get(key)];

                try {
                    ItemStack stack = node.getValue(TypeToken.of(ItemStack.class));
                    slot.set(stack);
                } catch (ObjectMappingException e) {
                    pluginInstance.getLogger().error("Could not map ItemStack");
                    e.printStackTrace();
                }
            }

            player.sendMessage(Text.of("Restored your saved outfit"));
            return CommandResult.success();

        }
    }

    private class SaveExecutor implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
            if (src instanceof Player) {
                Player player = (Player)src;

                CommentedConfigurationNode saveNode = pluginInstance.getConfigHelper().rootNode.getNode("outfit", "savedOutfits", player.getUniqueId().toString());

                Slot[] slotArr = Iterables
                        .toArray(player.getInventory().slots(), Slot.class);

                HashMap<String, Integer> slotIDs = new HashMap<String, Integer>();

                slotIDs.put("head", 39);
                slotIDs.put("chest",38);
                slotIDs.put("legs",37);
                slotIDs.put("feet",36);

                for(String key : slotIDs.keySet()){
                    ConfigurationNode node = saveNode.getNode(key);
                    Slot slot = slotArr[slotIDs.get(key)];

                    if(slot.totalItems() != 0){
                        try {
                            node.setValue(TypeToken.of(ItemStack.class),slot.peek().get());
                        } catch (ObjectMappingException e) {
                            pluginInstance.getLogger().error("Could not map ItemStack");
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            node.setValue(TypeToken.of(ItemStack.class),ItemStack.empty());
                        } catch (ObjectMappingException e) {
                            pluginInstance.getLogger().error("Could not map empty ItemStack");
                            e.printStackTrace();
                        }
                    }
                }
                pluginInstance.getConfigHelper().saveConfig();

                player.sendMessage(Text.of("Saved your current outfit"));

                return CommandResult.success();
            } else {
                src.sendMessage(Text.builder("Only players can use this command").color(TextColors.RED).build());
                return CommandResult.empty();
            }
        }
    }
}
