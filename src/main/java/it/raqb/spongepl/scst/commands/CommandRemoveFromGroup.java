package it.raqb.spongepl.scst.commands;

import it.raqb.spongepl.scst.SCST;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

// Created by Vauff on 07-02-2017

public class CommandRemoveFromGroup extends Command {

	public CommandRemoveFromGroup(SCST plugin) {
		super(plugin);
	}

	@Override
	public CommandSpec getCommand()
	{
		return CommandSpec.builder().description(Text.of("Takes care of removing a LuckPerms group and the corresponding scoreboard team in one command")).executor(new Executor()).permission("scst.command.removefromgroup").arguments(GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))), GenericArguments.onlyOne(GenericArguments.string(Text.of("group")))).build();
	}

	@Override
	public String[] getAliases()
	{
		return new String[] { "removefromgroup", "rfg" };
	}

	private class Executor implements CommandExecutor
	{

		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException
		{
			Player player = args.<Player>getOne("player").get();
			String playerName = player.getName().toLowerCase();
			String group = args.getOne("group").get().toString().toLowerCase();

			Sponge.getCommandManager().process(src, "lp user " + playerName + " parent remove " + group);
			Sponge.getCommandManager().process(src, "scoreboard teams leave " + playerName);

			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			src.sendMessage(Text.builder("Removed ").color(TextColors.GOLD).append(Text.builder(player.getName()).color(TextColors.RED).append(Text.builder(" from the LuckPerms group and scoreboard team ").color(TextColors.GOLD).append(Text.builder(group).color(TextColors.RED).build()).build()).build()).build());

			return CommandResult.success();
		}
	}
}
