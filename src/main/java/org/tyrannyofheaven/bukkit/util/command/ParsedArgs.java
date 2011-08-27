package org.tyrannyofheaven.bukkit.util.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

final class ParsedArgs {

    private final Map<String, String> options;

    private ParsedArgs(Map<String, String> options) {
        this.options = options;
    }

    private static OptionMetaData getOption(String flag, Set<OptionMetaData> options) {
        for (OptionMetaData omd : options) {
            for (String name : omd.getNames()) {
                if (flag.equals(name)) {
                    return omd;
                }
            }
        }
        return null;
    }

    public static ParsedArgs parse(CommandMetaData cmd, String[] args) {
        Map<String, String> options = new HashMap<String, String>();

        int pos = 0;
        
        // Parse flags
        while (pos < args.length) {
            String arg = args[pos];
            if (OptionMetaData.isArgument(arg)) {
                // positional argument
                break;
            }
            else {
                OptionMetaData omd = getOption(arg, cmd.getFlagOptions());
                if (omd == null) {
                    // Unknown option
                    return null;
                }
                // Special handling of Boolean and boolean
                if (omd.getType() == Boolean.class || omd.getType() == Boolean.TYPE) {
                    options.put(omd.getName(), ""); // value doesn't matter, only existence
                }
                else {
                    // Get value
                    pos++;
                    if (pos >= args.length) {
                        // Premature end
                        return null;
                    }
                    
                    options.put(omd.getName(), args[pos]);
                }
                pos++;
            }
        }
        
        // Parse positional args
        for (OptionMetaData omd : cmd.getPositionalArguments()) {
            if (!omd.isOptional()) {
                if (pos >= args.length) {
                    // Ran out of args
                    return null;
                }
                else {
                    options.put(omd.getName(), args[pos++]);
                }
            }
            else {
                if (pos >= args.length) {
                    // No more args, this and the rest should be optional
                    break;
                }
                else {
                    options.put(omd.getName(), args[pos++]);
                }
            }
        }

        return new ParsedArgs(options);
    }

    public boolean hasOption(String name) {
        // TODO collapse into getValue
        return options.containsKey(name);
    }

    public String getOption(String name) {
        return options.get(name);
    }

}
